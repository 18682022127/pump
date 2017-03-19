package com.itouch8.pump.util.json.serial.wrapper.impl;

import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;


public class NoWrapJsonWrapper implements IJsonWrapper {

    @Override
    public Object wrap(Object original) {
        return original;
    }
}
