package com.agos.model.data.security;

import com.agos.model.data.Wrapper;
import com.agos.model.entities.security.Profile;

import java.sql.Connection;
import java.util.List;

/**
 * Created by arielgos on 12/31/16.
 */
public class DProfile extends Wrapper {

    public DProfile(Connection connection) {
        super(Profile.class, connection);
    }

    public Profile search(Long id) {
        return (Profile) super.search(id);
    }

    public List<Profile> list(String filter) {
        return super.list("select * from " + this.tableName);
    }

}
