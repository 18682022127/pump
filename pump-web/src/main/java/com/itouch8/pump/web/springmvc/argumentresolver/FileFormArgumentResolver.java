package com.itouch8.pump.web.springmvc.argumentresolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;

import com.itouch8.pump.web.WebUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
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
        if (request instanceof MultipartRequest) {
            ServletHelp.setRequestAndResponse(request, null);
            IUploadFile[] uploadFiles = ServletHelp.getUploadFile();
            if (null != uploadFiles && uploadFiles.length != 0)
                if (FileForm.class.isAssignableFrom(parameter.getParameterType())) {
                    form.setFiles(uploadFiles);
                }
        }
        return form;
    }
}
