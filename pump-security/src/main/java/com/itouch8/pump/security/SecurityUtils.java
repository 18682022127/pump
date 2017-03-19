package com.itouch8.pump.security;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.IExceptionMeta;
import com.itouch8.pump.security.core.common.info.ISecurityInfo;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.security.core.session.ISession;
import com.itouch8.pump.security.core.session.ISessionManager;
import com.itouch8.pump.util.Tool;


public class SecurityUtils {

    public static void setSecurityInfo(ISecurityInfo info, String code, Object... params) {
        if (null != info && null != code) {
            info.setCode(code);
            info.setSuccess(false);
            IExceptionMeta meta = Throw.lookupExceptionMeta(code, null);
            if (null != meta && null != meta.getMessageKey()) {
                String message = Tool.LOCALE.getMessage(meta.getMessageKey(), params);
                info.setMessage(message);
            }
        }
    }

    public static void setSecurityCodeAndMessage(ISecurityInfo info, String code, String message) {
        if (null != info && null != code) {
            info.setCode(code);
            info.setMessage(message);
            info.setSuccess(false);
        }
    }

    public static ISessionManager getSessionManager() {
        return PumpConfig.getSessionManager();
    }

    public static ISession getSession(String id) {
        ISessionManager manager = getSessionManager();
        return null == manager ? null : manager.getSession(id);
    }

    public static IUser getUser(String sessionId) {
        ISession session = getSession(sessionId);
        return null == session ? null : session.getUser();
    }
}
