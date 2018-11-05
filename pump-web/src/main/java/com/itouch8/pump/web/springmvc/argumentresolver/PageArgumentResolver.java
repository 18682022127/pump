package com.itouch8.pump.web.springmvc.argumentresolver;

import org.apache.ibatis.session.RowBounds;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.itouch8.pump.core.dao.mybatis.page.PageAdapter;
import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.PageUtils;

public class PageArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return IPage.class.isAssignableFrom(paramType) || RowBounds.class.isAssignableFrom(paramType);
    }

    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        IPage page = PageUtils.createPage();
        if (IPage.class.isAssignableFrom(paramType)) {
            return page;
        } else {
            PageAdapter pa = new PageAdapter(page);
            return pa;
        }
    }
}
