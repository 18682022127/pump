package com.itouch8.pump.core.util.locale.impl;

import java.util.Locale;

import com.itouch8.pump.core.util.locale.ILocaleMessageResolver;


public class DefaultLocaleMessageResolver extends SpringReloadableResourceBundleMessageSource implements ILocaleMessageResolver {

    @Override
    public String getMessage(String code, String defaultMessage, Locale locale, Object... args) {
        return this.getMessage(code, args, defaultMessage, locale);
    }
}
