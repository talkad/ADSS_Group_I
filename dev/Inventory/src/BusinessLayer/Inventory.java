package BusinessLayer;

import DTO.ItemDTO;
import DTO.ProductDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * a singleton class
 */
public class Inventory {

    private List<Product> productsList;

    public static Inventory instance = null;
    
    private Inventory(){
        productsList = new LinkedList<>();
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
        
        if(!doesProductExist(newProduct)){//checks if the product already exists
            productsList.add(newProduct);

            result.successful();
        }
        else{
            result.failure("Product already exists in the inventory");
        }
        
        return result;
    }


    /**
     * @param productToCheck the product to check
     * @return if {@code productToCheck} already exists in the inventory
     */
    private boolean doesProductExist(Product productToCheck){
        for(Product product : productsList){
            if(product.equals(productToCheck)){
                return false;
            }
        }

        return true;
    }

    /**
     * removes a product from the inventory
     * @param name the name of the product to remove
     * @param manufacturer the name of the manufacturer of the product to remove
     * @return a Result object with information about the result of the operation 
     */
    public Result removeProduct(String name, String manufacturer){
        Result result = new Result();
    
        Product toRemove = getProduct(name, manufacturer);

        if(toRemove != null){ //if toRemove is null then the product does not exist in the inventory
            productsList.remove(toRemove);

            result.successful();
        }
        else{
            result.failure("The product you are trying to remove does not exist");
        }

        return result;
    }

    /**
     * return the product represnted by the given name and manufacturer
     * @param name the name of the product
     * @param manufacturer the name of the manufacturer of the product
     * @return the product object represented by the {@code name} and {@code manufacturer}
     */
    private Product getProduct(String name, String manufacturer){
        for(Product product : productsList){
            if(product.isRepresentedProduct(name, manufacturer)){
                return product;
            }
        }

        return null;
    }


    /**
     * adds an item from type product to the inventory
     * @param productName the product's name of the item to add
     * @param manufacturerName the manufacturer's name of the item to add
     * @param itemDTO the item to add
     * @return a Result object with information about the result of the operation
     */
    public Result addItem(String productName, String manufacturerName, ItemDTO itemDTO){
        Result result = new Result();

        Product productToAddTo = getProduct(productName, manufacturerName);

        Item itemToAdd = new Item(itemDTO);
        productToAddTo.addItem(itemToAdd);

        result.successful();

        return result;
    }

    /**
     * removes an item of type some type product from the inventory
     * @param productName the product's name of the item to remove
     * @param manufacturerName the manufacturer's name of the item to remove
     * @param id the id of the item
     * @return a Result object with information about the result of the operation
     */
    public Result removeItem(String productName, String manufacturerName, int id){
        Result result = new Result();

        Product productToRemoveFrom = getProduct(productName, manufacturerName);

        Item itemToRemove = productToRemoveFrom.getItem(id);

        if(itemToRemove != null){// if itemToRemove is null the item does not exist
            productToRemoveFrom.removeItem(itemToRemove);

            String quantityMessage = minQuantityNotification(productToRemoveFrom);

            result.successful(quantityMessage);
        }
        else{
            result.failure("The item you are trying to remove does not exist");
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
            return "Minimum quantity for " + product.getName() + " reached!";
        }

        return null;
    }

