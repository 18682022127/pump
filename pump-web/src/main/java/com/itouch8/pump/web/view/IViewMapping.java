package com.itouch8.pump.web.view;

import com.itouch8.pump.core.service.request.IRequestInfo;


public interface IViewMapping {

    
    public String getViewname(IRequestInfo requestInfo);

    
    public boolean overrideUserView();
}
