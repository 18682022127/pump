package com.itouch8.pump.web.report;

import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import com.itouch8.pump.web.report.annotation.ReportSupport;
import com.itouch8.pump.web.report.intercept.ReportHandlerInterceptor;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Copy Right Information : 云途信息资讯有限公司 <br>
 * Project : 途途物流公众平台 <br>
 * Description : 打印报表视图<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2017-09-04<br>
 */
public class ReportView extends JasperReportsMultiFormatView {

    private JasperReport jasperReport;

    public ReportView() {
        super();
        setReportDataKey(ReportHandlerInterceptor.DEFAULT_DATA_KEY);
    }

    protected JasperPrint fillReport(Map<String, Object> model) throws Exception {
        if (model.containsKey("url")) {
            this.jasperReport = loadReport((Resource) model.get("url"));
            JasperPrint fillReport = super.fillReport(model);
            ReportSupport.removeReportDataFromContext();
            return fillReport;
        }
        ReportSupport.removeReportDataFromContext();
        return null;
    }

    protected JasperReport getReport() {
        return this.jasperReport;
    }

}
