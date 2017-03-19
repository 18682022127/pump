package com.itouch8.pump.core.dao.mybatis.call;

import org.apache.ibatis.session.RowBounds;

import com.itouch8.pump.core.dao.call.ICallResult;


public class CallAdapter extends RowBounds {

    
    private ICallResult callResult;

    
    public ICallResult getCallResult() {
        return callResult;
    }

    
    public void setCallResult(ICallResult callResult) {
        this.callResult = callResult;
    }
}
