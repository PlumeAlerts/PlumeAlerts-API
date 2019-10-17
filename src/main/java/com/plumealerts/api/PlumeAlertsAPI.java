package com.plumealerts.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumealerts.api.endpoints.cors.CorsHandler;
import com.plumealerts.api.endpoints.v1.auth.AuthAPI;
import com.plumealerts.api.endpoints.v1.auth.twitch.TwitchAuthAPI;
import com.plumealerts.api.endpoints.v1.user.UserAPI;
import com.plumealerts.api.ratelimit.RateLimitHandler;
import com.zaxxer.hikari.HikariDataSource;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class PlumeAlertsAPI {
    private static DSLContext dslContext;
    private static RateLimitHandler requestHandler;
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        new PlumeAlertsAPI().start();
    }

    private void start() {
        System.getProperties().setProperty("org.jooq.no-logo", "true");

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(Constants.DB_HOSTNAME);
        ds.setUsername(Constants.DB_USERNAME);
        ds.setPassword(Constants.DB_PASSWORD);
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.migrate();

        PlumeAlertsAPI.dslContext = DSL.using(ds, SQLDialect.POSTGRES);

        //TODO Read a config value in the future
        Undertow server = Undertow.builder()
                .addHttpListener(4567, "0.0.0.0")
                .setHandler(this.getHandler())
                .build();
        server.start();
        System.out.println("Started Server");
    }

    public HttpHandler getHandler() {
        RoutingHandler routing = Handlers.routing();
        routing.addAll(new AuthAPI());
        routing.addAll(new TwitchAuthAPI());
        routing.addAll(new UserAPI());
        return new BlockingHandler(new CorsHandler(routing));
    }

    public static RateLimitHandler request() {
        if (requestHandler == null) {
            requestHandler = new RateLimitHandler();
        }
        return requestHandler;
    }

    public static DSLContext dslContext() {
        return dslContext;
    }
}
