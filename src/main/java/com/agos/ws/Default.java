package com.agos.ws;


import com.agos.model.business.base.BConfiguration;
import com.agos.model.entities.base.Configuration;
import com.agos.util.Log;
import com.agos.ws.annotations.Method;
import com.agos.ws.annotations.Path;
import com.sun.org.glassfish.gmbal.ManagedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newuser on 12/31/16.
 */
@Path
public class Default extends WebService {

    @Method
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

    @Method
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
