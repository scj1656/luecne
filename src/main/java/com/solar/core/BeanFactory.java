package com.solar.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.util.ScanPackage;

public class BeanFactory {

    private static final Logger         logger            = LoggerFactory
        .getLogger(BeanFactory.class);

    private volatile static BeanFactory beanFactory       = null;

    private static final String         SCAN_PACKAGE_PATH = "com.solar.handler";

    private Map<String, SolarBean>      beanMap           = new HashMap<String, SolarBean>();

    private BeanFactory() {
        Set<Class<?>> classBeans = ScanPackage.getInstance().getClassBean(SCAN_PACKAGE_PATH);
        if (!classBeans.isEmpty()) {
            for (Class<?> classBean : classBeans) {
                Object bean = null;
                try {
                    bean = classBean.getClass().newInstance();
                } catch (InstantiationException e) {
                    logger.error("", e);
                } catch (IllegalAccessException e) {
                    logger.error("", e);
                }
                if (bean instanceof SolarBean) {
                    SolarBean solarBean = (SolarBean) bean;
                    beanMap.put(solarBean.getName(), solarBean);
                }
            }
        }
    }

    public static BeanFactory getInstence() {
        if (beanFactory == null) {
            synchronized (BeanFactory.class) {
                beanFactory = new BeanFactory();
            }
        }
        return beanFactory;
    }

    public Map<String, SolarBean> getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(Map<String, SolarBean> beanMap) {
        this.beanMap = beanMap;
    }
}
