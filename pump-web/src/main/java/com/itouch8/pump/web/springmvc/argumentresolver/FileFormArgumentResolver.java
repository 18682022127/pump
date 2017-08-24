package com.itouch8.pump.web.springmvc.argumentresolver;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;

import com.itouch8.pump.util.Tool;
import com.itouch8.pump.web.WebUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.upload.FileData;
import com.itouch8.pump.web.upload.FileForm;
import com.itouch8.pump.web.upload.IUploadFile;

public class FileFormArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return FileForm.class.isAssignableFrom(paramType);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = null;
        FileForm form = new FileForm();
        if (webRequest instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes) webRequest).getRequest();
        } else {
            request = WebUtils.getRequest();
        }
        String[] fileNames = request.getParameterValues("fileName");
        String[] fileTypes = request.getParameterValues("fileType");
        String[] fileBase64 = request.getParameterValues("fileBase64");
        String addonData = request.getParameter("addonData");
        List<FileData> ls = new ArrayList<FileData>();
        if (fileNames != null && fileTypes != null) {
            for (int i = 0; i < fileNames.length; i++) {
                if (!Tool.CHECK.isBlank(fileNames[i]) && !Tool.CHECK.isBlank(fileTypes[i]) && !Tool.CHECK.isBlank(fileBase64[i])) {
                    FileData data = new FileData();
                    data.setFileName(fileNames[i]);
                    data.setFileType(fileTypes[i]);
                    data.setFileBase64(fileBase64[i]);
                    ls.add(data);
                }

            }
        }
        if (request instanceof MultipartRequest) {
            ServletHelp.setRequestAndResponse(request, null);
            IUploadFile[] uploadFiles = ServletHelp.getUploadFile();
            if (null != uploadFiles && uploadFiles.length != 0) {
                for (IUploadFile f : uploadFiles) {
                    FileData data = new FileData();
                    data.setFileName(f.getOriginalFilename());
                    data.setInputStream(f.getInputStream());
                    data.setFileType(f.getContentType());
                    data.setSize(f.getSize());
                    ls.add(data);
                }
            }

        }
        form.setAddonData(addonData);
        form.setFileDatas(ls);
        return form;
    }
}
