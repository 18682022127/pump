package com.itouch8.pump.web.springmvc.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.util.Tool;
import com.itouch8.pump.web.ajax.AjaxInvoker;
import com.itouch8.pump.web.springmvc.download.builder.DownloadObjectBuilderManager;
import com.itouch8.pump.web.springmvc.download.builder.IDownloadObjectBuilder;


public class DownloadView extends AbstractView {

    
    private final static String SHOW_TYPE_PARAM_NAME = "showType";
    
    private final static String AJAX_ID_PARAM_NAME = "ajaxStatusId";

    protected boolean generatesDownloadContent() {
        return true;
    }

    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File local = null;
        ByteArrayOutputStream os = null;
        ServletOutputStream out = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        String ajaxStatusId = getParameter(request, AJAX_ID_PARAM_NAME);
        String showType = getParameter(request, SHOW_TYPE_PARAM_NAME, "attachment");
        boolean flag = false;
        try {
            IUser user = getUser(request);
            if (null != user) {
                CommonLogger.info("下载:用户代码【" + user.getUserId() + "】用户名称【" + user.getUserName() + "】日期【" + Tool.DATE.getDateAndTime() + "】");
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            out = response.getOutputStream();

            IDownloadObjectBuilder builder = getDownloadObjectBuilder(request);
            if (null == builder) {
                Throw.throwRuntimeException("未找到下载构建器");
            }

            Object result = builder.build(model, request, response);
            if (result instanceof File)// 如果返回的是文件
            {
                local = (File) result;
                String filename = local.getName();
                response.addHeader("Content-Disposition", showType + ";filename=" + new String(filename.getBytes("gb2312"), "iso-8859-1"));
                fis = new FileInputStream(local);
                bis = new BufferedInputStream(fis);
                Tool.IO.copy(bis, out);
            } else if (result instanceof ByteArrayOutputStream) {
                String filename = getParameter(request, DownloadConsts.DOWNLOAD_TITLE_PARAM_NAME);
                response.addHeader("Content-Disposition", showType + ";filename=" + new String(filename.getBytes("gb2312"), "iso-8859-1"));
                os = (ByteArrayOutputStream) result;
                response.setContentLength(os.size());
                os.writeTo(out);
                out.flush();
            } else if (result instanceof CharSequence) {
                this.deal(response, showType, result.toString(), os, out);
            } else {
                // 其它，暂不实现
            }
            flag = true;
        } catch (OutOfMemoryError me) {
            flag = false;
            out.print("下载数据量过大，内存溢出！");
            me.printStackTrace();
            throw me;
        } catch (Exception se) {
            String c = "org.apache.catalina.connector.ClientAbortException";
            if (c.equals(se.getClass().getName()) || (null != se.getCause() && c.equals(se.getCause().getClass().getName()))) {// 客户取消下载
                flag = true;
            } else {
                try {
                    this.deal(response, showType, "下载出现异常：" + Throw.getMessage(se), os, out);
                    se.printStackTrace();
                    flag = true;
                } catch (Exception ii) {
                }
            }
        } finally {
            Tool.IO.closeQuietly(out, bis, fis, os);
            Tool.FILE.forceDelete(local);
            if (flag) {
                AjaxInvoker.updateSuccess(ajaxStatusId);
            } else {
                AjaxInvoker.updateFailure(ajaxStatusId);
            }
        }
    }

    
    protected IDownloadObjectBuilder getDownloadObjectBuilder(HttpServletRequest request) {
        String buildType = getParameter(request, DownloadConsts.BUILD_TYPE_PARAM_NAME);
        IDownloadObjectBuilder builder = DownloadObjectBuilderManager.getDownloadObjectBuilder(buildType);
        return builder;
    }

    protected IUser getUser(HttpServletRequest request) {
        return null;
    }

    
    private void deal(HttpServletResponse response, String showType, String result, ByteArrayOutputStream os, ServletOutputStream out) throws IOException {
        String filename = "download.txt";
        response.addHeader("Content-Disposition", showType + ";filename=" + new String(filename.getBytes("gb2312"), "iso-8859-1"));
        os = new ByteArrayOutputStream();
        os.write(result.getBytes());
        response.setContentLength(os.size());
        os.writeTo(out);
        out.flush();
    }

    
    private String getParameter(HttpServletRequest request, String name) {
        return getParameter(request, name, null);
    }

    
    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (!Tool.CHECK.isBlank(value)) {
            return value;
        }
        return defaultValue;
    }
}
