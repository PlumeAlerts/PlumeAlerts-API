package com.plumealerts.api.utils;

import com.jsoniter.output.JsonStream;
import com.plumealerts.api.v1.domain.error.ErrorResponse;
import com.plumealerts.api.v1.domain.error.ErrorType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class ResponseUtil {

    private ResponseUtil() {
    }

    public static void response(HttpServerExchange exchange, Object object) {
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(JsonStream.serialize(object));
    }

    public static void errorResponse(HttpServerExchange exchange, ErrorType errorType, String message) {
        exchange.setStatusCode(errorType.getCode());
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send(JsonStream.serialize(new ErrorResponse(errorType, message)));
    }
}
