package com.plumealerts.api.twitch.oauth2;

import com.netflix.hystrix.HystrixCommand;
import com.plumealerts.api.twitch.oauth2.domain.RefreshToken;
import com.plumealerts.api.twitch.oauth2.domain.Token;
import com.plumealerts.api.twitch.oauth2.domain.Validate;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface TwitchOAuth2 {

    @RequestLine("GET /validate")
    @Headers("Authorization: OAuth {token}")
    HystrixCommand<Validate> validate(
            @Param("token") String token
    );

    @RequestLine("POST /token?grant_type=refresh_token&client_id={client_id}&client_secret={client_secret}&refresh_token={refresh_token}")
    HystrixCommand<RefreshToken> refreshToken(
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret,
            @Param("refresh_token") String refreshToken
    );

    @RequestLine("POST /token?grant_type=refresh_token&client_id={client_id}&client_secret={client_secret}&refresh_token={refresh_token}&scope={scope}")
    HystrixCommand<RefreshToken> refreshToken(
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret,
            @Param("refresh_token") String refreshToken,
            @Param("scope") String... scope
    );

    @RequestLine("POST /revoke?client_id={client_id}&token={token}")
    HystrixCommand<Void> revoke(
            @Param("client_id") String clientId,
            @Param("token") String token
    );

    @RequestLine("POST /token?grant_type=authorization_code&client_id={client_id}&client_secret={client_secret}&code={code}&redirect_uri={redirect_uri}")
    HystrixCommand<Token> authorizationCode(
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret,
            @Param("code") String code,
            @Param("redirect_uri") String redirectUri
    );

    @RequestLine("POST /token?grant_type=client_credentials&client_id={client_id}&client_secret={client_secret}")
    HystrixCommand<Token> clientCredentials(
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret
    );

    @RequestLine("POST /token?grant_type=client_credentials&client_id={client_id}&client_secret={client_secret}&scope={scope}")
    HystrixCommand<Token> clientCredentials(
            @Param("client_id") String clientId,
            @Param("client_secret") String clientSecret,
            @Param("scope") String scope
    );
}
