package com.itouch8.pump.util.toolimpl;

import java.text.ParseException;
import java.util.Date;

import com.itouch8.pump.core.util.CoreUtils;


public abstract class DateUtilsImpl {

    private static final DateUtilsImpl instance = new DateUtilsImpl() {};

    private DateUtilsImpl() {}

    public static void main(String[] args) throws ParseException {
        System.out.println(getInstance().dateCalculate("20160620", 0, 0, 128));
    }

    
    public static DateUtilsImpl getInstance() {
        return instance;
    }

    
    public String getDate() {
        return CoreUtils.getDate();
    }

    
    public String getTime() {
        return CoreUtils.getTime();
    }

    
    public String getDateAndTime() {
        return CoreUtils.getDateAndTime();
    }

    
    public String getDateAndTime(Date date) {
        return CoreUtils.getDateAndTime(date);
    }

    
    public String getFormatDate(Date date, String format) {
        return CoreUtils.getFormatDate(date, format);
    }

    
    public String dateCalculate(String date, int mYear, int mMonth, int mDay) {
        return CoreUtils.dateCalculate(date, mYear, mMonth, mDay);
    }

    
    public String dateCalculate(String date, String format, int mYear, int mMonth, int mDay) {
        return CoreUtils.dateCalculate(date, format, mYear, mMonth, mDay);
    }

    
    public Date dateCalculate(Date date, int mYear, int mMonth, int mDay) {
        return CoreUtils.dateCalculate(date, mYear, mMonth, mDay);
    }

    
    public int dayOfYear(int year, int month, int day) {
        return CoreUtils.dayOfYear(year, month, day);
    }

    
    public boolean isLeapYear(int year) {
        return CoreUtils.isLeapYear(year);
    }

    
    public int getMaxDayOfMonth(int year, int month) {
        return CoreUtils.getMaxDayOfMonth(year, month);
    }

    
    public long getTime(String datetime, String datetimeFormat) {
        return CoreUtils.getTime(datetime, datetimeFormat);
    }

    
    public boolean isValidDate(String date, String format) {
        boolean result = false;

        try {
            int year, month, day;
            int minYear = 1800, maxYear = 2200;
            if (null == date)
                return false;
            if ("YYYY".equalsIgnoreCase(format)) {
                date = date.trim();
                if (date.length() == 4) {
                    year = Integer.parseInt(date);
                    return year >= minYear && year <= maxYear;
                } else {
                    return false;
                }
            } else if ("YYYYMM".equalsIgnoreCase(format)) {
                date = date.trim();
                if (date.length() == 6) {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(4));
                    return year >= minYear && year <= maxYear && month <= 12 && month >= 1;
                } else {
                    return false;
                }
            } else if ("YYYYMMDD".equalsIgnoreCase(format)) {
                date = date.trim();
                if (date.length() == 8) {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(4, 6));
                    day = Integer.parseInt(date.substring(6));
                } else {
                    return false;
                }
            } else if ("YYYY-MM-DD".equalsIgnoreCase(format)) {
                date = date.trim();
                if (date.length() == 10 && date.charAt(4) == '-' && date.charAt(7) == '-') {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(5, 7));
                    day = Integer.parseInt(date.substring(8));
                } else {
                    return false;
                }
            } else if ("YYYY/MM/DD".equalsIgnoreCase(format)) {
                date = date.trim();
                if (date.length() == 10 && date.charAt(4) == '/' && date.charAt(7) == '/') {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(5, 7));
                    day = Integer.parseInt(date.substring(8));
                } else {
                    return false;
                }
            } else {
                year = Integer.parseInt(date.substring(0, 4));
                month = Integer.parseInt(date.substring(4, 6));
                day = Integer.parseInt(date.substring(6));
            }
            result = isValidDate(year, month, day);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    
    public boolean isValidDate(int year, int month, int day) {
        if ((month < 1) || (month > 12)) {
            return false;
        }
        int ml = getMaxDayOfMonth(year, month);
        if ((day < 1) || (day > ml)) {
            return false;
        }
        return true;
    }

    
    public boolean isValidTime(int hour, int minute, int second, int millisecond) {
        if ((hour < 0) || (hour >= 24)) {
            return false;
        }
        if ((minute < 0) || (minute >= 60)) {
            return false;
        }
        if ((second < 0) || (second >= 60)) {
            return false;
        }
        if ((millisecond < 0) || (millisecond >= 1000)) {
            return false;
        }
        return true;
    }

    
    public boolean isValidDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return (isValidDate(year, month, day) && isValidTime(hour, minute, second, millisecond));
    }
}
