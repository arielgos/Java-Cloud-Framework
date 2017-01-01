package com.agos.model.data;

/**
 * Created by arielgos on 12/30/16.
 */

import java.lang.annotation.Annotation;

public class Column {
    private String key;
    private Object value;
    private Annotation annotation;

    public Column(String key, Object value, Annotation annotation) {
        this.key = key;
        this.value = value;
        this.annotation = annotation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

}