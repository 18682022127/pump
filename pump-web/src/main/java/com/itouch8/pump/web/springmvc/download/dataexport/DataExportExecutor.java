package com.itouch8.pump.web.springmvc.download.dataexport;

import javax.servlet.http.HttpServletRequest;

import com.itouch8.pump.core.dao.mybatis.mapper.impl.DataStreamMapperMethodExecutor;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.springmvc.download.DownloadConsts;


public class DataExportExecutor extends DataStreamMapperMethodExecutor {

    protected boolean isSupport() {
        return DataExportHelp.isExportData();
    }

    protected int getFetchSize() {
        try {
            HttpServletRequest request = ServletHelp.getRequest();
            return Integer.parseInt(request.getParameter(DownloadConsts.FETCH_SIZE_PARAM_NAME));
        } catch (Exception e) {
            return 1000;
        }
    }
}
