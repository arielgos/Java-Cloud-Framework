package com.agos.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by arielgos on 12/30/16.
 */
public class Log {

    private static Logger log = Logger.getLogger("Source");

    static {

        InputStream objStream = null;
        try {
            objStream = Log.class.getClassLoader().getResourceAsStream("main.properties");
            if (objStream != null) {
                Properties properties = new Properties();
                properties.load(objStream);
                PropertyConfigurator.configure(properties);
                objStream.close();
                objStream = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (objStream != null) {
                try {
                    objStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void d(String message) {
        log.debug(message);
    }

    public static void i(String message) {
        log.info(message);
    }

    public static void e(String message) {
        log.error(message);
    }

    public static void w(String message, Throwable ex) {
        log.warn(message, ex);
    }


    public static void e(String message, Throwable ex) {
        log.error(message, ex);
    }

}
