package com.itouch8.pump.core.util.track.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;


public class RandomTracker extends AbstractThreadLocalTracker {

    private final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    
    private boolean includeTime = true;

    
    private int randomCount = 8;

    
    @Override
    protected String generateTrackId() {
        StringBuffer sb = new StringBuffer();
        if (isIncludeTime()) {
            sb.append(df.format(new Date()));
        }
        int randomCount = Math.max(4, getRandomCount());
        sb.append(RandomStringUtils.randomNumeric(randomCount));
        return sb.toString();
    }

    public boolean isIncludeTime() {
        return includeTime;
    }

    public void setIncludeTime(boolean includeTime) {
        this.includeTime = includeTime;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }
}
