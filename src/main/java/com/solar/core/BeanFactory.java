package com.solar.core;

public class BeanFactory {

    private volatile static BeanFactory beanFactory = null;

    private BeanFactory() {

    }

    public static BeanFactory getInstence() {
        if (beanFactory == null) {
            synchronized (BeanFactory.class) {
                beanFactory = new BeanFactory();
            }
        }
        return beanFactory;
    }
}
