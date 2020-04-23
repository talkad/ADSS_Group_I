package Interface;

import Buisness.FixedArrangement;
import Buisness.Item;
import Buisness.SuperLi;
import Presentation.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ArrangementMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void runMenu() {
        System.out.println("=====Buisness.Arrangement-Presentation.Menu=====\n" +
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
            System.out.println("error:404 - Supplier not found");
            runMenu();
            return;
        }

        System.out.println("Please choose a function:\n" +
                "1.Add or Edit Quantity Agreement\n" +
                "2.Modify items\n" +
                "3.Modify Delivery Dates\n" +
                "4.Print Buisness.Arrangement\n" +
                "5.Return to Presentation.Main Presentation.Menu\n");
        int choice;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu();
            return;
        }
        if (choice<1 || choice >5){
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu();
            return;
        }
        switch (choice){
            case 1:
                runQuantityAgreementMenu(companyID);
                break;
            case 2:
                runItemMenu(companyID);
                break;
            case 3:
                runDeliveryDateMenu(companyID);
                break;
            case 4:
                System.out.println(SuperLi.getSuppliers().get(companyID).getArrangement().toString());
                break;
            case 5:
                Menu.runMenu();
                break;
        }
    }
    public static void runItemMenu(int companyID){
        System.out.println("Please choose a function:\n" +
                "1.Add items to the arrangement\n" +
                "2.Remove items from the arrangement\n" +
                "3.Change an item's price\n" +
                "4.Return to Buisness.Arrangement Presentation.Menu\n");
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
                addItems(companyID);
                break;
            case 2:
                removeItems(companyID);
                break;
            case 3:
                changePrice(companyID);
                break;
            case 4:
                runMenu();
                break;
        }
    }

    private static void changePrice(int companyId) {
        System.out.println("Please enter the items you wish to change their price in the arrangement in the following format:\n" +
                "itemID-price:itemID-price:itemID-price:...");
        String input = scanner.next();
        Map<Integer,Double> map = new HashMap<>();
        String[] itemInput = input.split(":");
        for (int i = 0; i < itemInput.length; i++){
            String[] itemVal = itemInput[i].split("-");
            try{
                int itemID = Integer.parseInt(itemVal[0]);
                double price = Double.parseDouble(itemVal[1]);
                if(price <= 0) {
                    System.out.println("price cannot be <= 0\n" +
                            "returning to Buisness.Arrangement Presentation.Menu\n");
                    runMenu();
                    return;
                }
                map.put(itemID, price);
            }
            catch (Exception e){
                System.out.println("Input was not of the right format!\n" +
                        "returning to Buisness.Arrangement Presentation.Menu\n");
                runMenu();
                return;
            }
        }
        SuperLi.getSuppliers().get(companyId).changePriceInAgreement(map);
        System.out.println("Items' prices changed successfully!");
    }

    public static void addItems(int companyId){
        System.out.println("Please enter the items you wish to add to the arrangement in the following format:\n" +
                "itemID-price-name:itemID-price-name:itemID-price-name:...");
        String input = scanner.next();
        ArrayList<Item> list = new ArrayList<Item>();
        String[] itemInput = input.split(":");
        for (int i = 0; i < itemInput.length; i++){
            String[] itemVal = itemInput[i].split("-");
            try{
                int itemID = Integer.parseInt(itemVal[0]);
                double priceInt = Double.parseDouble(itemVal[1]);
                list.add(new Item(itemID,priceInt,itemVal[2],companyId));
            }
            catch (Exception e){
                System.out.println("Input was not of the right format!\n" +
                        "returning to Buisness.Arrangement Presentation.Menu\n");
                runMenu();
                return;
            }
        }
        SuperLi.getSuppliers().get(companyId).addToArrangement(list);
        System.out.println("Items added successfully!");
    }

    public static void removeItems (int commanyId){
        System.out.println("Please enter the items you wish to remove from the arrangement in the following format:\n" +
                "itemID:itemID:itemID:...");
        String input = scanner.next();
        ArrayList<Integer> list = new ArrayList<Integer>();
        String[] itemInput = input.split(":");
        for (int i = 0; i < itemInput.length; i++){
            try{
                int itemID = Integer.parseInt(itemInput[i]);
                list.add(itemID);
            }
            catch (Exception e){
                System.out.println("Input was not of the right format!\n" +
                        "returning to Buisness.Arrangement Presentation.Menu\n");
                runMenu();
                return;
            }
        }
        SuperLi.getSuppliers().get(commanyId).deleteFromArrangement(list);
        System.out.println("Deleted Items successfully");
    }

    public static void runQuantityAgreementMenu(int companyID){
        System.out.flush();
        System.out.println("Please choose a function:\n" +
                "1.Add a quantity agreement\n" +
                "2.Add items' discounts to agreement\n" +
                "3.Remove items from agreement\n" +
                "4.Change items' discounts\n" +
                "5.Return to Buisness.Arrangement Presentation.Menu\n");
        int choice;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.flush();
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu();
            return;
        }
        if (choice<1 || choice >5){
            System.out.flush();
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu();
            return;
        }
        switch (choice){
            case 1:
                addQuantityAgreement(companyID);
                break;
            case 2:
                addItemsToQuantity(companyID);
                break;
            case 3:
                removeItemsFromQuantity(companyID);
                break;
            case 4:
                changePriceInAgreement(companyID);
                break;
            case 5:
                runMenu();
                break;
        }
    }
    public static Map<Integer,Map<Integer,Double>> parseQuantity(String msg){
        System.out.println(msg+ " by this format:\n" +
                "itemID-amount1-price2:amount2-price2...::itemID-amount1-price1:amount2-price2:...");
        String input = scanner.next();
        int currentItemID = 0;
        Map<Integer,Map<Integer,Double>> map = new HashMap<>();
        String[] discountInput = input.split("::");
        for (int i = 0; i < discountInput.length; i++){
            String[] itemVal = discountInput[i].split(":");
            for (int j = 0; j < itemVal.length; j++) {
                String[] data = itemVal[j].split("-");
                if (data.length == 3){
                    try{
                        currentItemID = Integer.parseInt(data[0]);
                        map.put(currentItemID,new HashMap<Integer, Double>());
                    }
                    catch (Exception e){
                        return null;
                    }
                }
                try {
                    int amount = Integer.parseInt(data[data.length-2]);
                    double price = Double.parseDouble(data[data.length-1]);
                    map.get(currentItemID).put(amount,price);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return map;
    }

    public static void addQuantityAgreement(int companyID){
        Map<Integer,Map<Integer,Double>> map = parseQuantity("Please enter the items you with to add to the agreement");
        if (map == null){
            System.out.println("Error - input was not of the right format!\n" +
                    "Returning to arrangement menu");
            runMenu();
            return;
        }
        if (SuperLi.getSuppliers().get(companyID).getArrangement().addNewAgreement(map))
            System.out.println("Quantity agreement added successfully!");
        else
            System.out.println("Adding quantity agreement failed, item is not in the arrangement or price is invalid");
    }

    public static void addItemsToQuantity(int companyID){
        Map<Integer,Map<Integer,Double>> map = parseQuantity("Please enter the items you with to add to the agreement");
        if (map == null){
            System.out.println("Error - input was not of the right format!\n" +
                    "Returning to arrangement menu");
            runMenu();
            return;
        }
        if (SuperLi.getSuppliers().get(companyID).getArrangement().addItemsToAgreement(map))
            System.out.println("Quantity agreement changed successfully!");
        else
            System.out.println("Updating quantity agreement failed, item is not in the arrangement or price is invalid");
    }

    public static void changePriceInAgreement(int companyID){
        Map<Integer,Map<Integer,Double>> map = parseQuantity("Please enter the items whose price you want to update the agreement");
        if (map == null){
            System.out.println("Error - input was not of the right format!\n" +
                    "Returning to arrangement menu");
            runMenu();
            return;
        }
        if (SuperLi.getSuppliers().get(companyID).getArrangement().editItemInAgreement(map))
            System.out.println("Quantity agreement changed successfully!");
        else
            System.out.println("Updating quantity agreement failed, item is not in the arrangement or price is invalid");
    }

    public static void removeItemsFromQuantity(int companyId){
        System.out.println("Please enter the items you wish to remove from the agreement in the following format:\n" +
                "itemID:itemID:itemID:...");
        String input = scanner.next();
        ArrayList<Integer> list = new ArrayList<Integer>();
        String[] itemInput = input.split(":");
        for (int i = 0; i < itemInput.length; i++){
            try{
                int itemID = Integer.parseInt(itemInput[i]);
                list.add(itemID);
            }
            catch (Exception e){
                System.out.println("Input was not of the right format!\n" +
                        "returning to Buisness.Arrangement Presentation.Menu\n");
                runMenu();
                return;
            }
        }
        SuperLi.getSuppliers().get(companyId).getArrangement().deleteItemsFromAgreement(list);
        System.out.println("Deleted Items successfully");
    }

    public static void runDeliveryDateMenu(int companyID){
        System.out.flush();
        System.out.println("Please choose a function:\n" +
                "1.Print past delivery dates\n" +
                "2.Print future delivery dates\n" +
                "3.Modify date\n" +
                "4.Return to Buisness.Arrangement Presentation.Menu\n");
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
                System.out.println(SuperLi.getSuppliers().get(companyID).getArrangement().pastDateToString());
                break;
            case 2:
                System.out.println(SuperLi.getSuppliers().get(companyID).getArrangement().futureDateToString());
                break;
            case 3:
                modifyDate(companyID);
                break;
            case 4:
                runMenu();
                break;
        }
    }

    public static void modifyDate(int companyID){
        if (!(SuperLi.getSuppliers().get(companyID).getArrangement() instanceof FixedArrangement)) {
            System.out.println("Error - cannot modify date of a non fixed arrangement. go to Buisness.Order Presentation.Menu to place orders!\n" +
                    "returning to Buisness.Arrangement Presentation.Menu\n");
            runMenu();
            return;
        }
        else{
            System.out.println("Please enter a new future date in the following format: DD/MM/YYYY");
            String dateInput = scanner.next();
            LocalDate date;
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                date = LocalDate.parse(dateInput, formatter);
            }
            catch (Exception e){
                System.out.println("Input was not of the right format!\n" +
                        "returning to Buisness.Arrangement Presentation.Menu\n");
                runMenu();
                return;
            }
            FixedArrangement arr = (FixedArrangement) SuperLi.getSuppliers().get(companyID).getArrangement();
            if(arr.modifyDate(date)){
                System.out.println("Date updated successfully!");
            }
            else
                System.out.println("Date update failed, date is in the past");
        }

    }

}

