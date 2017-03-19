package com.itouch8.pump.web.path.impl;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.path.IPathResolver;


public class PathResolver implements IPathResolver {

    private static final Pattern root = Pattern.compile("\\Q{root}\\E");
    private static final Pattern locale = Pattern.compile("\\Q{locale}\\E");
    private static final Pattern theme = Pattern.compile("\\Q{theme}\\E");
    private static final Pattern debug = Pattern.compile("\\Q{debug}\\E");
    private static final Pattern min = Pattern.compile("\\Q{min}\\E");

    @Override
    public String resolver(HttpServletRequest request, String path) {
        if (CoreUtils.isBlank(path)) {
            return path;
        }
        Matcher matcher = locale.matcher(path);
        if (matcher.find()) {
            path = matcher.replaceAll(getLocale(request));
        }
        matcher = theme.matcher(path);
        if (matcher.find()) {
            path = matcher.replaceAll(getTheme(request));
        }
        matcher = root.matcher(path);
        if (matcher.find()) {
            path = matcher.replaceAll(request.getContextPath());
        }
        boolean isDebug = isDebug(request);
        matcher = debug.matcher(path);
        if (matcher.find()) {
            path = matcher.replaceAll(isDebug ? "-debug" : "");
        }
        matcher = min.matcher(path);
        if (matcher.find()) {
            path = matcher.replaceAll(isDebug ? "" : ".min");
        }
        return path;
    }

    protected String getLocale(HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        if (locale == null) {
            locale = WebPumpConfig.getLocaleResolver().getLocale();
        }
        return null == locale ? EnvConsts.SYSTEM_LOCALE.toString() : locale.toString();
    }

    protected String getTheme(HttpServletRequest request) {
        ThemeResolver tr = RequestContextUtils.getThemeResolver(request);
        return null == tr ? WebPumpConfig.getDefaultTheme() : tr.resolveThemeName(request);
    }

    protected boolean isDebug(HttpServletRequest request) {
        return true;
    }
}
