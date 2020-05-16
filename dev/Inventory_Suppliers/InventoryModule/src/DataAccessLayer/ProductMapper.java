package DataAccessLayer;

import BusinessLayer.Item;
import BusinessLayer.Pair;
import BusinessLayer.Product;
import BusinessLayer.Result;
import DAL_Connector.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.util.Date;


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
                Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getString("manufacturer"),
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
     * given a productID, returns a productDTO with id {@param productID}
     * @param productID the id of the product
     * @return productDTO with id {@param productID}. or null if no such entry exists
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
    public boolean doesProductExist(String name, String manufacturer){

        if(identityProductMap.size() == 0)
            initProductMap();

        for(Product product: identityProductMap.values()){
            if(product.getName().equals(name) && product.getManufacturer().equals(manufacturer))
                return true;
        }

        return false;
    }



    /**
     *
     * @return all of the records in a List
     */
    public List<Product> getAll(){

        if(identityProductMap.size() == 0)
            initProductMap();

        List<Product> list = new LinkedList<>();

        Iterator<Map.Entry<Integer, Product>> it = identityProductMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Product> e = it.next();
            int id = e.getKey();
            list.add(getProduct(id));
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

  /*  public static void main(String[] args){

        List<String> categories = new LinkedList<>();
        categories.add("milky");
        categories.add("salty");

         Item milk1= new Item(1,20, 5, new Date(),"Inventory");
        Item milk2= new Item(2,30, 5, new Date(),"Inventory");
        Item milk3= new Item(3,50, 5, new Date(),"Inventory");
        Item milk4= new Item(4,80, 5, new Date(),"Inventory");
        Item milk5= new Item(5,90, 5, new Date(),"Inventory");
        Item milk6= new Item(6,5555, 5, new Date(),"Inventory");

        List<Item> items = new LinkedList<>();

        items.add(milk1);
        items.add(milk2);
        items.add(milk3);
        items.add(milk4);
        items.add(milk5);
        items.add(milk6);

        Product product = new Product(128, "milk", "tara", 10, 300,
                200, 2.5, 10, 15, categories, items);

        Result result = ProductMapper.getInstance().addMapper(product);

        System.out.println(result.getErrorMsg());
    }*/
}
