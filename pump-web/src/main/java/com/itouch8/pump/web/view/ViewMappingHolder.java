package com.itouch8.pump.web.view;

import java.util.ArrayList;
import java.util.List;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.init.Init;
import com.itouch8.pump.web.WebPumpConfig;


@Init
public class ViewMappingHolder {

    
    private static final List<IViewMapping> overrideViewMapping = new ArrayList<IViewMapping>();

    
    private static final List<IViewMapping> defaultViewMapping = new ArrayList<IViewMapping>();

    
    public void init() {
        this.initOverrideViewMapping();
        this.initViewMapping();
    }

    
    public static String getOverrideView(IRequestInfo info) {
        if (!overrideViewMapping.isEmpty()) {
            for (IViewMapping viewMapping : overrideViewMapping) {
                String view = viewMapping.getViewname(info);
                if (!CoreUtils.isBlank(view)) {
                    return view;
                }
            }
        }
        return null;
    }

    
    public static String getDefaultView(IRequestInfo info) {
        if (!defaultViewMapping.isEmpty()) {
            for (IViewMapping viewMapping : defaultViewMapping) {
                String view = viewMapping.getViewname(info);
                if (!CoreUtils.isBlank(view)) {
                    return view;
                }
            }
        }
        return null;
    }

    
    private void initOverrideViewMapping() {
        List<IViewMapping> viewMappings = WebPumpConfig.getViewMappings();
        if (null != viewMappings) {
            for (IViewMapping viewMapping : viewMappings) {
                if (viewMapping.overrideUserView()) {
                    overrideViewMapping.add(viewMapping);
                }
            }
        }
    }

    
    private void initViewMapping() {
        List<IViewMapping> viewMappings = WebPumpConfig.getViewMappings();
        if (null != viewMappings) {
            for (IViewMapping viewMapping : viewMappings) {
                if (!viewMapping.overrideUserView()) {
                    defaultViewMapping.add(viewMapping);
                }
            }
        }
    }
}
