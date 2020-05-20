package Menu;

import DAL_Connector.DatabaseManager;
import Presentation.MenuSuppliers;
import PresentationLayer.InventoryMenu;

import java.util.Scanner;

public class Menu {

    private static InventoryMenu inventoryMenu = InventoryMenu.getInstance();
    private static MenuSuppliers menu = MenuSuppliers.getInstance();

    /**
     * This function displays the menu to the standard output stream
     */
    public static void displayMenu(String[] options){
        System.out.println("\nMenu- choose an index:");
        for(int i=0; i<options.length; i++)
            System.out.println((i+1) +". "+ options[i]);

        System.out.print("Selection: ");
    }

    /**
     * The function takes care on invalid inputs in order to make the menu algorithm more robust for invalid inputs.
     * @param in get an input stream
     * @return the input number. return 0 if the input wasn't a number.
     */
    public static int getInputIndex(Scanner in){
        int input=0;
        try{
            input= in.nextInt();
        } catch (Exception e){
            System.out.println("Invalid input- this is not a number.");
        }
        return input;
    }


    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        String[] options=new String[]
                {"Inventory menu", "Suppliers menu", "exit"};

        boolean shouldTerminate=false;
        int input;

        while(!shouldTerminate){
            displayMenu(options);
            input=getInputIndex(in);

            switch (input) {
                case 1:
                    inventoryMenu.display(in);
                    break;
                case 2:
                    menu.runMenu(in);
                    break;
                case 3:
                    System.out.println ( "Bye..." );
                    DatabaseManager.getInstance().closeConnection();
                    shouldTerminate=true;
                    break;
                default:
                    System.out.println ( "Unrecognized option" );
                    DatabaseManager.getInstance().closeConnection();
                    shouldTerminate=true;
                    break;
            }
        }
    }
}
