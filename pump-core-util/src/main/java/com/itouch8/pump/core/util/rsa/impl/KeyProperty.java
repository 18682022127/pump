package com.itouch8.pump.core.util.rsa.impl;

import java.math.BigInteger;

import com.itouch8.pump.core.util.rsa.AbstractKey;

public class KeyProperty extends AbstractKey {

    private String module = null;

    private String publicEmpoent = null;

    private String privateEmpoent = null;

    public KeyProperty(String module, String publicEmpoent, String privateEmpoent) {
        super();
        this.module = module;
        this.publicEmpoent = publicEmpoent;
        this.privateEmpoent = privateEmpoent;
    }

    public KeyProperty() {

    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPublicEmpoent() {
        return publicEmpoent;
    }

    public void setPublicEmpoent(String publicEmpoent) {
        this.publicEmpoent = publicEmpoent;
    }

    public String getPrivateEmpoent() {
        return privateEmpoent;
    }

    public void setPrivateEmpoent(String privateEmpoent) {
        this.privateEmpoent = privateEmpoent;
    }

    public KeyProperty buildHexKey() {
        return this.toHex();
    }

    private KeyProperty toHex() {
        this.setModule(new BigInteger(this.module).toString(16));
        this.setPrivateEmpoent(new BigInteger(this.privateEmpoent).toString(16));
        this.setPublicEmpoent(new BigInteger(this.publicEmpoent).toString(16));
        return this;
    }

}
