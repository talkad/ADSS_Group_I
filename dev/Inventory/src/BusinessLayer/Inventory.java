package BusinessLayer;

import DTO.ItemDTO;

import java.util.List;

public class Inventory {
    private List<Products> productsList;

    void initialize(){}

    /**
     *
     * @param item
     * @return
     */
    public static boolean addItem(ItemDTO item){
        return true;
    }

    public static boolean removeItem(int id){
        return true;
    }

    public static boolean addProduct(){
        return true;
    }

    public static boolean removeProduct(){
        return true;
    }

    public static boolean isItemExists(int id){
        return true;
    }

    public static String notification(){
        return "";
    }

    public static boolean updateMinQuantity(int id, int minQuantity){
        return true;
    }

    public static boolean updateSellingPrice(String manufacturer, String name, int price){
        return true;
    }

    public static boolean updateBuyingPrice(String manufacturer, String name, int price){
        return true;
    }

    public static boolean setDefect(int id){
        return true;
    }

    public static boolean updateItemLocation(int id, String location){
        return true;
    }

    public static String getCategoriesReport(List<String> categories){
        return "";
    }

    public static String getDefectsReports(){
        return "";
    }

    public static boolean isIdUnique(int id){ return true; }





}
