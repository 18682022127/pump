package com.itouch8.pump.core.service.request;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.service.request.impl.BaseRequestInfo;
import com.itouch8.pump.core.util.track.Tracker;


public class RequestInfoContext {

    private static final ThreadLocal<BaseRequestInfo> context = new InheritableThreadLocal<BaseRequestInfo>();

    public static void setRequestUrl(String requestUrl) {
        BaseRequestInfo info = initialContext();
        info.setRequestUrl(requestUrl);
    }

    public static void setProtocol(String protocol) {
        BaseRequestInfo info = initialContext();
        info.setProtocol(protocol);
    }

    public static void setRemoteIp(String remoteIp) {
        BaseRequestInfo info = initialContext();
        info.setRemoteIp(remoteIp);
    }

    public static void setRemoteOperateSystem(String remoteOperateSystem) {
        BaseRequestInfo info = initialContext();
        info.setRemoteOperateSystem(remoteOperateSystem);
    }

    public static void setRemoteBrowser(String remoteBrowser) {
        BaseRequestInfo info = initialContext();
        info.setRemoteBrowser(remoteBrowser);
    }

    public static void addParameters(String key, Object parameter) {
        BaseRequestInfo info = initialContext();
        Map<String, Object> parameters = info.getParameters();
        if (null == parameters) {
            parameters = new LinkedHashMap<String, Object>();
            info.setParameters(parameters);
        }
        parameters.put(key, parameter);
    }

    public static void addAllParameters(Map<String, Object> parameters) {
        BaseRequestInfo info = initialContext();
        Map<String, Object> params = info.getParameters();
        if (null == parameters) {
            parameters = new LinkedHashMap<String, Object>();
            info.setParameters(params);
        }
        params.putAll(parameters);
    }

    public static void remove() {
        context.remove();
    }

    public static IRequestInfo getRequestInfo() {
        return context.get();
    }

    public static void setRequestInfo(BaseRequestInfo info) {
        context.set(info);
    }

    private static BaseRequestInfo initialContext() {
        BaseRequestInfo info = context.get();
        if (info == null) {
            info = new BaseRequestInfo();
            if (!Tracker.isTracking()) {
                Tracker.start();
            }
            info.setRequestId(Tracker.getCurrentTrackId());
            info.setDate(new Date());
            info.setTime(System.currentTimeMillis());
            context.set(info);
        }
        return info;
    }
}
