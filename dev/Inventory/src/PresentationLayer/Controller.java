package PresentationLayer;

import BusinessLayer.Inventory;
import DTO.ItemDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class provides services for menu
 */
public class Controller implements Observer {

    /**
     * This function takes care over illegal date inputs
     * @param date is a string which have to be in the pre-defined format
     * @return a new date by the given string. if the string invalid return null.
     */
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Inventory invetory = Inventory.getInstance();

    /**
     * This function get input from the user and activates the addItem method in the Inventory.
     * @param in standard input stream
     */
    public static void addItem(Scanner in){
        int id= 0;
        Date expiryDate= null;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();
        while(!invetory.isIdUnique(id)){
            System.out.print("The id is not unique- try again [number]: ");
            id=in.nextInt();
        }

        System.out.print("insert the item expiry date [dd-mm-yyyy]: ");
        expiryDate=parseDate(in.next());
        while(expiryDate==null){
            System.out.print("The date is illegal- try again [dd-mm-yyyy]: ");
            expiryDate=parseDate(in.next());
        }

        invetory.addItem(new ItemDTO(id,false,expiryDate,"Inventory"));
    }

    /**
     * This function get as input id of an item, and removes it if the item exists
     * @param in  standard input stream
     */
    public static void removeItem(Scanner in){
        int id;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();
        if(invetory.removeItem(id))
            System.out.println("item with id "+id+" removed successfully");
        else
            System.out.println("id "+id+" is not exists");
    }

    /**
     * Add a new product to the inventory
     */
    public static void addProduct(){
        invetory.addProduct();
    }

    /**
     * Remove an exists product from the inventory
     */
    public static void removeProduct(){
        invetory.removeProduct();
    }

    /**
     * This function get as an input id and min quantity, and updating this quantity of the relevant item
     * @param in standard input stream
     */
    public static void updateMinQuantity(Scanner in){
        int id, minQuantity;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();
        System.out.print("insert min quantity [number]: ");
        minQuantity=in.nextInt();

        if(invetory.updateMinQuantity(id,minQuantity))
            System.out.println("item with id "+id+" was updated");
        else
            System.out.println("id "+id+" is not exists");
    }

    /**
     * This function get as an input the identifiers of a product (its manufacturer and the product name)
     * and updates the selling price
     * @param in standard input stream
     */
    public static void updateSellingPrice(Scanner in){
        int price;
        String name, manufacturer;
        System.out.print("insert the manufacturer name [String]: ");
        name=in.next();
        System.out.print("insert the product name [String]: ");
        manufacturer=in.next();
        System.out.print("insert new price [number]: ");
        price=in.nextInt();

        if(invetory.updateSellingPrice(manufacturer,name,price))
            System.out.println("Updated successfully");
        else
            System.out.println("The update failed");
    }

    /**
     * This function get as an input the identifiers of a product (its manufacturer and the product name)
     * and updates the buying price
     * @param in  standard input stream
     */
    public static void updateBuyingPrice(Scanner in){
        int price;
        String name, manufacturer;
        System.out.print("insert the manufacturer name [String]: ");
        name=in.next();
        System.out.print("insert the product name [String]: ");
        manufacturer=in.next();
        System.out.print("insert new price [number]: ");
        price=in.nextInt();

        if(invetory.updateBuyingPrice(manufacturer,name,price))
            System.out.println("Updated successfully");
        else
            System.out.println("The update failed");
    }

    /**
     * This function get as an input an id of an item, and set its status to be a defect item
     * @param in standard input stream
     */
    public static void updateItemStatus(Scanner in){
        int id;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();

        if(invetory.setDefect(id))
            System.out.println("item with id "+id+" was updated");
        else
            System.out.println("id "+id+" is not exists");
    }

    /**
     * This function updates the location of an item
     * @param in standard input stream
     */
    public static void updateItemLocation(Scanner in){
        int id;
        String location;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();

        System.out.print("insert a new location [String]: ");
        location=in.next();

        if(invetory.updateItemLocation(id,location))
            System.out.println("item with id "+id+" was updated");
        else
            System.out.println("id "+id+" is not exists");
    }

    /**
     * This function gets a list of categories the user is interested about,
     * and display the relevant items status on the screen
     * @param in standard input stream
     */
    public static void getCategoriesReport(Scanner in){
        String categoriesSTR;
        List<String> categories;
        System.out.print("insert categories separated by ',': ");
        categoriesSTR=in.next();
        categories=Arrays.asList(categoriesSTR.split(","));
        System.out.println(invetory.getCategoriesReport(categories));
    }

    /**
     * This function displays all the defect items status on screen
     */
    public static void getDefectsReports(){
        System.out.println(invetory.getDefectsReports());
    }

    /**
     * This function takes care of notification when minCapacity is achieved.
     * When it happens a notification will display on screen.
     * @param observable all the exist product in the inventory
     * @param o minCapacity
     */
    @Override
    public void update(Observable observable, Object o) {
        //print notification
    }
}
