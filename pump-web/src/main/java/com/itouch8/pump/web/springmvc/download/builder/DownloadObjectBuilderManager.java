package com.itouch8.pump.web.springmvc.download.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


public class DownloadObjectBuilderManager {

    // 对象创建器
    private static final Map<String, DownloadObjectBuilder> objectBuilders = new HashMap<String, DownloadObjectBuilder>();

    static {
        Set<Class<? extends IDownloadObjectBuilder>> builders = CoreUtils.scanClassesByParentCls("com.itouch8", IDownloadObjectBuilder.class);
        if (null != builders) {
            for (Class<? extends IDownloadObjectBuilder> builder : builders) {
                registerDownloadObjectBuilder(builder);
            }
        }
    }

    
    public static <E extends IDownloadObjectBuilder> void registerDownloadObjectBuilder(Class<E> cls) {
        IDownloadObjectBuilder builder = CoreUtils.newInstance(cls);
        String type = builder.getBuildType();
        for (String buildType : type.split("\\s*(\\s+|,)\\s*")) {
            DownloadObjectBuilder old = objectBuilders.get(resolveBuildType(buildType));
            if (null == old) {
                registerDownloadObjectBuilder(cls, builder, buildType);
            } else if (builder.getOrder() < old.getOrder()) {//新的优先级更高
                objectBuilders.remove(resolveBuildType(buildType));
                registerDownloadObjectBuilder(cls, builder, buildType);
            } else {//旧的优先级高，不做处理

            }
        }
    }

    
    public static IDownloadObjectBuilder getDownloadObjectBuilder(String buildType) {
        DownloadObjectBuilder builder = objectBuilders.get(resolveBuildType(buildType));
        if (null == builder) {
            Throw.throwRuntimeException("未找到和构建类型" + buildType + "对应的下载对象生成器");
        }
        return builder.getDownloadObjectBuilder();
    }

    private static <E extends IDownloadObjectBuilder> void registerDownloadObjectBuilder(Class<E> cls, IDownloadObjectBuilder builder, String buildType) {
        buildType = resolveBuildType(buildType);
        if (builder.isSingleon()) {
            objectBuilders.put(buildType, new InstanceDownloadObjectBuilder(builder));
        } else {
            objectBuilders.put(buildType, new ClsDownloadObjectBuilder(cls, builder.getOrder()));
        }
    }

    private static String resolveBuildType(String buildType) {
        return buildType.toUpperCase();
    }

    private static abstract class DownloadObjectBuilder {

        private final int order;

        public DownloadObjectBuilder(int order) {
            this.order = order;
        }

        abstract IDownloadObjectBuilder getDownloadObjectBuilder();

        int getOrder() {
            return order;
        }
    }

    private static class InstanceDownloadObjectBuilder extends DownloadObjectBuilder {

        private final IDownloadObjectBuilder instance;

        public InstanceDownloadObjectBuilder(IDownloadObjectBuilder instance) {
            super(instance.getOrder());
            this.instance = instance;
        }

        @Override
        IDownloadObjectBuilder getDownloadObjectBuilder() {
            return instance;
        }
    }

    private static class ClsDownloadObjectBuilder extends DownloadObjectBuilder {

        private final Class<? extends IDownloadObjectBuilder> cls;

        public ClsDownloadObjectBuilder(Class<? extends IDownloadObjectBuilder> cls, int order) {
            super(order);
            this.cls = cls;
        }

        @Override
        IDownloadObjectBuilder getDownloadObjectBuilder() {
            return CoreUtils.newInstance(cls);
        }
    }
}
