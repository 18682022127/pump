package com.itouch8.pump.web.report.annotation;

import org.springframework.core.MethodParameter;

public class ReportSupport {

    private static final ThreadLocal<Object> reportDataInfo = new ThreadLocal<Object>();

    public static void setReportDataContext(Object data) {
        ReportSupport.reportDataInfo.set(data);
    }

    public static Object getReportDataContext() {
        return reportDataInfo.get();
    }

    public static void removeReportDataFromContext() {
        reportDataInfo.remove();
    }

    public static boolean hasReportAnnotation(MethodParameter returnType) {
        return returnType.getMethodAnnotation(Report.class) != null;
    }

}
