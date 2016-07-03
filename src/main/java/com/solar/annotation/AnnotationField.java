package com.solar.annotation;

import java.lang.annotation.Annotation;

/**
 * 注解标签的属性
 * 
 * @author xiaojie
 * @version $Id: AnnotationField.java, v 0.1 2016年7月1日 下午3:57:36 xiaojie Exp $
 */
public class AnnotationField {

    private String       fieldName;
    private Object       fieldValue;
    private Annotation[] annotations;

    public AnnotationField(String fieldName, Object fieldValue, Annotation[] annotations) {
        super();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.annotations = annotations;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

}
