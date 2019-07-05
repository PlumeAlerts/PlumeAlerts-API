package com.plumealerts.api.utils;

import org.apache.commons.lang3.StringUtils;

public class Validate {

    private Validate() {

    }

    public static boolean isAlphanumericAndLength(String text, int length) {
        return text != null && text.length() == length && StringUtils.isAlphanumeric(text);
    }
}
