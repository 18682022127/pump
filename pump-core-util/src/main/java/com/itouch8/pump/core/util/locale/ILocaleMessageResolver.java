package com.itouch8.pump.core.util.locale;

import java.util.Locale;


public interface ILocaleMessageResolver {

    
    public String getMessage(String code, String defaultMessage, Locale locale, Object... args);
}
