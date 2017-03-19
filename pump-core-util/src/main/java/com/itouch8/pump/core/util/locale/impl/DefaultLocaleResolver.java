package com.itouch8.pump.core.util.locale.impl;

import java.util.Locale;

import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.locale.ILocaleResolver;


public class DefaultLocaleResolver implements ILocaleResolver {

    
    @Override
    public Locale getLocale() {
        return EnvConsts.SYSTEM_LOCALE;
    }

    
    @Override
    public void setLocale(Locale locale) {
        if (null != locale) {
            Locale.setDefault(locale);
        }
    }
}
