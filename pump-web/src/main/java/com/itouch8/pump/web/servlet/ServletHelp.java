package com.itouch8.pump.web.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.springmvc.upload.SpringMVCUploadFile;
import com.itouch8.pump.web.upload.IUploadFile;


final public class ServletHelp {

    
    private static final ThreadLocal<ServletHelp> local = new ThreadLocal<ServletHelp>();
    
    private static ServletContext application;// 应用级别
    
    private HttpServletRequest request;// 请求级别
    
    private HttpServletResponse response;// 请求级别
    
    private IRequestInfo requestInfo;// 请求级别
    
    private IUploadFile[] uploadFiles;// 请求级别

    private ServletHelp() {}

    
    public static void setApplication(final ServletContext application) {
        if (ServletHelp.application != application) {
            ServletHelp.application = application;
        }
    }

    
    public static void setRequestAndResponse(final HttpServletRequest request, final HttpServletResponse response) {
        setHttpServletRequest(request);
        setHttpServletResponse(response);
    }

    
    public static void remove() {
        local.remove();
    }

    
    public static ServletContext getApplication() {
        return application;
    }

    
    public static HttpServletRequest getRequest() {
        return get().request;
    }

    
    public static HttpServletResponse getResponse() {
        return get().response;
    }

    
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (null != request) {
            return request.getSession(false);
        }
        return null;
    }

    
    public static IRequestInfo getRequestInfo() {
        return get().requestInfo;
    }

    
    public static IUploadFile[] getUploadFile() {
        return get().uploadFiles;
    }

    
    private static void setHttpServletRequest(final HttpServletRequest request) {
        if (null != request && get().request != request) {
            ServletHelp help = get();
            help.request = request;
            if (null == help.requestInfo) {
                help.requestInfo = WebPumpConfig.getRequestInfoFactory().getRequestInfo();
            }
            if (request instanceof MultipartRequest) {// 如果是上传
                MultipartRequest mr = (MultipartRequest) request;
                List<IUploadFile> list = new ArrayList<IUploadFile>();
                for (List<MultipartFile> files : mr.getMultiFileMap().values()) {
                    for (MultipartFile file : files) {
                        list.add(new SpringMVCUploadFile(file));
                    }
                }
                IUploadFile[] uploadFiles = new IUploadFile[list.size()];
                list.toArray(uploadFiles);
                help.uploadFiles = uploadFiles;
            }
        }
    }

    
    private static void setHttpServletResponse(final HttpServletResponse response) {
        if (null != response && get().response != response) {
            get().response = response;
        }
    }

    
    private static ServletHelp get() {
        ServletHelp help = local.get();
        if (null == help) {
            help = new ServletHelp();
            local.set(help);
        }
        return help;
    }
}
