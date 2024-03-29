package InventoryModule.DTO;

import java.util.List;

public class ProductDTO {

    private int id;
    private int storeID;
    protected String name;
    private String manufacturer;
    private int minCapacity;
    private int buyingPrice;
    private int sellingPrice;
    private double weight;
    private int inventoryCapacity;
    private int storeCapacity;
    private List<String> categories;
    private List<ItemDTO> itemDTOs;

    public ProductDTO(int id, int storeID, String name, String manufacturer, int minCapacity, int buyingPrice, int sellingPrice,
                      double weight, int inventoryCapacity, int storeCapacity, List<String> categories, List<ItemDTO> itemDTOs) {
        this.id = id;
        this.storeID = storeID;
        this.name = name;
        this.manufacturer = manufacturer;
        this.minCapacity = minCapacity;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.weight = weight;
        this.inventoryCapacity = inventoryCapacity;
        this.storeCapacity = storeCapacity;
        this.categories = categories;
        this.itemDTOs = itemDTOs;
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

    public List<ItemDTO> getItemDTOs() {
        return itemDTOs;
    }

    public void setItemDTOs(List<ItemDTO> itemDTOs) {
        this.itemDTOs = itemDTOs;
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

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }
}
