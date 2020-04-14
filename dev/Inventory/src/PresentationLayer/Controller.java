package PresentationLayer;

import BusinessLayer.Inventory;
import BusinessLayer.Result;
import DTO.ItemDTO;
import DTO.ProductDTO;
import Initialize.HardCodeInitializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class provides services for menu
 */
public class Controller{

    public static Controller controller = null;

    /**
     *
     * @return an instance of Controller
     */
    public static Controller getInstance(){
        if(controller == null){
            controller = new Controller();
        }
        return controller;
    }

    public void initialize(){
        HardCodeInitializer.getInstance().initialize();
    }

    /**
     * This function takes care over illegal date inputs
     * @param date is a string which have to be in the pre-defined format
     * @return a new date by the given string. if the string invalid return null.
     */
    public Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * This function get input from the user and activates the addItem method in the Inventory.
     * @param in standard input stream
     */
    public void addItem(Scanner in){
        int id;
        Date expiryDate;
        String productName, manufacturer;
        Result result;

        System.out.print("insert the item name [String]: ");
        productName=in.next();

        System.out.print("insert the item manufacturer name [String]: ");
        manufacturer=in.next();

        System.out.print("insert the item id [number]: ");
        id=in.nextInt();

        System.out.print("insert the item expiry date [dd-mm-yyyy]: ");
        expiryDate=parseDate(in.next());

        result= Inventory.getInstance().addItem(productName, manufacturer, new ItemDTO(id,false,expiryDate,"Inventory"));
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as input id of an item, and removes it if the item exists
     * @param in  standard input stream
     */
    public void removeItem(Scanner in){
        int id;
        String productName, manufacturer;
        Result result;

        System.out.print("insert the item id [number]: ");
        id=in.nextInt();

        System.out.print("insert the item name [String]: ");
        productName=in.next();

        System.out.print("insert the item manufacturer name [String]: ");
        manufacturer=in.next();

        result= Inventory.getInstance().removeItem(productName,manufacturer,id);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }


    /**
     * Add a new product to the inventory
     */
    public void addProduct(Scanner in){
        String productName, manufacturer, categoriesSTR;
        int minCapacity, buyingPrice, sellingPrice;
        List<String> categories;
        Result result;

        System.out.print("insert the product name [String]: ");
        productName=in.next();

        System.out.print("insert the manufacturer name [String]: ");
        manufacturer=in.next();

        System.out.print("insert the minimal capacity [number]: ");
        minCapacity=in.nextInt();

        System.out.print("insert the buying price [number]: ");
        buyingPrice=in.nextInt();

        System.out.print("insert the selling price [number]: ");
        sellingPrice=in.nextInt();

        System.out.print("insert the categories the product belongs to separated by ','  [exp. milk, salty, 500ml]: ");
        categoriesSTR=in.next();
        categories= Arrays.asList(categoriesSTR.split(","));

        result= Inventory.getInstance().addProduct(new ProductDTO(productName,manufacturer,minCapacity,buyingPrice,sellingPrice,
                                          0,0, categories, new LinkedList<>()));
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * Remove an exists product from the inventory
     */
    public void removeProduct(Scanner in){
        String productName, manufacturer;
        Result result;

        System.out.print("insert the item name [String]: ");
        productName=in.next();

        System.out.print("insert the item manufacturer name [String]: ");
        manufacturer=in.next();

        result= Inventory.getInstance().removeProduct(productName,manufacturer);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as an input id and min quantity, and updating this quantity of the relevant item
     * @param in standard input stream
     */
    public void updateMinQuantity(Scanner in){
        int minQuantity;
        String productName, manufacturer;
        Result result;

        System.out.print("insert the item name [String]: ");
        productName=in.next();

        System.out.print("insert the item manufacturer name [String]: ");
        manufacturer=in.next();

        System.out.print("insert min quantity [number]: ");
        minQuantity=in.nextInt();

        result= Inventory.getInstance().updateMinQuantity(productName,manufacturer,minQuantity);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as an input the identifiers of a product (its manufacturer and the product name)
     * and updates the selling price
     * @param in standard input stream
     */
    public void updateSellingPrice(Scanner in){
        int price;
        String name, manufacturer;
        Result result;

        System.out.print("insert the manufacturer name [String]: ");
        name=in.next();

        System.out.print("insert the product name [String]: ");
        manufacturer=in.next();

        System.out.print("insert new price [number]: ");
        price=in.nextInt();

        result= Inventory.getInstance().updateSellingPrice(manufacturer,name,price);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as an input the identifiers of a product (its manufacturer and the product name)
     * and updates the buying price
     * @param in  standard input stream
     */
    public void updateBuyingPrice(Scanner in){
        int price;
        String productName, manufacturer;
        Result result;

        System.out.print("insert the product name [String]: ");
        productName=in.next();

        System.out.print("insert the product name [String]: ");
        manufacturer=in.next();

        System.out.print("insert new price [number]: ");
        price=in.nextInt();

        result= Inventory.getInstance().updateBuyingPrice(productName,manufacturer,price);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }


    /**
     * This function get as an input an id of an item, and set its status to be a defect item
     * @param in standard input stream
     */
    public void updateItemStatus(Scanner in){
        int id;
        Result result;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();

        result= Inventory.getInstance().setDefect("asd","asd",id);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function updates the location of an item
     * @param in standard input stream
     */
    public void updateItemLocation(Scanner in){
        int id;
        String location;
        Result result;
        System.out.print("insert the item id [number]: ");
        id=in.nextInt();

        System.out.print("insert a new location [String]: ");
        location=in.next();

        result= Inventory.getInstance().updateItemLocation("asd","asd",id,location);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * clean the spaces at the beginning and at the end of a string
     * @param str assume not null and not empty string
     * @return new string without spaces at the beginning ant at the end
     */
    private String spaceCleaner(String str){
        while(str.charAt(0)==' ')
            str= str.substring(1);

        while(str.charAt(str.length()-1)==' ')
            str= str.substring(0,str.length()-1);

        return str;
    }

    /**
     * This function removes the redundant spaces in each cell of the list
     * @param list List that contains redundant spaces
     * @return new list cleaned of redundant spaces
     */
    private List<String> cleanList(List<String> list){
        List<String> cleanedList=new LinkedList<>();

        for(String str: list)
            cleanedList.add(spaceCleaner(str));

        return cleanedList;
    }


    /**
     * split a string to categories list
     * @param categoriesST categories in CNF format [milk,500ml],[shampoo]
     * @return return list of categories list
     */
    private List<List<String>> splitList(String categoriesST){
        List<List<String>> allCategories=new LinkedList<>();
        List<List<String>> allCategoriesSpaces=new LinkedList<>();
        List<String> splitList, filterList=new LinkedList<>();

        //exp. [milk,500ml],[shampoo]
        splitList= Arrays.asList(categoriesST.split("\\["));  //   ,  milk,500ml]  ,   shampoo]
        for(String categories: splitList)
        {
            if(categories.length()>1) {
                filterList.add(categories.substring(0, categories.indexOf(']')));  //   milk,500ml  ,  shampoo
            }
        }

        for(String categories: filterList) {
            allCategoriesSpaces.add(Arrays.asList(categories.split(",")));
        }

        for(List<String> list: allCategoriesSpaces) { //clean redundant spaces
            allCategories.add(cleanList(list));
        }

        return allCategories;
    }

    /**
     * This function gets a list of categories the user is interested about,
     * and display the relevant items status on the screen
     * @param in standard input stream
     */
    public void getCategoriesReport(Scanner in){
        String categoriesSTR="";

        System.out.println("insert categories you wish to see in the report:");
        System.out.println("The format should be like [milk,500ml],[shampoo].");
        System.out.println("There's an AND inside the brackets and an OR between them:");

        while(categoriesSTR.length()==0) //get over '\n' at the start of a line
            categoriesSTR= in.nextLine();

        System.out.println(Inventory.getInstance().getCategoriesReport(splitList(categoriesSTR)));
    }

    /**
     * This function displays all the defect items status on screen
     */
    public void getDefectsReports(){
        System.out.println(Inventory.getInstance().getDefectsReports());
    }

}
