package com.itouch8.pump.web.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.util.Tool;
import com.itouch8.pump.web.report.intercept.ReportHandlerInterceptor;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportTool {

    public static JRDataSource getData(Object data, Object param) {
        Map<String, Object> rs = new HashMap<String, Object>();
        List<Map<String, Object>> resultSet = new ArrayList<Map<String, Object>>();
        if (!Tool.CHECK.isEmpty(data)) {
            rs.put(ReportHandlerInterceptor.DEFAULT_DATA_KEY, data);
        }
        if (!Tool.CHECK.isEmpty(param)) {
            rs.put(ReportHandlerInterceptor.DEFAULT_PARAM_KEY, param);
        }
        resultSet.add(rs);
        return new JRBeanCollectionDataSource(resultSet);
    }

    public static JRDataSource getData(Object data) {
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

}
