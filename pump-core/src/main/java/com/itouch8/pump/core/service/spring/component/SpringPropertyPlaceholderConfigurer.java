package com.itouch8.pump.core.service.spring.component;

import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ObjectUtils;

import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.encrypt.IEncrypt;
import com.itouch8.pump.core.util.encrypt.impl.ConfigKeyEncrypt;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class SpringPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    
    private String encryptPropertyName = "encrypt";

    
    private IEncrypt encryptAlgorithm = new ConfigKeyEncrypt();

    
    private IPropertiesStore propertiesStore;

    
    @Override
    protected void convertProperties(Properties props) {
        String encryptPropertyName = getEncryptPropertyName();
        IEncrypt encryptAlgorithm = getEncryptAlgorithm();
        if (null == encryptAlgorithm || CoreUtils.isBlank(encryptPropertyName)) {
            super.convertProperties(props);
        } else {
            encryptPropertyName = "." + encryptPropertyName;
            Enumeration<?> propertyNames = props.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                String propertyValue = props.getProperty(propertyName);
                if (propertyName.endsWith(encryptPropertyName)) {
                    props.remove(propertyName);// 将标志去掉
                    if (isEncrypt(propertyValue)) {
                        String p = propertyName.substring(0, propertyName.lastIndexOf("."));
                        if (props.containsKey(p)) {
                            String v = props.getProperty(p);
                            String convertedValue = decode(encryptAlgorithm, propertyValue, v);
                            if (null != convertedValue && !ObjectUtils.nullSafeEquals(v, convertedValue)) {
                                props.setProperty(p, convertedValue);
                            }
                        }
                    }
                } else {
                    String convertedValue = convertProperty(propertyName, propertyValue);
                    if (!ObjectUtils.nullSafeEquals(propertyValue, convertedValue)) {
                        props.setProperty(propertyName, convertedValue);
                    }
                }
            }
        }
        SpringHelp.setPlaceholderPropertis(props);
        storeProperties(props);
    }

    private String decode(IEncrypt encryptAlgorithm, String propertyValue, String v) {
        try {
            String convertedValue = encryptAlgorithm.decode(v, propertyValue);
            return convertedValue;
        } catch (Exception e) {
            CommonLogger.error("decode the encrypt data is error, data is " + v);
            return null;
        }
    }

    
    protected void storeProperties(Properties props) {
        IPropertiesStore propertiesStore = getPropertiesStore();
        if (null != propertiesStore) {
            propertiesStore.store(props);
        }
    }

    
    protected boolean isEncrypt(String encrypt) {
        return CoreUtils.string2Boolean(encrypt);
    }

    public String getEncryptPropertyName() {
        return encryptPropertyName;
    }

    public void setEncryptPropertyName(String encryptPropertyName) {
        this.encryptPropertyName = encryptPropertyName;
    }

    public IEncrypt getEncryptAlgorithm() {
        return encryptAlgorithm;
    }

    public void setEncryptAlgorithm(IEncrypt encryptAlgorithm) {
        this.encryptAlgorithm = encryptAlgorithm;
    }

    public IPropertiesStore getPropertiesStore() {
        return propertiesStore;
    }

    public void setPropertiesStore(IPropertiesStore propertiesStore) {
        this.propertiesStore = propertiesStore;
    }

    
    public interface IPropertiesStore {

        public void store(Properties properties);
    }
}
