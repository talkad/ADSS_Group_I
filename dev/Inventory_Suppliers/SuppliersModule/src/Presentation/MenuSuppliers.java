package Presentation;

import Interface.ArrangementMenu;
import Interface.OrderMenu;
import Interface.SupplierMenu;

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


    public void runMenu(Scanner scanner){
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
            runMenu(scanner);
            return;
        }
        if (choice<1 || choice >5){
            System.out.println("Please Enter a number Between 1 and 5");
            runMenu(scanner);
            return;
        }
        switch (choice){
            case 1:
                SupplierMenu.runMenu();
                break;
            case 2:
               ArrangementMenu.runMenu();
                break;
            case 3:
              OrderMenu.runMenu();
                break;
            case 4:
                exit(0);
                break;
        }


    }

}
