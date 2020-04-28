package DataAccessLayer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private Connection conn;
    public static Connector connector =null;

    public Connector(){
        connect();
    }

    /**
     * singleton class- returns the only one instance of Connector
     * @return instance of connector
     */
    public static Connector getInstance(){
        if(connector == null)
            connector = new Connector();

        return connector;
    }

    /**
     * Connects to InventoryDB
     */
    private void connect() {
        try {
            File dbFile=new File(".");
            // db parameters
            String url = "jdbc:sqlite:"+dbFile.getAbsolutePath()+"\\..\\..\\..\\InventoryDB.db"; // relative path of db
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

    public Connection getConnection() {
        return conn;
    }
}
