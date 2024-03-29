package InventoryModule.Business;

import Interface.Bussiness_Connector.Connector;
import InventoryModule.DTO.ItemDTO;
import InventoryModule.DTO.ProductDTO;
import InventoryModule.DataAccessLayer.ItemMapper;
import InventoryModule.DataAccessLayer.ProductMapper;
import InventoryModule.PresentationLayer.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.portable.ApplicationException;
/**
 * a singleton class
 */
public class Inventory {

    private ProductMapper productMapper = ProductMapper.getInstance();
    private ItemMapper itemMapper = ItemMapper.getInstance();
    private Controller controller = Controller.getInstance();

    public static Inventory instance = null;

    private Inventory(){
    }

    public ProductMapper getProductMapper() {
        return productMapper;
    }

    /**
     * @return an instance of Inventory
     */
    public static Inventory getInstance(){
        if(instance == null){
            instance = new Inventory();
        }
        return instance;
    }

    /**
     * adds a product to the inventory
     * @param productDTO the product's information to be added
     * @return a Result object with information about the result of the operation
     */
    public Result addProduct(ProductDTO productDTO){
        Result result = new Result();
        Product newProduct = new Product(productDTO);

        int getNewId = productMapper.getID();

        if(productMapper.getProduct(newProduct.getId(), productDTO.getStoreID()) == null) { // checking if there's already a product with the same id
            if (!productMapper.doesProductExist(newProduct.getName(), newProduct.getManufacturer(), newProduct.getStoreID())) {//checks if the product already exists
                newProduct.setId(getNewId);
                productMapper.addMapper(newProduct);
                //productsList.add(newProduct);
                result.successful();
            } else {
                result.failure("Product already exists in the store's inventory");
            }
        }
        else{
            result.failure("There's already a product with this id in the selected store. Id must be unique");
        }

        return result;
    }

    /**
     * removes a product from the inventory
     * @param productID the id of the product to remove
     * @return a Result object with information about the result of the operation
     */
    public Result removeProduct(int productID, int storeID){
        Result result = new Result();

        Product toRemove = productMapper.getProduct(productID, storeID);


        if(toRemove != null){ //if toRemove is null then the product does not exist in the inventory
            for(Item item: toRemove.getItems()){
                itemMapper.deleteMapper(productID, item.getOrderID());
            }

            productMapper.deleteMapper(toRemove.getId());
            //productsList.remove(toRemove);

            result.successful();
        }
        else{
            result.failure("The product you are trying to remove does not exist in the selected store");
        }

        return result;
    }

//    /**
//     * return the product represented by the given name and manufacturer
//     * @param name the name of the product
//     * @param manufacturer the name of the manufacturer of the product
//     * @return the product object represented by the {@code name} and {@code manufacturer}
//     */
//    public Product getProduct(String name, String manufacturer){
//        for(Product product : productsList){
//            if(product.isRepresentedProduct(name, manufacturer)){
//                return product;
//            }
//        }
//
//        return null;
//    }


    /**
     * adds an item from type product to the inventory
     * @param productID the id of the product to add to
     * @param itemDTO the item to add
     * @return a Result object with information about the result of the operation
     */
    public Result addItem(int productID, ItemDTO itemDTO, int storeID){
        Result result = new Result();

        Product productToAddTo = productMapper.getProduct(productID, storeID);

        if(productToAddTo != null){// if productToAddTo is null then there is no such product in the inventory
            Item itemToAdd = new Item(itemDTO);

            productToAddTo.addItem(itemToAdd);


            itemMapper.addMapper(itemToAdd, productID);
            productMapper.updateMapper(productToAddTo);

            result.successful();
        }
        else{
            result.failure("The item you are trying to add does not have a corresponding product in the selected store's inventory.");
        }

        return result;
    }

