package com.itouch8.pump.web.springmvc.exceptionresolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.PumpRuntimeException;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.util.Tool;
import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.WebUtils;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 异常解析器<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class PumpHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    
    private String exceptionAttribute = "exception";

    
    private String exceptionHandlerResultsAttribute = "exceptionHandlerResults";

    
    private Map<String, String> exceptionCodeHttpStatusMapping;

    private MediaType jsonContentType = MediaType.APPLICATION_JSON_UTF8;

    public PumpHandlerExceptionResolver() {
        setOrder(1);
    }
    
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof BindException) {
                return handleBindException((BindException) ex, request, response, handler);
            }

            PumpRuntimeException be = Throw.createRuntimeException(ex);
           /* List<IExceptionHandler> handlers = be.getHandlers();
            String view = be.getView();
            List<Object> results = handlerException(handlers, be);
            if (isAjaxRequest(request, handler)) {
                this.handlerAjaxException(request, response, handler, be, view, results);
                return new ModelAndView();
            } else {
                String statusCode = this.getHttpStatus(be.getCode());
                if (!Tool.CHECK.isBlank(statusCode)) {
                    response.sendError(Integer.parseInt(statusCode));
                    return new ModelAndView();
                } else {
                    return handlerException(request, response, handler, be, view, results);
                }
            }*/
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
            }
        }
        return new ModelAndView();
    }

    @Override
    protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (isAjaxRequest(request, handler)) {
            this.handlerAjaxException(request, response, handler, ex);
            return new ModelAndView();
        } else {
            return super.handleBindException(ex, request, response, handler);
        }
    }

    
    protected boolean isAjaxRequest(HttpServletRequest request, Object handler) {
        if (WebUtils.isAjaxRequest(request)) {
            return true;
        } else if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            return hm.getMethodAnnotation(ResponseBody.class) != null;
        } else {
            return false;
        }
    }

    
    protected void handlerAjaxException(HttpServletRequest request, HttpServletResponse response, Object handler, PumpRuntimeException be, String view, List<Object> results) {
        this.handlerAjaxException(request, response, handler, be);
    }

    
    private void handlerAjaxException(HttpServletRequest request, HttpServletResponse response, Object handler, Throwable e) {
        try {
            IJsonWrapper exceptionWrapper = WebPumpConfig.getExceptionWrapper();
            if (null != exceptionWrapper) {
                @SuppressWarnings("resource")
                ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
                HttpHeaders headers = outputMessage.getHeaders();
                headers.setContentType(this.getJsonContentType());

                Object target = exceptionWrapper.wrap(e);
                Tool.JSON.serialize(outputMessage.getBody(), target);

                outputMessage.getBody().flush();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    protected ModelAndView handlerException(HttpServletRequest request, HttpServletResponse response, Object handler, PumpRuntimeException be, String view, List<Object> results) {
        ModelAndView mv = new ModelAndView(view);
        String exceptionAttribute = this.getExceptionAttribute();
        if (!CoreUtils.isBlank(exceptionAttribute)) {
            mv.addObject(exceptionAttribute, be);
        }
        String exceptionHandlerResultsAttribute = this.getExceptionHandlerResultsAttribute();
        if (!CoreUtils.isBlank(exceptionHandlerResultsAttribute)) {
            mv.addObject(exceptionHandlerResultsAttribute, results);
        }
        CommonLogger.info("handler exception, return view :" + view);
        return mv;
    }

    protected String getHttpStatus(String exceptionCode) {
        if (this.exceptionCodeHttpStatusMapping == null || this.exceptionCodeHttpStatusMapping.isEmpty() || exceptionCode == null) {
            return null;
        }
        return this.exceptionCodeHttpStatusMapping.get(exceptionCode);
    }

    public String getExceptionAttribute() {
        return exceptionAttribute;
    }

    public void setExceptionAttribute(String exceptionAttribute) {
        this.exceptionAttribute = exceptionAttribute;
    }

    public String getExceptionHandlerResultsAttribute() {
        return exceptionHandlerResultsAttribute;
    }

    public void setExceptionHandlerResultsAttribute(String exceptionHandlerResultsAttribute) {
        this.exceptionHandlerResultsAttribute = exceptionHandlerResultsAttribute;
    }

    public MediaType getJsonContentType() {
        return jsonContentType;
    }

    public void setJsonContentType(MediaType jsonContentType) {
        this.jsonContentType = jsonContentType;
    }

    public Map<String, String> getExceptionCodeHttpStatusMapping() {
        return exceptionCodeHttpStatusMapping;
    }

    public void setExceptionCodeHttpStatusMapping(Map<String, String> exceptionCodeHttpStatusMapping) {
        this.exceptionCodeHttpStatusMapping = exceptionCodeHttpStatusMapping;
    }

    
    private List<Object> handlerException(List<IExceptionHandler> handlers, Throwable throwable) {
        if (null == throwable || null == handlers || handlers.isEmpty()) {
            return null;
        }
        List<Object> rs = new ArrayList<Object>();
        for (IExceptionHandler handler : handlers) {
            try {
                Object obj = handler.handler(throwable);
                rs.add(obj);
            } catch (Throwable he) {
                if (handler.ignoreHandlerException()) {
                    rs.add(null);
                } else {
                    rs.add(he);
                }
            }
        }
        return rs;
    }
}
