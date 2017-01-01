package com.agos.model.data.base;

import com.agos.model.data.Wrapper;
import com.agos.model.entities.base.Configuration;

import java.sql.Connection;
import java.util.List;

/**
 * Created by arielgos on 12/31/16.
 */
public class DConfiguration extends Wrapper {

    public DConfiguration(Connection connection) {
        super(Configuration.class, connection);
    }

    public Configuration search(Long id) {
        return (Configuration) super.search(id);
    }

    public List<Configuration> list() {
        return super.list("select * from " + this.tableName);
    }

}
