package com.itouch8.pump.core.util.track;

import com.itouch8.pump.core.util.annotation.Warning;


public interface ITracker {

    
    public void start();

    
    @Warning("一般用于多线程环境中开始新的跟踪")
    public void start(String trackId);

    
    public boolean isTracking();

    
    public String getCurrentTrackId();

    
    public void stop();
}
