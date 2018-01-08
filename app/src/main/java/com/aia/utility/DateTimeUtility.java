package com.aia.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtility
{

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String VISIT_TIME = "hh:mm a";
    private static final String VISIT_DATE = "dd/MM";

    /**
     * Convert date from utc to local
     *
     * @param pDate
     * @return
     */
    public static Date convertToLocal(Date pDate) {
        TimeZone lTimeZone = TimeZone.getDefault();
        // Log.d("TAG", "UTC Date = " + pDate);
        // Date lDate = new Date(pDate.getTime() + lTimeZone.getRawOffset());

        // Log.d("TAG", "Local lDate2 = " + lDate);
        return new Date(pDate.getTime()
                + lTimeZone.getOffset(pDate.getTime()));
    }

    /**
     * Comvert date in universal time
     *
     * @param pDate
     * @return
     */
    public static Date convertToUTC(Date pDate) {
        TimeZone lTimeZone = TimeZone.getDefault();

        return new Date(pDate.getTime()
                - lTimeZone.getOffset(pDate.getTime()));
    }

    public static Date convertStringToDate(String strDate){
        Date dateObject = null;
        //Log.d("String date","String = "+strDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateObject = dateFormat.parse(strDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateObject;
    }

    public static String getMonthInString(int value){
        String retVal = "";
        if (value == 0)
            retVal = "Jan";
        if (value == 1)
            retVal = "Feb";
        if (value == 2)
            retVal = "Mar";
        if (value == 3)
            retVal = "Apr";
        if (value == 4)
            retVal = "May";
        if (value == 5)
            retVal = "Jun";
        if (value == 6)
            retVal = "Jul";
        if (value == 7)
            retVal = "Aug";
        if (value == 8)
            retVal = "Sep";
        if (value == 9)
            retVal = "Oct";
        if (value == 10)
            retVal = "Nov";
        if (value == 11)
            retVal = "Dec";

        return retVal;
    }

    public static String displayDate(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        dateValue = day + " " + getMonthInString(month) + " "+year;
        return dateValue;
    }


    public static String displayTime(){
        /*String dateValue = "";
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        dateValue = hour +":"+ minute;
        return dateValue;*/

        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);
        int ampm = c.get(Calendar.AM_PM);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedTime = sdf.format(c.getTime());

        return formattedTime;
    }

    public static String getCurrentDate(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        dateValue = day + "/" + (month+1) + "/" +year;
        return dateValue;
    }

    public static String getTodaySend(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        dateValue = day + "/" + (month+1) + "/" +year;
        return dateValue;
    }

    public static String getTomorrowSend(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        dateValue = day + "/" + (month+1) + "/" +year;
        return dateValue;
    }

    public static String getFifteenDaysDate(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,15);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        dateValue = day + "/" + (month+1) + "/" +year;
        return dateValue;
    }

    public static String getDisplayDateToday(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        dateValue = day + " " + getMonthInString(month) + " "+year;
        return dateValue;
    }

    public static String getDisplayDateTomorrow(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        c.add(Calendar.DAY_OF_MONTH,1);
        //dateValue = day + " " + getMonthInString(month) + " "+year;
        dateValue = c.get(Calendar.DAY_OF_MONTH) + " " + getMonthInString(c.get(Calendar.MONTH)) + " " + c.get(Calendar.YEAR) ;//getMonthInString(month) + " "+year;
        return dateValue;
    }

    public static String getDisplayDateAfterFifteenDays(){
        String dateValue = "";
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        c.add(Calendar.DAY_OF_MONTH,15);
        //dateValue = day + " " + getMonthInString(month) + " "+year;
        dateValue = c.get(Calendar.DAY_OF_MONTH) + " " + getMonthInString(c.get(Calendar.MONTH)) + " " + c.get(Calendar.YEAR) ;//getMonthInString(month) + " "+year;
        return dateValue;
    }

    public static String displayFifteenDaysAfterDate(String stringCheckInDate){
        String dateValue = "";
        Date checkInDate = null;
        Calendar c=null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            checkInDate = formatter.parse(stringCheckInDate);
        } catch (ParseException e) {
            //Log.d("Parse ex",e.getMessage());
            e.printStackTrace();
        }
        c = Calendar.getInstance();
        c.setTime(checkInDate);
        c.add(Calendar.DAY_OF_MONTH,15);
        dateValue = c.get(Calendar.DAY_OF_MONTH) + " " + getMonthInString(c.get(Calendar.MONTH)) + " " + c.get(Calendar.YEAR) ;//getMonthInString(month) + " "+year;
        return dateValue;
    }

    public static String stringFifteenDaysAfterDate(String stringCheckInDate){
        String dateValue = "";
        Date checkInDate = null;
        Calendar c=null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            checkInDate = formatter.parse(stringCheckInDate);
        } catch (ParseException e) {
            //Log.d("Parse ex",e.getMessage());
            e.printStackTrace();
        }
        c = Calendar.getInstance();
        c.setTime(checkInDate);
        c.add(Calendar.DAY_OF_MONTH,15);
        dateValue = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR) ;//getMonthInString(month) + " "+year;
        return dateValue;
    }



}
