package com.example.util;

import java.util.Collection;
import java.util.Map;

/**
 * 做空/非空校验的工具类
 */
public class ValidateUtil {
    public static final String ContiguousUSStateCodes = "AL|AZ|AR|CA|CO|CT|DE|DC|FL|GA|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY";

    public ValidateUtil() {
    }

    public static boolean areEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equals(obj2);
        }
    }

    public static boolean areEqualIgnoreCase(String obj, String obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equalsIgnoreCase(obj2);
        }
    }

    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof String) {
            return ((String)value).length() == 0;
        } else if (value instanceof Collection) {
            return ((Collection)value).isEmpty();
        } else if (value instanceof Map) {
            return ((Map)value).size() == 0;
        } else if (value instanceof String[]) {
            return ((String[])((String[])value)).length == 0;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isNotEmpty(Collection c) {
        return c != null && !c.isEmpty();
    }

    public static boolean isString(Object obj) {
        return obj instanceof String;
    }
}
