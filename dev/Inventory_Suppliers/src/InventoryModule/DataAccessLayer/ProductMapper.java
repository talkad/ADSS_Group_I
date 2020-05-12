package DataAccessLayer;

import BusinessLayer.Result;
import DTO.ProductDTO;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductMapper {

    private static Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of Product. The identifier are its name and manufacturer
    private Map<Integer,ProductDTO> identityProductMap;

    private static ProductMapper instance;


    public static ProductMapper getInstance(){
        if(instance == null)
            instance = new ProductMapper();
        return instance;
    }

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
                ProductDTO dto = new ProductDTO(rs.getInt("id"), rs.getString("name"), rs.getString("manufacturer"),
                        rs.getInt("minCapacity"), rs.getInt("buyingPrice"), rs.getInt("sellingPrice"),
                        rs.getDouble("weight"), rs.getInt("inventoryCapacity"), rs.getInt("storeCapacity"),
                        CategoryMapper.getInstance().getCategories(rs.getString("name"), rs.getString("manufacturer")),
                        null);

                identityProductMap.put(dto.getId(), dto);

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
    public Result insert(ProductDTO productDTO){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO Product(id, name, manufacturer, minCapacity, buyingPrice, sellingPrice, " +
                "weight, inventoryCapacity, storeCapacity)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";


        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, productDTO.getId());
            statement.setString(2, productDTO.getName());
            statement.setString(3,productDTO.getManufacturer());
            statement.setInt(4, productDTO.getMinCapacity());
            statement.setDouble(5, productDTO.getBuyingPrice());
            statement.setDouble(6, productDTO.getSellingPrice());
            statement.setDouble(7, productDTO.getWeight());
            statement.setInt(8, productDTO.getInventoryCapacity());
            statement.setInt(9, productDTO.getStoreCapacity());
            numRowsInserted = statement.executeUpdate();

            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

            for(String category: productDTO.getCategories()){
                CategoryMapper.getInstance().insertCategory(category, productDTO.getName(), productDTO.getManufacturer());
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
        String updateCommand = "UPDATE Product SET minCapacity = ?, buyingPrice = ?, sellingPrice = ?, weight = ?" +
                "inventoryCapacity = ?, storeCapacity = ? WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setInt(1, productDTO.getMinCapacity());
            statement.setDouble(2, productDTO.getBuyingPrice());
            statement.setDouble(3, productDTO.getSellingPrice());
            statement.setDouble(4, productDTO.getWeight());
            statement.setInt(5, productDTO.getInventoryCapacity());
            statement.setInt(6, productDTO.getStoreCapacity());
            statement.setInt(7, productDTO.getId());

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
        String deleteCommand = "DELETE FROM Product WHERE id = ?";

        // delete the product only if there are no items belong to this product
        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, productDTO.getId());

            numRowsDeleted = statement.executeUpdate();

            if (numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to delete Product");

        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }


        return result;
    }

    //TODO: a function which returns a product. call it getProduct and it gets an int which is the product id. getProduct(int productID)
    //TODO: return it with the list of itemDTOs which are related to it
    /**
     * given a productID, returns a productDTO with id {@code productID}
     * @param productID the id of the product
     * @return productDTO with id {@code productID}. or null if no such entry exists
     */
    public ProductDTO getProduct(int productID){
        return null;
    }

    /**
     * checks whether there's already a product with the same name and manufacturer name
     * @param name the product's name
     * @param manufacturer the manufacturer's name
     * @return whether there's already a product with the same name and manufacturer name
     */
    public boolean doesProductExist(String name, String manufacturer){
        return true;
    }

    /**
     *
     * @return all of the records in a List
     */
    public List<ProductDTO> getAll(){
        return null;
    }



    public static void main(String[] args) {
        List<String> milkCategories= new LinkedList<>(Arrays.asList("milky","500ml"));
        ProductDTO milk= new ProductDTO(5,"milk","Tnuva",3, 10,15,
                20,0,0,milkCategories,new LinkedList<>());

        ProductMapper.getInstance().insert(milk);
    }

}
