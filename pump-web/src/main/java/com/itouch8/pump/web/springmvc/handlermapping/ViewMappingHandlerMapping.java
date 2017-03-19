package com.itouch8.pump.web.springmvc.handlermapping;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.view.ViewMappingHolder;


@Component
public class ViewMappingHandlerMapping extends AbstractHandlerMapping {

    public ViewMappingHandlerMapping() {
        super.setOrder(Integer.MAX_VALUE - 1);
    }

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        IRequestInfo info = ServletHelp.getRequestInfo();
        String defaultView = ViewMappingHolder.getDefaultView(info);
        if (!CoreUtils.isBlank(defaultView)) {
            return new ViewMappingRequestHandler(defaultView);
        }
        return null;
    }

    public class ViewMappingRequestHandler {

        private String defaultView;

        private ViewMappingRequestHandler(String defaultView) {
            this.defaultView = defaultView;
        }

        public ModelAndView handler() {
            ModelAndView mv = new ModelAndView();
            mv.setViewName(defaultView);
            return mv;
        }
    };
}
