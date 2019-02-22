package com.carriel.gregory.moodtracker.controler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class MyToolsDate {

    private static final String TAG= "MoodMessage: Tools:";

    private static final long CONST_DURATION_OF_DAY = 1000l * 60 * 60 * 24;


    /**
     * convert a date to String in the format (yyyy-MM-dd)
     * @param uneDate
     * @return date in the format string
     */
    public static String convertDatetoString(Date uneDate){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = formatter.format(uneDate);

        return date1; //yyyy-MM-dd en String
    }

    /**
     * convert a String to Date into format (yyyy-MM-dd)
     * @param uneDate
     * @return date in the format Date
     */
    public static Date convertStringtoDate(String uneDate){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateConvert = null;
        try {
            dateConvert = formatter.parse(uneDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date dateRecord = new java.sql.Date(dateConvert.getTime());
        return dateRecord;//yyyy-MM-dd en Date()
    }

    /**
     * recover new date and old date for know difference day between the 2
     * @param pNewDate
     * @param pLastDate
     * @return difference of day between old date and new date
     */
    public static long compareDate(Date pNewDate, Date pLastDate){

        long diff = Math.abs(pNewDate.getTime() - pLastDate.getTime());
        long numberOfDay = diff/CONST_DURATION_OF_DAY;
        System.out.println(numberOfDay);

        return numberOfDay;
    }

    /**
     *subtracted somes days of current day
     */
    public static Date substractDay(int day){
        Calendar todayCalendar= Calendar.getInstance();

        todayCalendar.add(Calendar.DATE, -day);
        java.sql.Date resultDay = new java.sql.Date(todayCalendar.getTimeInMillis());
        return resultDay;
    }

}
