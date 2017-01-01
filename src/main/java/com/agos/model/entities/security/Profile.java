package com.agos.model.entities.security;

import com.agos.model.entities.Entity;
import com.agos.model.entities.annotations.KeyField;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by arielgos on 12/31/16.
 */
public class Profile extends Entity {

    @KeyField
    private BigInteger id;
    private String code;
    private String name;
    private Boolean active;
    private Date registry;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getRegistry() {
        return registry;
    }

    public void setRegistry(Date registry) {
        this.registry = registry;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", registry=" + registry +
                '}';
    }
}
