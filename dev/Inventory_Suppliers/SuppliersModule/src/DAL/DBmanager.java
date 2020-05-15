package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBmanager {

    private Connection conn;
    private static DBmanager connector =null;

    public DBmanager(){
        connect();
    }


    /**
     * singleton class- returns the only one instance of Connector
     * @return instance of connector
     */
    public static DBmanager getInstance(){
        if(connector == null)
            connector = new DBmanager();

        return connector;
    }

    /**
     * Connects to InventoryDB
     */
    private void connect() {

        try {
            // db parameters
            String url = "jdbc:sqlite:Storage";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * This function close an existing open connection to Storage database
     */
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    public Connection getConnection() {
        return conn;
    }

}
