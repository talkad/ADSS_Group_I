package DTO;

import java.util.List;

public class ProductDTO {
    private String name;
    private String manufacturer;
    private int minCapacity;
    private int buyingPrice;
    private int sellingPrice;
    private int inventoryCapacity;
    private int storeCapacity;
    private List<String> categories;
    private List<ItemDTO> itemDTOs;

    public ProductDTO(String name, String manufacturer, int minCapacity, int buyingPrice, int sellingPrice,
                      int inventoryCapacity, int storeCapacity, List<String> categories, List<ItemDTO> itemDTOs) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.minCapacity = minCapacity;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.inventoryCapacity = inventoryCapacity;
        this.storeCapacity = storeCapacity;
        this.categories = categories;
        this.itemDTOs = itemDTOs;
    }

}
