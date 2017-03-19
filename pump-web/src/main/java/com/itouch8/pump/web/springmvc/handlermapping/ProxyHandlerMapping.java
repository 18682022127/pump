package com.itouch8.pump.web.springmvc.handlermapping;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;


public class ProxyHandlerMapping extends AbstractHandlerMapping {

    private List<IProxyHandler> proxyHandlers;

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        if (null != proxyHandlers) {
            for (IProxyHandler proxyHandler : proxyHandlers) {
                if (proxyHandler.isSupport(request)) {
                    return new ProxyRequestHandler(request, proxyHandler);
                }
            }
        }
        return null;
    }

    public List<IProxyHandler> getProxyHandlers() {
        return proxyHandlers;
    }

    public void setProxyHandlers(List<IProxyHandler> proxyHandlers) {
        this.proxyHandlers = proxyHandlers;
    }

    public class ProxyRequestHandler {

        private final HttpServletRequest request;
        private final IProxyHandler proxyHandler;

        private ProxyRequestHandler(HttpServletRequest request, IProxyHandler proxyHandler) {
            this.request = request;
            this.proxyHandler = proxyHandler;
        }

        public ModelAndView handler() {
            return proxyHandler.handler(request);
        }
    }
}
