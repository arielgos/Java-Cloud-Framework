package com.agos.model.entities.security;

import com.agos.model.entities.Entity;
import com.agos.model.entities.annotations.KeyField;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by arielgos on 12/31/16.
 */
public class User extends Entity {

    @KeyField
    private BigInteger id;
    private String code;
    private String name;
    private String email;
    private String password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "User{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", registry=" + registry +
                '}';
    }
}
