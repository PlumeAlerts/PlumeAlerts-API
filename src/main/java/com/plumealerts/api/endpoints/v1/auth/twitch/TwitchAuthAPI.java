package com.plumealerts.api.endpoints.v1.auth.twitch;

import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.plumealerts.api.Constants;
import com.plumealerts.api.db.ScopeDatabase;
import com.plumealerts.api.db.TwitchUserAccessTokenDatabase;
import com.plumealerts.api.db.UserDatabase;
import com.plumealerts.api.db.UserLoginRequestDatabase;
import com.plumealerts.api.db.record.UserRecord;
import com.plumealerts.api.endpoints.v1.auth.domain.AccessTokenDomain;
import com.plumealerts.api.endpoints.v1.auth.twitch.domain.TwitchLogin;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.handler.user.AccessTokenHandler;
import com.plumealerts.api.handler.user.AuthHandler;
import com.plumealerts.api.handler.user.DashboardHandler;
import com.plumealerts.api.handler.user.TwitchUserHandler;
import com.plumealerts.api.twitch.TwitchAPI;
import com.plumealerts.api.twitch.oauth2.domain.Token;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.Validate;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import org.apache.http.client.utils.URIBuilder;
import org.jose4j.lang.JoseException;

import java.net.URISyntaxException;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwitchAuthAPI extends RoutingHandler {

    private static final Logger LOGGER = Logger.getLogger(TwitchAuthAPI.class.getName());

    private String scopes;

    public TwitchAuthAPI() {
        List<String> scopesList = ScopeDatabase.getScopes();
        if (scopesList.isEmpty()) {
            System.exit(-1);
            //TODO Change to something better
        }
        StringJoiner joiner = new StringJoiner("+");
        for (String scope : scopesList) {
            joiner.add(scope);
        }
        this.scopes = joiner.toString();
        this.get("/v1/auth/twitch/login", this::getLogin);
        this.get("/v1/auth/twitch/response", this::getResponse);
    }


    private Domain getLogin(HttpServerExchange exchange) {
        String state = UUID.randomUUID().toString().replace("-", "");
        URIBuilder ub;
        try {
            ub = new URIBuilder("https://id.twitch.tv/oauth2/authorize");
        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "Error creating uri", e);
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
        }
        ub.addParameter("client_id", Constants.TWITCH_CLIENT_ID);
        ub.addParameter("redirect_uri", Constants.TWITCH_CLIENT_REDIRECT);
        ub.addParameter("state", state);
        ub.addParameter("scope", this.scopes.replace("+", " "));
        ub.addParameter("response_type", "code");
        String url = ub.toString();

        if (!UserLoginRequestDatabase.insertUserLoginRequest(state, this.scopes)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
        }

        return ResponseUtil.successResponse(exchange, new TwitchLogin(url));
    }

    private Domain getResponse(HttpServerExchange exchange) {
        String code = Validate.getQueryParam(exchange, "code");
        String state = Validate.getQueryParam(exchange, "state");

        if (code == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Code is required");
        }
        if (state == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "State is required");
        }

        if (!Validate.isAlphanumericAndLength(code, 30)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Code must be alphanumerical and 30 characters long");
        }

        if (!Validate.isAlphanumericAndLength(state, 32)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "State must be alphanumerical and 30 characters long");
        }

        if (!AuthHandler.isLoginRequestValid(state)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "The state is invalid");
        }

        Token userToken;
        try {
            userToken = TwitchAPI.oAuth2()
                    .authorizationCode(Constants.TWITCH_CLIENT_ID, Constants.TWITCH_CLIENT_SECRET, code, Constants.TWITCH_CLIENT_REDIRECT)
                    .execute();
        } catch (HystrixRuntimeException e) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        UserList users;
        try {
            users = TwitchAPI.helix().getUsers(userToken.getAccessToken(), null, null).execute();
        } catch (HystrixRuntimeException e) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        if (users.getUsers() == null || users.getUsers().size() != 1) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        User user = users.getUsers().get(0);
        String userId = user.getId();

        UserRecord usersRecord = UserDatabase.findById(userId);
        if (usersRecord == null) {
            UserDatabase.insertUser(user);
            TwitchUserAccessTokenDatabase.insertAccessToken(userId, userToken);
        } else {
            if (!usersRecord.getId().equalsIgnoreCase(userId)) {
                LOGGER.log(Level.SEVERE, "User Id changed from {0} to {1}", new String[]{usersRecord.getId(), userId});
                return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "User id changed, this isn't possible");
            }
            UserDatabase.updateUser(user);
            TwitchUserHandler.updateAccessToken(userId, userToken);
        }

        DashboardHandler.createDefaultDashboard(userId, userId);

        AccessTokenDomain accessToken;
        try {
            accessToken = AccessTokenHandler.generateTokens(userId);
        } catch (JoseException e) {
            LOGGER.log(Level.SEVERE, "Error making jwt token", e);
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
        }
        return ResponseUtil.successResponse(exchange, accessToken);
    }
}