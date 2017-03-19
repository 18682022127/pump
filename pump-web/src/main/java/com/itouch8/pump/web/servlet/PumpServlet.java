package com.itouch8.pump.web.servlet;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.init.InitManage;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.view.ViewMappingHolder;


public class PumpServlet extends DispatcherServlet {

    
    private static final long serialVersionUID = -1598541136448241235L;

    @Override
    protected void onRefresh(ApplicationContext context) {
        super.onRefresh(context);
        ServletHelp.setApplication(super.getServletContext());
        initRequestMappingHandlerAdapter(context);
        InitManage.initialize();
        init(super.getServletContext(), context);
    }

    private void initRequestMappingHandlerAdapter(ApplicationContext context) {
        try {
            List<HandlerMethodReturnValueHandler> returnValueHandlers = WebPumpConfig.getPriorReturnValueHandlers();
            if (null != returnValueHandlers && !returnValueHandlers.isEmpty()) {
                RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class.getName(), RequestMappingHandlerAdapter.class);
                Field returnValueHandlersField = RequestMappingHandlerAdapter.class.getDeclaredField("returnValueHandlers");
                returnValueHandlersField.setAccessible(true);
                HandlerMethodReturnValueHandlerComposite composite = (HandlerMethodReturnValueHandlerComposite) returnValueHandlersField.get(adapter);
                returnValueHandlersField = HandlerMethodReturnValueHandlerComposite.class.getDeclaredField("returnValueHandlers");
                returnValueHandlersField.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<HandlerMethodReturnValueHandler> origin = (List<HandlerMethodReturnValueHandler>) returnValueHandlersField.get(composite);
                origin.addAll(0, returnValueHandlers);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    
    protected void init(ServletContext servletContext, ApplicationContext context) {

    }

    
    @Override
    protected String getDefaultViewName(HttpServletRequest request) throws Exception {
        IRequestInfo info = ServletHelp.getRequestInfo();
        String defaultView = ViewMappingHolder.getDefaultView(info);
        if (!CoreUtils.isBlank(defaultView)) {
            return defaultView;
        } else {
            return super.getDefaultViewName(request);
        }
    }

    
    @Override
    public void destroy() {
        try {
            InitManage.destory();
        } finally {
            super.destroy();
        }
    }
}
