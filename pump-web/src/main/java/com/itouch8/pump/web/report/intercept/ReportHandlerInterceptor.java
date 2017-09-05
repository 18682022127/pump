package com.itouch8.pump.web.report.intercept;

import java.io.File;
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
                if (null != modelAndView && !Tool.CHECK.isBlank(reportPath)) {
                    String url = getClassPathUrlPrefix() + getUrl(reportPath);
                    String dir = ReportHandlerInterceptor.class.getResource("/").getPath().substring(1) + getUrlPrefix() + getUrl(reportPath);
                    String subReportDir = new File(dir).getParent() + File.separator;
                    String server = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/doc/view?fileId=";
                    modelAndView.getModelMap().clear();
                    modelAndView.setViewName(VIEW_NAME);
                    modelAndView.addObject("url", url);
                    modelAndView.addObject("format", getFormat());
                    modelAndView.addObject(getDatakey(), getData(data));
                    modelAndView.addObject("SUBREPORT_DIR", subReportDir);
                    modelAndView.addObject("IMAGE_SERVER", server);
                }
            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    private JRDataSource getData(Object data) {
        if (Tool.CHECK.isEmpty(data)) {
            return null;
        }
        if (data instanceof Collection) {
            return new JRBeanCollectionDataSource((Collection<?>) data);
        } else {
            List<Object> resultSet = new ArrayList<Object>();
            resultSet.add(data);
            return new JRBeanCollectionDataSource(resultSet);
        }

    }

    private String getUrl(String url) {
        if (url.endsWith(".jasper")) {
            return url;
        } else {
            return url + ".jasper";
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}

    private static final String VIEW_NAME = "ReportView";

    private static final String DEFAULT_FORMAT = "pdf";

    public static final String DEFAULT_DATA_KEY = "report_data_key";

    private static final String CLASSPATH_URL_PREFIX = "classpath:jaspers/";

    private static final String URL_PREFIX = "jaspers/";

    private String format = DEFAULT_FORMAT;

    private String datakey = DEFAULT_DATA_KEY;

    private String classPathUrlPrefix = CLASSPATH_URL_PREFIX;

    private String urlPrefix = URL_PREFIX;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDatakey() {
        return datakey;
    }

    public void setDatakey(String datakey) {
        this.datakey = datakey;
    }

    public String getClassPathUrlPrefix() {
        return classPathUrlPrefix;
    }

    public void setClassPathUrlPrefix(String classPathUrlPrefix) {
        this.classPathUrlPrefix = classPathUrlPrefix;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

}
