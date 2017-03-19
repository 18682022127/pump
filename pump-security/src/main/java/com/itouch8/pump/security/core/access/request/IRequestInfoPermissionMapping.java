package com.itouch8.pump.security.core.access.request;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.permission.IPermission;


public interface IRequestInfoPermissionMapping {

    
    public IPermission lookup(IRequestInfo requestInfo);
}
