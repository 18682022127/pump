package com.itouch8.pump.security.core.login.user;

import com.itouch8.pump.security.core.access.permission.IPermissionManager;
import com.itouch8.pump.util.param.single.ISingleParamService;


public interface IUser {

    
    public String DEFAULT_ROLE_OPTION_NAME = "USER_DEFAULT_ROLE";

    
    public String getUserId();

    
    public String getUserName();

    
    public String getNickName();

    
    public String getUserPwd();

    
    public String getUserStatus();

    
    public String getOrgId();

    
    public String getCertType();

    
    public String getCertNo();

    
    public String getMobilePhone();

    
    public String getTelephone();

    
    public String getEmail();

    
    public String getLimitIp();

    
    public int getOnlineSessionNum();

    
    public String getLockFlag();

    
    public String getLockDate();

    
    public String getLockTime();

    
    public int getLoginNum();

    
    public String getLastLoginIp();

    
    public String getLastLoginDate();

    
    public String getLastLoginTime();

    
    public String getModiPwdDate();

    
    public String getModiPwdTime();

    
    public int getCurrentRoleId();

    
    public void setCurrentRoleId(int currentRoleId);

    
    public IPermissionManager getPermissionManager();

    
    public void setPermissionManager(IPermissionManager permissionManager);

    
    public ISingleParamService getParamService();

    
    public void setParamService(ISingleParamService paramService);
}
