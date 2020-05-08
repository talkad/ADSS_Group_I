package DataAccessLayer;

import BusinessLayer.Result;
import DTO.ItemDTO;

import java.sql.*;
import java.util.Map;


public class ItemMapper {

    private Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of items. The identifier is the name and manufacturer of product
    private Map<Integer,ItemDTO> identityItemMap;

    private static ItemMapper instance;


    public static ItemMapper getInstance(){
        if(instance == null)
            instance = new ItemMapper();
        return instance;
    }


    /**
     * Add to identity map specific items which belong to product
     */
    public void initItemMap(String productName, String productManufacturer){
        String sql = "SELECT * FROM Item productName = "+productName+" AND productManufacturer = "+productManufacturer;
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                ItemDTO dto = new ItemDTO(rs.getInt("orderId"), rs.getInt("count"), rs.getInt("numOfDefects"),
                        rs.getDate("expiryDate"), rs.getString("location"));

                identityItemMap.put(rs.getInt("orderId"), dto);

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Insert a given item to DB
     * @param itemDTO is the item to be stored in DB
     * @param productName is the product name the item belongs to
     * @param productManufacturer is the product manufacturer name
     * @return  a Result object with information about the result of the operation
     */
    public Result insert(ItemDTO itemDTO, String productName, String productManufacturer){
        Result result = new Result();
        int numRowsInserted;
        String insertCommand = "INSERT INTO Item(orderId, count, numOfDefects, expiryDate, location, productName, productManufacturer)" +
                                "VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, itemDTO.getOrderId());
            statement.setInt(2, itemDTO.getCount());
            statement.setInt(3, itemDTO.getNumOfDefects());
            statement.setDate(4, new Date(itemDTO.getExpiryDate().getTime()));
            statement.setString(5, itemDTO.getLocation());
            statement.setString(6, productName);
            statement.setString(7, productManufacturer);
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
     * @param itemDTO is the item to be updated
     * @return  a Result object with information about the result of the operation
     */
    public Result update(ItemDTO itemDTO){
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Item SET count = ?, numOfDefects = ?, expiryDate = ?, location = ?"+
                "WHERE orderId = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setInt(1, itemDTO.getCount());
            statement.setInt(2, itemDTO.getNumOfDefects());
            statement.setDate(3, new Date(itemDTO.getExpiryDate().getTime()));
            statement.setString(4, itemDTO.getLocation());
            statement.setInt(5, itemDTO.getOrderId());


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
     * @param itemDTO is the item to be deleted
     * @return  a Result object with information about the result of the operation
     */
    public Result delete(ItemDTO itemDTO){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Item WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, itemDTO.getOrderId());

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

}
