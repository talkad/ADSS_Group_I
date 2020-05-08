package DataAccessLayer;

import BusinessLayer.Result;
import DTO.ProductDTO;

import java.sql.*;
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
     * @param productName is the product name
     * @param productManufacturer is the product manufacturer
     * @return List of categories belong to a specific product
     */
    public List<String> getCategories(String productName, String productManufacturer){
        List<String> categories = new LinkedList<>();
        String sql = "SELECT * FROM Categories WHERE productName = ? AND productManufacturer = ? ";

        try {

            PreparedStatement categoryStatement = conn.prepareStatement(sql);
            categoryStatement.setString(1, productName);
            categoryStatement.setString(2, productManufacturer);

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
     * @param category is one of the product categories
     * @param productName is the product name
     * @param productManufacturer is the product manufacturer
     * @return true if the insertion succeeded, false otherwise.
     */
    public boolean insertCategory(String category, String productName, String productManufacturer){
        int numRowsInserted;
        String insertCategory = "INSERT INTO Categories(category, productName, productManufacturer)" +
                "VALUES(?,?,?)";

        try {
            PreparedStatement categoryStatement = conn.prepareStatement(insertCategory);
            categoryStatement.setString(1, category);
            categoryStatement.setString(2, productName);
            categoryStatement.setString(3, productManufacturer);
            numRowsInserted = categoryStatement.executeUpdate();

        }catch (java.sql.SQLException e){
            return false;
        }

        return numRowsInserted == 1;
    }


    /**
     * Delete a given product categories from DB
     * @param productDTO is the product that its categories will be deleted
     * @return  a Result object with information about the result of the operation
     */
    public Result deleteCategories(ProductDTO productDTO){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Categories WHERE name = ? AND manufacturer = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setString(1, productDTO.getName());
            statement.setString(2, productDTO.getManufacturer());

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
