package com.agos.model.business.security;

import com.agos.model.business.Business;
import com.agos.model.data.security.DProfile;
import com.agos.model.entities.security.Profile;

/**
 * Created by arielgos on 12/31/16.
 */
public class BProfile extends Business {

    public BProfile() throws Exception {
        super();
    }

    public Profile get(long id) {
        DProfile data = new DProfile(connection);
        Profile obj = data.search(id);
        return obj;
    }

    public boolean save(Profile entity) {
        DProfile data = new DProfile(connection);
        return data.save(entity);
    }

}
