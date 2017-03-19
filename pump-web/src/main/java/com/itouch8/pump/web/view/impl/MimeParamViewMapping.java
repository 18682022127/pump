package com.itouch8.pump.web.view.impl;

import java.util.Map;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.view.IViewMapping;


public class MimeParamViewMapping implements IViewMapping {

    
    private String mimeParamName = "mime";

    
    private Map<String, String> mimeMapping;

    
    @Override
    public String getViewname(IRequestInfo requestInfo) {
        String mime = ServletHelp.getRequest().getParameter(getMimeParamName());
        if (!CoreUtils.isBlank(mime)) {
            Map<String, String> mapping = getMimeMapping();
            if (null != mapping) {
                String view = mapping.get(mime);
                if (!CoreUtils.isBlank(view)) {
                    return view;
                }
            }
        }
        return null;
    }

    
    @Override
    public boolean overrideUserView() {
        return true;
    }

    public String getMimeParamName() {
        return mimeParamName;
    }

    public void setMimeParamName(String mimeParamName) {
        this.mimeParamName = mimeParamName;
    }

    public Map<String, String> getMimeMapping() {
        return mimeMapping;
    }

    public void setMimeMapping(Map<String, String> mimeMapping) {
        this.mimeMapping = mimeMapping;
    }
}
