package com.itouch8.pump.core.util.track;

import com.itouch8.pump.core.util.config.BaseConfig;


public class Tracker {

    
    public static void start() {
        getTracker().start();
    }

    
    public static void start(String trackId) {
        getTracker().start(trackId);
    }

    
    public static boolean isTracking() {
        return getTracker().isTracking();
    }

    
    public static String getCurrentTrackId() {
        return getTracker().getCurrentTrackId();
    }

    
    public static void stop() {
        getTracker().stop();
    }

    
    private static ITracker getTracker() {
        return BaseConfig.getTracker();
    }
}
