package com.plumealerts.api.utils;

import com.jsoniter.output.JsonStream;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorResponse;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class ResponseUtil {

    private ResponseUtil() {
    }

    private static Domain response(HttpServerExchange exchange, int status, Domain domain) {
        exchange.setStatusCode(status);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        if (domain != null) {
            exchange.getResponseSender().send(JsonStream.serialize(domain));
        }
        return domain;
    }

    public static Domain successResponse(HttpServerExchange exchange, Domain domain) {
        return response(exchange, 200, domain);
    }


    public static Domain errorResponse(HttpServerExchange exchange, ErrorType errorType, String message) {
        return response(exchange, errorType.getCode(), new ErrorResponse(errorType, message));
    }


}
