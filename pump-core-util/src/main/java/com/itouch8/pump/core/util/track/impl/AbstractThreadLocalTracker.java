package com.itouch8.pump.core.util.track.impl;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.track.ITracker;


public abstract class AbstractThreadLocalTracker implements ITracker {

    private static final ThreadLocal<String> trackIdLocal = new ThreadLocal<String>();

    
    abstract protected String generateTrackId();

    
    @Override
    public void start() {
        String id = trackIdLocal.get();
        if (!CoreUtils.isBlank(id)) {
            CommonLogger.debug("the tracker has started, the trackId is " + id + ".");
        } else {
            this.startNewTracker();
        }
    }

    
    private void startNewTracker() {
        trackIdLocal.set(generateTrackId());
    }

    
    @Override
    public void start(String trackId) {
        String id = trackIdLocal.get();
        if (!CoreUtils.isBlank(id)) {
            CommonLogger.debug("the old trackId is " + id + ", and override using new trackId " + trackId + ".");
        }
        trackIdLocal.set(trackId);
    }

    
    @Override
    public boolean isTracking() {
        return !CoreUtils.isBlank(trackIdLocal.get());
    }

    
    @Override
    public String getCurrentTrackId() {
        return trackIdLocal.get();
    }

    
    @Override
    public void stop() {
        trackIdLocal.remove();
    }
}
