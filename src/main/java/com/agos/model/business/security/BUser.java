package com.agos.model.business.security;

import com.agos.model.business.Business;
import com.agos.model.data.security.DUser;
import com.agos.model.entities.security.User;

/**
 * Created by arielgos on 12/31/16.
 */
public class BUser extends Business {

    public BUser() throws Exception {
        super();
    }

    public User get(long id) {
        DUser data = new DUser(connection);
        User obj = data.search(id);
        return obj;
    }

    public boolean save(User entity) {
        DUser data = new DUser(connection);
        return data.save(entity);
    }

}
