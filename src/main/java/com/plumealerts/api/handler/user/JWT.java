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
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class JWT extends JwtClaims {

    private static final String ISSUER = "PlumeAlerts";
    private static final String AUDIENCE = "plumealerts.com";

    private static final String ALGORITHM_IDENTIFIERS = AlgorithmIdentifiers.RSA_USING_SHA512;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    static {
        try {
            privateKey = getPrivateKey("private.pem");
            publicKey = getPublicKey("public.pem");

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JWT(String userId, String type, NumericDate numericDate) {
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

    public static PrivateKey getPrivateKey(String fileLocation) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        String privateKeyPEM = IOUtils.toString(fileInputStream, Charset.defaultCharset());
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("\n", "");
        privateKeyPEM = privateKeyPEM.replaceAll("\r", "");
        KeySpec spec = new PKCS8EncodedKeySpec(SimplePEMEncoder.decode(privateKeyPEM));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String fileLocation) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        String privateKeyPEM = IOUtils.toString(fileInputStream, Charset.defaultCharset());
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PUBLIC KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("\n", "");
        privateKeyPEM = privateKeyPEM.replaceAll("\r", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(SimplePEMEncoder.decode(privateKeyPEM));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
}
