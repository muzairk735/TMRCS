package com.telemedicine.models;

public final class UIHelper {

    private UIHelper() {}

    public static void printRow(String label, String value, int innerWidth) {
        String prefix = " " + label + ": ";
        int valueWidth = innerWidth - prefix.length();
        if (valueWidth < 1) valueWidth = 1;
        String v = value != null ? value : "";
        if (v.length() > valueWidth) v = v.substring(0, valueWidth - 1) + "…";
        System.out.println("║" + prefix + padRight(v, valueWidth) + "║");
    }

    public static String padRight(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        return s + " ".repeat(width - s.length());
    }

    public static String center(String s, int width) {
        if (s == null) s = "";
        int pad = width - s.length();
        if (pad < 0) pad = 0;
        int left = pad / 2;
        int right = pad - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }
}
