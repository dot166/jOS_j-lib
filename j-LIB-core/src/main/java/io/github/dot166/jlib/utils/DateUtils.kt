package io.github.dot166.jlib.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import io.github.dot166.jlib.time.jDate
import java.text.ParseException
import java.time.Month
import java.util.Locale

object DateUtils {
    /**
     * Parses a date string in the format "dd-MM-yyyy" and creates a jDate object.
     *
     * @param date The date string to be parsed.
     * @return A jDate object representing the parsed date.
     */
    fun convertToDateFromString(date: String): jDate {
        val dateSplit: Array<String?> =
            date.replace(" .*".toRegex(), "").split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val year = if (dateSplit.size > 2) DateUtils.parseIntOrDefault(dateSplit[2]!!, 0) else 0
        val month = if (dateSplit.size > 1) DateUtils.parseIntOrDefault(dateSplit[1]!!, 0) else 0
        val day = if (dateSplit.isNotEmpty()) DateUtils.parseIntOrDefault(dateSplit[0]!!, 0) else 0

        return jDate(
            year,
            Month.of(month),
            day
        )
    }

    private fun parseIntOrDefault(value: String, defaultValue: Int): Int {
        return try {
            value.toInt()
        } catch (_: NumberFormatException) {
            defaultValue
        }
    }

    /**
     * The target output date format: 'EEE, dd MMM yyyy HH:mm:ss Z'
     * (e.g., 'Tue, 10 Jun 2025 04:45:42 +0000')
     */
    private val TARGET_OUTPUT_FORMAT: SimpleDateFormat =
        SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)

    init {
        // It's good practice to explicitly set the desired output time zone.
        // For +0000, GMT/UTC is appropriate.
        TARGET_OUTPUT_FORMAT.timeZone = TimeZone.getTimeZone("GMT")
    }

    /**
     * Converts a date string from a specified input format to the target format:
     * 'EEE, dd MMM yyyy HH:mm:ss Z'.
     *
     * @param dateString The date string to convert.
     * @param inputFormatPattern The pattern of the input date string (e.g., "yyyy-MM-dd'T'HH:mm:ssZ").
     * @return The converted date string in 'EEE, dd MMM yyyy HH:mm:ss Z' format, or null if conversion fails.
     */
    fun convertToTargetFormat(dateString: String?, inputFormatPattern: String?): String? {
        if (dateString == null || dateString.trim { it <= ' ' }
                .isEmpty() || inputFormatPattern == null || inputFormatPattern.trim { it <= ' ' }
                .isEmpty()) {
            return null
        }

        val inputFormat = SimpleDateFormat(inputFormatPattern, Locale.ENGLISH)
        // Set lenient to false for input parsing as well, to ensure strict parsing of the input.
        inputFormat.setLenient(false)

        // For input formats that include timezone (like Z), setting timezone for parsing might not be strictly
        // necessary for correctness, but it's good for consistency.
        // If the input format implies a specific timezone (e.g., no 'Z' but known to be UTC), set it here.
        // For formats like "yyyy-MM-dd'T'HH:mm:ssZ", the 'Z' handles the timezone during parsing.
        // inputFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // Uncomment if input is always in a fixed timezone without 'Z'
        try {
            val date = inputFormat.parse(dateString)
            return TARGET_OUTPUT_FORMAT.format(date)
        } catch (e: ParseException) {
            System.err.println("Error parsing date string '" + dateString + "' with pattern '" + inputFormatPattern + "': " + e.message)
            return null
        } catch (e: Exception) { // Catch any other unexpected errors
            System.err.println("An unexpected error occurred during conversion: " + e.message)
            return null
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
    @JvmStatic
    fun convertFromCommonFormats(dateString: String?): String? {
        // List of common input date formats you might encounter.
        // You can extend this list based on your specific needs.
        val commonInputFormats = arrayOf<String?>(
            "EEE, dd MMM yyyy HH:mm:ss Z",  // expected output
            "yyyy-MM-dd'T'HH:mm:ssZ",  // ISO 8601 with Z for timezone (+0000 or +HHMM)
            "yyyy-MM-dd'T'HH:mm:ssXXX",  // ISO 8601 with X for timezone (+00:00 or Z) (API 24+)
            "yyyy-MM-dd HH:mm:ss Z",  // Common SQL/log format with timezone
            "yyyy-MM-dd HH:mm:ss",  // Date and time without timezone (assumes local or UTC)
            "MM/dd/yyyy HH:mm:ss",  // US common format
            "dd-MM-yyyy HH:mm:ss",  // European common format
            "yyyy/MM/dd HH:mm:ss",  // Alternative common format
        )

        for (pattern in commonInputFormats) {
            val converted = convertToTargetFormat(dateString, pattern)
            if (converted != null) {
                return converted // Return the first successful conversion
            }
        }
        System.err.println("No matching format found for date string: '$dateString'")
        return null // No known format matched
    }

    @JvmStatic
    fun convertDateToEpochSeconds(dateString: String?): Long {
        // Define the input date format
        // The "EEE, dd MMM yyyy HH:mm:ss Z" pattern matches "Tue, 10 Jun 2025 14:14:07 +0000"
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)

        // Set the TimeZone to UTC to ensure correct interpretation of the +0000 offset
        // This is important because the input string specifies a UTC offset.
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            // Parse the date string into a Date object
            val date = sdf.parse(dateString)

            // Get the time in milliseconds since epoch
            val milliseconds = date.time

            // Convert milliseconds to seconds
            val seconds = milliseconds / 1000

            return seconds
        } catch (e: ParseException) {
            e.printStackTrace()
            // Handle the error appropriately, e.g., throw an exception or return a default value
            return -1 // Or throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}


