package com.itouch8.pump.util.param.tree.impl;

import java.util.Map;

import com.itouch8.pump.util.param.AbstractParamServiceApi;
import com.itouch8.pump.util.param.common.IParamStore;
import com.itouch8.pump.util.param.tree.ITreeParam;


public class TreeParamService extends AbstractParamServiceApi<ITreeParam> {

    @Override
    protected void afterLoad(Map<String, ITreeParam> params) {
        IParamStore<ITreeParam> store = super.getStore();
        for (String name : params.keySet()) {
            ITreeParam tree = params.get(name);
            tree.build();
            if (null != store) {
                store.save(name, tree);
            }
        }
    }

    @Override
    protected String getStoreElementName() {
        return ITreeParam.class.getName();
    }
}
