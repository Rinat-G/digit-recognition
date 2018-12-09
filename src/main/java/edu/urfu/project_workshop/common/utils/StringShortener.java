package edu.urfu.project_workshop.common.utils;

public class StringShortener {
    private static final int LENGTH = 50;

    public static String shortenString(final String input) {
        return shortenString(input, LENGTH);
    }

    public static String shortenString(final String input, final Integer length) {
        if (input.length() < length) {
            return input;
        }
        return input.substring(0, length) + "<...>";
    }
}