    /**
     * removes one item from a product from the inventory
     * @param productID the id of the product to remove from
     * @param itemID the order ID of the item
     * @return a Result object with information about the result of the operation
     * @throws ApplicationException 
     * @throws ParseException 
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public Result removeOneItem(int productID, int itemID, int storeID) throws NumberFormatException, IOException, ParseException, ApplicationException{
        Result result = new Result();

        Product productToRemoveFrom = productMapper.getProduct(productID, storeID);

        if(productToRemoveFrom != null){
            Item itemToRemove = productToRemoveFrom.getItem(itemID);

            if(itemToRemove != null){// if itemToRemove is null the item does not exist
                if(!productToRemoveFrom.removeItem(itemToRemove)) {// checking if the product was completely removed from the prodduct
                    itemMapper.updateMapper(itemToRemove, productID);
                }
                else{
                    itemMapper.deleteMapper(productID, itemToRemove.getOrderID());
                }

                productMapper.updateMapper(productToRemoveFrom);

                String quantityMessage = minQuantityNotification(productToRemoveFrom, storeID);

                result.successful(quantityMessage);
            }
            else{
                result.failure("The item you are trying to remove does not exist");
            }
        }
        else{
            result.failure("The item you are trying to remove does not have a corresponding product in the inventory.");
        }

        return result;
    }

    public double getProductsWeight(int productID, int storeID){
        Product product = productMapper.getProduct(productID, storeID);

        if(product != null){ // checking if the asked product exists
            return product.getWeight();
        }

        return -1; // error the product does not exist
    }

    /**
     * given a product, checks if the minimum quantity set for the product is reached
     * @param product the product to check
     * @return a String saying the minimum quantity is reached if so or null if the quantity hasn't reached yet
     * @throws ApplicationException 
     * @throws ParseException 
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public String minQuantityNotification(Product product, int storeID) throws NumberFormatException, IOException, ParseException, ApplicationException{
        if(product.hasMinQuantityReached()){
            return sendLackOrder(product, storeID);
        }
        return null;
    }

    public String sendLackOrder(Product product, int storeID) throws NumberFormatException, IOException, ParseException, ApplicationException{
        if(Connector.getInstance().sendLackOfItemOrder(product.getId(),product.getMinCapacity()*2, storeID) != -1){

            return "Minimum quantity for product " + product.getName() + " by " + product.getManufacturer() +
                    " reached is store " + storeID + "! Sent an order to fill the need!";
        }

        return "Minimum quantity for product " + product.getName() + " by " + product.getManufacturer() + " reached!\n" +
                "Couldn't send an order to fill the lack cause there is no supplier to supply the need.";
    }

    private LocalDate getDate2WeeksFromNow(LocalDate date){
        return date.plusDays(14);
    }

    public Result setPeriodicOrder(int orderId, Map<Integer, Integer> toSet, int status, int storeID){
        Result result = new Result();
        if(Connector.getInstance().setPeriodicOrder(orderId, toSet, status, storeID)){
            result.successful();
        }
        else{
            result.failure("Couldn't change the order");
        }
        return result;
    }

    public Result setPeriodicOrderDate(int orderId, LocalDate newDate, int storeID){
        Result result = new Result();

        if(Connector.getInstance().changePeriodicOrderDate(orderId, newDate, storeID)){
            result.successful();
        }
        else{
            result.failure("Couldn't change the date of the order");
        }

        return result;
    }

    public boolean tryLoadInventory(int orderID, int storeID) throws NumberFormatException, IOException, ParseException, ApplicationException{
        Map<Integer,Integer> order;

        order = Connector.getInstance().tryLoadInventory(orderID, storeID);

        if(order != null){
            loadInventory(orderID, order, storeID);
            return true;
        }

        return false;
    }

    public void loadInventory(int orderID, Map<Integer, Integer> toLoad, int storeID) throws NumberFormatException, IOException, ParseException, ApplicationException{
        boolean confirmItem;
        for(int productId: toLoad.keySet()){
            Product product = productMapper.getProduct(productId, storeID);

            if(product != null){

                LocalDate date = LocalDate.now();

                Item toAdd = new Item(orderID, toLoad.get(productId), 0, getDate2WeeksFromNow(date), "InventoryModule");

                confirmItem = controller.confirmItem(product, toAdd);

                if(confirmItem) {
                    product.addItem(toAdd);
                    itemMapper.addMapper(toAdd, productId);
                }
                else{
                    if(product.equalOrLessThanTheMin()){ // if the manager declined the product, checking if there's less than the minimum of the product. if so, sending a lack order of the product
                        sendLackOrder(product, storeID);
                    }
                }
            }
        }
        Connector.VerifyOrder(orderID);
    }

    /**
     * updates the min quantity of a product from which the user will get notification about the quantity of the product
     * @param productID the id of the product to update
     * @param newMinQuantity the new minimum quantity to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateMinQuantity(int productID, int newMinQuantity, int storeID){
        Result result = new Result();

        Product product = productMapper.getProduct(productID, storeID);

        if(product != null){
            product.setMinCapacity(newMinQuantity);

            productMapper.updateMapper(product);


            result.successful();
        }
        else{
            result.failure("The product you are trying to update its minimum quantity does not exist");
        }

        return result;
    }

    /**
     * updates the selling price of a product
     * @param productID the id of the product to update
     * @param newSellingPrice the new selling price to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateSellingPrice(int productID, int newSellingPrice, int storeID){
        Result result = new Result();

        Product product = productMapper.getProduct(productID, storeID);

        if(product != null){
            product.setSellingPrice(newSellingPrice);

            productMapper.updateMapper(product);

            result.successful();
        }
        else{
            result.failure("The product you are trying to update its selling price does not exist");
        }

        return result;
    }

    /**
     * updates the buying price of a product
     * @param productID the id of the product to update
     * @param newBuyingPrice the new buying price to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateBuyingPrice(int productID, int newBuyingPrice, int storeID){
        Result result = new Result();

        Product product = productMapper.getProduct(productID, storeID);

        if(product != null){
            product.setBuyingPrice(newBuyingPrice);

            productMapper.updateMapper(product);

            result.successful();
        }
        else{
            result.failure("The product you are trying to update its buying price does not exist");
        }

        return result;
    }

    /**
     * sets a given item as defect
     * @param productID the id of the product to update
     * @param itemId the item's id to set as defect
     * @param numOfDefects the amount of defected items
     * @return a Result object with information about the result of the operation
     */
    public Result setDefect(int productID, int itemId, int numOfDefects, int storeID){
        Result result = new Result();

        Product productToSetDefectFrom = productMapper.getProduct(productID, storeID);

        if(productToSetDefectFrom != null) { // if productToSetDefectFrom is null then it means he does not exist

            Item itemToSetDefectTo = productToSetDefectFrom.getItem(itemId);

            if (itemToSetDefectTo != null) {// if itemToSetAsDefect is null the item does not exist

                if(numOfDefects < itemToSetDefectTo.getCount()) {
                    itemToSetDefectTo.setNumOfDefects(numOfDefects);

                    itemMapper.updateMapper(itemToSetDefectTo, productID);

                    result.successful();
                }
                else{
                    result.failure("The amount of defected items cannot exceed the count of item in the batch");
                }
            } else {
                result.failure("The item you are trying to update its status, does not exist");
            }
        }
        else{
            result.failure("You are trying to set an item as defect in a product which does not exist");
        }

        return result;
    }

