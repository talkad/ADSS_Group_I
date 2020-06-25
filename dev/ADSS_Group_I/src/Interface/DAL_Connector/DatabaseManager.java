package Interface.DAL_Connector;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection conn;
    private static DatabaseManager connector =null;

    public DatabaseManager(){
        connect();
    }


    /**
     * singleton class- returns the only one instance of Connector
     * @return instance of connector
     */
    public static DatabaseManager getInstance(){
        if(connector == null)
            connector = new DatabaseManager();

        return connector;
    }

    /**
     * Connects to InventoryDB
     */
    public void connect() {
        try {
            File file = new File(".");
            // db parameters
            //String url = "jdbc:sqlite::resource:Storage.db";
            // create a connection to the database
            //System.out.println(System.getProperty("user.dir")+"/Storage.db");
            //conn = DriverManager.getConnection(url);
            conn = DriverManager.getConnection("jdbc:sqlite:src/Storage.db");
            //+System.getProperty("user.dir")+
        } catch (SQLException  e) {
            System.err.println(e.getMessage());
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
