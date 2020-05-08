package DataAccessLayer;

import BusinessLayer.Pair;
import BusinessLayer.Result;
import DTO.ItemDTO;

import java.sql.*;
import java.util.Map;


public class ItemMapper {

    private Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of items. The identifier is the name and manufacturer of product
    private Map<Pair<String, String>,ItemDTO> identityItemMap;

    private static ItemMapper instance;


    public static ItemMapper getInstance(){
        if(instance == null)
            instance = new ItemMapper();
        return instance;
    }

    /**
     * check the quantity of items which belongs to a specific product
     * @param productName is the product name
     * @param productManufacturer is the manufacturer of the product
     * @return the quantity of items that belong to the given product details
     */
    public int countItems(String productName, String productManufacturer){
        int counter = 0;

        for(Pair pair : identityItemMap.keySet()){
            if(pair.getFirst().equals(productName) &&  pair.getSecond().equals(productManufacturer))
                counter++;
        }

        return counter;
    }


    /**
     * initiate the item list with the data in DB when the system starts
     */
    public void initItemMap(){
        String sql = "SELECT * FROM Item";
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                ItemDTO dto = new ItemDTO(rs.getInt("id"), rs.getBoolean("isDefect"),
                        rs.getDate("expiryDate"), rs.getString("location"));

                identityItemMap.put(new Pair<>(rs.getString("productName"), rs.getString("productManufacturer")), dto);

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
        String insertCommand = "INSERT INTO Item(id, isDefect, expiryDate, location, productName, productManufacturer)" +
                                "VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, itemDTO.getId());
            statement.setBoolean(2, itemDTO.isDefect());
            statement.setDate(3, new Date(itemDTO.getExpiryDate().getTime()));
            statement.setString(4, itemDTO.getLocation());
            statement.setString(5, productName);
            statement.setString(6, productManufacturer);
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
        String updateCommand = "UPDATE Item SET isDefect = ?, expiryDate = ?, location = ?,"+
                "WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setBoolean(1, itemDTO.isDefect());
            statement.setDate(2, new Date(itemDTO.getExpiryDate().getTime()));
            statement.setString(3, itemDTO.getLocation());
            statement.setInt(4, itemDTO.getId());


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
            statement.setInt(1, itemDTO.getId());

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
