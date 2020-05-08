package PresentationLayer;

import java.util.Scanner;

public class Menu {

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

    /**
     * This function initialize the system with the hard coded items.
     * @param in get an input stream
     */
    public static void initialize(Scanner in){
        char answer;
        System.out.print("Would you like to initialize the system? [y/n] ");
        answer=in.next().charAt(0);
        if(answer=='y' || answer=='Y')
            Controller.getInstance().initialize();
    }

    public static void main(String [] args){
        Scanner in= new Scanner(System.in);
        String[] options=new String[]
                {"Add a new product", "Remove a product", "Add a new item", "Remove an item",
                        "Update minimum quantity", "Update selling price", "Update buying price", "Set defect",
                        "Update item location", "Get report by categories", "Get defect report", "exit"};

        boolean shouldTerminate=false;
        int input;

        initialize(in);

        while(!shouldTerminate){
            displayMenu(options);
            input=getInputIndex(in);

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
                    System.out.println ( "Bye..." );
                    shouldTerminate=true;
                    break;
                default:
                    System.out.println ( "Unrecognized option" );
                    shouldTerminate=true;
                    break;
            }
        }

    }

}
