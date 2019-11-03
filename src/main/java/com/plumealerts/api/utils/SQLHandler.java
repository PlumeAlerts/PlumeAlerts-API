package com.plumealerts.api.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public final class SQLHandler {
    private SQLHandler() {
    }

    public static String readFile(String filename) {
        try {
            File f = new File(SQLHandler.class.getClassLoader().getResource("db/query/" + filename).getFile());
            return FileUtils.readFileToString(f, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO Throw exception(crash?)
        }
        return null;
    }
}
