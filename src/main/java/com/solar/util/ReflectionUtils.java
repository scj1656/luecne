package com.solar.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Field getDeclaredField(Object obj, String fieldName) {
        Field field = null;

        Class<?> cls = obj.getClass();

        for (; cls != Object.class; cls = cls.getSuperclass()) {
            try {
                field = cls.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                logger.error("", e);
            } catch (SecurityException e) {
                logger.error("", e);
            }
        }
        return null;
    }

}
