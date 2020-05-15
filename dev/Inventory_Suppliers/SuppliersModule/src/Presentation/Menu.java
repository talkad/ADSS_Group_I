package Presentation;

import Interface.ArrangementMenu;
import Interface.OrderMenu;
import Interface.SupplierMenu;
import setUp.SetUpArguments;

import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    public static void runMenu(){
        System.out.println("=====Main-Menu=====\n" +
                "Choose a function:\n" +
                "1.Manage Suppliers\n" +
                "2.Manage Arrangements\n" +
                "3.Manage Orders\n" +
                "4.Set up all information\n" +
                "5.Exit\n");
        Scanner scanner = new Scanner(System.in);
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
                SupplierMenu.runMenu();
                break;
            case 2:
               ArrangementMenu.runMenu();
                break;
            case 3:
              OrderMenu.runMenu();
                break;
            case 4:
                SetUpArguments.setup();
                runMenu();
                break;
            case 5:
                exit(0);
        }


    }
}
