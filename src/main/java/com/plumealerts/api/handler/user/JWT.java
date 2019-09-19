package com.plumealerts.api.handler.user;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

public class JWT extends JwtClaims {

    public JWT(String userId, NumericDate numericDate) {
        this.setIssuer("PlumeAlerts");
        this.setGeneratedJwtId();
        this.setIssuedAtToNow();
        this.setAudience(userId);
        this.setExpirationTime(numericDate);
        this.setClaim("user_id", userId);
    }

    public String generate() throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(this.toJson());
//        jws.setKey(privateKey);
//        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);
        jws.setAlgorithmConstraints(AlgorithmConstraints.NO_CONSTRAINTS);//TODO remove
        jws.setDoKeyValidation(false); //TODO remove
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.NONE); //TODO remove

        return jws.getCompactSerialization();
    }
}
