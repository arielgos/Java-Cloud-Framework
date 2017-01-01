package com.agos.ws;


import com.agos.model.business.base.BConfiguration;
import com.agos.model.entities.base.Configuration;
import com.agos.ws.annotations.HttpGet;
import com.agos.ws.annotations.Method;
import com.agos.ws.annotations.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newuser on 12/31/16.
 */
@Path(uri = "default/")
public class Default extends WebService {

    @HttpGet
    @Method(uri = "value")
    public List<Configuration> getValue() {
        BConfiguration bc = null;
        try {
            bc = new BConfiguration();
            return bc.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bc.dispose();
        }
        return new ArrayList<Configuration>();
    }

    @HttpGet
    @Method(uri = "value/{id}")
    public Configuration getValue(long id) {
        BConfiguration bc = null;
        try {
            bc = new BConfiguration();
            return bc.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bc.dispose();
        }
        return null;
    }

}
