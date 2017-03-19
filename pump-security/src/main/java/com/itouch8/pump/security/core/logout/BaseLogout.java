package com.itouch8.pump.security.core.logout;

import java.util.List;
import java.util.Set;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.security.core.logout.handler.ILogoutHandler;
import com.itouch8.pump.security.core.logout.info.ILogoutInfo;
import com.itouch8.pump.security.core.logout.info.impl.BaseLogoutInfo;
import com.itouch8.pump.security.core.logout.provider.IExpiredSessionProvider;
import com.itouch8.pump.security.core.session.ISession;
import com.itouch8.pump.security.core.session.ISessionManager;


public class BaseLogout {

    private List<IExpiredSessionProvider> providers;

    private List<ILogoutHandler> handlers;

    public void logout() {
        ISessionManager manager = PumpConfig.getSessionManager();
        List<IExpiredSessionProvider> providers = getProviders();
        List<ILogoutHandler> handlers = getHandlers();
        if (null != providers && !providers.isEmpty() && null != handlers && !handlers.isEmpty()) {
            for (IExpiredSessionProvider provider : providers) {
                Set<ISession> sessions = provider.getExpiredSessions();
                if (null != sessions && !sessions.isEmpty()) {
                    for (ISession session : sessions) {
                        ILogoutInfo info = new BaseLogoutInfo(session);
                        for (ILogoutHandler handler : handlers) {
                            handler.handler(info);
                            // if(!info.isSuccess()){
                            // break;
                            // }
                        }
                        if (null != manager) {
                            manager.delete(session.getId());
                        }
                    }
                }
            }
        }
    }

    public List<IExpiredSessionProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<IExpiredSessionProvider> providers) {
        this.providers = providers;
    }

    public List<ILogoutHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<ILogoutHandler> handlers) {
        this.handlers = handlers;
    }
}
