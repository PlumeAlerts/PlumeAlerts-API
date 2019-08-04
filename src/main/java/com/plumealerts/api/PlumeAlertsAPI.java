package com.plumealerts.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumealerts.api.ratelimit.RequestHandler;
import com.plumealerts.api.v1.TwitchAuth;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import spark.Spark;

public class PlumeAlertsAPI {
    private static DSLContext dslContext;
    private static RequestHandler requestHandler;
    private static ObjectMapper mapper;

    public static void main(String[] args) {
        new PlumeAlertsAPI().start();
    }

    public void start() {
        System.getProperties().setProperty("org.jooq.no-logo", "true");

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(Constants.DB_URL);
        ds.setUsername(Constants.DB_USERNAME);
        ds.setPassword(Constants.DB_PASSWORD);
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.migrate();

        PlumeAlertsAPI.dslContext = DSL.using(ds, SQLDialect.POSTGRES_9_5);

        Spark.path("/v1", () -> Spark.path("/auth/twitch", TwitchAuth::new));
    }

    public static RequestHandler request() {
        if (requestHandler == null) {
            requestHandler = new RequestHandler();
        }
        return requestHandler;
    }

    public static ObjectMapper mapper(){
        if(mapper == null){
            mapper = new ObjectMapper();
        }

        return mapper;
    }
    public static DSLContext dslContext() {
        return dslContext;
    }
}
