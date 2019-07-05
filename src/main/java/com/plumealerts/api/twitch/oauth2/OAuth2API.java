package com.plumealerts.api.twitch.oauth2;

import com.plumealerts.api.twitch.oauth2.response.ResponseAuthorizationCode;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OAuth2API {

    @GET("oauth2/validate")
    Call<ResponseBody> getValidate(@Header("Authorization") String token);

    @POST("oauth2/revoke")
    Call<Void> postRevoke(@Query("client_id") String clientId, @Query("token") String token);

    @POST("oauth2/token?grant_type=authorization_code")
    Call<ResponseAuthorizationCode> postAuthorizationCode(@Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("code") String code, @Query("redirect_uri") String redirectURI);

    @POST("oauth2/token?grant_type=client_credentials")
    Call<ResponseBody> postClientCredentials(@Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("scope") String scope);

    @POST("oauth2/token?grant_type=refresh_token")
    Call<ResponseBody> postRefreshToken(@Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("refresh_token") String refreshToken, @Query("scope") String scope);
}
