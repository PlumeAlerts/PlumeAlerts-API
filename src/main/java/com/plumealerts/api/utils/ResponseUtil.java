package com.plumealerts.api.utils;

import com.plumealerts.api.endpoints.response.error.ErrorResponse;
import com.plumealerts.api.endpoints.response.error.ErrorType;
import org.apache.http.HttpStatus;
import spark.ModelAndView;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    private ResponseUtil() {

    }

    public static String redirect(Response response, String url) {
        return redirect(response, url, HttpStatus.SC_SEE_OTHER);
    }

    public static String redirect(Response response, String url, int code) {
        response.status(code);
        response.header("Location", url);

        Map<String, String> model = new HashMap<>();
        model.put("redirect", url);
        return new MustacheTemplateEngine().render(new ModelAndView(model, "redirect.mustache"));
    }

    public static ErrorResponse errorResponse(Response response, ErrorType errorType, String message) {
        response.status(errorType.getCode());
        return new ErrorResponse(errorType, message);
    }
}
