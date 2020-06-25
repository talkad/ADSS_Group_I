package Interface.Menu;

import DeliveryModule.PresentationLayer.Start;
import EmployeeModule.InterfaceLayer.Service;
import Interface.Bussiness_Connector.Connector;
import InventoryModule.PresentationLayer.InventoryMenu;
import SuppliersModule.Interface.OrderMenu;
import SuppliersModule.Presentation.MenuSuppliers;
import org.omg.CORBA.portable.ApplicationException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Menu {

    private static InventoryMenu inventoryMenu = InventoryMenu.getInstance();
    private static MenuSuppliers suppliersMenu = MenuSuppliers.getInstance();
    private static Service employeeMenu = Service.getInstance();
    private static Start deliveryMenu = new Start();
    protected static int login = -1;


    /**
     * This function displays the menu to the standard output stream
     */
    public static void displayMenu(String[] options){
        System.out.println("\nchoose action:");
        for(int i=0; i<options.length; i++)
            System.out.println((i+1) +") "+ options[i]);

        System.out.print("Selection: ");
    }

    public static int isNumeric(String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e){
            return -1;
        }
    }

    private static boolean isValidLogin(){
        if(login < 0){
            System.out.println("A valid employee must be logged in");
            return false;
        }
        else return true;
    }

    private static void login(Scanner scanner) {
        System.out.println("Insert employee id: ");
        login = isNumeric(scanner.nextLine());
        if(login > -1){
            try {
                if(employeeMenu.searchEmployee(login))
                    System.out.println("Successfully logged in");
                else login = -1;
            } catch (ApplicationException e){
                System.out.println(e.getId());
            }
        }
        else{
            System.out.println("Employee doesn't exists in the System");
        }
    }

    public static void main(String[] args) throws NumberFormatException, IOException, ParseException, ApplicationException {
        Scanner scanner = new Scanner(System.in);
        String[] options = new String[]
                {"Employee menu", "Inventory menu", "Suppliers menu", "Delivery menu", "login",  "exit"};
        String input;
        boolean quit = false;
        Connector.Load();

        while(!quit){
            displayMenu(options);
            input=scanner.nextLine();

            switch (input) {
                case ("1"):
                    try {
                        if (isValidLogin() && employeeMenu.hasRole(login, "hr manager")) {
                            employeeMenu.display(scanner);
                        }
                    } catch (ApplicationException e){
                        System.out.println(e.getId());
                    }
                    break;
                case ("2"):
                    try {
                        if (isValidLogin() && employeeMenu.hasRole(login, "storekeeper")) {
                            inventoryMenu.display(scanner);
                        }
                    } catch (ApplicationException e){
                        System.out.println(e.getId());
                    }
                    break;
                case ("3"):
                    boolean flag = false;
                        System.out.println(login);
                        try {
                            if (isValidLogin() && employeeMenu.hasRole(login, "storekeeper")) {
                                suppliersMenu.runMenu(scanner, false);
                            }
                        } catch (ApplicationException ignored){ flag = true; }
                        if(flag){
                            try {
                                if (isValidLogin() && employeeMenu.hasRole(login, "store manager")) {
                                	suppliersMenu.runMenu(scanner,true);
                                }
                            } catch (ApplicationException e){
                                System.out.println(e.getId());
                            }
                        }
                    break;
                case ("4"):
                    try {
                        if (isValidLogin() && employeeMenu.hasRole(login, "logistics manager")) {
                            deliveryMenu.StartDelivery();
                        }
                    } catch (ApplicationException e){
                        System.out.println(e.getId());
                    }//TODO NEEDS TO BE DELIVERY MENU
                    break;
                case("5"):
                    login(scanner);
                    break;
                case ("6"):
                    System.out.println ( "Bye..." );
                    quit = true;
                    break;
                default:
                    System.out.println ( "Unrecognized option" );
                    break;
            }
        }
    }


}
