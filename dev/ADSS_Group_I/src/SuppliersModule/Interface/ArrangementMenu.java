package SuppliersModule.Interface;


import SuppliersModule.Buisness.FixedArrangement;
import SuppliersModule.Buisness.SupplierManager;
import SuppliersModule.Presentation.MenuSuppliers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ArrangementMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void runMenu(boolean storeManager) {
        System.out.println("=====Arrangement-Menu=====\n" +
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
            System.out.println("error:404 - Supplier not found");
            MenuSuppliers.getInstance().runMenu(scanner, storeManager);
            return;
        }

       String[] commands = {"Please choose a function:" ,
                "1.Add or Edit Quantity Agreement",
                "2.Modify items",
                "3.Modify Delivery Dates",
                "4.Print Arrangement",
                "5.Return to Main Menu"};
        MenuHandler.getInstance().handleArrangementMenu(commands,companyID,storeManager);
    }
    public static void runItemMenu(int companyID,boolean storeManager){
        String[] commands = {"Please choose a function:",
                "1.Add items to the arrangement",
                "2.Remove items from the arrangement",
                "3.Return to Main Menu"};
        MenuHandler.getInstance().handleItemMenu(commands,companyID,storeManager);
    }


    public static void addItems(int companyId,boolean storeManager){
        System.out.println("Please enter the items you wish to add to the arrangement in the following format:\n" +
                "productID:productId...");
        String input = scanner.next();
        ArrayList<Integer> list = new ArrayList<Integer>();
        String[] itemInput = input.split(":");
        for (int i = 0; i < itemInput.length; i++){
            try{
                int productId = Integer.parseInt(itemInput[i]);
                list.add(productId);
            }
            catch (Exception e){
                System.out.println("Input was not of the right format!\n" +
                        "returning to Arrangement Menu\n");
                runMenu(storeManager);
                return;
            }
        }
        SupplierManager.getSuppliers().get(companyId).addToArrangement(list);
        System.out.println("Items added successfully!");
    }

    public static void removeItems (int commanyId,boolean storeManager){
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
                        "returning to Arrangement Menu\n");
                runMenu(storeManager);
                return;
            }
        }
        SupplierManager.getSuppliers().get(commanyId).deleteFromArrangement(list);
        System.out.println("Deleted Items successfully");
    }

    public static void runQuantityAgreementMenu(int companyID,boolean storeManager){
        String [] commands = {"Please choose a function:" ,
                "1.Add a quantity agreement",
                "2.Add items' discounts to agreement",
                "3.Remove items from agreement",
                "4.Change items' discounts",
                "5.Return to Main Menu"};
        MenuHandler.getInstance().handleQuantityAgreementMenu(commands, companyID,storeManager);
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

    public static void addQuantityAgreement(int companyID,boolean storeManager){
        Map<Integer,Map<Integer,Double>> map = parseQuantity("Please enter the items you with to add to the agreement");
        if (map == null){
            System.out.println("Error - input was not of the right format!\n" +
                    "Returning to arrangement menu");
            runMenu(storeManager);
            return;
        }
        if (SupplierManager.getSuppliers().get(companyID).getArrangement().addNewAgreement(map, companyID))
            System.out.println("Quantity agreement added successfully!");
        else
            System.out.println("Adding quantity agreement failed, item is not in the arrangement, agreement already exists or price is invalid");
    }

    public static void addItemsToQuantity(int companyID,boolean storeManager){
        Map<Integer,Map<Integer,Double>> map = parseQuantity("Please enter the items you with to add to the agreement");
        if (map == null){
            System.out.println("Error - input was not of the right format!\n" +
                    "Returning to arrangement menu");
            runMenu(storeManager);
            return;
        }
        if (SupplierManager.getSuppliers().get(companyID).getArrangement().addItemsToAgreement(map,companyID))
            System.out.println("Quantity agreement changed successfully!");
        else
            System.out.println("Updating quantity agreement failed, item is not in the arrangement or price is invalid");
    }

    public static void changePriceInAgreement(int companyID,boolean storeManager){
        Map<Integer,Map<Integer,Double>> map = parseQuantity("Please enter the items whose price you want to update the agreement");
        if (map == null){
            System.out.println("Error - input was not of the right format!\n" +
                    "Returning to arrangement menu");
            runMenu(storeManager);
            return;
        }
        if (SupplierManager.getSuppliers().get(companyID).getArrangement().editItemInAgreement(map,companyID))
            System.out.println("Quantity agreement changed successfully!");
        else
            System.out.println("Updating quantity agreement failed, item is not in the arrangement or price is invalid");
    }

    public static void removeItemsFromQuantity(int companyId,boolean storeManager){
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
                        "returning to Arrangement Menu\n");
                runMenu(storeManager);
                return;
            }
        }
        SupplierManager.getSuppliers().get(companyId).getArrangement().deleteItemsFromAgreement(list,companyId);
        System.out.println("Deleted Items successfully");
    }

    public static void runDeliveryDateMenu(int companyID,boolean storeManager){
        String [] commands = {"Please choose a function:",
                "1.Print past delivery dates",
                "2.Print future delivery dates",
                "3.Modify date",
                "4.Return to Main Menu"};
        MenuHandler.getInstance().handleDeliverDateMenu(commands, companyID,storeManager);
    }

    public static void modifyDate(int companyID,boolean storeManager){
        if (!(SupplierManager.getSuppliers().get(companyID).getArrangement() instanceof FixedArrangement)) {
            System.out.println("Error - cannot modify date of a non fixed arrangement. go to Order Menu to place orders!\n" +
                    "returning to Arrangement Menu\n");
            runMenu(storeManager);
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
                        "returning to Arrangement Menu\n");
                runMenu(storeManager);
                return;
            }
            FixedArrangement arr = (FixedArrangement) SupplierManager.getSuppliers().get(companyID).getArrangement();
            if(arr.modifyDate(date)){
                System.out.println("Date updated successfully!");
            }
            else
                System.out.println("Date update failed, date is in the past");
        }

    }

}

