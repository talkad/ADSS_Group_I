package Interface;

import SuppliersModule.Business.SupplierManager;
import SuppliersModule.Presentation.MenuSuppliers;

import java.util.Scanner;

public class MenuHandler {

    private Scanner scanner;

    public static MenuHandler Menu = null;

    /**
     *
     * @return an instance of Controller
     */
    public static MenuHandler getInstance(){
        if(Menu == null){
            Menu = new MenuHandler();
        }
        return Menu;
    }


    public int handleChoice (String [] commands){
        for (String command : commands) System.out.println(command);
        int choice = -1;
        try {
            scanner = new Scanner(System.in);
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("Bad input, please try again");
            MenuSuppliers.getInstance().runMenu(scanner);
        }
        if(!(choice>=1 & choice<=commands.length-1)){
            System.out.println("Bad input, please try again");
            MenuSuppliers.getInstance().runMenu(scanner);
        }
        return choice;
    }

    public void handleSupplierMenu(String [] commands){
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
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleEditSupplierMenu(String [] commands, int companyID) {
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
                System.out.println(SupplierManager.getSuppliers().get(companyID).toString());
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleContactMenu(String [] commands, int companyID) {
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
                System.out.println(SupplierManager.getSuppliers().get(companyID).contactsToString());
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleEditContactMenu(String [] commands, int companyID, String contactName) {
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

    public void handleArrangementMenu(String [] commands, int companyID){
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
                System.out.println(SupplierManager.getSuppliers().get(companyID).getArrangement().toString());
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleItemMenu(String [] commands, int companyID){
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                ArrangementMenu.addItems(companyID);
                break;
            case 2:
                ArrangementMenu.removeItems(companyID);
                break;
            case 3:
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
         }
    }

    public void handleDeliverDateMenu(String [] commands, int companyID){
        int choice = handleChoice(commands);
        switch (choice){
            case 1:
                System.out.println(SupplierManager.getSuppliers().get(companyID).getArrangement().pastDateToString());
                break;
            case 2:
                System.out.println(SupplierManager.getSuppliers().get(companyID).getArrangement().futureDateToString());
                break;
            case 3:
                ArrangementMenu.modifyDate(companyID);
                break;
            case 4:
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleQuantityAgreementMenu(String [] commands, int companyID) {
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
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleOrderMenu(String [] commands, int companyID) {
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
                MenuSuppliers.getInstance().runMenu(scanner);
                break;
        }
    }

    public void handleModifyOrderMenu(String [] commands, int companyID, int orderNumber) {
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
                MenuSuppliers.getInstance().runMenu(scanner);
                return;
        }
    }
}
