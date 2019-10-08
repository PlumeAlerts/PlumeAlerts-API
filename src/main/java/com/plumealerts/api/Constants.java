package com.plumealerts.api;

import org.apache.commons.io.IOUtils;
import org.jose4j.base64url.SimplePEMEncoder;
import org.jose4j.jws.AlgorithmIdentifiers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Constants {

    public static final String ALGORITHM_IDENTIFIERS = AlgorithmIdentifiers.RSA_USING_SHA512;

    public static final String TWITCH_CLIENT_ID = getRequiredValue("CLIENT_ID");
    public static final String TWITCH_CLIENT_SECRET = getRequiredValue("CLIENT_SECRET");
    public static final String TWITCH_CLIENT_REDIRECT = getRequiredValue("CLIENT_REDIRECT");

    public static final String ALLOW_ORIGIN = getValueOrDefault("ALLOW_ORIGIN", "http://localhost:8080");
    public static final String DB_HOSTNAME = getValueOrDefault("DB_HOSTNAME", "jdbc:postgresql://localhost:5432/plumealerts");
    public static final String DB_USERNAME = getValueOrDefault("DB_USERNAME", "root");
    public static final String DB_PASSWORD = getValueOrDefault("DB_PASSWORD", "");

    public static PublicKey publicKey;
    public static PrivateKey privateKey;
    public static KeyFactory keyFactory;

    static {
        //TODO Force stop or switch to not require a token
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            publicKey = getPublicKey("public.pem");
            privateKey = getPrivateKey("private.pem");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static PrivateKey getPrivateKey(String fileLocation) throws InvalidKeySpecException, IOException {
        String privateKey = getKey(fileLocation);
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
        KeySpec spec = new PKCS8EncodedKeySpec(SimplePEMEncoder.decode(privateKey));
        return keyFactory.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String fileLocation) throws InvalidKeySpecException, IOException {
        String publicKey = getKey(fileLocation);
        publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(SimplePEMEncoder.decode(publicKey));
        return keyFactory.generatePublic(spec);
    }

    public static String getKey(String fileLocation) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        return IOUtils.toString(fileInputStream, Charset.defaultCharset()).replaceAll("\n", "").replaceAll("\r", "");
    }
}
