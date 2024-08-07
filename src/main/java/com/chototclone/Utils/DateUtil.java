package com.chototclone.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for performing various operations on dates.
 */
public class DateUtil {

    /**
     * Adds a specified number of days to the current date.
     *
     * @param days the number of days to add (can be positive or negative)
     * @return the resulting date after adding the specified number of days
     */
    public static Date addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    /**
     * Subtracts a specified number of days from the current date.
     *
     * @param days the number of days to subtract (can be positive or negative)
     * @return the resulting date after subtracting the specified number of days
     */
    public static Date subtractDays(int days) {
        return addDays(-days);
    }

    /**
     * Sets the time of a date to the start of the day (00:00:00).
     *
     * @param date the date to modify
     * @return the date with the time set to the start of the day
     */
    public static Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Sets the time of a date to the end of the day (23:59:59).
     *
     * @param date the date to modify
     * @return the date with the time set to the end of the day
     */
    public static Date endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * Returns the number of days between two dates.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the number of days between the two dates
     */
    public static long daysBetween(Date startDate, Date endDate) {
        long differenceInMillis = endDate.getTime() - startDate.getTime();
        return differenceInMillis / (24 * 60 * 60 * 1000);
    }

    /**
     * Gets the current date in the specified time zone.
     *
     * @param timeZoneId the ID of the time zone (e.g., "America/New_York")
     * @return the current date in the specified time zone
     */
    public static Date getCurrentDateInTimeZone(String timeZoneId) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
        return calendar.getTime();
    }

    /**
     * Formats a date to a string in the specified format.
     *
     * @param date   the date to format
     * @param format the date format (e.g., "yyyy-MM-dd HH:mm:ss")
     * @return the formatted date string
     */
    public static String formatDate(Date date, String format) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Parses a string to a date using the specified format.
     *
     * @param dateString the date string to parse
     * @param format     the date format (e.g., "yyyy-MM-dd HH:mm:ss")
     * @return the parsed date
     * @throws java.text.ParseException if the date string cannot be parsed
     */
    public static Date parseDate(String dateString, String format) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.parse(dateString);
    }
}
