package com.plumealerts.api.twitch.helix;

import com.plumealerts.api.twitch.helix.response.ResponseUser;
import com.plumealerts.api.twitch.response.SuccessfulResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.util.List;

public interface HelixAPI {

    @GET("users")
    Call<SuccessfulResponse<List<ResponseUser>>> getUsers(@Header("Authorization") String auth);

}
