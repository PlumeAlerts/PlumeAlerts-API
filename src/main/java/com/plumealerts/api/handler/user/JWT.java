package com.plumealerts.api.handler.user;

import org.apache.commons.io.IOUtils;
import org.jose4j.base64url.SimplePEMEncoder;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

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

public class JWT extends JwtClaims {

    private static final String ISSUER = "PlumeAlerts";
    private static final String AUDIENCE = "plumealerts.com";

    private static KeyFactory KEY_FACTORY;
    private static final String ALGORITHM_IDENTIFIERS = AlgorithmIdentifiers.RSA_USING_SHA512;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    static {
        //TODO Force stop or switch to not require a token
        try {
            KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = getPrivateKey("private.pem");
            publicKey = getPublicKey("public.pem");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JWT(String userId, TokenType type, NumericDate numericDate) {
        this.setIssuer(ISSUER);
        this.setGeneratedJwtId();
        this.setIssuedAtToNow();
        this.setAudience(AUDIENCE);
        this.setSubject(userId);
        this.setExpirationTime(numericDate);
        this.setClaim("type", type);
    }

    public String generate() throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(ALGORITHM_IDENTIFIERS);

        jws.setPayload(this.toJson());

        return jws.getCompactSerialization();
    }

    public static JwtClaims decrypt(String token) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedAudience(AUDIENCE)
                .setExpectedIssuer(ISSUER)
                .setVerificationKey(publicKey)
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, ALGORITHM_IDENTIFIERS))
                .build();
        return jwtConsumer.processToClaims(token);
    }

    public static PrivateKey getPrivateKey(String fileLocation) throws InvalidKeySpecException, IOException {
        String privateKey = getKey(fileLocation);
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
        KeySpec spec = new PKCS8EncodedKeySpec(SimplePEMEncoder.decode(privateKey));
        return KEY_FACTORY.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String fileLocation) throws InvalidKeySpecException, IOException {
        String publicKey = getKey(fileLocation);
        publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(SimplePEMEncoder.decode(publicKey));
        return KEY_FACTORY.generatePublic(spec);
    }

    public static String getKey(String fileLocation) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        return IOUtils.toString(fileInputStream, Charset.defaultCharset()).replaceAll("\n", "").replaceAll("\r", "");
    }
}
