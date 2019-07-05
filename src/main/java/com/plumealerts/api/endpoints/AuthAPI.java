package com.plumealerts.api.endpoints;


import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.ScopesRecord;
import com.plumealerts.api.db.tables.records.UserAccessTokenRecord;
import com.plumealerts.api.db.tables.records.UserLoginRequestRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.endpoints.response.error.ErrorType;
import com.plumealerts.api.twitch.TwitchAPI;
import com.plumealerts.api.twitch.helix.response.ResponseUser;
import com.plumealerts.api.twitch.oauth2.response.ResponseAuthorizationCode;
import com.plumealerts.api.twitch.response.SuccessfulResponse;
import com.plumealerts.api.utils.Constants;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.Validate;
import okhttp3.ResponseBody;
import org.apache.http.client.utils.URIBuilder;
import org.jooq.Result;
import spark.Request;
import spark.Response;
import spark.Session;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import static com.plumealerts.api.db.Tables.*;
import static spark.Spark.get;

//TODO CHANGE ALL THE ERRORS TO A HTML ERROR NOT API
public class AuthAPI {
    private String scopes;


    public AuthAPI() {
        Result<ScopesRecord> scopesRecords = PlumeAlertsAPI.dslContext().selectFrom(SCOPES).fetch();
        if (scopesRecords.isEmpty()) {
            System.exit(-1);
        }
        StringJoiner joiner = new StringJoiner("+");
        for (ScopesRecord scope : scopesRecords) {
            joiner.add(scope.getScope());
        }
        this.scopes = joiner.toString();
        get("/login", this::login);
        get("/response", this::response);
    }

    public Object login(Request request, Response response) {
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

        int i = PlumeAlertsAPI.dslContext().insertInto(USER_LOGIN_REQUEST).columns(USER_LOGIN_REQUEST.STATE, USER_LOGIN_REQUEST.SCOPES).values(state, this.scopes).execute();
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

        // Expire after 30 minutes, force them to resign in again.
        if ((System.currentTimeMillis() - loginRequestRecord.getCreatedAt().getTime()) > 1000 * 60 * 30) {
            return ResponseUtil.errorResponse(response, ErrorType.BAD_REQUEST, "State has expired");
        }

        retrofit2.Response<ResponseAuthorizationCode> codeResponse;
        try {
            codeResponse = TwitchAPI.oAuth2().postAuthorizationCode(Constants.CLIENT_ID, Constants.CLIENT_SECRET, code, Constants.CLIENT_REDIRECT).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        if (!codeResponse.isSuccessful()) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        ResponseAuthorizationCode authorizationCode = codeResponse.body();
        if (authorizationCode == null) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        retrofit2.Response<SuccessfulResponse<List<ResponseUser>>> helixUserResponse;
        try {
            helixUserResponse = TwitchAPI.helix().getUsers("Bearer " + authorizationCode.getAccessToken()).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        if (!helixUserResponse.isSuccessful()) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        SuccessfulResponse<List<ResponseUser>> responseUser = helixUserResponse.body();
        if (responseUser == null || responseUser.getData() == null) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        if (responseUser.getData().size() != 1) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        ResponseUser user = responseUser.getData().get(0);

        UsersRecord usersRecord = PlumeAlertsAPI.dslContext().insertInto(USERS, USERS.ID, USERS.EMAIL, USERS.LOGIN, USERS.DISPLAY_NAME, USERS.BROADCASTER_TYPE, USERS.TYPE, USERS.VIEW_COUNT)
                .values(user.getId(), user.getEmail(), user.getLogin(), user.getDisplayName(), user.getBroadcastType(), user.getType(), user.getViewCount())
                .onDuplicateKeyUpdate()
                .set(USERS.LOGIN, user.getLogin())
                .set(USERS.DISPLAY_NAME, user.getDisplayName())
                .set(USERS.BROADCASTER_TYPE, user.getBroadcastType())
                .set(USERS.TYPE, user.getType())
                .set(USERS.VIEW_COUNT, user.getViewCount())
                .returning()
                .fetchOne();

        if (usersRecord == null) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        if (usersRecord.getEmail().equalsIgnoreCase(user.getEmail())) {
            //TODO FUTURE PROOF, ASK THEM IF THEY WANT TO CHANGE THEIR EMAIL. Implement a system so it doesn't ask them every time though
        }

        if (!usersRecord.getId().equalsIgnoreCase(user.getId())) {
            return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        UserAccessTokenRecord accessTokenRecord = PlumeAlertsAPI.dslContext().selectFrom(USER_ACCESS_TOKEN).where(USER_ACCESS_TOKEN.USER_ID.eq(user.getId())).fetchOne();

        if (accessTokenRecord == null) {
            PlumeAlertsAPI.dslContext().insertInto(USER_ACCESS_TOKEN, USER_ACCESS_TOKEN.USER_ID, USER_ACCESS_TOKEN.ACCESS_TOKEN, USER_ACCESS_TOKEN.REFRESH_TOKEN, USER_ACCESS_TOKEN.EXPIRED_AT)
                    .values(user.getId(), authorizationCode.getAccessToken(), authorizationCode.getRefreshToken(), authorizationCode.getExpire())
                    .execute();

        } else {
            retrofit2.Response<Void> revokeResponse;
            try {
                revokeResponse = TwitchAPI.oAuth2().postRevoke(Constants.CLIENT_ID, accessTokenRecord.getAccessToken()).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
            }
            if (!revokeResponse.isSuccessful()) {
                return ResponseUtil.errorResponse(response, ErrorType.INTERNAL_SERVER_ERROR, "");
            }

            PlumeAlertsAPI.dslContext().update(USER_ACCESS_TOKEN)
                    .set(USER_ACCESS_TOKEN.ACCESS_TOKEN, authorizationCode.getAccessToken())
                    .set(USER_ACCESS_TOKEN.REFRESH_TOKEN, authorizationCode.getRefreshToken())
                    .set(USER_ACCESS_TOKEN.EXPIRED_AT, authorizationCode.getExpire())
                    .execute();

        }

        //TODO CALL FETCH ALL ALERT DATA

        Session session = request.session(true);
        session.maxInactiveInterval(3600);
        session.attribute("id", user.getId());

        return ResponseUtil.redirect(response, "/dashboard");
    }
}
