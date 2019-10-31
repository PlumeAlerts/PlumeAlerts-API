package com.plumealerts.api.twitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixErrorDecoder;
import com.netflix.config.ConfigurationManager;
import com.plumealerts.api.twitch.oauth2.TwitchOAuth2;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public final class TwitchAPI {

    private static TwitchHelix helixAPI;
    private static TwitchOAuth2 oAuth2API;

    private TwitchAPI() {
    }

    public static TwitchHelix helix() {
        if (helixAPI == null) {
            helixAPI = TwitchClientBuilder.builder()
                    .withEnableHelix(true)
                    .build().getHelix();
        }

        return helixAPI;
    }

    public static TwitchOAuth2 oAuth2() {
        if (oAuth2API == null) {
            ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 5000);

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            oAuth2API = HystrixFeign.builder()
                    .encoder(new JacksonEncoder(mapper))
                    .decoder(new JacksonDecoder(mapper))
                    .logger(new Logger.ErrorLogger())
                    .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder()))
                    .retryer(new Retryer.Default(1, 10000, 3))
                    .options(new Request.Options(5000, 15000))
                    .target(TwitchOAuth2.class, "https://id.twitch.tv/oauth2");
        }

        return oAuth2API;
    }
}
