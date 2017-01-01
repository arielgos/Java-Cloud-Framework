package com.agos.model.business.base;

import com.agos.model.business.Business;
import com.agos.model.data.base.DConfiguration;
import com.agos.model.entities.base.Configuration;

import java.util.List;

/**
 * Created by arielgos on 12/31/16.
 */
public class BConfiguration extends Business {

    public BConfiguration() throws Exception {
        super();
    }

    public Configuration get(long id) {
        DConfiguration data = new DConfiguration(connection);
        Configuration obj = data.search(id);
        return obj;
    }

    public boolean save(Configuration entity) {
        DConfiguration data = new DConfiguration(connection);
        return data.save(entity);
    }

    public List<Configuration> list() {
        DConfiguration data = new DConfiguration(connection);
        return data.list();
    }

}
