package com.paulocaldeira.movies.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 2/3/17
 */

public class FormatHelper {
    // Constants
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String YEAR_FORMAT = "yyyy";

    /**
     * Formats a date object to a year string
     * @param date Date object
     * @return Date
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        return formatter.format(date);
    }

    /**
     * Formats a date object a date string
     * @param date Date object
     * @return Date string
     */
    public static String formatYear(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(YEAR_FORMAT);

        return formatter.format(date);
    }

    /**
     * Parses a date from string
     * @param dateString Date string
     * @return Date
     */
    public static Date parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }

        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
