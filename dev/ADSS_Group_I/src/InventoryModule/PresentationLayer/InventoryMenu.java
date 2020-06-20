package InventoryModule.PresentationLayer;

import Menu.Menu;

import java.util.Scanner;

public class InventoryMenu {

    private static InventoryMenu instance;

    /**
     * @return an instance of InventoryMenu
     */
    public static InventoryMenu getInstance(){
        if(instance == null){
            instance = new InventoryMenu();
        }
        return instance;
    }

    public void display(Scanner in){
        String[] mainMenu = new String[] {"Add to Inventory", "Remove from Inventory", "Update Inventory",
                "Inventory reports", "Orders menu", "Return to main menu"};

        String[] addMenu = new String[] {"Add product", "Add item"};
        String[] removeMenu = new String[] {"Remove product", "Remove item"};
        String[] updateMenu = new String[] {"Update min quantity", "Update selling price", "Update buying price",
        "Update item status", "Update item location"};
        String[] reports = new String[] {"Get categories reports", "Get defects report"};
        String[] orders = new String[] {"Set periodic order", "Set periodic order date", "Confirm order"};

        boolean shouldTerminate=false;
        int input;

        while(!shouldTerminate){
            Menu.displayMenu(options);
            input = Menu.getInputIndex(in);

            switch (input) {
                case 1:
                    Controller.getInstance().addProduct(in);
                    break;
                case 2:
                    Controller.getInstance().removeProduct(in);
                    break;
                case 3:
                    Controller.getInstance().addItem(in);
                    break;
                case 4:
                    Controller.getInstance().removeItem(in);
                    break;
                case 5:
                    Controller.getInstance().updateMinQuantity(in);
                    break;
                case 6:
                    Controller.getInstance().updateSellingPrice(in);
                    break;
                case 7:
                    Controller.getInstance().updateBuyingPrice(in);
                    break;
                case 8:
                    Controller.getInstance().updateItemStatus(in);
                    break;
                case 9:
                    Controller.getInstance().updateItemLocation(in);
                    break;
                case 10:
                    Controller.getInstance().getCategoriesReport(in);
                    break;
                case 11:
                    Controller.getInstance().getDefectsReports(in);
                    break;
                case 12:
                    Controller.getInstance().setPeriodicOrder(in);
                    break;
                case 13:
                    Controller.getInstance().setPeriodicOrderDate(in);
                    break;
                case 14:
                    Controller.getInstance().loadInventory(in);
                    break;
                case 15:
                    shouldTerminate=true;
                    break;
                default:
                    System.out.println ( "Unrecognized option" );
                    break;
            }
        }

    }
}