    /**
     * sets a new location to a given item
     * @param productID the id of the product to update
     * @param itemId the item's id to set a new location to
     * @param location the new location to set
     * @return a Result object with information about the result of the operation
     */
    public Result updateItemLocation(int productID, int itemId, String location, int storeID){
        Result result = new Result();

        Product productToSetLocationFrom = productMapper.getProduct(productID, storeID);

        if(productToSetLocationFrom != null) {// if productToSetLocationFrom is null then it means he does not exist
            Item itemToSetNewLocationTo = productToSetLocationFrom.getItem(itemId);

            if (itemToSetNewLocationTo != null) {// if itemToSetNewLocationTo is null the item does not exist

                if(!itemToSetNewLocationTo.getLocation().equals(location)) { //checking if the item is already where we're trying to change its location to
                    if(itemToSetNewLocationTo.getLocation().equals("Store")){ // checks where the item was so to update the product's quantities accordingly
                        productToSetLocationFrom.setStoreCapacity(productToSetLocationFrom.getStoreCapacity() -
                                itemToSetNewLocationTo.getCount()); // removing from the store
                        productToSetLocationFrom.setInventoryCapacity(productToSetLocationFrom.getInventoryCapacity() +
                                itemToSetNewLocationTo.getCount()); // and moving the item to the inventory
                    }
                    else{ // else the item is in the store's inventory
                        productToSetLocationFrom.setInventoryCapacity(productToSetLocationFrom.getInventoryCapacity() -
                                itemToSetNewLocationTo.getCount()); // removing from the inventory
                        productToSetLocationFrom.setStoreCapacity(productToSetLocationFrom.getStoreCapacity() +
                                itemToSetNewLocationTo.getCount()); // and moving the item to the store
                    }

                    itemToSetNewLocationTo.setLocation(location);

                    productMapper.updateMapper(productToSetLocationFrom);

                    itemMapper.updateMapper(itemToSetNewLocationTo, productID);

                    result.successful();
                }
                else{
                    result.failure("The item is already in the " + location);
                }
            } else {
                result.failure("The item you are trying to set a new location to does not exist");
            }
        }
        else{
            result.failure("You are trying to set an item a new location in a product which does not exist");
        }

        return result;
    }

    /**
     * creates an inventory report according to given catagories
     * @param categories the catagories to include in the report
     * @return an inventory report according to the given {@code catagories}
     */
    public String getCategoriesReport(List<List<String>> categories, int storeID){
        //getting all the products from the db
        List<Product> productsList = productMapper.getAll(storeID);


        String categoriesReport = "Categories Report:\n";

        boolean isIncluded = false; // a flag if the current product is to be included in the report
        boolean isInAllCategories = true; // a flag if the current product is in all of the subcategories asked for
        for(Product product: productsList){
            for(List<String> category: categories){
                for(String categoryName: category){
                    isInAllCategories = isInAllCategories && product.isInCategory(categoryName); // needs to be in all of the subcategories
                }
                isIncluded = isIncluded || isInAllCategories; // has to be in only one of the "main" categories asked for
                isInAllCategories = true;
            }


            if(isIncluded){
                categoriesReport += product.toString() + "\n";
            }

            isIncluded = false;
        }

        if(categoriesReport.compareTo("Categories Report:\n") == 0){
            categoriesReport = "There are no products in the requested categories";
        }
        return categoriesReport;
    }

    /**
     *
     * @return a report listing all the defected and expired items in the inventory
     */
    public String getDefectsReports(int storeID){
        //getting all the products from the db
        List<Product> productsList = productMapper.getAll(storeID);

        String defectsReport = "Defects or Expired Report:\n\n";

        for(Product product: productsList){
            defectsReport += product.productDefects() + "\n";
        }


        if(defectsReport.compareTo("Defects or Expired Report:\n\n") == 0){
            defectsReport = "There are no defect or expired items in the inventory";
        }
        return defectsReport;
    }
    
    
    public static String getItemName(int itemid)
    {
    	Product p = ProductMapper.getInstance().getProduct(itemid);
    	return p.getName() + " " + p.getManufacturer();
    }
}