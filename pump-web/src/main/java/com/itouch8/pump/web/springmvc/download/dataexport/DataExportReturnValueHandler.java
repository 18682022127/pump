package com.itouch8.pump.web.springmvc.download.dataexport;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.itouch8.pump.web.springmvc.download.DownloadConsts;


public class DataExportReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return DataExportHelp.isExportData() && DataExportExecutor.getDataStreamReader() != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setViewName(DownloadConsts.DOWNLOAD_VIEW_BEAN_NAME);
    }
}
