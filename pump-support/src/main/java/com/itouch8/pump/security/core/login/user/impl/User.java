package com.itouch8.pump.security.core.login.user.impl;

import com.itouch8.pump.security.core.access.permission.IPermissionManager;
import com.itouch8.pump.security.core.access.permission.impl.PermissionManager;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.util.param.single.ISingleParamService;

public class User implements IUser {

    private String userId;

    private String userName;

    private String nickName;

    private String userPwd;

    private String userStatus;

    private String orgId;

    private String certType;

    private String certNo;

    private String mobilePhone;

    private String telephone;

    private String email;

    private String limitIp;

    private int onlineSessionNum;

    private String lockFlag;

    private String lockDate;

    private String lockTime;

    private int loginNum;

    private String lastLoginIp;

    private String lastLoginDate;

    private String lastLoginTime;

    private String modiPwdDate;

    private String modiPwdTime;

    private String currentRoleId = "-1";

    private IPermissionManager permissionManager = new PermissionManager();

    private ISingleParamService paramService;

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    @Override
    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    @Override
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getLimitIp() {
        return limitIp;
    }

    public void setLimitIp(String limitIp) {
        this.limitIp = limitIp;
    }

    @Override
    public int getOnlineSessionNum() {
        return onlineSessionNum;
    }

    public void setOnlineSessionNum(int onlineSessionNum) {
        this.onlineSessionNum = onlineSessionNum;
    }

    @Override
    public String getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(String lockFlag) {
        this.lockFlag = lockFlag;
    }

    @Override
    public String getLockDate() {
        return lockDate;
    }

    public void setLockDate(String lockDate) {
        this.lockDate = lockDate;
    }

    @Override
    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    @Override
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @Override
    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String getModiPwdDate() {
        return modiPwdDate;
    }

    public void setModiPwdDate(String modiPwdDate) {
        this.modiPwdDate = modiPwdDate;
    }

    @Override
    public String getModiPwdTime() {
        return modiPwdTime;
    }

    public void setModiPwdTime(String modiPwdTime) {
        this.modiPwdTime = modiPwdTime;
    }

    @Override
    public String getCurrentRoleId() {
        return currentRoleId;
    }

    @Override
    public void setCurrentRoleId(String currentRoleId) {
        this.currentRoleId = currentRoleId;
    }

    @Override
    public IPermissionManager getPermissionManager() {
        return permissionManager;
    }

    @Override
    public void setPermissionManager(IPermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @Override
    public ISingleParamService getParamService() {
        return paramService;
    }

    public void setParamService(ISingleParamService paramService) {
        this.paramService = paramService;
    }
}
