package com.itouch8.pump.util.param.single.impl;

import java.io.Serializable;
import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.encrypt.IEncrypt;
import com.itouch8.pump.core.util.encrypt.impl.ConfigKeyEncrypt;
import com.itouch8.pump.util.param.AbstractParamServiceApi;
import com.itouch8.pump.util.param.common.IParamLoader;
import com.itouch8.pump.util.param.common.IParamStore;
import com.itouch8.pump.util.param.single.ISingleParam;
import com.itouch8.pump.util.param.single.ISingleParamService;

public class SingleParamService extends AbstractParamServiceApi<ISingleParam> implements ISingleParamService, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2175598670576814529L;

    private static final String ENCRYPT_FLAG = "ENCRYPT";

    private final IEncrypt encrypt = new ConfigKeyEncrypt();

    @Override
    public String get(String name) {
        ISingleParam param = this.getParam(name);
        String value = null;
        if (null != param) {
            value = param.getParamValue();
            if (CoreUtils.isBlank(value)) {
                value = param.getDefaultValue();
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E get(String name, Class<E> cls) {
        String param = get(name);
        if (null != param) {
            if (Number.class.isAssignableFrom(cls)) {
                return (E) CoreUtils.convertStringToTargetClass(param, (Class<? extends Number>) cls);
            } else if (boolean.class.equals(cls) || Boolean.class.equals(cls)) {
                Boolean b = CoreUtils.string2Boolean(param);
                return (E) b;
            } else {
                return CoreUtils.convert(param, cls);
            }
        }
        return null;
    }

    @Override
    public <E> E get(String name, E defaultValue, Class<E> cls) {
        E value = get(name, cls);
        return null == value ? defaultValue : value;
    }

    public void refresh() {
        IParamStore<ISingleParam> store = super.getStore();
        if (null != store) {
            store.clear();
        }
        IParamLoader<ISingleParam> loader = super.getLoader();
        if (null != loader) {
            Map<String, ISingleParam> params = loader.loadParams(null);
            if (null != params && null != store) {
                for (String name : params.keySet()) {
                    store.save(name, params.get(name));
                }
            }
        }
    }

    @Override
    protected String getStoreElementName() {
        return ISingleParam.class.getName();
    }

    @Override
    protected void afterLoad(Map<String, ISingleParam> params) {
        IParamStore<ISingleParam> store = super.getStore();
        for (String name : params.keySet()) {
            ISingleParam param = params.get(name);
            if (ENCRYPT_FLAG.equalsIgnoreCase(param.getStoreType())) {
                String value = param.getParamValue();
                if (!CoreUtils.isBlank(value)) {
                    param.setParamValue(encrypt.decode(value, null));
                }
                String defaultValue = param.getDefaultValue();
                if (!CoreUtils.isBlank(defaultValue)) {
                    param.setDefaultValue(encrypt.decode(defaultValue, null));
                }
            }
            if (null != store) {
                store.save(name, param);
            }
        }
    }
}
