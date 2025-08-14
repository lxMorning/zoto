//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sparkframework.util;

import org.apache.commons.lang.StringUtils;

public class SqlInfusion {
    public static String FilterSqlInfusion(String input) {
        if (input != null && input.trim() != "") {
            return !StringUtils.isNumeric(input) ? input.replaceAll("\\b(drop|exec|execute|create|truncate|delete|insert|update)\\b", "`$1`") : input;
        } else {
            return "";
        }
    }
}