    /**
     * updates the min quantity of a product from which the user will get notification about the quantity of the product
     * @param productName the name of the product to be updated
     * @param manufacturerName the name of the manufacturer of the product to be updated
     * @param newMinQuantity the new mininum quantity to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateMinQuantity(String productName, String manufacturerName, int newMinQuantity){
        Result result = new Result();

        Product product = getProduct(productName, manufacturerName);

        if(product != null){
            product.setMinCapacity(newMinQuantity);


            result.successful();
        }
        else{
            result.failure("The product you are trying to update its minimum quantity does not exist");
        }

        return result;
    }

    /**
     * updates the selling price of a product
     * @param productName the name of the product to be updated
     * @param manufacturerName the name of the manufacturer of the product to be updated
     * @param newSellingPrice the new selling price to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateSellingPrice(String productName, String manufacturerName, int newSellingPrice){
        Result result = new Result();

        Product product = getProduct(productName, manufacturerName);

        if(product != null){
            product.setSellingPrice(newSellingPrice);


            result.successful();
        }
        else{
            result.failure("The product you are trying to update its selling price does not exist");
        }

        return result;
    }

    /**
     * updates the buying price of a product
     * @param productName the name of the product to be updated
     * @param manufacturerName the name of the manufacturer of the product to be updated
     * @param newBuyingPrice the new buying price to update to
     * @return a Result object with information about the result of the operation
     */
    public Result updateBuyingPrice(String productName, String manufacturerName, int newBuyingPrice){
        Result result = new Result();

        Product product = getProduct(productName, manufacturerName);

        if(product != null){
            product.setSellingPrice(newBuyingPrice);


            result.successful();
        }
        else{
            result.failure("The product you are trying to update its buying price does not exist");
        }

        return result;
    }

    /**
     * sets a given item as defect
     * @param productName the product's name from which to set an item as defect
     * @param manufacturerName the product manufacturer's name from which to set an item as defect 
     * @param itemId the item's id to set as defect 
     * @return a Result object with information about the result of the operation
     */
    public Result setDefect(String productName, String manufacturerName, int itemId){
        Result result = new Result();

        Product productToSetDefectFrom = getProduct(productName, manufacturerName);

        Item itemToSetAsDefect = productToSetDefectFrom.getItem(itemId);

        if(itemToSetAsDefect != null){// if itemToSetAsDefect is null the item does not exist
            itemToSetAsDefect.setDefect(true);

            result.successful();
        }
        else{
            result.failure("The item you are trying to update its status does not exist");
        }

        return result;
    }

    /**
     * sets a new location to a given item
     * @param productName the product's name from which to set an item a new location
     * @param manufacturerName the product manufacturer's name from which to set an item a new location
     * @param itemId the item's id to set a new location to
     * @param location the new location to set
     * @return a Result object with information about the result of the operation
     */
    public Result updateItemLocation(String productName, String manufacturerName, int itemId, String location){
        Result result = new Result();

        Product productToSetLoactionFrom = getProduct(productName, manufacturerName);

        Item itemToSetNewLocationTo = productToSetLoactionFrom.getItem(itemId);

        if(itemToSetNewLocationTo != null){// if itemToSetNewLocationTo is null the item does not exist
            itemToSetNewLocationTo.setLocation(location);

            result.successful();
        }
        else{
            result.failure("The item you are trying to set a new location to does not exist");
        }

        return result;
    }

    /**
     * creates an inventory report according to given catagories
     * @param categories the catagories to include in the report 
     * @return an inventory report according to the given {@code catagories} 
     */
    public String getCategoriesReport(List<String> categories){
        String catagoriesReport = "Catagories Report:\n";

        boolean isIncluded = true; // a flag if the current product is to be included in the report
        for(Product product: productsList){
            for(String catagory: categories){
                isIncluded = isIncluded & product.isInCatagory(catagory);
            }

            if(isIncluded){
                catagoriesReport += product.toString() + "\n";
            }
            
            isIncluded = true;
        }

        if(catagoriesReport.compareTo("Catagories Report:\n") == 0){
            catagoriesReport = "There are no products in the requested catagories";
        }
        return catagoriesReport;
    }

    /**
     * 
     * @return a report listing all the defected and expired items in the inventory
     */
    public String getDefectsReports(){
        String defectsReport = "Defects or Expired Report:\n\n";

        for(Product product: productsList){
            defectsReport += product.productDefects() + "\n";
        }


        if(defectsReport.compareTo("Defects or Expired Report:\n") == 0){
            defectsReport = "There are no defect or expired items in the inventory";
        }
        return defectsReport;
    }

    /**
     * given an id, the function checks if there is already an item with this id
     * @param id the id to check
     * @return whether there's already an item with id {@code id}
     */
    public boolean isIdUnique(int id){
        for(Product product: productsList){
            if(product.hasAnItemWithID(id)){
                return false;
            }
        }

        return true;
    }
    
}
