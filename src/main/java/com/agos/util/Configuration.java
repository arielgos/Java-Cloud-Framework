package com.agos.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by arielgos on 12/30/16.
 */
public class Configuration {

    private Properties properties;

    public Configuration() {
        InputStream stream = null;
        try {
            stream = Configuration.class.getClassLoader().getResourceAsStream("main.properties");
            if (stream != null) {
                this.properties = new Properties();
                this.properties.load(stream);
                stream.close();
                stream = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String get(String key) {
        try {
            String value = this.properties.getProperty(key);
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
