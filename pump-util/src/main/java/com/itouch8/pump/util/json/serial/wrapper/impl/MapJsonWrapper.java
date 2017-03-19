package com.itouch8.pump.util.json.serial.wrapper.impl;

import java.util.HashMap;
import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;


public class MapJsonWrapper implements IJsonWrapper {

    private String dataJsonPropertyName = "data";

    private String statusJsonPropertyName = "success";

    @Override
    public Object wrap(Object original) {
        Map<String, Object> wrapper = new HashMap<String, Object>();
        String dataJsonPropertyName = getDataJsonPropertyName();
        if (addOriginalData() && !CoreUtils.isBlank(dataJsonPropertyName)) {
            wrapper.put(dataJsonPropertyName, original);
        }

        String statusJsonPropertyName = getStatusJsonPropertyName();
        if (!CoreUtils.isBlank(statusJsonPropertyName)) {
            wrapper.put(statusJsonPropertyName, getStatus(original));
        }
        doWrap(wrapper, original);
        return wrapper;
    }

    protected boolean getStatus(Object original) {
        return true;
    }

    protected boolean addOriginalData() {
        return !CoreUtils.isBlank(getDataJsonPropertyName());
    }

    protected void doWrap(Map<String, Object> wrapper, Object original) {

    }

    public String getDataJsonPropertyName() {
        return dataJsonPropertyName;
    }

    public void setDataJsonPropertyName(String dataJsonPropertyName) {
        this.dataJsonPropertyName = dataJsonPropertyName;
    }

    public String getStatusJsonPropertyName() {
        return statusJsonPropertyName;
    }

    public void setStatusJsonPropertyName(String statusJsonPropertyName) {
        this.statusJsonPropertyName = statusJsonPropertyName;
    }
}
