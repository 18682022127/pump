package com.itouch8.pump.web.springmvc.download.dataexport;

import javax.servlet.http.HttpServletRequest;

import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.springmvc.download.DownloadConsts;


public class DataExportHelp {

    
    public static boolean isExportData() {
        try {
            HttpServletRequest request = ServletHelp.getRequest();
            return null != request //
                    && DownloadConsts.DOWNLOAD_MIME.equalsIgnoreCase(request.getParameter(DownloadConsts.MIME_PARAM_NAME)) //
                    && DownloadConsts.DATA_EXPORT_BUILD_TYPE.equalsIgnoreCase(request.getParameter(DownloadConsts.BUILD_TYPE_PARAM_NAME));
        } catch (Exception e) {
            return false;
        }
    }
}
