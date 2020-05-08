package DataAccessLayer;

import BusinessLayer.Pair;
import BusinessLayer.Result;
import DTO.ProductDTO;

import java.sql.*;
import java.util.Map;

public class ProductMapper {

    private static Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of Product. The identifier are its name and manufacturer
    private Map<Pair<String,String>,ProductDTO> identityProductMap;

    /**
     * initiate the product list with the data in DB when the system starts
     */
    public void initProductMap(){
        String sql = "SELECT * FROM Product";
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                ProductDTO dto = new ProductDTO(rs.getString("name"), rs.getString("manufacturer"),
                        rs.getInt("minCapacity"), rs.getInt("buyingPrice"), rs.getInt("sellingPrice"),
                        rs.getInt("inventoryCapacity"), rs.getInt("storeCapacity"),
                        CategoryMapper.getInstance().getCategories(rs.getString("name"), rs.getString("manufacturer")),
                        null);

                identityProductMap.put(new Pair(dto.getName(), dto.getManufacturer()), dto);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    /**
     * insert a given product to DB
     * @param productDTO is the product to be stored in DB
     * @return  a Result object with information about the result of the operation
     */
    public static Result insert(ProductDTO productDTO){
        Result result = new Result();
        int numRowsInserted;

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
            numRowsInserted = statement.executeUpdate();

            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

            for(String category: productDTO.getCategories()){
                if(! CategoryMapper.getInstance().insertCategory(category, productDTO.getName(), productDTO.getManufacturer()))
                    break;
            }

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /**
     * Update all fields of a given product in DB
     * @param productDTO is the product to be updated
     * @return  a Result object with information about the result of the operation
     */
    public Result update(ProductDTO productDTO){
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Product SET minCapacity = ?, buyingPrice = ?, sellingPrice = ?, inventoryCapacity = ?,"+
                "storeCapacity = ? WHERE name = ? AND manufacturer = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setInt(1, productDTO.getMinCapacity());
            statement.setDouble(2, productDTO.getBuyingPrice());
            statement.setDouble(3, productDTO.getSellingPrice());
            statement.setInt(4, productDTO.getInventoryCapacity());
            statement.setInt(5, productDTO.getStoreCapacity());
            statement.setString(6, productDTO.getName());
            statement.setString(7, productDTO.getManufacturer());

            numRowsUpdated = statement.executeUpdate();

            if(numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }




    /**
     * Delete a given product from DB
     * @param productDTO is the product to be deleted
     * @return  a Result object with information about the result of the operation
     */
    public Result delete(ProductDTO productDTO){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Product WHERE name = ? AND manufacturer = ?";

        // delete the product only if there are no items belong to this product
        if(ItemMapper.getInstance().countItems(productDTO.getName(), productDTO.getManufacturer()) == 0) {
            try {
                PreparedStatement statement = conn.prepareStatement(deleteCommand);
                statement.setString(1, productDTO.getName());
                statement.setString(2, productDTO.getManufacturer());

                numRowsDeleted = statement.executeUpdate();

                if (numRowsDeleted > 0)
                    result.successful();
                else
                    result.failure("Failed to delete Product");

            } catch (java.sql.SQLException e) {
                result.failure("Failed to create a statement");
            }
        }
        else
            result.failure("Deletion failed- There are items belong to the given product");

        return result;
    }

}
