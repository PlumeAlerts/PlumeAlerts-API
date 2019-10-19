package com.plumealerts.api.utils;

import io.undertow.server.HttpServerExchange;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;

public class Validate {

    private Validate() {

    }

    public static String getQueryParam(HttpServerExchange exchange, String paramName) {
        Deque<String> param = exchange.getQueryParameters().get(paramName);

        if (param == null) {
            return null;
        }
        return param.peek();
    }

    public static boolean isAlphanumericAndLength(String text, int length) {
        return text != null && text.length() == length && StringUtils.isAlphanumeric(text);
    }
}
