package com.solar.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.handler.Handler;
import com.solar.util.ScanPackage;

public class BeanFactory {

    private static final Logger         logger            = LoggerFactory
        .getLogger(BeanFactory.class);

    private volatile static BeanFactory beanFactory       = null;

    private static final String         SCAN_PACKAGE_PATH = "com.solar.handler";

    private Map<String, Object>         beanMap           = new HashMap<String, Object>();

    private BeanFactory() {
        Set<Class<?>> classBeans = ScanPackage.getInstance().getClassBean(SCAN_PACKAGE_PATH);
        if (!classBeans.isEmpty()) {
            for (Class<?> classBean : classBeans) {
                Object bean = classBean;
                try {
                    bean = classBean.newInstance();
                } catch (InstantiationException e) {
                    System.out.println(e);
                    logger.error("", e);
                } catch (IllegalAccessException e) {
                    System.out.println(e);
                    logger.error("", e);
                }
                if (bean instanceof Handler) {
                    SolarBean solarBean = (SolarBean) bean;
                    beanMap.put(solarBean.getName(), bean);
                }
            }
        }
    }

    public static BeanFactory getInstence() {
        synchronized (BeanFactory.class) {
            if (beanFactory == null) {
                beanFactory = new BeanFactory();
            }
        }
        return beanFactory;
    }

    public Map<String, Object> getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

}
