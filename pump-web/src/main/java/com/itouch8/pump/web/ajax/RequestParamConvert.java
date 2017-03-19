package com.itouch8.pump.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.reflect.method.IParamExtractor;


public class RequestParamConvert implements IParamExtractor {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    public RequestParamConvert(HttpServletRequest req, HttpServletResponse resp) {
        super();
        this.req = req;
        this.resp = resp;
    }

    @Override
    public <E> E extract(Class<E> type, String expression, Object args) {
        Object param = this.extract1(type, expression, args);
        if (null == param) {
            return null;
        }
        return CoreUtils.convert(param, type);
    }

    private Object extract1(Class<?> type, String expression, Object args) {
        if (HttpServletRequest.class.isAssignableFrom(type)) {
            return req;
        } else if (HttpServletResponse.class.isAssignableFrom(type)) {
            return resp;
        } else {
            if (null != expression) {
                String[] param = req.getParameterValues(expression);
                if (null == param || 0 == param.length) {
                    return null;
                } else if (String.class.equals(type)) {
                    return param[0];
                } else if (int.class.equals(type)) {
                    return Integer.parseInt(param[0]);
                } else if (double.class.equals(type)) {
                    return Double.parseDouble(param[0]);
                } else if (String[].class.equals(type)) {
                    return param;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }
}
