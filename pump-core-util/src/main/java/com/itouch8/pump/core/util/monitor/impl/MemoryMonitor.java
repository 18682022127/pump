package com.itouch8.pump.core.util.monitor.impl;

import com.itouch8.pump.core.util.monitor.IMonitor;


public class MemoryMonitor implements IMonitor {

    private transient boolean monitor = false;

    
    @Override
    public boolean isMonitoring() {
        return monitor;
    }

    
    @Override
    public void start() {
        this.monitor = true;
    }

    
    @Override
    public void stop() {
        this.monitor = false;
    }
}
