package EmployeeModule.DataAccessLayer;

import java.sql.*;

public class mainData {
    private static mainData instance;

    public static EmployeeModule.DataAccessLayer.mainData getInstance(){
        if(instance == null) {
            instance = new mainData();
        }
        return instance;
    }

    protected Connection connect() {
        Connection conn = null;
        try {
        // db parameters
        String url = "jdbc:sqlite:Employees.db";
        // create a connection to the database
        conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
                System.err.println(e.getMessage());
            } /*finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                }catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }*/
        return conn;
    }
}
