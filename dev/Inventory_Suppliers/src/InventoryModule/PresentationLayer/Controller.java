package PresentationLayer;

import BusinessLayer.Inventory;
import BusinessLayer.Result;
import DTO.ItemDTO;
import DTO.ProductDTO;
import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

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


    private String readLine(Scanner in){
        String input="";

        while(input.length()==0) //get over '\n' at the start of a line
            input= in.nextLine();

        return input;
    }

    /**
     * This function reads input from standard input stream, and ask the user to insert an integer.
     * if the input illegal, the user will try again until he inserts an integer.
     * @param in standard input stream
     * @return legal number input
     */
    private int readInteger(Scanner in){
        int num=0;
        String input="";
        boolean isLegal=false;

        while (!isLegal){
            try{
                input=in.nextLine();
                num= Integer.parseInt(input);
                isLegal=true;
            }catch (Exception e){
                System.out.print("This is not a number, try again: ");
            }
        }

        return num;
    }

    private double readDouble(Scanner in){
        double num=0;
        String input="";
        boolean isLegal=false;

        while (!isLegal){
            try{
                input=in.nextLine();
                num= Double.parseDouble(input);
                isLegal=true;
            }catch (Exception e){
                System.out.print("This is not a number, try again: ");
            }
        }

        return num;
    }

    /**
     * This function get input from the user and activates the addItem method in the Inventory.
     * @param in standard input stream
     */
    public void addItem(Scanner in){
        int productId;
        int orderId;
        int count;
        Date expiryDate;

        Result result;

        System.out.println("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.println("insert the order id [number]: ");
        orderId = readInteger(in);

        System.out.println("insert the item count that was delivered [number]: ");
        count = readInteger(in);

        System.out.print("insert the items expiry date [dd-mm-yyyy]: ");
        expiryDate=parseDate(in.next());

        result= Inventory.getInstance().addItem(productId, new ItemDTO(orderId, count, 0, expiryDate, "Inventory"));
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as input id of an item, and removes it if the item exists
     * @param in  standard input stream
     */
    public void removeItem(Scanner in){
        int productId;
        int itemId;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert the item id [number]: ");
        itemId = readInteger(in);

        result= Inventory.getInstance().removeOneItem(productId, itemId);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }


    /**
     * Add a new product to the inventory
     */
    public void addProduct(Scanner in){
        int productId;
        String productName, manufacturer, categoriesSTR;
        int minCapacity, buyingPrice, sellingPrice;
        double weight;
        List<String> categories;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert the product name [String]: ");
        productName= readLine(in);

        System.out.print("insert the manufacturer name [String]: ");
        manufacturer= readLine(in);

        System.out.print("insert the minimal capacity [number]: ");
        minCapacity= readInteger(in);

        System.out.print("insert the buying price [number]: ");
        buyingPrice= readInteger(in);

        System.out.print("insert the selling price [number]: ");
        sellingPrice= readInteger(in);

        System.out.print("insert the weight of the product (in Kg) [number]: ");
        weight = readDouble(in);

        System.out.print("insert the categories the product belongs to separated by ','  [exp. milk, salty, 500ml]: ");
        categoriesSTR= readLine(in);
        categories= Arrays.asList(categoriesSTR.split(","));

        result= Inventory.getInstance().addProduct(new ProductDTO(productId, productName, manufacturer, minCapacity,
                buyingPrice,sellingPrice, weight, 0,0, cleanList(categories), new LinkedList<>()));
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * Remove an exists product from the inventory
     */
    public void removeProduct(Scanner in){
        int productId;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        result = Inventory.getInstance().removeProduct(productId);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as an input id and min quantity, and updating this quantity of the relevant item
     * @param in standard input stream
     */
    public void updateMinQuantity(Scanner in){
        int productId;
        int minQuantity;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert min quantity [number]: ");
        minQuantity= readInteger(in);

        result= Inventory.getInstance().updateMinQuantity(productId, minQuantity);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as an input the identifiers of a product (its manufacturer and the product name)
     * and updates the selling price
     * @param in standard input stream
     */
    public void updateSellingPrice(Scanner in){
        int productId;
        int sellingPrice;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert new price [number]: ");
        sellingPrice = readInteger(in);

        result= Inventory.getInstance().updateSellingPrice(productId, sellingPrice);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function get as an input the identifiers of a product (its manufacturer and the product name)
     * and updates the buying price
     * @param in  standard input stream
     */
    public void updateBuyingPrice(Scanner in){
        int productId;
        int buyingPrice;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert new price [number]: ");
        buyingPrice = readInteger(in);

        result= Inventory.getInstance().updateBuyingPrice(productId, buyingPrice);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }


    /**
     * This function get as an input an id of an item, and set its status to be a defect item
     * @param in standard input stream
     */
    public void updateItemStatus(Scanner in){
        int productId;
        int itemId;
        int numOfDefects;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert the item id [number]: ");
        itemId = readInteger(in);

        System.out.print("insert the number of defected items in the batch [number]: ");
        numOfDefects = readInteger(in);

        result= Inventory.getInstance().setDefect(productId, itemId, numOfDefects);
        if(result.getErrorMsg()!=null)
            System.out.println(result.getErrorMsg());
    }

    /**
     * This function updates the location of an item
     * @param in standard input stream
     */
    public void updateItemLocation(Scanner in){
        int productId;
        int itemId;
        String location;
        Result result;

        System.out.print("insert the product id [number]: ");
        productId = readInteger(in);

        System.out.print("insert the item id [number]: ");
        itemId = readInteger(in);

        System.out.print("insert a new location [String]: ");
        location= readLine(in);;

        result= Inventory.getInstance().updateItemLocation(productId, itemId, location);
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
