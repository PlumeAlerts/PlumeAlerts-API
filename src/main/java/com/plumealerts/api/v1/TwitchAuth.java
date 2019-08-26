package com.plumealerts.api.v1;


import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import com.netflix.hystrix.exception.HystrixRuntimeException;
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
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import org.apache.http.client.utils.URIBuilder;
import org.jooq.Result;

import java.net.URISyntaxException;
import java.util.Deque;
import java.util.StringJoiner;
import java.util.UUID;

import static com.plumealerts.api.db.Tables.SCOPES;
import static com.plumealerts.api.db.Tables.USER_LOGIN_REQUEST;

public class TwitchAuth extends RoutingHandler {
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
        this.get("/v1/auth/twitch/login", this::login);
        this.get("/v1/auth/twitch/response", this::response);
    }


    private void login(HttpServerExchange exchange) {
        String state = UUID.randomUUID().toString().replace("-", "");
        URIBuilder ub;
        try {
            ub = new URIBuilder("https://id.twitch.tv/oauth2/authorize");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
            return;
        }
        ub.addParameter("client_id", Constants.TWITCH_CLIENT_ID);
        ub.addParameter("redirect_uri", Constants.TWITCH_CLIENT_REDIRECT);
        ub.addParameter("state", state);
        ub.addParameter("scope", this.scopes.replace("+", " "));
        ub.addParameter("response_type", "code");
        String url = ub.toString();

        int i = PlumeAlertsAPI.dslContext().insertInto(USER_LOGIN_REQUEST).columns(USER_LOGIN_REQUEST.STATE, USER_LOGIN_REQUEST.SCOPES)
                .values(state, this.scopes)
                .execute();
        if (i != 1) {
            ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
            return;
        }

        exchange.setStatusCode(200);
        exchange.getResponseSender().send(url);
    }

    private void response(HttpServerExchange exchange) {
        Deque<String> dequeCode = exchange.getQueryParameters().get("code");
        Deque<String> dequeState = exchange.getQueryParameters().get("state");

        if (dequeCode == null || dequeCode.peek() == null) {
            ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Code is required");
            return;
        }
        if (dequeState == null || dequeState.peek() == null) {
            ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "State is required");
            return;
        }
        String code = dequeCode.peek();
        String state = dequeState.peek();

        if (!Validate.isAlphanumericAndLength(code, 30)) {
            ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Code must be alphanumerical and 30 characters long");
            return;
        }

        if (!Validate.isAlphanumericAndLength(state, 32)) {
            ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "State must be alphanumerical and 30 characters long");
            return;
        }
        UserLoginRequestRecord loginRequestRecord = PlumeAlertsAPI.dslContext().selectFrom(USER_LOGIN_REQUEST)
                .where(USER_LOGIN_REQUEST.STATE.eq(state))
                .fetchOne();

        if (loginRequestRecord == null) {
            ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "The state is invalid");
            return;
        }

        // Expire state after 30 minutes, force them to login in again.
        if ((System.currentTimeMillis() - loginRequestRecord.getCreatedAt().getTime()) > 1000 * 60 * 30) {
            ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "State has expired");
            return;
        }
        Token userToken;
        try {
            userToken = TwitchAPI.oAuth2().authCode(Constants.TWITCH_CLIENT_ID, Constants.TWITCH_CLIENT_SECRET, code, Constants.TWITCH_CLIENT_REDIRECT).execute();
        } catch (HystrixRuntimeException e) {
            ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
            return;
        }
        if (userToken == null) {
            ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
            return;
        }
        UserList users;
        try {
            users = TwitchAPI.helix().getUsers(userToken.getAccessToken(), null, null).execute();
        } catch (HystrixRuntimeException e) {
            ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
            return;
        }
        if (users == null || users.getUsers() == null || users.getUsers().size() != 1) {
            ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
            return;
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
                ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
                return;
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

        exchange.setStatusCode(200);
        exchange.getResponseSender().send("DONE");
//        return ResponseUtil.redirect(response, "/dashboard");
    }

}
