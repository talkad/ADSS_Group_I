package DataAccessLayer;

import BusinessLayer.Result;
import DTO.ProductDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProductMapper {

    private static Connection conn = DatabaseManager.getInstance().getConnection();

    /**
     * This function inserts a new category to the categories relation.
     * @param category is one of the product categories
     * @param name is the product name
     * @param productManufacturer is the product manufacturer
     * @return true if the insertion succeeded, false otherwise.
     */
    private static boolean insertCategory(String category, String name, String productManufacturer){
        boolean isSucceed;
        String insertCategory = "INSERT INTO Categories(category, productName, productManufacturer)" +
                "VALUES(?,?,?)";

        try {
            PreparedStatement categoryStatement = conn.prepareStatement(insertCategory);
            categoryStatement.setString(1, category);
            categoryStatement.setString(2, name);
            categoryStatement.setString(3, productManufacturer);
            categoryStatement.addBatch(); // Adds a set of parameters to this PreparedStatement object's batch of commands

            categoryStatement.executeBatch();
            conn.commit();
            isSucceed = categoryStatement.execute();

            }catch (java.sql.SQLException e){
                return false;
         }

        return isSucceed;
    }


    /**
     * insert a given product to DB
     * @param productDTO is the product to be stored in DB
     * @return  a Result object with information about the result of the operation
     */
    public static Result insert(ProductDTO productDTO){
        Result result = new Result();
        String insertCommand = "INSERT INTO Product(name, manufacturer, minCapacity, buyingPrice, sellingPrice, " +
                "inventoryCapacity, storeCapacity)" +
                "VALUES(?,?,?,?,?,?,?)";


        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setString(1, productDTO.getName());
            statement.setString(2,productDTO.getManufacturer());
            statement.setInt(3, productDTO.getMinCapacity());
            statement.setDouble(4, productDTO.getBuyingPrice());
            statement.setDouble(5, productDTO.getSellingPrice());
            statement.setInt(6, productDTO.getInventoryCapacity());
            statement.setInt(7, productDTO.getStoreCapacity());
            statement.addBatch(); // Adds a set of parameters to this PreparedStatement object's batch of commands

            statement.executeBatch();
            conn.commit();

            for(String category: productDTO.getCategories()){
                if(! insertCategory(category, productDTO.getName(), productDTO.getManufacturer()))
                    break;
            }

            result.successful();

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

        List<String> milkCategories= new LinkedList<>(Arrays.asList("milky","500ml"));
        ProductDTO milk= new ProductDTO("milk","Tnuva",3, 10,15,
                0,0,milkCategories,new LinkedList<>());

        Result result = insert(milk);
        System.out.println(result.getErrorMsg()); // still not working

    }

}
