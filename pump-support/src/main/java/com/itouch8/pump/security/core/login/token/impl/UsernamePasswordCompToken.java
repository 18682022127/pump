package com.itouch8.pump.security.core.login.token.impl;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.itouch8.pump.security.core.login.token.IAuthenticationToken;

public class UsernamePasswordCompToken implements IAuthenticationToken {

    @NotBlank(message = "{NotBlank.usernamePasswordToken.userId}")
    private String userId;

    @Size(min = 6, max = 16, message = "{Size.usernamePasswordToken.userPwd}")
    private String userPwd;

    @NotBlank(message = "{Size.usernamePasswordToken.compId}")
    private String compId;

    @NotBlank(message = "{NotBlank.usernamePasswordToken.captcha}")
    private String captcha;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public Object getPrincipal() {
        return getUserId();
    }

    @Override
    public Object getCredentials() {
        return getUserPwd();
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
