package com.plumealerts.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plumealerts.api.endpoints.v1.domain.DataDomain;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorResponse;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.plumealerts.api.PlumeAlertsAPI.MAPPER;

public final class ResponseUtil {
    private static final Logger LOGGER = Logger.getLogger(ResponseUtil.class.getName());

    private ResponseUtil() {
    }

    private static Domain response(HttpServerExchange exchange, int status, Domain domain) {
        exchange.setStatusCode(status);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        if (domain != null) {
            try {
                exchange.getResponseSender().send(MAPPER.writeValueAsString(domain));
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "Error writing json", e);
                return errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
            }
        }
        return domain;
    }

    public static Domain successResponse(HttpServerExchange exchange, Object data) {
        return response(exchange, 200, new DataDomain(data));
    }

    public static Domain errorResponse(HttpServerExchange exchange, ErrorType errorType, String message) {
        return response(exchange, errorType.getCode(), new ErrorResponse(errorType, message));
    }
}