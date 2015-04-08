package com.data;

/**
 * Created by karthik on 12/30/14.
 */

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DatabaseHelper {

    public JDBCConnectionPool connectionPool = null;

    public DatabaseHelper() {
        initConnectionPool();
    }

    private void initConnectionPool() {

        try {
            connectionPool = new SimpleJDBCConnectionPool(
                    "com.mysql.jdbc.Driver",
                    "jdbc:mysql://localhost:3306/imageintegration", "root", "");

          //  System.out.println("connected :)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
