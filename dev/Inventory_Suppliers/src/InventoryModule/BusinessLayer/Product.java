package BusinessLayer;

import java.util.LinkedList;
import java.util.List;

import DTO.ProductDTO;

public class Product {

    private int id;
    private String name;
    private String manufacturer;
    private int minCapacity;
    private int buyingPrice;
    private int sellingPrice;
    private double weight; // the weight is in kilograms
    private int inventoryCapacity; // by inventory capacity we mean the amount of items of the current product currently physically in the store's inventory
    private int storeCapacity; // by store capacity we mean the amount of items of the current product currently physically in the store
    private List<String> categories;
    private List<Item> items;

    public Product(ProductDTO productDTO) {
        this.id = productDTO.getId();
        this.name = productDTO.getName();
        this.manufacturer = productDTO.getManufacturer();
        this.minCapacity = productDTO.getMinCapacity();
        this.buyingPrice = productDTO.getBuyingPrice();
        this.sellingPrice = productDTO.getSellingPrice();
        this.weight = productDTO.getWeight();
        this.inventoryCapacity = productDTO.getInventoryCapacity();
        this.storeCapacity = productDTO.getStoreCapacity();
        this.categories = productDTO.getCategories();
        this.items = new LinkedList<>();
    }

    /**
     * adds an item from type product to the inventory
     * @param item the item to add
     */
    public void addItem(Item item){
        items.add(item);
        if(item.getLocation().equals("Store") || item.getLocation().equals("store") ){// if the item was added to the store then we'll update the amount of items in the store accordingly
            this.storeCapacity += item.getCount();
        }
        else{ // else the item is about ot be added to the store's inventory
            this.inventoryCapacity += item.getCount();
        }
    }

    /**
     * removes an item from type product from the inventory
     * @param item the item to remove
     */
    //TODO: not sure if i delete here all of them or just one
    public void removeItem(Item item){

        if(item.getLocation().equals("Store") || item.getLocation().equals("store")){// if the item was in the store then we'll update the amount of items in the store accordingly
            this.storeCapacity--;
        }
        else{ // else the item is in the store's inventory
            this.inventoryCapacity--;
        }

        if(item.removeOne()){ // checks if the count of the item is 0
            items.remove(item); // is so, removes the item from the list
        }
    }

    /**
     * gets an item with the given id
     * @param itemOrderId the id of the item
     * @return an item with an id {@code id}, null if there is no such item
     */
    public Item getItem(int itemOrderId){
        for(Item item: items){
            if(item.getOrderID() == itemOrderId){
                return item;
            }
        }

        return null;
    }

    /**
     * @return if the minimum quantity set for the product is reached
     */
    //TODO: ask them if defected or expired do not count
    public boolean hasMinQuantityReached(){
        return inventoryCapacity + storeCapacity <= minCapacity;
    }

//    /**
//     * checks if the given name and manufacturer represnt the current product
//     * @param name the name of the product
//     * @param manufacturer the manufacturer of the product
//     * @return whether the given name and manufacturer represnt the current product
//     */
//    public boolean isRepresentedProduct(String name, String manufacturer){
//        return (this.name.compareTo(name) == 0) && (this.manufacturer.compareTo(manufacturer) == 0);
//    }

    /**
     *
     * @param category the catagory to check
     * @return if the current product is in category {@code category}
     */
    public boolean isInCategory(String category){
        return categories.contains(category);
    }

//    /**
//     * given an id, the function returns whether there is an item from this product with the id.
//     * @param itemOrderID the id to check
//     * @return whether there is an item with id {@code id}
//     */
//    public boolean hasAnItemWithID(int itemOrderID){
//        for(Item item: items){
//            if(item.getOrderID() == id){
//                return true;
//            }
//        }
//
//        return false;
//    }

    /**
     *
     * @return information about all the defected or expired items of the current product
     */
    //TODO: give more info?
    public String productDefects(){
        String defectsInfo = "Name: " + this.name + "\n";
        defectsInfo += "Manufacturer: " + this.manufacturer + "\n";
        defectsInfo += "Categories: " + categories.toString() + "\n";
        defectsInfo += "Defect items:\n";

        for(Item item: items){
            String itemInfo = item.defectInfo();

            if(itemInfo != null){// if null then the item is not defect or expired so we are not including it
                defectsInfo += itemInfo + "\n";
            }
        }

        return defectsInfo;
    }

    /**
     *
     * @return a DTO representation of the current Product
     */
    public ProductDTO getDTORepresentation(){
        return null; //TODO: implement this thingy
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    public void setInventoryCapacity(int inventoryCapacity) {
        this.inventoryCapacity = inventoryCapacity;
    }

    public int getStoreCapacity() {
        return storeCapacity;
    }

    public void setStoreCapacity(int storeCapacity) {
        this.storeCapacity = storeCapacity;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * check if two instances of Product are the same
     * @param product an instance of Product
     * @return whether {@code product} and the current object repesent the same product
     * when two different instances of Product have the same name and manufacturer they are considered the same.
     */
    public boolean equals(Product product){
        return (this.name.compareTo(product.name) == 0) && (this.manufacturer.compareTo(product.manufacturer) == 0);
    }

    @Override
    public String toString(){
        String toString = "Product ID: " + this.id + "\n";
        toString += "Name: " + this.name + "\n";
        toString += "Manufacturer: " + this.manufacturer + "\n";
        toString += "Minimum capacity: " + this.minCapacity + "\n";
        toString += "Buying price: " + this.buyingPrice + "\n";
        toString += "Selling price: " + this.sellingPrice + "\n";
        toString += "weight: " + this.weight + "\n";
        toString += "Quantity in inventory: " + this.inventoryCapacity + "\n";
        toString += "Quantity in store: " + this.storeCapacity + "\n";
        toString += "Categories: " + categories.toString() + "\n";
        toString += "Items: " + items.toString() + "\n";

        return toString;
    }
}