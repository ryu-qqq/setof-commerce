package com.setof.connectly.module.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    public static String extractDuplicateEntryMessage(String exceptionMessage) {
        Pattern pattern = Pattern.compile("'(.+?)'");
        Matcher matcher = pattern.matcher(exceptionMessage);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "Duplicate entry not found";
    }
}
