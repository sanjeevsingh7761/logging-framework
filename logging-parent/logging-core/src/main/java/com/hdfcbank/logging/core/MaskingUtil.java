package com.hdfcbank.logging.core;

public final class MaskingUtil {
    private MaskingUtil() {}
    public static String maskPan(String pan) {
        if (pan == null || pan.length() <= 4) return "****";
        return "XXXXXX" + pan.substring(pan.length() - 4);
    }
    public static String maskAadhaar(String a) {
        if (a == null || a.length() <= 4) return "****";
        return "XXXXXXXX" + a.substring(a.length() - 4);
    }
}
