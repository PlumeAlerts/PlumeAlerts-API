package com.plumealerts.api.twitch;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.twitch.helix.HelixAPI;
import com.plumealerts.api.twitch.oauth2.OAuth2API;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class TwitchAPI {

    private static HelixAPI helixAPI;
    private static OAuth2API oAuth2API;

    private TwitchAPI() {

    }

    public static HelixAPI helix() {
        if (helixAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.twitch.tv/helix/")
                    .addConverterFactory(MoshiConverterFactory.create(PlumeAlertsAPI.moshi))
                    .build();

            helixAPI = retrofit.create(HelixAPI.class);
        }

        return helixAPI;
    }

    public static OAuth2API oAuth2() {
        if (oAuth2API == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://id.twitch.tv/")
                    .addConverterFactory(MoshiConverterFactory.create(PlumeAlertsAPI.moshi))
                    .build();

            oAuth2API = retrofit.create(OAuth2API.class);
        }

        return oAuth2API;
    }
}
