package com.itouch8.pump.util.toolimpl;

import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.util.Tool;

public abstract class CheckUtilsImpl {

    private static final CheckUtilsImpl instance = new CheckUtilsImpl() {};

    private CheckUtilsImpl() {}

    public static CheckUtilsImpl getInstance() {
        return instance;
    }

    public boolean isBlank(CharSequence cs) {
        return CoreUtils.isBlank(cs);
    }

    public boolean isEmpty(Object obj) {
        return CoreUtils.isEmpty(obj);
    }

    public boolean isAlpha(final CharSequence cs) {
        return StringUtils.isAlpha(cs);
    }

    public boolean isAlphanumeric(final CharSequence cs) {
        return StringUtils.isAlphanumeric(cs);
    }

    public boolean isNumeric(final CharSequence cs) {
        return StringUtils.isNumeric(cs);
    }

    public boolean isMobileNumber(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        }

        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        String regExpHk = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobile);
        if (m.matches()) {
            return true;
        }
        p = Pattern.compile(regExpHk);
        m = p.matcher(mobile);
        if (m.matches()) {
            return true;
        }
        return false;

    }

    public boolean isTrue(Object bean, String expression) {
        return isTrue(bean, expression, false);
    }

    public boolean isTrue(Object bean, String expression, boolean valueIfNull) {
        try {
            if (null == bean || isBlank(expression)) {
                return valueIfNull;
            }
            return Tool.BEAN.getProperty(bean, expression, null, boolean.class);
        } catch (Exception e) {
            return valueIfNull;
        }
    }

    public boolean isTrue(Object bean, String expression, Map<String, Object> context, boolean valueIfNull) {
        try {
            if (null == bean || isBlank(expression)) {
                return valueIfNull;
            }
            return Tool.BEAN.getProperty(bean, expression, context, boolean.class);
        } catch (Exception e) {
            return valueIfNull;
        }
    }

    public boolean isDate(String date, String format) {
        return Tool.DATE.isValidDate(date, format);
    }

    public boolean isDate(int year, int month, int day) {
        return Tool.DATE.isValidDate(year, month, day);
    }

    public boolean isTime(int hour, int minute, int second, int millisecond) {
        return Tool.DATE.isValidTime(hour, minute, second, millisecond);
    }

    public boolean isHasDumpObject(String[] arr) {
        if (null != arr && arr.length > 0) {
            HashSet<String> hashSet = new HashSet<String>();
            for (int i = 0; i < arr.length; i++) {
                hashSet.add(arr[i]);
            }
            return hashSet.size() != arr.length;
        }
        return false;
    }
}
