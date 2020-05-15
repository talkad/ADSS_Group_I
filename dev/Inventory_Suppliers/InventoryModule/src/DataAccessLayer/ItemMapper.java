package DataAccessLayer;

import BusinessLayer.Item;
import BusinessLayer.Pair;
import BusinessLayer.Result;
import DAL_Connector.DatabaseManager;

import java.sql.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemMapper {

    private Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of items. The identifier is product_id + order_id
    private Map<Pair<Integer,Integer>, Item> identityItemMap;

    private static ItemMapper instance;


    public ItemMapper(){
        identityItemMap = new Hashtable<>();
    }

    public static ItemMapper getInstance(){
        if(instance == null)
            instance = new ItemMapper();
        return instance;
    }

    // ------ insert - update - delete ----------

    /**
     * Add to identity map specific items which belong to product
     * @param productID is the id of the product
     */
    private void initItemMap(int productID){
        String sql = "SELECT * FROM Item productID = "+productID;
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                Item item = new Item(rs.getInt("orderID"), rs.getInt("count"), rs.getInt("numOfDefects"),
                        rs.getDate("expirationDate"), rs.getString("location"));

                identityItemMap.put(new Pair<>(productID, rs.getInt("orderID")), item);

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Insert a given item to DB
     * @param item is the item to be stored in DB
     * @param productID is the product identifier
     * @return  a Result object with information about the result of the operation
     */
    private Result insert(Item item, int productID){
        Result result = new Result();
        int numRowsInserted;
        String insertCommand = "INSERT INTO Item(productID, orderID, count, numOfDefects, expirationDate, location)" +
                                "VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, productID);
            statement.setInt(2, item.getOrderID());
            statement.setInt(3, item.getCount());
            statement.setInt(4, item.getNumOfDefects());
            statement.setDate(5, new Date(item.getExpiryDate().getTime()));
            statement.setString(6, item.getLocation());
            numRowsInserted = statement.executeUpdate();

            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to insert item");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /**
     * Update all fields of a given item in DB
     * @param item is the item to be updated
     * @param productID is the product identifier
     * @return  a Result object with information about the result of the operation
     */
    private Result update(Item item, int productID){
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Item SET count = ?, numOfDefects = ?, expirationDate = ?, location = ?"+
                "WHERE orderID = ? AND productID = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setInt(1, item.getCount());
            statement.setInt(2, item.getNumOfDefects());
            statement.setDate(3, new Date(item.getExpiryDate().getTime()));
            statement.setString(4, item.getLocation());
            statement.setInt(5, item.getOrderID());
            statement.setInt(6, productID);

            numRowsUpdated = statement.executeUpdate();

            if(numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Item");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /**
     * Delete a given item from DB
     * @param productID is the product identifier
     * @param orderID is the order identifier
     * @return  a Result object with information about the result of the operation
     */
    private Result delete(int productID, int orderID){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Item WHERE orderID = ? AND productID = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, orderID);
            statement.setInt(2, productID);

            numRowsDeleted = statement.executeUpdate();

            if(numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to delete Item");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    // -------- mapper functionality ---------------

    /**
     * Get items of certain product
     * @param productID is the product id
     * @return list of items that belongs to {@param productID}
     */
    public List<Item>  getItems(int productID){
        List<Item> items = new LinkedList<>();

        for(Pair<Integer,Integer> pair: identityItemMap.keySet()){
            if(pair.getFirst() == productID)
                items.add(identityItemMap.get(pair));
        }

        return items;
    }


    /**
     * Add a given item from DB
     * @param productID is the product identifier
     * @param item is the item to be inserted to db
     * @return  a Result object with information about the result of the operation
     */
    public Result addMapper(Item item, int productID){

        Result result = insert(item, productID);

        if(result.isSuccessful())
            identityItemMap.put(new Pair<>(productID, item.getOrderID()), item);

        return result;
    }

    /**
     * Update a given item from DB
     * @param productID is the product identifier
     * @param item is the item to be inserted to db
     * @return  a Result object with information about the result of the operation
     */
    public Result updateMapper(Item item, int productID){

        Result result = update(item, productID);

        if(result.isSuccessful()){
            for(Pair<Integer, Integer> p : identityItemMap.keySet()){
                if(p.getFirst() == productID && p.getSecond() == item.getOrderID()){
                    identityItemMap.remove(p);
                    identityItemMap.put(new Pair<>(p.getFirst(), p.getSecond()), item);
                }
            }
        }

        return result;
    }

    /**
     * Delete a given item from DB
     * @param productID is the product identifier
     * @param orderID is the item to be inserted to db
     * @return  a Result object with information about the result of the operation
     */
    public Result deleteMapper(int productID, int orderID){

        Result result = delete(productID, orderID);

        if(result.isSuccessful()){
            for(Pair<Integer, Integer> p : identityItemMap.keySet()){
                if(p.getFirst() == productID && p.getSecond() == orderID){
                    identityItemMap.remove(p);
                }
            }
        }

        return result;
    }

}
