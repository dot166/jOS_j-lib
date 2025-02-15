package io.github.dot166.jLib.utils;

import java.time.Month;

import io.github.dot166.jLib.time.jDate;

public class DateUtils {

    /**
     * Parses a date string in the format "dd-MM-yyyy" and creates a jDate object.
     *
     * @param date The date string to be parsed.
     * @return A jDate object representing the parsed date.
     */
    public static jDate convertToDateFromString(String date) {
        String[] dateSplit = date.replaceAll(" .*", "").split("/");
        int year = (dateSplit.length > 2) ? parseIntOrDefault(dateSplit[2], 0) : 0;
        int month = (dateSplit.length > 1) ? parseIntOrDefault(dateSplit[1], 0) : 0;
        int day = (dateSplit.length > 0) ? parseIntOrDefault(dateSplit[0], 0) : 0;

        return new jDate(
                year,
                Month.of(month),
                day
        );
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}


