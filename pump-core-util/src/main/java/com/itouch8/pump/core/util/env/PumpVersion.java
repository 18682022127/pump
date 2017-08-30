package com.itouch8.pump.core.util.env;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;

public enum PumpVersion {

    V_1_0_0("1.0.0", 1.7),

    V_1_0_1("1.0.1", 1.7);

    public static final PumpVersion Version = V_1_0_1;

    private String version;

    private double jdkVersion;

    private PumpVersion(String version, double jdkVersion) {
        this.version = version;
        this.jdkVersion = jdkVersion;
    }

    public static void checkJdkVersion() {
        if (EnvConsts.JDK_VERSION < Version.jdkVersion) {
            Throw.throwRuntimeException(ExceptionCodes.BF010004, EnvConsts.JDK_VERSION, Version.jdkVersion);
        }
    }

    public String toString() {
        return "pump-" + version;
    }
}
