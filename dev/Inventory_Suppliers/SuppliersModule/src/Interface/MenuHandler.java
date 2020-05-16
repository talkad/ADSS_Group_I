package Interface;

import Buisness.SuperLi;
import Presentation.Menu;

import java.util.Scanner;

public class MenuHandler {
    static Scanner scanner;

    public static int handleChoice (String [] commands){
        for (String command : commands) System.out.println(command);
        int choice = -1;
        try {
            scanner = new Scanner(System.in);
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("Bad input, please try again");
            SupplierMenu.runMenu();
        }
        if(!(choice>=1 & choice<commands.length-1)){
            System.out.println("Bad input, please try again");
            SupplierMenu.runMenu();
        }
        return choice;
    }

    public static void handleSupplierMenu(String [] commands){
        int choice = handleChoice(commands);

        switch (choice){
            case 1:
                SupplierMenu.addSupplier();
                break;
            case 2:
                SupplierMenu.deleteSupplier();
                break;
            case 3:
                SupplierMenu.runEditSupplierMenu();
                break;
            case 4:
                Menu.runMenu();
                break;
        }
    }

    public static void handleEditSupplierMenu(String [] commands, int companyID) {
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                SupplierMenu.runContactMenu(companyID);
                break;
            case 2:
                SupplierMenu.changePayment(companyID);
                break;
            case 3:
                SupplierMenu.changeBank(companyID);
                break;
            case 4:
                System.out.println(SuperLi.getSuppliers().get(companyID).toString());
                break;
            case 5:
                Menu.runMenu();
                break;
        }
    }

    public static void handleContactMenu(String [] commands, int companyID) {
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                SupplierMenu.addContact(companyID);
                break;
            case 2:
                SupplierMenu.deleteContact(companyID);
                break;
            case 3:
                SupplierMenu.runEditContactMenu(companyID);
                break;
            case 4:
                System.out.println(SuperLi.getSuppliers().get(companyID).contactsToString());
                break;
            case 5:
                Menu.runMenu();
                break;
        }
    }

    public static void handleEditContactMenu(String [] commands, int companyID, String contactName) {
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                SupplierMenu.addMethod(companyID,contactName);
                break;
            case 2:
                SupplierMenu.deleteMethod(companyID,contactName);
                break;
            case 3:
                SupplierMenu.editMethod(companyID,contactName);
                break;
            case 4:
                SupplierMenu.runContactMenu(companyID);
        }
    }

    public static void handleArrangementMenu(String [] commands, int companyID){
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                ArrangementMenu.runQuantityAgreementMenu(companyID);
                break;
            case 2:
                ArrangementMenu.runItemMenu(companyID);
                break;
            case 3:
                ArrangementMenu.runDeliveryDateMenu(companyID);
                break;
            case 4:
                System.out.println(SuperLi.getSuppliers().get(companyID).getArrangement().toString());
                break;
            case 5:
                Menu.runMenu();
                break;
        }
    }

    public static void handleItemMenu(String [] commands, int companyID){
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                ArrangementMenu.addItems(companyID);
                break;
            case 2:
                ArrangementMenu.removeItems(companyID);
                break;
              case 3:
                ArrangementMenu.changePrice(companyID);
                break;
             case 4:
                ArrangementMenu.runMenu();
                break;
         }
    }

    public static void handleDeliverDateMenu(String [] commands, int companyID){
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                System.out.println(SuperLi.getSuppliers().get(companyID).getArrangement().pastDateToString());
                break;
            case 2:
                System.out.println(SuperLi.getSuppliers().get(companyID).getArrangement().futureDateToString());
                break;
            case 3:
                ArrangementMenu.modifyDate(companyID);
                break;
            case 4:
                ArrangementMenu.runMenu();
                break;
        }
    }

    public static void handleQuantityAgreementMenu(String [] commands, int companyID) {
        int choice = handleChoice(commands);
        switch (choice) {
            case 1:
                ArrangementMenu.addQuantityAgreement(companyID);
                break;
            case 2:
                ArrangementMenu.addItemsToQuantity(companyID);
                break;
            case 3:
                ArrangementMenu.removeItemsFromQuantity(companyID);
                break;
            case 4:
                ArrangementMenu.changePriceInAgreement(companyID);
                break;
            case 5:
                ArrangementMenu.runMenu();
                break;
        }
    }

    public static void handleOrderMenu(String [] commands, int companyID) {
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                OrderMenu.placeOrder(companyID);
                break;
            case 2:
                OrderMenu.runModifyOrderMenu(companyID);
                break;
            case 3:
                OrderMenu.cancelOrder(companyID);
                break;
            case 4:
                OrderMenu.printOrderHistory(companyID);
                break;
            case 5:
                OrderMenu.printOrderDetails(companyID);
                break;
            case 6:
                Menu.runMenu();
                break;
        }
    }

    public static void handleModifyOrderMenu(String [] commands, int companyID, int orderNumber) {
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                OrderMenu.changeOrderDate(companyID, orderNumber);
                break;
            case 2:
                OrderMenu.addItems(companyID, orderNumber);
                break;
            case 3:
                OrderMenu.removeItems(companyID, orderNumber);
                break;
            case 4:
                OrderMenu.runMenu();
                return;
        }
    }
}
