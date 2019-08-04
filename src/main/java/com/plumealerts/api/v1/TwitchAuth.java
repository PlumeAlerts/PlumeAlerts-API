package com.plumealerts.api.v1;


import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import com.plumealerts.api.Constants;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.ScopesRecord;
import com.plumealerts.api.db.tables.records.UserLoginRequestRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.handler.DatabaseUser;
import com.plumealerts.api.handler.DatabaseUserAccessTokens;
import com.plumealerts.api.ratelimit.future.UserFollower;
import com.plumealerts.api.twitch.TwitchAPI;
import com.plumealerts.api.twitch.oauth2.domain.Token;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.Validate;
import com.plumealerts.api.v1.domain.error.ErrorType;
import org.apache.http.client.utils.URIBuilder;
import org.jooq.Result;
import spark.Request;
import spark.Response;

import java.net.URISyntaxException;
import java.util.StringJoiner;
import java.util.UUID;

import static com.plumealerts.api.db.Tables.SCOPES;
import static com.plumealerts.api.db.Tables.USER_LOGIN_REQUEST;
import static spark.Spark.get;

public class TwitchAuth {
    private String scopes;

    public TwitchAuth() {
        Result<ScopesRecord> scopesRecords = PlumeAlertsAPI.dslContext().selectFrom(SCOPES).fetch();
        if (scopesRecords.isEmpty()) {
            System.exit(-1);
        }
        StringJoiner joiner = new StringJoiner("+");
        for (ScopesRecord scope : scopesRecords) {
            joiner.add(scope.getScope());
        }
        this.scopes = joiner.toString();
        get("/login", this::login, PlumeAlertsAPI.mapper()::writeValueAsString);
        get("/response", this::response, PlumeAlertsAPI.mapper()::writeValueAsString);
    }

    public Object login(Request request, Response response) {
//        String userId = request.session().attribute("userId");
//        UsersRecord user = UserHandler.findUser(userId);
//        if (user != null && !user.getRefreshLogin()) {
//            UserAccessTokenRecord userAccessToken = UserAccessTokensHandler.getAccessToken(userId);
//            if (userAccessToken != null)
//                return ResponseUtil.redirect(response, "/dashboard");
//        }

        String state = UUID.randomUUID().toString().replace("-", "");
        URIBuilder ub;
        try {
            ub = new URIBuilder("https://id.twitch.tv/oauth2/authorize");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, null);
        }
        ub.addParameter("client_id", Constants.CLIENT_ID);
        ub.addParameter("redirect_uri", Constants.CLIENT_REDIRECT);
        ub.addParameter("state", state);
        ub.addParameter("scope", this.scopes.replace("+", " "));
        ub.addParameter("response_type", "code");
        String url = ub.toString();

        int i = PlumeAlertsAPI.dslContext().insertInto(USER_LOGIN_REQUEST).columns(USER_LOGIN_REQUEST.STATE, USER_LOGIN_REQUEST.SCOPES)
                .values(state, this.scopes)
                .execute();
        if (i != 1) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, null);
        }

        return ResponseUtil.redirect(response, url);
    }

    public Object response(Request request, Response response) {
        String code = request.queryParams("code");
        String state = request.queryParams("state");
        String scope = request.queryParams("scope");

        if (scope == null) {
            return ResponseUtil.errorResponse(response, ErrorType.BAD_REQUEST, "Scope is required");
        }

        if (!Validate.isAlphanumericAndLength(code, 30)) {
            return ResponseUtil.errorResponse(response, ErrorType.BAD_REQUEST, "Code must be alphanumerical and 30 characters long");
        }

        if (!Validate.isAlphanumericAndLength(state, 32)) {
            return ResponseUtil.errorResponse(response, ErrorType.BAD_REQUEST, "State must be alphanumerical and 30 characters long");
        }
        UserLoginRequestRecord loginRequestRecord = PlumeAlertsAPI.dslContext().selectFrom(USER_LOGIN_REQUEST)
                .where(USER_LOGIN_REQUEST.STATE.eq(state))
                .fetchOne();

        if (loginRequestRecord == null) {
            return ResponseUtil.errorResponse(response, ErrorType.BAD_REQUEST, "The state is invalid");
        }

        // Expire state after 30 minutes, force them to login in again.
        if ((System.currentTimeMillis() - loginRequestRecord.getCreatedAt().getTime()) > 1000 * 60 * 30) {
            return ResponseUtil.errorResponse(response, ErrorType.BAD_REQUEST, "State has expired");
        }
        Token userToken = TwitchAPI.oAuth2().authCode(Constants.CLIENT_ID, Constants.CLIENT_SECRET, code, Constants.CLIENT_REDIRECT).execute();

        if (userToken == null) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        UserList users = TwitchAPI.helix().getUsers(userToken.getAccessToken(), null, null).execute();
        if (users.getUsers() == null || users.getUsers().size() != 1) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        User user = users.getUsers().get(0);
        String userId = user.getId().toString();

        boolean newUser = false;
        UsersRecord usersRecord = DatabaseUser.findUser(userId);
        if (usersRecord == null) {
            DatabaseUser.insertUser(user);
            newUser = true;
        } else {
            if (usersRecord.getEmail().equalsIgnoreCase(user.getEmail())) {
                //TODO FUTURE PROOF, ASK THEM IF THEY WANT TO CHANGE THEIR EMAIL. Implement a system so it doesn't ask them every time though
            }
            if (!usersRecord.getId().equalsIgnoreCase(userId)) {
                //TODO MAJOR ISSUE
                return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
            }

            DatabaseUser.updateUser(user);
        }

        if (newUser) {
            DatabaseUserAccessTokens.setAccessToken(userId, userToken);
            PlumeAlertsAPI.request().add(new UserFollower(userId, userToken.getAccessToken()));
            //TODO Fetch mods
        } else {
            DatabaseUserAccessTokens.updateAccessToken(userId, userToken);
        }

        return ResponseUtil.redirect(response, "/dashboard");
    }
}
