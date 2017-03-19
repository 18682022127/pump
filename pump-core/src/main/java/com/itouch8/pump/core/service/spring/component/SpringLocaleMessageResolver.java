package com.itouch8.pump.core.service.spring.component;

import java.util.Locale;

import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.util.locale.impl.DefaultLocaleMessageResolver;


public class SpringLocaleMessageResolver extends DefaultLocaleMessageResolver {

    
    @Override
    public String getMessage(String code, String defaultMessage, Locale locale, Object... args) {
        if (SpringHelp.hasInit()) {
            return SpringHelp.getApplicationContext().getMessage(code, args, defaultMessage, locale);
        } else {
            return super.getMessage(code, defaultMessage, locale, args);
        }
    }
}
