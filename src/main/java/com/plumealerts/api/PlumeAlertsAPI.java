package com.plumealerts.api;

import com.plumealerts.api.endpoints.AuthAPI;
import com.plumealerts.api.utils.Constants;
import com.squareup.moshi.Moshi;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import spark.Spark;

public class PlumeAlertsAPI {
    private static DSLContext dslContext;
    public static final Moshi moshi = new Moshi.Builder().build();

    public static void main(String[] args) {
        System.getProperties().setProperty("org.jooq.no-logo", "true");

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(Constants.DB_URL);
        ds.setUsername(Constants.DB_USERNAME);
        ds.setPassword(Constants.DB_PASSWORD);
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.migrate();

        PlumeAlertsAPI.dslContext = DSL.using(ds, SQLDialect.POSTGRES_9_5);

        new PlumeAlertsAPI().start();
    }

    public void start() {
        Spark.path("/v1", () -> Spark.path("/auth", AuthAPI::new));
    }

    public static DSLContext dslContext() {
        return dslContext;
    }
}
