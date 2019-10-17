package com.plumealerts.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plumealerts.api.endpoints.v1.domain.Data;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorResponse;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import static com.plumealerts.api.PlumeAlertsAPI.MAPPER;

public class ResponseUtil {

    private ResponseUtil() {
    }

    private static Domain response(HttpServerExchange exchange, int status, Domain domain) {
        exchange.setStatusCode(status);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        if (domain != null) {
            try {
                exchange.getResponseSender().send(MAPPER.writeValueAsString(domain));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
            }
        }
        return domain;
    }

    public static Domain successResponse(HttpServerExchange exchange, Object data) {
        return response(exchange, 200, new Data(data));
    }


    public static Domain errorResponse(HttpServerExchange exchange, ErrorType errorType, String message) {
        return response(exchange, errorType.getCode(), new ErrorResponse(errorType, message));
    }


}
