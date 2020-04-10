package BusinessLayer;

import java.util.LinkedList;
import java.util.List;

import DTO.ProductDTO;

public class Product {

    private String name;
    private String manufacturer;
    private int minCapacity;
    private int buyingPrice;
    private int sellingPrice;
    private int inventoryCapacity;
    private int storeCapacity;
    private List<String> categories;
    private List<Item> items;

    public Product(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.manufacturer = productDTO.getManufacturer();
        this.minCapacity = productDTO.getMinCapacity();
        this.buyingPrice = productDTO.getBuyingPrice();
        this.sellingPrice = productDTO.getSellingPrice();
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
    }

    /**
     * removes an item from type product from the inventory
     * @param item the item to remove
     */
    public void removeItem(Item item){
        items.remove(item);
    }

    /**
     * gets an item with the given id
     * @param id the id of the item
     * @return an item with an id {@code id}, null if there is no such item
     */
    public Item getItem(int id){
        for(Item item: items){
            if(item.getId() == id){
                return item;
            }
        }

        return null;
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

    /**
     * checks if the given name and manufacturer represnt the current product
     * @param name the name of the product
     * @param manufacturer the manufacturer of the product
     * @return whether the given name and manufacturer represnt the current product
     */
    public boolean isRepresentedProduct(String name, String manufacturer){
        return (this.name.compareTo(name) == 0) && (this.manufacturer.compareTo(manufacturer) == 0);
    }
}
