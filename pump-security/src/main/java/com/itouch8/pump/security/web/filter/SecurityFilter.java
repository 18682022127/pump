package com.itouch8.pump.security.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.authz.IAuthorizer;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.servlet.filter.AbstractSkipPathMatcherFilter;


public class SecurityFilter extends AbstractSkipPathMatcherFilter {

    public static final String AUTHORIZATION_INFO_ATTRIBUTE_NAME = SecurityFilter.class.getName() + ".AUTHORIZATION_INFO_ATTRIBUTE_NAME";

    
    private IAuthorizer authorizer;

    
    @Override
    protected void executeFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        IAuthorizer authorizer = getAuthorizer();
        if (null != authorizer) {
            ServletHelp.setRequestAndResponse(request, response);
            IRequestInfo requestInfo = ServletHelp.getRequestInfo();
            IAuthorizationInfo info = authorizer.isPermitted(requestInfo);
            if (null != info && !info.isSuccess()) {// 未通过权限验证
                // 这里只做标记，而将实际处理推迟
                request.setAttribute(IAuthorizationInfo.class.getName(), info);
                // request.getRequestDispatcher().forward(request, response);
            }
            request.setAttribute(AUTHORIZATION_INFO_ATTRIBUTE_NAME, info);
        }
        filterChain.doFilter(request, response);
    }

    public IAuthorizer getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(IAuthorizer authorizer) {
        this.authorizer = authorizer;
    }
}
