package com.agos.model.data.security;

import com.agos.model.data.Wrapper;
import com.agos.model.entities.security.User;


import java.sql.Connection;
import java.util.List;

/**
 * Created by arielgos on 12/31/16.
 */
public class DUser extends Wrapper {

    public DUser(Connection connection) {
        super(User.class, connection);
    }

    public User search(Long id) {
        return (User) super.search(id);
    }

    public List<User> list(String filter) {
        return super.list("select * from " + this.tableName);
    }

}
