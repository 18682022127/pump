package com.itouch8.pump.web.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class AjaxController implements InitializingBean, DisposableBean {

    @ResponseBody
    @RequestMapping("/ajax.AjaxServlet")
    public Object doHandlerAjaxRequest(HttpServletRequest req, HttpServletResponse resp) {
        return AjaxInvoker.invokedAjaxMethod(req, resp);
    }

    @Override
    public void destroy() throws Exception {
        AjaxInvoker.clear();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AjaxInvoker.initAjaxMethod();
    }
}
