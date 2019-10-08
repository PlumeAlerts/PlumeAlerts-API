package com.plumealerts.api.handler.user;

import com.plumealerts.api.Constants;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

public class JWT extends JwtClaims {

    private static final String ISSUER = "PlumeAlerts";
    private static final String AUDIENCE = "plumealerts.com";

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

        jws.setKey(Constants.privateKey);
        jws.setAlgorithmHeaderValue(Constants.ALGORITHM_IDENTIFIERS);

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
                .setVerificationKey(Constants.publicKey)
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, Constants.ALGORITHM_IDENTIFIERS))
                .build();
        return jwtConsumer.processToClaims(token);
    }


}
