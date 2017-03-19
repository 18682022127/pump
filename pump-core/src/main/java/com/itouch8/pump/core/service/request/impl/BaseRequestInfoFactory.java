package com.itouch8.pump.core.service.request.impl;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.service.request.IRequestInfoFactory;
import com.itouch8.pump.core.service.request.RequestInfoContext;


public class BaseRequestInfoFactory implements IRequestInfoFactory {

    
    @Override
    public IRequestInfo getRequestInfo() {
        return RequestInfoContext.getRequestInfo();
    }

}
