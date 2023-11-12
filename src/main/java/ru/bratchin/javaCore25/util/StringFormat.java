package ru.bratchin.javaCore25.util;

import org.apache.commons.lang3.StringUtils;

public class StringFormat {
    public static String nameFormat(String str) {
        String trimStr = StringUtils.trim(str);
        String lowerCase = StringUtils.toRootLowerCase(trimStr);
        return StringUtils.capitalize(lowerCase);
    }
}
