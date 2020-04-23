package Interface;

import Buisness.Order;
import Buisness.SuperLi;
import Buisness.SupplierCard;
import Presentation.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrderMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void runMenu(){
        System.out.println("=====Buisness.Order-Presentation.Menu=====\n" +
                "Please enter a Supplier's Company Id:\n");
        int companyID;
        try {
            companyID = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("A Supplier's Company Id is an integer!");
            runMenu();
            return;
        }
        if (!SuperLi.getSuppliers().containsKey(companyID)){
            System.out.println("error:404 - Supplier not found!");
            runMenu();
            return;
        }

        System.out.println("Please choose a function:\n" +
                "1.Place Buisness.Order\n" +
                "2.Modify Buisness.Order\n" +
                "3.Cancel Buisness.Order\n" +
                "4.Print Buisness.Order History\n" +
                "5.Print Buisness.Order Details\n" +
                "6.Return to Presentation.Main Presentation.Menu\n");
        int choice;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.flush();
            System.out.println("Please Enter a number Between 1 and 6");
            runMenu();
            return;
        }
        if (choice<1 || choice >5){
            System.out.flush();
            System.out.println("Please Enter a number Between 1 and 6");
            runMenu();
            return;
        }
        switch (choice){
            case 1:
                placeOrder(companyID);
                break;
            case 2:
                runModifyOrderMenu(companyID);
                break;
            case 3:
               cancelOrder(companyID);
                break;
            case 4:
                printOrderHistory(companyID);
                break;
            case 5:
                printOrderDetails(companyID);
                break;
            case 6:
                System.out.flush();
                Menu.runMenu();
                break;
        }
    }
    public static void placeOrder(int companyID){
        SupplierCard supplier = SuperLi.getSuppliers().get(companyID);
        Map<Integer,Integer> map = parseItems("Please enter the items you wish to order");
        if (map == null){
            System.out.println("Wrong input supplied, place order failed. returning to order menu");
            runMenu();
            return;
        }
        System.out.println("Please enter the items you wish to order in the following format: DD/MM/YYYY\n");
        String date = scanner.nextLine();
        LocalDate dateTime;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateTime = LocalDate.parse(date, formatter);
        }
        catch (Exception e){
            System.out.println("Input was not of the right format!\n" +
                    "returning to Buisness.Order Presentation.Menu\n");
            runMenu();
            return;
        }
        if(supplier.placeOrder(map,dateTime))
            System.out.println("Buisness.Order placed successfully!");
        else{
            System.out.println("One or more of the items specified are not part of the arrangement with the supplier\n" +
                    "Placing order failed, returning to Buisness.Order Presentation.Menu\n");
            runMenu();
            return;
        }
    }
    public static void printOrderHistory(int companyID){
        System.out.println("Buisness.Order Number | Date Created | Buisness.Order Date | Status");
        System.out.println("-------------------------------------------------");

        int comp = 0;
        int length = 0;
        for (Order order: SuperLi.getSuppliers().get(companyID).getOrders().values()) {
            comp = 13;
            System.out.print(order.getOrderNum());
            length = String.valueOf(order.getOrderNum()).length();
            comp -= length;
            for (int i = 0; i < comp; i++){
                System.out.print(" ");
            }
            System.out.print("|");
            System.out.print("  " + order.getDateCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "  |");
            if (order.getOrderDate() !=null)
                System.out.print(" " + order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " |");
            else
                System.out.print("------------|");
            System.out.println(order.getStatus());
        }

        System.out.println("\n\n");
        runMenu();
    }
    public static void cancelOrder(int companyID){
        System.out.println("Please enter the number of the order you would like to cancel");
        Scanner scanner = new Scanner((System.in));
        int choice;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.flush();
            System.out.println("an order number is an Integer");
            runMenu();
            return;
        }
        SupplierCard supplier = SuperLi.getSuppliers().get(companyID);
        if (!supplier.getOrders().containsKey(choice)){
            System.out.println("error:404 - Buisness.Order not found!");
            runMenu();
        }
        else{
            supplier.cancelOrder(choice);
            System.out.println("Buisness.Order canceled successfully!");
            Menu.runMenu();
        }
        return;
    }
    public static void runModifyOrderMenu(int companyID){
        System.out.println("Please enter the number of the order you would like to modify");
        int orderNumber;
        try {
            orderNumber = scanner.nextInt();
        }
        catch (Exception e){
            System.out.flush();
            System.out.println("An order number is an integer!");
            runMenu();
            return;
        }
        if (!SuperLi.getSuppliers().get(companyID).getOrders().containsKey(orderNumber)){
            System.out.flush();
            System.out.println("error:404 - order not found!");
            runMenu();
            return;
        }
        System.out.println("Please choose a function:\n" +
                "1.Change Buisness.Order Date\n" +
                "2.Add items to order\n" +
                "3.Remove Items from order\n" +
                "4.Return to Buisness.Order Presentation.Menu\n");
        int choice;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.flush();
            System.out.println("Please Enter a number Between 1 and 4");
            runMenu();
            return;
        }
        if (choice<1 || choice >4){
            System.out.flush();
            System.out.println("Please Enter a number Between 1 and 4");
            runMenu();
            return;
        }
        switch (choice){
            case 1:
                changeOrderDate(companyID, orderNumber);
                break;
            case 2:
                addItems(companyID, orderNumber);
                break;
            case 3:
                removeItems(companyID, orderNumber);
                break;
            case 4:
                runMenu();
                return;
        }

    }
    public static void changeOrderDate(int companyID, int order){
        System.out.println("Please enter the new date in the following format: DD/MM/YYYY");
        String dateInput = scanner.nextLine();
        LocalDate datetime;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            datetime = LocalDate.parse(dateInput, formatter);
        }
        catch (Exception e){
            System.out.println("Input was not of the right format!\n" +
                    "returning to Buisness.Order Presentation.Menu\n");
            runMenu();
            return;
        }
        SuperLi.getSuppliers().get(companyID).getOrders().get(order).setOrderDate(datetime);
        System.out.println("Buisness.Order date changed successfully!");
    }
    public static HashMap<Integer,Integer> parseItems(String message){
        HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();
        int i = 0;
        boolean amountFlag = false;
        String item = "",amount = "";
        System.out.println(message + " in the following format:\n" +
                "itemID-amount|itemID-amount|...");
        String order = scanner.next();
        while (i < order.length()){
            if (order.charAt(i) == '-'){
                amountFlag = true;
                i++;
            }
            else if (order.charAt(i) == '|'){
                try{
                    amountFlag = false;
                    int itemID = Integer.parseInt(item);
                    int amountInt = Integer.parseInt(amount);
                    map.put(itemID,amountInt);
                    item = "";
                    amount = "";
                }
                catch (Exception e){
                    System.out.println("Input was not of the right format!\n" +
                            "returning to Buisness.Order Presentation.Menu\n");
                    return null;
                }
                i++;
            }
            else if (amountFlag){
                amount += order.charAt(i);
                i++;
            }
            else{
                item +=order.charAt(i);
                i++;
            }
        }
        return map;
    }
    public static void addItems(int companyID, int orderNumber){
        HashMap<Integer,Integer> map = parseItems("Please enter the items you wish to add to the order");
        if (map == null){
            System.out.println("Wrong input supplied, adding items failed. returning to order menu");
            runMenu();
            return;
        }
        if (SuperLi.getSuppliers().get(companyID).addItemsToOrder(map, orderNumber)){
            System.out.println("Items added successfully!");
            return;
        }
        else{
            System.out.println("One of the specified items is not a part of the arrangement with this supplier\n" +
                    "Adding items failed, returning to Buisness.Order Presentation.Menu\n");
            runMenu();
            return;
        }
    }
    public static void removeItems(int companyID, int orderNumber){
        HashMap<Integer,Integer> map = parseItems("Please enter the items you wish to remove from the order");
        if (map == null){
            System.out.println("Wrong input supplied, removing items failed. returning to order menu");
            runMenu();
            return;
        }
        if (SuperLi.getSuppliers().get(companyID).deleteItemsFromOrder(map, orderNumber)){
            System.out.println("Items removed successfully!");
            return;
        }
        else{
            System.out.println("One of the specified items is not a part of the order specified or attempted to remove more than ordered\n" +
                    "Adding items failed, returning to Buisness.Order Presentation.Menu\n");
            runMenu();
            return;
        }
    }
    public static void printOrderDetails(int companyID){
        System.out.println("Please enter the number of the order you would like to print");
        int orderNumber;
        try {
            orderNumber = scanner.nextInt();
        }
        catch (Exception e){
            System.out.flush();
            System.out.println("An order number is an integer!");
            runMenu();
            return;
        }
        if (!SuperLi.getSuppliers().get(companyID).getOrders().containsKey(orderNumber)){
            System.out.flush();
            System.out.println("error:404 - order not found!");
            runMenu();
            return;
        }
        Order order = SuperLi.getSuppliers().get(companyID).getOrders().get(orderNumber);
        System.out.println("Buisness.Order Number: " + order.getOrderNum());
        System.out.println("Date Created: " + order.getDateCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Arrive Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Buisness.Order Status: " + order.getStatus());
        System.out.println("Items Ordered(Id | Amount):");
        for (int key: order.getItemList().keySet()) {
            System.out.println("\t\t\t"+ key + " | " + order.getItemList().get(key));
        }
        System.out.println("\n");
    }
}
