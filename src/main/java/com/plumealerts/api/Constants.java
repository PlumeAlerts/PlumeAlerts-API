package com.plumealerts.api;

public class Constants {

    public static final String TWITCH_CLIENT_ID = getRequiredValue("CLIENT_ID");
    public static final String TWITCH_CLIENT_SECRET = getRequiredValue("CLIENT_SECRET");
    public static final String TWITCH_CLIENT_REDIRECT = getRequiredValue("CLIENT_REDIRECT");

    public static final String DB_HOSTNAME = getValueOrDefault("DB_HOSTNAME", "jdbc:postgresql://localhost:5432/plumealerts");
    public static final String DB_USERNAME = getValueOrDefault("DB_USERNAME", "root");
    public static final String DB_PASSWORD = getValueOrDefault("DB_PASSWORD", "");

    private Constants() {
    }

    private static String getRequiredValue(String env) {
        String value = System.getenv(env);
        if (value == null) {
            System.err.println("Missing env variable " + env);
            System.exit(1);
        }
        return value;
    }

    private static String getValueOrDefault(String env, String defaultValue) {
        String value = System.getenv(env);
        if (value == null)
            return defaultValue;
        return value;
    }
}
