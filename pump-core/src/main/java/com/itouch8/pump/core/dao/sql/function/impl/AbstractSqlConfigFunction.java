package com.itouch8.pump.core.dao.sql.function.impl;

import com.itouch8.pump.core.dao.sql.function.ISqlConfigFunction;


public abstract class AbstractSqlConfigFunction implements ISqlConfigFunction {

    private int order;

    private String name;

    
    @Override
    public boolean isSingleon() {
        return true;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
