package com.plumealerts.api;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqCodegen {

    public static void main(String[] args) throws Exception {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(System.getenv("DB_HOSTNAME"));
        ds.setUsername(System.getenv("DB_USERNAME"));
        ds.setPassword(System.getenv("DB_PASSWORD"));
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.clean();
        flyway.migrate();

        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withUrl(ds.getJdbcUrl())
                        .withUser(ds.getUsername())
                        .withPassword(ds.getPassword())
                )
                .withGenerator(
                        new Generator()
                                .withName("org.jooq.codegen.JavaGenerator")
                                .withDatabase(new Database()
                                        .withName("org.jooq.meta.postgres.PostgresDatabase")
                                        .withExcludes("flyway_schema_history")
                                        .withInputSchema("public")
                                )
                                .withTarget(new Target()
                                        .withPackageName("com.plumealerts.api.db")
                                        .withDirectory("src/main/java")
                                )
                );
        GenerationTool.generate(configuration);
    }

}