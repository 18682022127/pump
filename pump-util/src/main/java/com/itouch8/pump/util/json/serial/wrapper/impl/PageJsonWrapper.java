package com.itouch8.pump.util.json.serial.wrapper.impl;

import java.util.Map;

import com.itouch8.pump.core.util.page.PageUtils;
import com.itouch8.pump.util.json.serial.wrapper.impl.MapJsonWrapper;


public class PageJsonWrapper extends MapJsonWrapper {

    
    private String totalRecordsJsonPropertyName = "total";

    public PageJsonWrapper() {
        super.setDataJsonPropertyName("rows");
    }

    @Override
    protected void doWrap(Map<String, Object> wrapper, Object original) {
        wrapper.put(getTotalRecordsJsonPropertyName(), getTotalRecords());
    }

    protected long getTotalRecords() {
        return PageUtils.getPage().getTotalRecords();
    }

    public String getTotalRecordsJsonPropertyName() {
        return totalRecordsJsonPropertyName;
    }

    public void setTotalRecordsJsonPropertyName(String totalRecordsJsonPropertyName) {
        this.totalRecordsJsonPropertyName = totalRecordsJsonPropertyName;
    }
}
