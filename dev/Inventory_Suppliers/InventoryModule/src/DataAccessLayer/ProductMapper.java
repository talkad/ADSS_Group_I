package DataAccessLayer;

import BusinessLayer.Item;
import BusinessLayer.Product;
import BusinessLayer.Result;
import DAL_Connector.DatabaseManager;

import java.sql.*;
import java.util.Hashtable;
import java.util.Map;

public class ProductMapper {

    private static Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of Product. The identifier are its name and manufacturer
    private Map<Integer, Product> identityProductMap;

    private static ProductMapper instance;

    public ProductMapper(){
        identityProductMap = new Hashtable<>();
    }

    public static ProductMapper getInstance(){
        if(instance == null)
            instance = new ProductMapper();
        return instance;
    }


    /**
     * initiate the product list with the data in DB when the system starts
     */
    private void initProductMap(){
        String sql = "SELECT * FROM Product";
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("manufacturer"),
                        rs.getInt("minCapacity"), rs.getInt("buyingPrice"), rs.getInt("sellingPrice"),
                        rs.getDouble("weight"), rs.getInt("inventoryCapacity"), rs.getInt("storeCapacity"),
                        CategoryMapper.getInstance().getCategories(rs.getInt("id")),
                        null);

                identityProductMap.put(product.getId(), product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    /**
     * insert a given product to DB
     * @param product is the product to be stored in DB
     * @return  a Result object with information about the result of the operation
     */
    public Result insert(Product product){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO Product(id, name, manufacturer, minCapacity, buyingPrice, sellingPrice, " +
                "weight, inventoryCapacity, storeCapacity)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3,product.getManufacturer());
            statement.setInt(4, product.getMinCapacity());
            statement.setDouble(5, product.getBuyingPrice());
            statement.setDouble(6, product.getSellingPrice());
            statement.setDouble(7, product.getWeight());
            statement.setInt(8, product.getInventoryCapacity());
            statement.setInt(9, product.getStoreCapacity());
            numRowsInserted = statement.executeUpdate();


            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

            for(String category: product.getCategories()){ // insert categories
                CategoryMapper.getInstance().insertCategory(category, product.getId());
            }

            for(Item item: product.getItems()){ // insert items
                ItemMapper.getInstance().addMapper(item, product.getId());
            }

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /**
     * Update all fields of a given product in DB
     * @param product is the product to be updated
     * @return  a Result object with information about the result of the operation
     */
    public Result update(Product product){
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Product SET minCapacity = ?, buyingPrice = ?, sellingPrice = ?, weight = ?" +
                "inventoryCapacity = ?, storeCapacity = ? WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setInt(1, product.getMinCapacity());
            statement.setDouble(2, product.getBuyingPrice());
            statement.setDouble(3, product.getSellingPrice());
            statement.setDouble(4, product.getWeight());
            statement.setInt(5, product.getInventoryCapacity());
            statement.setInt(6, product.getStoreCapacity());
            statement.setInt(7, product.getId());

            numRowsUpdated = statement.executeUpdate();

            for(Item item: product.getItems()){ // update items
                ItemMapper.getInstance().updateMapper(item, product.getId());
            }

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
     * @param product is the product to be deleted
     * @return  a Result object with information about the result of the operation
     */
    public Result delete(Product product){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Product WHERE id = ?";

        // delete the product only if there are no items belong to this product
        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, product.getId());

            for(Item item: product.getItems()){ // delete items
                ItemMapper.getInstance().deleteMapper(product.getId(), item.getOrderID());
            }

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
     * given a productID, returns a productDTO with id {@param productID}
     * @param productID the id of the product
     * @return productDTO with id {@param productID}. or null if no such entry exists
     */
    public ProductDTO getProduct(int productID){
        // check if map empty - if so try to init
        // if id exists in map return the product with its items
        // if not return null
        return null;
    }

    /**
     * checks whether there's already a product with the same name and manufacturer name
     * @param name the product's name
     * @param manufacturer the manufacturer's name
     * @return whether there's already a product with the same name and manufacturer name
     */
    public boolean doesProductExist(String name, String manufacturer){
        // if map empty - try an init
        // check if the product exists in map
        return true;
    }

    /**
     *
     * @return all of the records in a List
     */
    public List<ProductDTO> getAll(){
        return null;
    }

}
