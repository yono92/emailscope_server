package com.yono.emailscope.utils;

import org.apache.james.mime4j.dom.TextBody;

import java.io.InputStreamReader;

public class EmailUtils {
    public static String textBodyToString(TextBody textBody) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(textBody.getInputStream(), "UTF-8")) {
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
        }
        return sb.toString();
    }
}
