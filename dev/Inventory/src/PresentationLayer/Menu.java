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

    public static void main(String [] args){
        Scanner in= new Scanner(System.in);
        String[] options=new String[]
                {"Initialize the system", "Add a new item", "Remove an item", "Add a new product", "Remove a product",
                        "Update minimum quantity", "Update selling price", "Update buying price", "Set defect",
                        "Update item location", "Get report by categories", "Get defect report", "exit"};

        boolean shouldTerminate=false;
        int input=0;

        while(!shouldTerminate){
            displayMenu(options);
            input=getInputIndex(in);

            switch (input) {
                case 1:
                    //Controller.inialize();
                    break;
                case 2:
                    Controller.addItem(in);
                    break;
                case 3:
                    Controller.removeItem(in);
                    break;
                case 4:
                    Controller.addProduct();
                    break;
                case 5:
                    Controller.removeProduct();
                    break;
                case 6:
                    Controller.updateMinQuantity(in);
                    break;
                case 7:
                    Controller.updateSellingPrice(in);
                    break;
                case 8:
                    Controller.updateBuyingPrice(in);
                    break;
                case 9:
                    Controller.updateItemStatus(in);
                    break;
                case 10:
                    Controller.updateItemLocation(in);
                    break;
                case 11:
                    Controller.getCategoriesReport(in);
                    break;
                case 12:
                    Controller.getDefectsReports();
                    break;
                case 13:
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
