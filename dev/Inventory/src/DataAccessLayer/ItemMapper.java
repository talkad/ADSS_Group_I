package DataAccessLayer;

import BusinessLayer.Item;
import BusinessLayer.Result;
import DTO.ItemDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Map;


public class ItemMapper {

    private Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of items. The identifier is the id
    private Map<Integer,Item> identityItemMap;

    /**
     * Insert a given item to DB
     * @param itemDTO is the item to be stored in DB
     * @param productName is the product name the item belongs to
     * @param productManufacturer is the product manufacturer name
     * @return  a Result object with information about the result of the operation
     */
    public Result insert(ItemDTO itemDTO, String productName, String productManufacturer){
        Result result = new Result();
        boolean isSucceed;
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
            statement.addBatch(); // Adds a set of parameters to this PreparedStatement object's batch of commands
            statement.executeBatch();

            conn.commit();
            isSucceed = statement.execute();
            if(isSucceed)
                result.successful();
            else
                result.failure("Failed to create a statement");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /*  irrelevant functions for this class

    public Result update(ItemDTO itemDTO, String productName, String productManufacturer){
        Result result = new Result();

        return result;
    }



    public Result delete(ItemDTO itemDTO, String productName, String productManufacturer){
        Result result = new Result();

        return result;
    }*/


    public static void main(String[] args){
        System.out.println(DatabaseManager.getInstance()==null);
    }
}
