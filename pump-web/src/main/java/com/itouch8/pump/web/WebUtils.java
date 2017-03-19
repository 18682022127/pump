package com.itouch8.pump.web;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itouch8.pump.core.service.encrypt.EncryptHelp;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.upload.IUploadFile;


public class WebUtils {

    
    public static ServletContext getApplication() {
        return ServletHelp.getApplication();
    }

    
    public static Object getApplicationAttr(String attr) {
        return getApplication().getAttribute(attr);
    }

    
    public static void setApplicationAttr(String attr, Object value) {
        getApplication().setAttribute(attr, value);
    }

    
    public static void removeApplicationAttr(String attr) {
        getApplication().removeAttribute(attr);
    }

    
    public static String getApplicationInitParameter(String name) {
        return getApplication().getInitParameter(name);
    }

    
    public static String getProjectPath() {
        if (null == projectPath) {
            synchronized (WebUtils.class) {
                if (null == projectPath) {
                    String realPath = getApplication().getRealPath("/");
                    if (realPath.charAt(realPath.length() - 1) != '/' && realPath.charAt(realPath.length() - 1) != '\\') {
                        realPath = realPath + "/";
                    }
                    projectPath = realPath;
                }
            }
        }
        return projectPath;
    }

    
    public static String getProjectRoot() {
        if (null == projectRoot) {
            synchronized (WebUtils.class) {
                if (null == projectRoot) {
                    projectRoot = getApplication().getContextPath() + "/";
                }
            }
        }
        return projectRoot;
    }

    
    public static HttpServletRequest getRequest() {
        return ServletHelp.getRequest();
    }

    
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String h = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(h)) {
            return true;
        }
        return false;
    }

    
    public static Object getRequestAttr(String attr) {
        HttpServletRequest request = getRequest();
        return null == request ? null : request.getAttribute(attr);
    }

    
    public static void setRequestAttr(String attr, Object value) {
        HttpServletRequest request = getRequest();
        if (null != request) {
            request.setAttribute(attr, value);
        }
    }

    
    public static void removeRequestAttr(String attr) {
        HttpServletRequest request = getRequest();
        if (null != request) {
            request.removeAttribute(attr);
        }
    }

    
    public static String getRequestParameter(String name) {
        HttpServletRequest request = getRequest();
        return null == request ? null : request.getParameter(name);
    }

    
    public static String getRequestParameter(String name, String defaultValue) {
        HttpServletRequest request = getRequest();
        if (null != request) {
            String value = request.getParameter(name);
            if (!CoreUtils.isBlank(value)) {
                return value;
            }
        }
        return defaultValue;
    }

    
    public static String[] getRequestParameterValues(String name) {
        HttpServletRequest request = getRequest();
        return null == request ? null : request.getParameterValues(name);
    }

    
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (null != ip && -1 != ip.indexOf(',')) {
            ip = ip.substring(0, ip.indexOf(','));
        }
        return CoreUtils.convertIp(ip);
    }

    
    public static String getUriWithoutRoot(String uri) {
        String root = getProjectRoot();
        int i = uri.indexOf(root);
        if (-1 != i) {
            uri = uri.substring(i + root.length());
        }
        return uri;
    }

    
    public static String getRestUriWithoutRoot(String uri) {
        String root = getProjectRoot();
        int i = uri.indexOf(root);
        if (-1 != i) {
            uri = uri.substring(i + root.length());
        }
        i = uri.lastIndexOf('.');
        if (-1 != i) {
            uri = uri.substring(0, i);
        }
        return uri;
    }

    
    public static IUploadFile[] getUploadFile() {
        return ServletHelp.getUploadFile();
    }

    
    public static HttpSession getSession() {
        return ServletHelp.getSession();
    }

    
    public static Object getSessionAttr(String attr) {
        return getSessionAttr(getSession(), attr);
    }

    
    public static void setSessionAttr(String attr, Object value) {
        setSessionAttr(getSession(), attr, value);
    }

    
    public static void removeSessionAttr(String attr) {
        removeSessionAttr(getSession(), attr);
    }

    
    public static boolean isInvalidated(HttpSession session) {
        try {
            session.getAttribute("");
            return false;
        } catch (java.lang.IllegalStateException e) {
            return true;
        }
    }

    
    public static void removeSessionAttr(HttpSession session, String attr) {
        if (null != session && !isInvalidated(session)) {
            session.removeAttribute(attr);
        }
    }

    
    public static Object getSessionAttr(HttpSession session, String attr) {
        if (null != session && !isInvalidated(session)) {
            return session.getAttribute(attr);
        }
        return null;
    }

    
    public static void setSessionAttr(HttpSession session, String attr, Object value) {
        if (null != session && !isInvalidated(session)) {
            session.setAttribute(attr, value);
        }
    }

    
    public static HttpServletResponse getResponse() {
        return ServletHelp.getResponse();
    }

    
    public static void addHeader(String name, String context) {
        HttpServletResponse response = getResponse();
        if (null != response) {
            response.addHeader(name, context);
        }
    }

    
    public static void addCookie(Cookie cookie) {
        HttpServletResponse response = getResponse();
        if (null != response) {
            response.addCookie(cookie);
        }
    }

    
    public static void addCookie(String name, String value) {
        HttpServletResponse response = getResponse();
        if (null != response) {
            response.addCookie(new Cookie(name, value));
        }
    }

    
    public static void addCookie(String name, String value, boolean encrypt) {
        HttpServletResponse response = getResponse();
        if (null != response) {
            response.addCookie(new Cookie(name, encrypt ? EncryptHelp.encode(value) : value));
        }
    }

    
    private static String projectRoot = null;

    
    private static String projectPath = null;
}
