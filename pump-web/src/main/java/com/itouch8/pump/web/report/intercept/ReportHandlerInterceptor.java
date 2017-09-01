package com.itouch8.pump.web.report.intercept;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.itouch8.pump.util.Tool;
import com.itouch8.pump.web.report.annotation.Report;
import com.itouch8.pump.web.report.annotation.ReportSupport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Description : 报表拦截器<br>
 * Author : Kingdom <br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2017-08-27<br>
 */
public class ReportHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final String VIEW_NAME = "report";

    private static final String DEFAULT_FORMAT = "pdf";

    public static final String DEFAULT_DATA_KEY = "report_data_key";

    public static final String DEFAULT_PARAM_KEY = "report_param_key";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Report annotation = method.getMethod().getAnnotation(Report.class);
            Object data = ReportSupport.getReportDataContext();
            if (null != annotation) {

                String reportPath = annotation.value();
                if (null != modelAndView) {
                    modelAndView.getModelMap().clear();
                    modelAndView.setViewName(VIEW_NAME);
                    modelAndView.addObject("url", reportPath);
                    if (!modelAndView.getModelMap().containsKey("format")) {
                        modelAndView.addObject("format", DEFAULT_FORMAT);
                    }
                    modelAndView.addObject(DEFAULT_DATA_KEY, getData(data));
                }
            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    private JRDataSource getData(Object data) {
        if (data instanceof Collection) {
            return new JRBeanCollectionDataSource((Collection<?>) data);
        } else {
            List<Object> resultSet = new ArrayList<Object>();
            if (!Tool.CHECK.isEmpty(data)) {
                resultSet.add(data);
            }
            return new JRBeanCollectionDataSource(resultSet);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
