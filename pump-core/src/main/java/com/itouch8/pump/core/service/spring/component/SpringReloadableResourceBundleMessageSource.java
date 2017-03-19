package com.itouch8.pump.core.service.spring.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ObjectUtils;

import com.itouch8.pump.core.service.exception.ServiceExceptionCodes;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.exception.Throw;


public class SpringReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            // 4.3 之后版本
            @SuppressWarnings("unchecked")
            Set<String> basenameSet = (Set<String>) FieldUtils.readField(this, "basenameSet", true);
            String[] basenames = BaseConfig.getPumpLocaleBasenames();
            if (!ObjectUtils.isEmpty(basenames)) {
                for (String basename : basenames) {
                    if (!CoreUtils.isBlank(basename)) {
                        basenameSet.add(basename.trim());
                    }
                }
            }
        } catch (Throwable e) {
            String[] basenames = (String[]) FieldUtils.readField(this, "basenames", true);
            if (null == basenames || basenames.length == 0) {
                basenames = BaseConfig.getPumpLocaleBasenames();
            } else {
                List<String> bs = new ArrayList<String>(Arrays.asList(BaseConfig.getPumpLocaleBasenames()));
                for (String basename : basenames) {
                    if (bs.contains(basename)) {
                        Throw.createRuntimeException(ServiceExceptionCodes.BF030006, basename);
                    } else {
                        bs.add(basename);
                    }
                }
                if (null != bs && !bs.isEmpty()) {
                    String[] ss = new String[bs.size()];
                    bs.toArray(ss);
                    basenames = ss;
                }
            }
            FieldUtils.writeField(this, "basenames", basenames, true);
        }
    }

}
