package com.itouch8.pump.web.springmvc.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itouch8.pump.util.param.IParamService;
import com.itouch8.pump.util.param.enums.IEnumParam;
import com.itouch8.pump.util.param.enums.IEnumParamItem;


@Component("enum")
public class EnumServiceRender {

    
    @Autowired(required = false)
    private IParamService service;

    public IParamService getService() {
        return service;
    }

    public void setService(IParamService service) {
        this.service = service;
    }

    public Object val(String type, String code) {
        if (null == service) {
            return code;
        }
        IEnumParam ep = service.getEnumParameter(type);
        if (null == ep) {
            return code;
        } else {
            IEnumParamItem item = ep.getItem(code);
            if (null == item) {
                return code;
            } else {
                return item.getDataText();
            }
        }
    }
}
