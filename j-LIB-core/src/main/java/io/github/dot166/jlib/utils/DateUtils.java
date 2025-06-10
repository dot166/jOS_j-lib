package io.github.dot166.jlib.utils;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.text.ParseException;
import java.time.Month;
import java.util.Date;
import java.util.Locale;

import io.github.dot166.jlib.time.jDate;

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

    /**
     * The target output date format: 'EEE, dd MMM yyyy HH:mm:ss Z'
     * (e.g., 'Tue, 10 Jun 2025 04:45:42 +0000')
     */
    private static final SimpleDateFormat TARGET_OUTPUT_FORMAT;

    static {
        TARGET_OUTPUT_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        // It's good practice to explicitly set the desired output time zone.
        // For +0000, GMT/UTC is appropriate.
        TARGET_OUTPUT_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Converts a date string from a specified input format to the target format:
     * 'EEE, dd MMM yyyy HH:mm:ss Z'.
     *
     * @param dateString The date string to convert.
     * @param inputFormatPattern The pattern of the input date string (e.g., "yyyy-MM-dd'T'HH:mm:ssZ").
     * @return The converted date string in 'EEE, dd MMM yyyy HH:mm:ss Z' format, or null if conversion fails.
     */
    public static String convertToTargetFormat(String dateString, String inputFormatPattern) {
        if (dateString == null || dateString.trim().isEmpty() || inputFormatPattern == null || inputFormatPattern.trim().isEmpty()) {
            return null;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatPattern, Locale.ENGLISH);
        // Set lenient to false for input parsing as well, to ensure strict parsing of the input.
        inputFormat.setLenient(false);
        // For input formats that include timezone (like Z), setting timezone for parsing might not be strictly
        // necessary for correctness, but it's good for consistency.
        // If the input format implies a specific timezone (e.g., no 'Z' but known to be UTC), set it here.
        // For formats like "yyyy-MM-dd'T'HH:mm:ssZ", the 'Z' handles the timezone during parsing.
        // inputFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // Uncomment if input is always in a fixed timezone without 'Z'

        try {
            Date date = inputFormat.parse(dateString);
            return TARGET_OUTPUT_FORMAT.format(date);
        } catch (ParseException e) {
            System.err.println("Error parsing date string '" + dateString + "' with pattern '" + inputFormatPattern + "': " + e.getMessage());
            return null;
        } catch (Exception e) { // Catch any other unexpected errors
            System.err.println("An unexpected error occurred during conversion: " + e.getMessage());
            return null;
        }
    }

    /**
     * Attempts to convert a date string from a predefined set of common formats
     * to the target format: 'EEE, dd MMM yyyy HH:mm:ss Z'.
     * It tries each format in order until one succeeds.
     *
     * @param dateString The date string to convert.
     * @return The converted date string in 'EEE, dd MMM yyyy HH:mm:ss Z' format, or null if no format matches.
     */
    public static String convertFromCommonFormats(String dateString) {
        // List of common input date formats you might encounter.
        // You can extend this list based on your specific needs.
        String[] commonInputFormats = {
                "yyyy-MM-dd'T'HH:mm:ssZ",       // ISO 8601 with Z for timezone (+0000 or +HHMM)
                "yyyy-MM-dd'T'HH:mm:ssXXX",     // ISO 8601 with X for timezone (+00:00 or Z) (API 24+)
                "yyyy-MM-dd HH:mm:ss Z",        // Common SQL/log format with timezone
                "yyyy-MM-dd HH:mm:ss",          // Date and time without timezone (assumes local or UTC)
                "MM/dd/yyyy HH:mm:ss",          // US common format
                "dd-MM-yyyy HH:mm:ss",          // European common format
                "yyyy/MM/dd HH:mm:ss",          // Alternative common format
                "EEE, dd MMM yyyy HH:mm:ss Z"   // output (compatibility)
        };

        for (String pattern : commonInputFormats) {
            String converted = convertToTargetFormat(dateString, pattern);
            if (converted != null) {
                return converted; // Return the first successful conversion
            }
        }
        System.err.println("No matching format found for date string: '" + dateString + "'");
        return null; // No known format matched
    }

    public static long convertDateToEpochSeconds(String dateString) {
        // Define the input date format
        // The "EEE, dd MMM yyyy HH:mm:ss Z" pattern matches "Tue, 10 Jun 2025 14:14:07 +0000"
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

        // Set the TimeZone to UTC to ensure correct interpretation of the +0000 offset
        // This is important because the input string specifies a UTC offset.
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            // Parse the date string into a Date object
            Date date = sdf.parse(dateString);

            // Get the time in milliseconds since epoch
            long milliseconds = date.getTime();

            // Convert milliseconds to seconds
            long seconds = milliseconds / 1000;

            return seconds;

        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the error appropriately, e.g., throw an exception or return a default value
            return -1; // Or throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}


