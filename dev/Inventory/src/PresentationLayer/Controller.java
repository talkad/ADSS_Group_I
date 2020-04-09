package PresentationLayer;

import java.util.Scanner;

public class Controller {

    /**
     * This function displays the menu to the standard output stream
     */
    public static void displayMenu(){
        String[] options=new String[]
                {"opt 1", "opt 2", "exit"};

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
        boolean shouldTerminate=false;
        int input=0;

        while(!shouldTerminate){
            displayMenu();
            input=getInputIndex(in);

            switch (input) {
                case 1:
                    System.out.println ( "You picked option 1" );
                    break;
                case 2:
                    System.out.println ( "You picked option 2" );
                    break;
                case 3:
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
