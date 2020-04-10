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
     * 
     * @return an instance of Inventory
     */
    public static Inventory getInstance(){
        if(instance == null){
            instance = new Inventory();
        }

        return instance;
    }

    ;

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
     * @param item the item to add
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

            result.successful();
        }
        else{
            result.failure("The item you are trying to remove does not exist");
        }

        return result;
    }

    public boolean isItemExists(int id){
        return true;
    }

    public String notification(){
        return "";
    }

    public boolean updateMinQuantity(int id, int minQuantity){
        return true;
    }

    public boolean updateSellingPrice(String manufacturer, String name, int price){
        return true;
    }

    public boolean updateBuyingPrice(String manufacturer, String name, int price){
        return true;
    }

    public boolean setDefect(int id){
        return true;
    }

    public boolean updateItemLocation(int id, String location){
        return true;
    }

    public String getCategoriesReport(List<String> categories){
        return "";
    }

    public String getDefectsReports(){
        return "";
    }

    public boolean isIdUnique(int id){ return true; }





}
