package com.itouch8.pump.util.param.enums.impl;

import java.util.Map;

import com.itouch8.pump.util.param.AbstractParamServiceApi;
import com.itouch8.pump.util.param.common.IParamStore;
import com.itouch8.pump.util.param.enums.IEnumParam;


public class EnumParamService extends AbstractParamServiceApi<IEnumParam> {

    @Override
    protected String getStoreElementName() {
        return IEnumParam.class.getName();
    }

    @Override
    protected void afterLoad(Map<String, IEnumParam> params) {
        IParamStore<IEnumParam> store = super.getStore();
        if (null != store) {
            for (String name : params.keySet()) {
                store.save(name, params.get(name));
            }
        }
    }
}
