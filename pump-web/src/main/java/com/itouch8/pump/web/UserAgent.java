package com.itouch8.pump.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserAgent {

    private final String userAgent;

    public final boolean isOpera;
    public final boolean isOpera10_5;
    public final boolean isChrome;
    public final boolean isWebKit;
    public final boolean isSafari;
    public final boolean isSafari2;
    public final boolean isSafari3;
    public final boolean isSafari4;
    public final boolean isSafari5_0;
    public final boolean isSafari5;
    public final boolean isIE;
    public final boolean isIE6;
    public final boolean isIE7;
    public final boolean isIE8;
    public final boolean isIE9;
    public final boolean isIE10;
    public final boolean isIE11;
    public final boolean isGecko;
    public final boolean isGecko3;
    public final boolean isGecko4;
    public final boolean isGecko5;
    public final boolean isGecko10;
    public final boolean isFF3_0;
    public final boolean isFF3_5;
    public final boolean isFF3_6;
    public final boolean isFF4;
    public final boolean isFF5;
    public final boolean isFF10;
    public final boolean isWindows;
    public final boolean isMac;
    public final boolean isLinux;
    public final double chromeVersion;
    public final double firefoxVersion;
    public final double ieVersion;
    public final double operaVersion;
    public final double safariVersion;
    public final double webKitVersion;

    private final static Pattern opera = Pattern.compile("opera");// ==>/opera/
    private final static Pattern opera10_5 = Pattern.compile("version\\/10\\.5");// ==>/version\/10\.5/
    private final static Pattern chrome = Pattern.compile("\\bchrome\\b");// ==>/\bchrome\b/
    private final static Pattern webkit = Pattern.compile("webkit");// ==>/webkit/
    private final static Pattern safari = Pattern.compile("safari");// ==>/safari/
    private final static Pattern applewebkit = Pattern.compile("applewebkit\\/4");// ==>/applewebkit\/4/
    private final static Pattern appversion3 = Pattern.compile("version\\/3");// ==>/version\/3/
    private final static Pattern appversion4 = Pattern.compile("version\\/4");// ==>/version\/4/
    private final static Pattern appversion5_0 = Pattern.compile("version\\/5\\.0");// ==>/version\/5\.0/
    private final static Pattern appversion5 = Pattern.compile("version\\/5");// ==>/version\/5/

    private final static Pattern mise = Pattern.compile("msie");// ==>/msie/
    private final static Pattern mise6 = Pattern.compile("msie 6");// ==>/msie 6/
    private final static Pattern mise7 = Pattern.compile("msie 7");// ==>/msie 7/
    private final static Pattern mise8 = Pattern.compile("msie 8");// ==>/msie 8/
    private final static Pattern mise9 = Pattern.compile("msie 9");// ==>/msie 9/
    private final static Pattern mise10 = Pattern.compile("msie 10");// ==>/msie 10/
    private final static Pattern mise11 = Pattern.compile("msie 11");// ==>/msie 11/

    private final static Pattern gecko = Pattern.compile("gecko");// ==>/gecko/
    private final static Pattern gecko3 = Pattern.compile("rv:1\\.9");// ==>/rv:1\.9/
    private final static Pattern gecko4 = Pattern.compile("rv:2\\.0");// ==>/rv:2\.0/
    private final static Pattern gecko5 = Pattern.compile("rv:5\\.");// ==>/rv:5\./
    private final static Pattern gecko10 = Pattern.compile("rv:10\\.");// ==>/rv:10\./

    private final static Pattern ff3_0 = Pattern.compile("rv:1\\.9\\.0");// ==>/rv:1\.9\.0/
    private final static Pattern ff3_5 = Pattern.compile("rv:1\\.9\\.1");// ==>/rv:1\.9\.1/
    private final static Pattern ff3_6 = Pattern.compile("rv:1\\.9\\.2");// ==>/rv:1\.9\.2/

    private final static Pattern windows = Pattern.compile("windows|win32");// ==>/windows|win32/
    private final static Pattern mac = Pattern.compile("macintosh|mac os x");// ==>/macintosh|mac os
                                                                             // x/
    private final static Pattern linux = Pattern.compile("linux");// ==>/linux/

    private final static Pattern chromeVersionP = Pattern.compile("\\bchrome\\/(\\d+\\.\\d+)");// ==>/\bchrome\/(\d+\.\d+)/),
    private final static Pattern firefoxVersionP = Pattern.compile("\\bfirefox\\/(\\d+\\.\\d+)");// ==>/\bfirefox\/(\d+\.\d+)/),
    private final static Pattern ieVersionP = Pattern.compile("msie (\\d+\\.\\d+)");// ==>/msie
                                                                                    // (\d+\.\d+)/),
    private final static Pattern operaVersionP = Pattern.compile("version\\/(\\d+\\.\\d+)");// ==>/version\/(\d+\.\d+)/),
    private final static Pattern safariVersionP = Pattern.compile("version\\/(\\d+\\.\\d+)");// ==>/version\/(\d+\.\d+)/),
    private final static Pattern webKitVersionP = Pattern.compile("webkit\\/(\\d+\\.\\d+)");// ==>/webkit\/(\d+\.\d+)/),

    public static void main(String[] args) {
        String agent = "Mozilla/5.0 (Windows NT 6.1; rv:45.0) Gecko/20100101 Firefox/45.0";
        UserAgent ua = new UserAgent(agent, 0);
        System.out.println("browser ===> " + ua.getBrowser());
        System.out.println("os ===> " + ua.getOperateSystem());
    }

    
    public UserAgent(String userAgent, int docMode) {
        this.userAgent = userAgent.toLowerCase();
        this.isOpera = check(opera);
        this.isOpera10_5 = isOpera && check(opera10_5);
        this.isChrome = check(chrome);
        this.isWebKit = check(webkit);
        this.isSafari = !isChrome && check(safari);
        this.isSafari2 = isSafari && check(applewebkit);
        this.isSafari3 = isSafari && check(appversion3);
        this.isSafari4 = isSafari && check(appversion4);
        this.isSafari5_0 = isSafari && check(appversion5_0);
        this.isSafari5 = isSafari && check(appversion5);

        this.isIE = !isOpera && check(mise);
        this.isIE7 = isIE && ((check(mise7) && docMode != 8 && docMode != 9 && docMode != 10 && docMode != 11) || docMode == 7);
        this.isIE8 = isIE && ((check(mise8) && docMode != 7 && docMode != 9 && docMode != 10 && docMode != 11) || docMode == 8);
        this.isIE9 = isIE && ((check(mise9) && docMode != 7 && docMode != 8 && docMode != 10 && docMode != 11) || docMode == 9);
        this.isIE10 = isIE && ((check(mise10) && docMode != 7 && docMode != 8 && docMode != 9 && docMode != 11) || docMode == 10);
        this.isIE11 = isIE && ((check(mise11) && docMode != 7 && docMode != 8 && docMode != 9 && docMode != 10) || docMode == 11);
        this.isIE6 = isIE && check(mise6);

        this.isGecko = !isWebKit && check(gecko);
        this.isGecko3 = isGecko && check(gecko3);
        this.isGecko4 = isGecko && check(gecko4);
        this.isGecko5 = isGecko && check(gecko5);
        this.isGecko10 = isGecko && check(gecko10);

        this.isFF3_0 = isGecko3 && check(ff3_0);
        this.isFF3_5 = isGecko3 && check(ff3_5);
        this.isFF3_6 = isGecko3 && check(ff3_6);

        this.isWindows = check(windows);
        this.isMac = check(mac);
        this.isLinux = check(linux);

        this.chromeVersion = version(true, chromeVersionP);
        this.firefoxVersion = version(true, firefoxVersionP);
        this.ieVersion = version(isIE, ieVersionP);
        this.operaVersion = version(isOpera, operaVersionP);
        this.safariVersion = version(isSafari, safariVersionP);
        this.webKitVersion = version(isWebKit, webKitVersionP);

        this.isFF4 = 4 <= firefoxVersion && firefoxVersion < 5;
        this.isFF5 = 5 <= firefoxVersion && firefoxVersion < 6;
        this.isFF10 = 10 <= firefoxVersion && firefoxVersion < 11;
    }

    public String getOperateSystem() {
        if (isWindows) {
            return "Windows ";
        } else if (isMac) {
            return "Mac OS ";
        } else if (isLinux) {
            return "Linux ";
        } else {
            return "unknown";
        }
    }

    public String getBrowser() {
        String browser = "unknown";
        if (isIE) {
            if (isIE6) {
                browser = "IE6";
            } else if (isIE7) {
                browser = "IE7";
            } else if (isIE8) {
                browser = "IE8";
            } else if (isIE9) {
                browser = "IE9";
            } else if (isIE10) {
                browser = "IE10";
            } else if (isIE11) {
                browser = "IE11";
            } else {
                browser = "IE " + ieVersion;
            }
        } else if (isOpera) {
            browser = "Opera " + operaVersion;
        } else if (isSafari) {
            browser = "Safari " + safariVersion;
        } else if (isChrome) {
            browser = "Chrome " + chromeVersion;
        } else if (isFF3_0 || isFF3_5 || isFF3_6 || isFF4 || isFF5 || isFF10 || isGecko) {
            browser = "FireFox " + firefoxVersion;
        }
        return browser;
    }

    private boolean check(Pattern pattern) {
        return pattern.matcher(userAgent).find();
    }

    private double version(boolean is, Pattern pattern) {
        if (is) {
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            }
        }
        return 0;
    }
}
