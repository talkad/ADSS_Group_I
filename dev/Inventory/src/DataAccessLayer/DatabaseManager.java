package DataAccessLayer;


import java.sql.*;

public class DatabaseManager {

    private Connection conn;
    public static DatabaseManager connector =null;

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
    private void connect() {

        try {
            // db parameters
            String url = "jdbc:sqlite:StorageDB";
            Class.forName("org.sqlite.JDBC");

            // create a connection to the database
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

        } catch (SQLException | ClassNotFoundException e) {
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

    /**
     * This function execute a select query
     * @param query is an SQLite legal query
     * @return the result of the select query
     */
    public ResultSet executeQuerySelect(String query) {
        ResultSet resultSet = null;
        if (conn != null) {
            try {
                Statement sqlStatement = conn.createStatement();
                resultSet = sqlStatement.executeQuery(query);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return resultSet;
    }

    public Connection getConnection() {
        return conn;
    }


}
