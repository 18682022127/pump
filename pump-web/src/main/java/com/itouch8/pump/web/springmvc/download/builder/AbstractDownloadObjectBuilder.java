package com.itouch8.pump.web.springmvc.download.builder;


public abstract class AbstractDownloadObjectBuilder implements IDownloadObjectBuilder {

    private boolean singleon;

    private int order;

    private String buildType;

    @Override
    public boolean isSingleon() {
        return singleon;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getBuildType() {
        return buildType;
    }

    public void setSingleon(boolean singleon) {
        this.singleon = singleon;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }
}
