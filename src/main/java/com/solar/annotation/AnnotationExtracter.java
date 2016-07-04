package com.solar.annotation;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.util.ReflectionUtils;

public class AnnotationExtracter {

    private static final Logger         logger          = LoggerFactory
        .getLogger(AnnotationExtracter.class);

    private Annotation[]                classAnnotations;

    private final List<AnnotationField> annotatedFields = new ArrayList<AnnotationField>();

    public AnnotationExtracter(Object obj) {
        Class<?> cls = obj.getClass();

        setClassAnnotations(cls.getAnnotations());

        PropertyDescriptor[] propertyDescriptors = null;
        try {
            propertyDescriptors = Introspector.getBeanInfo(cls).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            logger.error("", e);
            return;
        }
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String fieldName = descriptor.getName();
            Field field = ReflectionUtils.getDeclaredField(obj, fieldName);

            if (field == null) {
                continue;
            }

            Method method = descriptor.getReadMethod();

            method.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = method.invoke(obj, new Object[0]);
            } catch (IllegalAccessException e) {
                logger.error("", e);
            } catch (IllegalArgumentException e) {
                logger.error("", e);
            } catch (InvocationTargetException e) {
                logger.error("", e);
            }

            Annotation[] annotations = field.getAnnotations();
            annotatedFields.add(new AnnotationField(fieldName, fieldValue, annotations));
        }
    }

    public Annotation[] getClassAnnotations() {
        return classAnnotations;
    }

    public void setClassAnnotations(Annotation[] classAnnotations) {
        this.classAnnotations = classAnnotations;
    }

    public List<AnnotationField> getAnnotatedFields() {
        return annotatedFields;
    }

}
