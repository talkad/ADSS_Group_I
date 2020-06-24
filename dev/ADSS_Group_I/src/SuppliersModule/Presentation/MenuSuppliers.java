package SuppliersModule.Presentation;


import SuppliersModule.Interface.ArrangementMenu;
import SuppliersModule.Interface.OrderMenu;
import SuppliersModule.Interface.SupplierMenu;

import java.util.Scanner;

import static java.lang.System.exit;


public class MenuSuppliers {

    public static MenuSuppliers Menu = null;

    /**
     *
     * @return an instance of Controller
     */
    public static MenuSuppliers getInstance(){
        if(Menu == null){
            Menu = new MenuSuppliers();
        }
        return Menu;
    }


    public void runMenu(Scanner scanner, boolean storeManager){
        System.out.println("=====Main-Menu=====\n" +
                "Choose a function:\n" +
                "1.Manage Suppliers\n" +
                "2.Manage Arrangements\n" +
                "3.Manage Orders\n" +
                "4. exit\n");
        int choice;
        try {
            choice = scanner.nextInt();
        }
        catch (Exception e){
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu(scanner, storeManager);
            return;
        }
        if (choice<1 || choice >5){
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu(scanner, storeManager);
            return;
        }
        switch (choice){
            case 1:
                SupplierMenu.runMenustoreManager();
                break;
            case 2:
                ArrangementMenu.runMenu(storeManager);
                break;
            case 3:
                OrderMenu.runMenu(storeManager);
                break;
            case 4:
                exit(0);
                break;
        }


    }

}
