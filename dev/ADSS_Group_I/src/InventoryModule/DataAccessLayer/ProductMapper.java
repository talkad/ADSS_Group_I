package InventoryModule.DataAccessLayer;


import Interface.DAL_Connector.DatabaseManager;
import InventoryModule.Business.Item;
import InventoryModule.Business.Product;
import InventoryModule.Business.Result;

import java.sql.*;
import java.util.*;


public class ProductMapper {

    private Connection conn = DatabaseManager.getInstance().getConnection();

    //this map manages all the instances of Product. The identifier is the product id
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

    // ------ insert - update - delete ----------

    /**
     * initiate the product list with the data in DB when the system starts
     * doesn't import items into the system
     */
    private void initProductMap(){
        String sql = "SELECT * FROM Product";
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                Product product = new Product(rs.getInt("id"),rs.getInt("storeID"), rs.getString("name"), rs.getString("manufacturer"),
                        rs.getInt("minCapacity"), rs.getInt("buyingPrice"), rs.getInt("sellingPrice"),
                        rs.getDouble("weight"), rs.getInt("inventoryCapacity"), rs.getInt("storeCapacity"),
                        CategoryMapper.getInstance().getCategories(rs.getInt("id")),
                        null);

                identityProductMap.put(product.getId(), product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " is it here 1");
        }
    }



    /**
     * insert a given product to DB
     * @param product is the product to be stored in DB
     * @return  a Result object with information about the result of the operation
     */
    private Result insert(Product product){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO Product(id, name, manufacturer, minCapacity, buyingPrice, sellingPrice, " +
                "weight, inventoryCapacity, storeCapacity, storeID)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";

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
            statement.setInt(10, product.getStoreID());
            numRowsInserted = statement.executeUpdate();


            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

            for(String category: product.getCategories()){ // insert categories
                CategoryMapper.getInstance().insertCategory(category, product.getId());
            }


            for (Item item : product.getItems()) { // insert items
                ItemMapper.getInstance().addMapper(item, product.getId());
            }


            result.successful();

        }catch (SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /**
     * Update all fields of a given product in DB
     * @param product is the product to be updated
     * @return  a Result object with information about the result of the operation
     */
    private Result update(Product product){
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Product SET minCapacity = ?, buyingPrice = ?, sellingPrice = ?, weight = ?," +
                "inventoryCapacity = ?, storeCapacity = ?, storeID = ? WHERE id = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setInt(1, product.getMinCapacity());
            statement.setDouble(2, product.getBuyingPrice());
            statement.setDouble(3, product.getSellingPrice());
            statement.setDouble(4, product.getWeight());
            statement.setInt(5, product.getInventoryCapacity());
            statement.setInt(6, product.getStoreCapacity());
            statement.setInt(7, product.getStoreID());
            statement.setInt(8, product.getId());

            numRowsUpdated = statement.executeUpdate();

            for(Item item: product.getItems()){ // update items
                ItemMapper.getInstance().updateMapper(item, product.getId());
            }


            if(numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

        }catch (SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }


    /**
     * Delete a given product from DB
     * @param productID is the product identifier
     * @return  a Result object with information about the result of the operation
     */
    private Result delete(int productID){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Product WHERE id = ?";

        // delete the product only if there are no items belong to this product
        try {

            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, productID);

            numRowsDeleted = statement.executeUpdate();

            CategoryMapper.getInstance().deleteCategories(productID);

            if (numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to delete Product");

        } catch (SQLException e) {
            result.failure("Failed to create a statement");
        }


        return result;
    }


    // -------- mapper functionality ---------------


    /**
     * given a productID, returns a product with id {@param productID}
     * @param productID the id of the product
     * @return product with id {@param productID}. or null if no such entry exists
     */
    public Product getProduct(int productID, int storeID){

        Product product = null;

        if(identityProductMap.size() == 0)
            initProductMap();

        if(identityProductMap.containsKey(productID))
        {
            product = identityProductMap.get(productID);
            if(product.getStoreID() != storeID) {
                return null;
            }
            else if(product.getItems() == null) {
                product.setItems(ItemMapper.getInstance().getItems(productID));
            }
        }

        return product;
    }
    
    
    /**
     * given a productID, returns a product with id {@param productID}
     * @param productID the id of the product
     * @return product with id {@param productID}. or null if no such entry exists
     */
    public Product getProduct(int productID){

        Product product = null;

        if(identityProductMap.size() == 0)
            initProductMap();

        if(identityProductMap.containsKey(productID))
        {
            product = identityProductMap.get(productID);
        	if(product.getItems() == null) {
        		product.setItems(ItemMapper.getInstance().getItems(productID));
        	}
        }

        return product;
    }

    /**
     * checks whether there's already a product with the same name and manufacturer name
     * @param name is the product name
     * @param manufacturer is the product manufacturer name
     * @return whether there's already a product with the same name and manufacturer name
     */
    public boolean doesProductExist(String name, String manufacturer, int storeID){

        if(identityProductMap.size() == 0)
            initProductMap();

        for(Product product: identityProductMap.values()){
            if(product.getName().equals(name) && product.getManufacturer().equals(manufacturer)
                    && product.getStoreID() == storeID)
                return true;
        }

        return false;
    }


    /**
     *
     * @param storeID is the store id
     * @return  all products with the given storeID
     */
    public List<Product> getAll(int storeID){

        if(identityProductMap.size() == 0)
            initProductMap();

        List<Product> list = new LinkedList<>();

        Iterator<Map.Entry<Integer, Product>> it = identityProductMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Product> e = it.next();
            int id = e.getKey();
            list.add(getProduct(id, storeID));
        }

        return list;
    }


    /**
     * Add a given product to DB
     * @param product is the product to be inserted
     * @return  a Result object with information about the result of the operation
     */
    public Result addMapper(Product product){

        Result result = insert(product);

        if(result.isSuccessful())
            identityProductMap.put(product.getId(), product);

        return result;
    }

    /**
     * Update a given product from DB
     * @param product is the product to be updated
     * @return  a Result object with information about the result of the operation
     */
    public Result updateMapper(Product product){

        Result result = update(product);

        if(result.isSuccessful()){
            identityProductMap.remove(product.getId());
            identityProductMap.put(product.getId(), product);
        }

        return result;
    }


    /**
     * Delete a given product from DB
     * @param productID is the product identifier
     * @return  a Result object with information about the result of the operation
     */
    public Result deleteMapper(int productID){

        Result result = delete(productID);

        if(result.isSuccessful()){
            identityProductMap.remove(productID);
        }

        return result;
    }

    /**
     * returns the next id
     * @return next id
     */
    public int getID(){

        if(identityProductMap.size() == 0)
            initProductMap();

        int nextID = 0;

        for(Integer id: identityProductMap.keySet()){
            if(nextID < id)
                nextID = id;
        }

        return nextID+1;
    }

}
