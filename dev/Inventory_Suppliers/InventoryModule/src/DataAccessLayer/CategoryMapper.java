package DataAccessLayer;

import BusinessLayer.Result;
import DAL_Connector.DatabaseManager;
import DTO.ProductDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class CategoryMapper {


    private Connection conn = DatabaseManager.getInstance().getConnection();

    private static CategoryMapper instance;

    public static CategoryMapper getInstance(){
        if(instance == null)
            instance = new CategoryMapper();
        return instance;
    }


    /**
     * Get a List of product's categories
     * @param id is the product id
     * @return List of categories belong to a specific product
     */
    public List<String> getCategories(int id){
        List<String> categories = new LinkedList<>();
        String sql = "SELECT * FROM Categories WHERE id = ?";

        try {

            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setInt(1, id);

            ResultSet rs = categoryStatement.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
               categories.add(rs.getString("category"));

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return categories;
    }



    /**
     * This function inserts a new category to the categories relation.
     * @param category is one of the product categories.
     * @param id is the product identifier.
     * @return true if the insertion succeeded, false otherwise.
     */
    public boolean insertCategory(String category, int id){
        int numRowsInserted;
        String insertCategory = "INSERT INTO Categories(category, id)" +
                "VALUES(?,?)";

        try {
            PreparedStatement categoryStatement = conn.prepareStatement(insertCategory);
            categoryStatement.setString(1, category);
            categoryStatement.setInt(2, id);
            numRowsInserted = categoryStatement.executeUpdate();

        }catch (java.sql.SQLException e){
            return false;
        }

        return numRowsInserted == 1;
    }



    /**
     * Delete a given product categories from DB
     * @param id is the product id
     * @return  a Result object with information about the result of the operation
     */
    public Result deleteCategories(int id){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Categories WHERE id= ?";

        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, id);

            numRowsDeleted = statement.executeUpdate();

            if(numRowsDeleted >0)
                result.successful();
            else
                result.failure("Failed to delete category");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }

}
