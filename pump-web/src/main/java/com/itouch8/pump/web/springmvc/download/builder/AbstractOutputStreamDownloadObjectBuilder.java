package com.itouch8.pump.web.springmvc.download.builder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.web.springmvc.download.DownloadConsts;


public abstract class AbstractOutputStreamDownloadObjectBuilder extends AbstractDownloadObjectBuilder {

    @Override
    public Object build(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = this.getFilename(model, request, response);
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "iso-8859-1"));
            this.doBuild(model, request, response.getOutputStream());
        } catch (Exception e) {
            Throw.throwRuntimeException(e);
        }
        return null;
    }

    protected String getFilename(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        String filename = request.getParameter(DownloadConsts.DOWNLOAD_TITLE_PARAM_NAME);
        String suffix = request.getParameter(DownloadConsts.DOWNLOAD_SUFFIX_PARAM_NAME);
        return filename + "." + suffix;
    }

    protected abstract void doBuild(Map<String, Object> model, HttpServletRequest request, OutputStream out) throws IOException;
}
