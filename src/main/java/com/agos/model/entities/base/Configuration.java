package com.agos.model.entities.base;

import com.agos.model.entities.Entity;
import com.agos.model.entities.annotations.KeyField;

import java.math.BigInteger;

/**
 * Created by arielgos on 12/31/16.
 */
public class Configuration extends Entity {

    @KeyField
    private BigInteger id;
    private String code;
    private String alias;
    private String value;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", alias='" + alias + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
