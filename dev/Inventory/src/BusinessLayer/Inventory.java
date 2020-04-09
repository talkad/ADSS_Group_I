package BusinessLayer;

import DTO.ItemDTO;

import java.util.List;

public class Inventory {
    private List<Products> productsList;

    //void initialize(){}

    /**
     *
     * @param item
     * @return
     */
    boolean addItem(ItemDTO item){
        return true;
    }

    boolean removeItem(int id){
        return true;
    }

    boolean addProduct(){
        return true;
    }

    boolean removeProduct(){
        return true;
    }

    boolean isItemExists(ItemDTO item){
        return true;
    }

    String notification(){
        return "";
    }

    boolean updateMinQuantity(ItemDTO item, int minQuantity){
        return true;
    }

    boolean updateSellingPrice(String manufacturer, String name, int price){
        return true;
    }

    boolean updateBuyingPrice(String manufacturer, String name, int price){
        return true;
    }

    boolean updateItemStatus(int id, String field, boolean status){
        return true;
    }

    boolean updateItemLocation(int id, String location){
        return true;
    }

    String getCategoriesReport(List<String> categories){
        return "";
    }

    String getDefectsReports(){
        return "";
    }





}
