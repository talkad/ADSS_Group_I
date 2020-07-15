package SuppliersModule.Interface;

import SuppliersModule.Buisness.SupplierManager;
import SuppliersModule.Presentation.MenuSuppliers;

import java.util.Scanner;

import org.omg.CORBA.portable.ApplicationException;

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


    public int handleChoice (String [] commands, boolean storeManager) throws ApplicationException {
        for (String command : commands) System.out.println(command);
        int choice = -1;
        try {
            scanner = new Scanner(System.in);
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("Bad input, please try again");
            MenuSuppliers.getInstance().runMenu(scanner, storeManager);
        }
        if(!(choice>=1 & choice<=commands.length-1)){
            System.out.println("Bad input, please try again");
            MenuSuppliers.getInstance().runMenu(scanner, storeManager);
        }
        return choice;
    }

    public void handleSupplierMenu(String [] commands, boolean storeManager) throws ApplicationException{
        int choice = handleChoice(commands, storeManager);

        switch (choice){
            case 1:
                SupplierMenu.addSupplier(storeManager);
                break;
            case 2:
                SupplierMenu.deleteSupplier(storeManager);
                break;
            case 3:
                SupplierMenu.runEditSupplierMenu(storeManager);
                break;
            case 4:
                MenuSuppliers.getInstance().runMenu(scanner, storeManager);
                break;
        }
    }

    public void handleEditSupplierMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                SupplierMenu.runContactMenu(companyID, storeManager);
                break;
            case 2:
                SupplierMenu.changePayment(companyID,storeManager);
                break;
            case 3:
                SupplierMenu.changeBank(companyID,storeManager);
                break;
            case 4:
                System.out.println(SupplierManager.getSuppliers().get(companyID).toString());
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                break;
        }
    }

    public void handleContactMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                SupplierMenu.addContact(companyID,storeManager);
                break;
            case 2:
                SupplierMenu.deleteContact(companyID,storeManager);
                break;
            case 3:
                SupplierMenu.runEditContactMenu(companyID,storeManager);
                break;
            case 4:
                System.out.println(SupplierManager.getSuppliers().get(companyID).contactsToString());
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner, storeManager);
                break;
        }
    }

    public void handleEditContactMenu(String [] commands, int companyID, String contactName, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                SupplierMenu.addMethod(companyID,contactName, storeManager);
                break;
            case 2:
                SupplierMenu.deleteMethod(companyID,contactName);
                break;
            case 3:
                SupplierMenu.editMethod(companyID,contactName,storeManager);
                break;
            case 4:
                SupplierMenu.runContactMenu(companyID,storeManager);
        }
    }

    public void handleArrangementMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                ArrangementMenu.runQuantityAgreementMenu(companyID,storeManager);
                break;
            case 2:
                ArrangementMenu.runItemMenu(companyID,storeManager);
                break;
            case 3:
                ArrangementMenu.runDeliveryDateMenu(companyID,storeManager);
                break;
            case 4:
                System.out.println(SupplierManager.getSuppliers().get(companyID).getArrangement().toString());
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                break;
        }
    }

    public void handleItemMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                ArrangementMenu.addItems(companyID,storeManager);
                break;
            case 2:
                ArrangementMenu.removeItems(companyID,storeManager);
                break;
            case 3:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                break;
         }
    }

    public void handleDeliverDateMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                System.out.println(SupplierManager.getSuppliers().get(companyID).getArrangement().pastDateToString());
                break;
            case 2:
                System.out.println(SupplierManager.getSuppliers().get(companyID).getArrangement().futureDateToString());
                break;
            case 3:
                ArrangementMenu.modifyDate(companyID,storeManager);
                break;
            case 4:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                break;
        }
    }

    public void handleQuantityAgreementMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice) {
            case 1:
                ArrangementMenu.addQuantityAgreement(companyID,storeManager);
                break;
            case 2:
                ArrangementMenu.addItemsToQuantity(companyID,storeManager);
                break;
            case 3:
                ArrangementMenu.removeItemsFromQuantity(companyID,storeManager);
                break;
            case 4:
                ArrangementMenu.changePriceInAgreement(companyID,storeManager);
                break;
            case 5:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                break;
        }
    }

    public void handleOrderMenu(String [] commands, int companyID, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                if (storeManager) {
                    System.out.println("a store manager cannot edit orders!\n");
                    break;
                }
                OrderMenu.placeOrder(companyID, storeManager);
                break;
            case 2:
                if (storeManager) {
                    System.out.println("a store manager cannot edit orders!\n");
                    break;
                }
                OrderMenu.runModifyOrderMenu(companyID,storeManager);
                break;
            case 3:
                if (storeManager) {
                    System.out.println("a store manager cannot edit orders!\n");
                    break;
                }
                OrderMenu.cancelOrder(companyID, storeManager);
                break;
            case 4:
                OrderMenu.printOrderHistory(companyID,storeManager);
                break;
            case 5:
                OrderMenu.printOrderDetails(companyID,storeManager);
                break;
            case 6:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                break;
        }
    }

    public void handleModifyOrderMenu(String [] commands, int companyID, int orderNumber, boolean storeManager) throws ApplicationException {
        int choice = handleChoice(commands, storeManager);
        switch (choice){
            case 1:
                OrderMenu.changeOrderDate(companyID, orderNumber,storeManager);
                break;
            case 2:
                OrderMenu.addItems(companyID, orderNumber,storeManager);
                break;
            case 3:
                OrderMenu.removeItems(companyID, orderNumber,storeManager);
                break;
            case 4:
                MenuSuppliers.getInstance().runMenu(scanner,storeManager);
                return;
        }
    }
}
