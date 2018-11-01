package com.itouch8.pump.core.util.env;

import java.io.File;
import java.net.Inet4Address;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.SystemUtils;


public abstract class EnvConsts {

    
    public static final boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;

    
    public static final String FILE_ENCODING = SystemUtils.FILE_ENCODING;

    
    public static final String OS_ENCODING = getSystemProperty("sun.jnu.encoding");

    
    public static final String PATH_SEPARATOR = File.separator;

    
    public static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;

    
    public static final double JDK_VERSION = Double.parseDouble(SystemUtils.JAVA_VERSION.substring(0, 3));

    
    public final static String TMP_DIR = SystemUtils.JAVA_IO_TMPDIR;

    
    public static final String USER_HOME = SystemUtils.USER_HOME;

    
    public static final String USER_COUNTRY = SystemUtils.USER_COUNTRY;

    
    public static final String USER_LANGUAGE = SystemUtils.USER_LANGUAGE;

    
    public static final String USER_TIMEZONE = SystemUtils.USER_TIMEZONE;

    
    public static final Locale SYSTEM_LOCALE = Locale.getDefault();

    
    public static final TimeZone SYSTEM_TIMEZONE = TimeZone.getDefault();

    
    public static final String LOCALE_HOST = ToolInnerConsts.LOCALE_HOST;

    // 利用内部类实现需要初始化，但是初始化之后就不能再修改的常量
    private static class ToolInnerConsts {
        private static String LOCALE_HOST;
        static {
            try {
                LOCALE_HOST = Inet4Address.getLocalHost().getHostAddress();
            } catch (Exception e) {
            }
        }
    }

    private static String getSystemProperty(final String property) {
        try {
            return System.getProperty(property);
        } catch (final SecurityException ex) {
            // we are not allowed to look at this property
            System.err.println("Caught a SecurityException reading the system property '" + property + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }
}
