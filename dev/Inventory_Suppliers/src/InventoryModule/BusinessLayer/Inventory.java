package BusinessLayer;

import DTO.ItemDTO;
import DTO.ProductDTO;
import DataAccessLayer.CategoryMapper;
import DataAccessLayer.ItemMapper;
import DataAccessLayer.ProductMapper;
//import Initialize.HardCodeInitializer; TODO: ask tal if he deleted this class

import java.util.LinkedList;
import java.util.List;

//TODO: add calls to the function of the db
//TODO: implement the periodic order
/**
 * a singleton class
 */
public class Inventory {

    //private List<Product> productsList;

    private ProductMapper productMapper = ProductMapper.getInstance();
    private ItemMapper itemMapper = ItemMapper.getInstance();
    private CategoryMapper categoryMapper = CategoryMapper.getInstance();

    public static Inventory instance = null;
    
    private Inventory(){
        //productsList = new LinkedList<>();
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



//    /**
//     * Initialize the system with hardcoded products and items
//     */
//    public void initialize(){
//        HardCodeInitializer.getInstance().initialize();
//    }


    /**
     * adds a product to the inventory
     * @param productDTO the product's information to be added
     * @return a Result object with information about the result of the operation
     */
    public Result addProduct(ProductDTO productDTO){
        Result result = new Result();
        Product newProduct = new Product(productDTO);

        //TODO: generate an id for the product?

        if(productMapper.getProduct(newProduct.getId()) != null) { // checking if there's already a product with the same id
            if (productMapper.doesProductExist(newProduct.getName(), newProduct.getManufacturer())) {//checks if the product already exists
                productMapper.insert(productDTO);
                //productsList.add(newProduct);
                result.successful();
            } else {
                result.failure("Product already exists in the inventory");
            }
        }
        else{
            result.failure("There's already a product with this id. Id must be unique");
        }

        return result;
    }


//    //TODO: if the getProduct function will be sufficient then delete this
//    /**
//     * @param productToCheck the product to check
//     * @return if {@code productToCheck} already exists in the inventory
//     */
//    private boolean doesProductExist(Product productToCheck){
//        for(Product product : productsList){
//            if(product.equals(productToCheck)){
//                return true;
//            }
//        }
//
//        return false;
//    }

    /**
     * removes a product from the inventory
     * @param productID the id of the product to remove
     * @return a Result object with information about the result of the operation 
     */
    public Result removeProduct(int productID){
        Result result = new Result();
    
        ProductDTO toRemove = productMapper.getProduct(productID);


        if(toRemove != null){ //if toRemove is null then the product does not exist in the inventory
            productMapper.delete(toRemove);
            //productsList.remove(toRemove);

            result.successful();
        }
        else{
            result.failure("The product you are trying to remove does not exist");
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
    public Result addItem(int productID, ItemDTO itemDTO){
        Result result = new Result();

        ProductDTO productDTO = productMapper.getProduct(productID);

        if(productDTO != null){// if productToAddTo is null then there is no such product in the inventory
            Product productToAddTo = new Product(productDTO);
            Item itemToAdd = new Item(itemDTO);

            productToAddTo.addItem(itemToAdd);


            productMapper.update(productToAddTo.getDTORepresentation());

            result.successful();
        }
        else{
            result.failure("The item you are trying to add does not have a corresponding product in the inventory.");
        }

        return result;
    }

    /**
     * removes one item from a product from the inventory
     * @param productID the id of the product to remove from
     * @param itemID the order ID of the item
     * @return a Result object with information about the result of the operation
     */
    public Result removeOneItem(int productID, int itemID){
        Result result = new Result();

        ProductDTO productDTO = productMapper.getProduct(productID);

        if(productDTO != null){
            Product productToRemoveFrom = new Product(productDTO);
            Item itemToRemove = productToRemoveFrom.getItem(itemID);

            if(itemToRemove != null){// if itemToRemove is null the item does not exist
                productToRemoveFrom.removeItem(itemToRemove);

                productMapper.update(productToRemoveFrom.getDTORepresentation());

                String quantityMessage = minQuantityNotification(productToRemoveFrom);

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

    /**
     * given a product, checks if the minimum quantity set for the product is reached
     * @param product the product to check
     * @return a String saying the minimum quantity is reached if so or null if the quantity hasn't reached yet
     */
    public String minQuantityNotification(Product product){
        if(product.hasMinQuantityReached()){
            return sendLackOrder(product);
        }

        return null;
    }

    //TODO: this
    public String sendLackOrder(Product product){

        if(stamFunction() != -1){


            return "Minimum quantity for product " + product.getName() + " by " + product.getManufacturer() + " reached!" +
                    "Sent an order to fill the need!";
        }

        return "Minimum quantity for product " + product.getName() + " by " + product.getManufacturer() + " reached!" +
                "Couldn't send an order to fill the lack cause there is no supplier to supply the need.";
    }

    private int stamFunction(){
        return 0;
    }

    //TODO: figure out how to do this thing
    public void setPeriodicOrder(){

    }

    /**
     * updates the min quantity of a product from which the user will get notification about the quantity of the product
     * @param productID the id of the product to update
     * @param newMinQuantity the new minimum quantity to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateMinQuantity(int productID, int newMinQuantity){
        Result result = new Result();

        ProductDTO product = productMapper.getProduct(productID);

        if(product != null){
            product.setMinCapacity(newMinQuantity);

            productMapper.update(product);


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
    public Result updateSellingPrice(int productID, int newSellingPrice){
        Result result = new Result();

        ProductDTO product = productMapper.getProduct(productID);

        if(product != null){
            product.setSellingPrice(newSellingPrice);

            productMapper.update(product);

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
    public Result updateBuyingPrice(int productID, int newBuyingPrice){
        Result result = new Result();

        ProductDTO product = productMapper.getProduct(productID);

        if(product != null){
            product.setBuyingPrice(newBuyingPrice);

            productMapper.update(product);

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
    public Result setDefect(int productID, int itemId, int numOfDefects){
        Result result = new Result();

        ProductDTO productDTO = productMapper.getProduct(productID);

        if(productDTO != null) { // if productToSetDefectFrom is null then it means he does not exist
            Product productToSetDefectFrom = new Product(productDTO);

            Item itemToSetDefectTo = productToSetDefectFrom.getItem(itemId);

            if (itemToSetDefectTo != null) {// if itemToSetAsDefect is null the item does not exist

                if(numOfDefects < itemToSetDefectTo.getCount()) {
                    itemToSetDefectTo.setNumOfDefects(numOfDefects);

                    productMapper.update(productToSetDefectFrom.getDTORepresentation());

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
    public Result updateItemLocation(int productID, int itemId, String location){
        Result result = new Result();

        ProductDTO productDTO = productMapper.getProduct(productID);

        if(productDTO != null) {// if productToSetLocationFrom is null then it means he does not exist
            Product productToSetLocationFrom = new Product(productDTO);
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

                    productMapper.update(productToSetLocationFrom.getDTORepresentation());

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
    public String getCategoriesReport(List<List<String>> categories){
        //getting and arranging the products from the db
        List<Product> productsList = new LinkedList<>();
        for(ProductDTO productDTO: productMapper.getAll()){
            productsList.add(new Product(productDTO));
        }


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

        if(categoriesReport.compareTo("Catagories Report:\n") == 0){
            categoriesReport = "There are no products in the requested catagories";
        }
        return categoriesReport;
    }

    /**
     * 
     * @return a report listing all the defected and expired items in the inventory
     */
    public String getDefectsReports(){
        //getting and arranging the products from the db
        List<Product> productsList = new LinkedList<>();
        for(ProductDTO productDTO: productMapper.getAll()){
            productsList.add(new Product(productDTO));
        }

        String defectsReport = "Defects or Expired Report:\n\n";

        for(Product product: productsList){
            defectsReport += product.productDefects() + "\n";
        }


        if(defectsReport.compareTo("Defects or Expired Report:\n\n") == 0){
            defectsReport = "There are no defect or expired items in the inventory";
        }
        return defectsReport;
    }

//    /**
//     * given an id, the function checks if there is already an item with this id
//     * @param id the id to check
//     * @return whether there's already an item with id {@code id}
//     */
//    public boolean isIdUnique(int id){
//        for(Product product: productsList){
//            if(product.hasAnItemWithID(id)){
//                return false;
//            }
//        }
//
//        return true;
//    }
    
}
