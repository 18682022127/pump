package com.itouch8.pump.util.toolimpl;

import java.util.Locale;

import com.itouch8.pump.core.util.CoreUtils;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 国际化工具类<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public abstract class LocaleUtilsImpl {

    private static final LocaleUtilsImpl instance = new LocaleUtilsImpl() {};

    private LocaleUtilsImpl() {}

    
    public static LocaleUtilsImpl getInstance() {
        return instance;
    }

    
    public String getMessage(String code, Object... args) {
        return CoreUtils.getMessage(code, args);
    }

    
    public String getMessage(String code, Locale locale, Object... args) {
        return CoreUtils.getMessage(code, locale, args);
    }

    
    public String getMessage(String code, String defaultMessage, Locale locale, Object... args) {
        return CoreUtils.getMessage(code, defaultMessage, locale, args);
    }

    
    public Locale getCurrentLocale() {
        return CoreUtils.getCurrentLocale();
    }

    
    public void setCurrentLocale(Locale locale) {
        CoreUtils.setCurrentLocale(locale);
    }

    
    public Locale toLocale(String str) {
        return CoreUtils.toLocale(str);
    }
}
