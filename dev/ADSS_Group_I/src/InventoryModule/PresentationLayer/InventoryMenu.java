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

    public static int getInputIndex(Scanner in){
        int input=0;
        try{
            input= in.nextInt();
        } catch (Exception e){
            System.out.println("Invalid input- this is not a number.");
        }
        return input;
    }

    public void display(Scanner in){
        String[] options=new String[]
                {"Add a new product", "Remove a product", "Add a new item", "Remove an item",
                        "Update minimum quantity", "Update selling price", "Update buying price", "Set defect",
                        "Update item location", "Get report by categories", "Get defect report",
                        "Set periodic order", "Set periodic order date", "load order into inventory", "Go to main menu"};

        boolean shouldTerminate = false;
        int input;

        while(!shouldTerminate){
            Menu.displayMenu(options);
            input = getInputIndex(in);

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
                    Controller.getInstance().getDefectsReports();
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
                    System.out.println ("Unrecognized option");
                    break;
            }
        }

    }




}
