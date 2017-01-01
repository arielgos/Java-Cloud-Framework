package com.agos.model.business;

import com.agos.util.Configuration;
import com.agos.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by arielgos on 12/30/16.
 */
public class Business {

    protected Connection connection;

    public Business() throws Exception {
        Configuration config = new Configuration();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            Log.e(e.getMessage(), e);
        }
        this.connection = DriverManager.getConnection("jdbc:mysql://" + config.get("db.server") + ":" + config.get("db.port") + "/" + config.get("db.name"), config.get("db.user"), config.get("db.password"));
    }

    public Business(Connection conection) throws Exception {
        this.connection = conection;
    }

    public Connection getConexion() {
        return connection;
    }

    public void setConexion(Connection connection) {
        this.connection = connection;
    }

    public void openTransaction() throws SQLException {
        this.connection.setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        this.connection.commit();
    }

    public void rollBackTransaction() throws SQLException {
        this.connection.rollback();
    }

    public void dispose() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    this.connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
