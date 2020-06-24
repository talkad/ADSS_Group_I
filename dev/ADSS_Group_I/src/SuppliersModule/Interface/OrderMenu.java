package SuppliersModule.Interface;

import SuppliersModule.Buisness.Order;
import SuppliersModule.Buisness.SupplierCard;
import SuppliersModule.Buisness.SupplierManager;
import SuppliersModule.Presentation.MenuSuppliers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrderMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void runMenu(boolean storeManager){
        System.out.println("=====Order-Menu=====\n" +
                "Please enter a Supplier's Company Id:\n");
        int companyID;
        try {
            companyID = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("A Supplier's Company Id is an integer!");
            runMenu(storeManager);
            return;
        }
        if (!SupplierManager.getSuppliers().containsKey(companyID)){
            System.out.println("error:404 - Supplier not found!");
            MenuSuppliers.getInstance().runMenu(scanner, storeManager);
            return;
        }

        String [] commands = {"Please choose a function:\n",
                "1.Place Order",
                "2.Modify Order",
                "3.Cancel Order",
                "4.Print Order History",
                "5.Print Order Details",
                "6.Return to Main Menu"};
        MenuHandler.getInstance().handleOrderMenu(commands, companyID,storeManager);

    }
    public static void placeOrder(int companyID, boolean storeManager){
        int supermarketId = -1;
        System.out.println("Please enter the supermarket ID of the supermarket branch you wish to order to\n");
        try {
            supermarketId = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("A Supermarket Id is an integer!");
            runMenu(storeManager);
            return;
        }
        SupplierCard supplier = SupplierManager.getSuppliers().get(companyID);
        Map<Integer,Integer> map = parseItems("Please enter the items you wish to order");
        if (map == null){
            System.out.println("Wrong input supplied, place order failed. returning to order menu");
            runMenu(storeManager);
            return;
        }
        System.out.println("Please enter the date of the order in the following format: DD/MM/YYYY\n");
        String date = scanner.next();
        LocalDate dateTime;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateTime = LocalDate.parse(date, formatter);
        }
        catch (Exception e){
            System.out.println("Input was not of the right format!\n" +
                    "returning to Order Menu\n");
            runMenu(storeManager);
            return;
        }
        if(supplier.placeOrder(map,dateTime,supermarketId))
            System.out.println("Order placed successfully!");
        else{
            System.out.println("One or more of the items specified are not part of the arrangement with the supplier\n" +
                    "Placing order failed, returning to Order Menu\n");
            runMenu(storeManager);
            return;
        }
    }
    public static void printOrderHistory(int companyID, boolean storeManager){
        System.out.println("Order Number | Date Created | Order Date | Status");
        System.out.println("-------------------------------------------------");

        int comp = 0;
        int length = 0;
        for (Order order: SupplierManager.getSuppliers().get(companyID).getOrders().values()) {
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
        runMenu(storeManager);
    }
    public static void cancelOrder(int companyID, boolean storeManager){
        System.out.println("Please enter the number of the order you would like to cancel");
        Scanner scanner = new Scanner((System.in));
        int choice;
        String Id;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("an order number is an Integer");
            runMenu(storeManager);
            return;
        }
        SupplierCard supplier = SupplierManager.getSuppliers().get(companyID);
        if (!supplier.getOrders().containsKey(choice)){
            System.out.println("error:404 - Order not found!");
            runMenu(storeManager);
        }
        System.out.println("Please enter the ID of the Human Resource Manager that authorized the cancellation\n");
        Id = scanner.next();
        if (true) //TODO  talk to Employees
        {
        System.out.println("the Id supplier does not correspond with any Human Resource Managers");
        runMenu(storeManager);
        return;
        }
        System.out.println("Please enter the ID of the Logistic Manager that authorized the cancellation\n");
        Id = scanner.next();
        if (true) // TODO talk to Employees
        {
            System.out.println("the Id supplier does not correspond with any Logistic Managers");
            runMenu(storeManager);
            return;
        }
        System.out.println("Please enter the ID of the Warehouse Manager that authorized the cancellation\n");
        Id = scanner.next();
        if (true) // TODO talk to Employees
        {
            System.out.println("the Id supplier does not correspond with any Warehouse Managers");
            runMenu(storeManager);
            return;
        }
        else{
            supplier.cancelOrder(choice);
            System.out.println("Order canceled successfully!");
            MenuSuppliers.getInstance().runMenu(scanner, storeManager);
        }
        return;
    }
    public static void runModifyOrderMenu(int companyID, boolean storeManager){
        System.out.println("Please enter the number of the order you would like to modify");
        int orderNumber;
        try {
            orderNumber = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("An order number is an integer!");
            runMenu(storeManager);
            return;
        }
        if (!SupplierManager.getSuppliers().get(companyID).getOrders().containsKey(orderNumber)){
            System.out.println("error:404 - order not found!");
            runMenu(storeManager);
            return;
        }

        String [] commands = {"Please choose a function:",
                "1.Change Order Date",
                "2.Add items to order",
                "3.Remove Items from order",
                "4.Return to Main Menu"};
        MenuHandler.getInstance().handleModifyOrderMenu(commands, companyID, orderNumber, storeManager);
    }

    public static void changeOrderDate(int companyID, int order, boolean storeManager){
        System.out.println("Please enter the new date in the following format: DD/MM/YYYY");
        String dateInput = scanner.next();
        LocalDate datetime;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            datetime = LocalDate.parse(dateInput, formatter);
        }
        catch (Exception e){
            System.out.println("Input was not of the right format!\n" +
                    "returning to Order Menu\n");
            runMenu(storeManager);
            return;
        }
        SupplierManager.getSuppliers().get(companyID).changeOrderDate(order, datetime);
        System.out.println("Order date changed successfully!");
    }
    public static HashMap<Integer,Integer> parseItems(String message) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        int i = 0;
        boolean amountFlag = false;
        System.out.println(message + " in the following format:\n" +
                "itemID-amount:itemID-amount:...");
        String order = scanner.next();
        for(String itemBlock: order.split(":")){
            String[] item = itemBlock.split("-");
            try {
                map.put(Integer.parseInt(item[0]), Integer.parseInt(item[1]));
            }
            catch (Exception e){
                return null;
            }
        }
        return map;
    }

    public static void addItems(int companyID, int orderNumber, boolean storeManager){
        HashMap<Integer,Integer> map = parseItems("Please enter the items you wish to add to the order");
        if (map == null){
            System.out.println("Wrong input supplied, adding items failed. returning to order menu");
            runMenu(storeManager);
            return;
        }
        if (SupplierManager.getSuppliers().get(companyID).addItemsToOrder(map, orderNumber)){
            System.out.println("Items added successfully!");
            return;
        }
        else{
            System.out.println("One of the specified items is not a part of the arrangement with this supplier\n" +
                    "Adding items failed, returning to Order Menu\n");
            runMenu(storeManager);
            return;
        }
    }
    public static void removeItems(int companyID, int orderNumber, boolean storeManager){
        HashMap<Integer,Integer> map = parseItems("Please enter the items you wish to remove from the order");
        if (map == null){
            System.out.println("Wrong input supplied, removing items failed. returning to order menu");
            runMenu(storeManager);
        }
        else if (SupplierManager.getSuppliers().get(companyID).deleteItemsFromOrder(map, orderNumber)){
            System.out.println("Items removed successfully!");
        }
        else{
            System.out.println("One of the specified items is not a part of the order specified or attempted to remove more than ordered\n" +
                    "Adding items failed, returning to Order Menu\n");
            runMenu(storeManager);
        }
    }
    public static void printOrderDetails(int companyID, boolean storeManager){
        System.out.println("Please enter the number of the order you would like to print");
        int orderNumber;
        try {
            orderNumber = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("An order number is an integer!");
            runMenu(storeManager);
            return;
        }
        if (!SupplierManager.getSuppliers().get(companyID).getOrders().containsKey(orderNumber)){
            System.out.println("error:404 - order not found!");
            runMenu(storeManager);
            return;
        }
        Order order = SupplierManager.getSuppliers().get(companyID).getOrders().get(orderNumber);
        System.out.println("Order Number: " + order.getOrderNum());
        System.out.println("Date Created: " + order.getDateCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Arrive Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Order Status: " + order.getStatus());
        System.out.println("Items Ordered(Id | Amount):");
        for (int key: order.getItemList().keySet()) {
            System.out.println("\t\t\t"+ key + " | " + order.getItemList().get(key));
        }
        System.out.println("\n");
    }
}
