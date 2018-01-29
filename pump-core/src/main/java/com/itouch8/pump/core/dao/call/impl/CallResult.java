package com.itouch8.pump.core.dao.call.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.itouch8.pump.core.dao.call.ICallResult;
import com.itouch8.pump.core.dao.exception.DaoExceptionCodes;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


public class CallResult implements ICallResult {

    private final Map<String, Object> resultMaps = new LinkedHashMap<String, Object>();

    
    public void addResult(String name, Object result) {
        resultMaps.put(name, result);
    }

    
    public void addAllResult(Map<String, Object> results) {
        resultMaps.putAll(results);
    }

    
    @Override
    public <T> T getOutputParam(String name) {
        if (!resultMaps.containsKey(name)) {
            Throw.createRuntimeException(DaoExceptionCodes.YT020001, name);
        }
        return CoreUtils.cast(resultMaps.get(name));
    }

    
    @Override
    public Iterator<String> iterator() {
        return resultMaps.keySet().iterator();
    }
}
