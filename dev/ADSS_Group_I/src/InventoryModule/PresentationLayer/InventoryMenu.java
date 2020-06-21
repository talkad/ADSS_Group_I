package InventoryModule.PresentationLayer;


import Interface.Menu.Menu;

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

    private int getInputIndex(Scanner in) {
        int input = 0;
        try {
            input = in.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input- this is not a number.");
        }
        return input;
    }

    private void displayMenu(String[] options){
        System.out.println("\nMenu- choose an index:");
        for(int i=0; i<options.length; i++)
            System.out.println((i+1) +". "+ options[i]);

        System.out.print("Selection: ");

    }

    public void display(Scanner in){
        String[] mainMenu = new String[] {"Add to Inventory", "Remove from Inventory", "Update Inventory",
                "Inventory reports", "Orders menu", "Return to main menu"};
        String[] addMenu = new String[] {"Add product", "Add item"};
        String[] removeMenu = new String[] {"Remove product", "Remove item"};
        String[] updateMenu = new String[] {"Update min quantity", "Update selling price", "Update buying price",
        "Update item status", "Update item location"};
        String[] reportsMenu = new String[] {"Get categories reports", "Get defects report"};
        String[] ordersMenu = new String[] {"Set periodic order", "Set periodic order date", "Confirm order"};

        boolean shouldTerminate = false;
        int input;

        while(!shouldTerminate){

            Menu.displayMenu(mainMenu);
            input = getInputIndex(in);
            System.out.println();

            switch (input) {
                case 1:
                    displayMenu(addMenu);
                    input = getInputIndex(in);
                    System.out.println();

                    switch (input) {
                        case 1:
                            Controller.getInstance().addProduct(in);
                            break;
                        case 2:
                            Controller.getInstance().addItem(in);
                            break;
                        default:
                            System.out.println ( "Unrecognized option" );
                            break;
                    }
                    break;
                case 2:
                    displayMenu(removeMenu);
                    input = getInputIndex(in);
                    System.out.println();

                    switch (input) {
                        case 1:
                            Controller.getInstance().removeProduct(in);
                            break;
                        case 2:
                            Controller.getInstance().removeItem(in);
                            break;
                        default:
                            System.out.println ( "Unrecognized option" );
                            break;
                    }
                    break;
                case 3:
                    displayMenu(updateMenu);
                    input = getInputIndex(in);
                    System.out.println();

                    switch (input) {
                        case 1:
                            Controller.getInstance().updateMinQuantity(in);
                            break;
                        case 2:
                            Controller.getInstance().updateSellingPrice(in);
                            break;
                        case 3:
                            Controller.getInstance().updateBuyingPrice(in);
                            break;
                        case 4:
                            Controller.getInstance().updateItemStatus(in);
                            break;
                        case 5:
                            Controller.getInstance().updateItemLocation(in);
                            break;
                        default:
                            System.out.println ( "Unrecognized option" );
                            break;
                    }
                    break;
                case 4:
                    displayMenu(reportsMenu);
                    input = getInputIndex(in);
                    System.out.println();

                    switch (input) {
                        case 1:
                            Controller.getInstance().getCategoriesReport(in);
                            break;
                        case 2:
                            Controller.getInstance().getDefectsReports(in);
                            break;
                        default:
                            System.out.println ( "Unrecognized option" );
                            break;
                    }
                    break;
                case 5:
                    displayMenu(ordersMenu);
                    input = getInputIndex(in);
                    System.out.println();

                    switch (input) {
                        case 1:
                            Controller.getInstance().setPeriodicOrder(in);
                            break;
                        case 2:
                            Controller.getInstance().setPeriodicOrderDate(in);
                            break;
                        case 3:
                            Controller.getInstance().loadInventory(in);
                            break;
                        default:
                            System.out.println ( "Unrecognized option" );
                            break;
                    }
                    break;
                case 6:
                    shouldTerminate=true;
                    break;
                default:
                    System.out.println ("Unrecognized option");
                    break;
            }
        }
    }
}
