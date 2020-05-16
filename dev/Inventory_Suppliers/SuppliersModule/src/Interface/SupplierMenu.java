package Interface;

import Buisness.SuperLi;

import java.util.HashMap;
import java.util.Scanner;

public class SupplierMenu {

    static Scanner scanner = new Scanner(System.in);

    public static void runMenu() {
        String[] commands = {"=====Supplier-Menu=====",
                "Please choose a function:",
                "1.Add new Supplier",
                "2.Delete a Supplier card",
                "3.Edit a Supplier card",
                "4.Return to Main Menu"};
        MenuHandler.handleSupplierMenu(commands);
    }

    public static void addSupplier(){
        System.out.println("Please Enter the following (without spaces):\nSupplier's name : Manifacturer's name : Company id : Bank Account : Payments conditions : arrangment type (fixed/single) : Self Pickup(true/false)");
        String input = scanner.nextLine();
        String [] values = input.split(":");
        if(values.length!=7){
            System.out.println("too many/few arguments...\n" +
                    "returning to supplier menu\n");
            runMenu();
            return;
        }
        int companyID, bankAccount;
        boolean selfPickup;
        try{
            companyID=Integer.parseInt(values[2]);
            bankAccount=Integer.parseInt(values[3]);
            selfPickup=Boolean.parseBoolean(values[6]);
        }
        catch (Exception e){
            System.out.println("Input was not of the right format\nReturning to Supplier Menu\n");
            runMenu();
            return;
        }
        if(SuperLi.addSupplier(values[0],values[1],companyID,bankAccount,values[4],values[5],selfPickup))
            System.out.println("Added Supplier successfully");
        else
            System.out.println("Supplier already existed in the System");
    }

    public static void deleteSupplier(){
        System.out.println("Please enter the Company Id of the supplier you wish to delete:\n");
        String input = scanner.next();
        int companyID;
        try{
            companyID=Integer.parseInt(input);
        }
        catch (Exception e){
            System.out.println("A Company ID is an integer\nReturning to Supplier Menu\n");
            runMenu();
            return;
        }
        if(SuperLi.deleteSupplier(companyID))
            System.out.println("Supplier deleted successfully");
        else
            System.out.println("Error 404: supplier not found");
    }

    public static void runEditSupplierMenu(){
        System.out.println("Please enter the Company Id of the supplier you wish to edit:\n");
        String input = scanner.nextLine();
        int companyID;
        try { companyID=Integer.parseInt(input); }
        catch (Exception e){
            System.out.println("A Company ID is an integer\nReturning to Supplier Menu\n");
            runMenu();
            return;
        }
        if (!SuperLi.getSuppliers().containsKey(companyID)){
            System.out.println("error:404 - Supplier not found");
            runMenu();
        }

        String[] commands = {"Please choose a function:" ,
                "1.Add,Edit or Print a contact person" ,
                "2.Change payment conditions" ,
                "3.Change Supplier Bank account number" ,
                "4.Print Supplier card" ,
                "5.Return to Main Menu"};
        MenuHandler.handleEditSupplierMenu(commands,companyID);
    }

    public static void changePayment(int companyID){
        System.out.println("Please enter the new payment conditions\n");
        String input = scanner.nextLine();
        SuperLi.getSuppliers().get(companyID).setPaymentConditions(input);
        System.out.println("Supplier edited successfully\n");
    }

    public static void changeBank(int companyID){
        System.out.println("Please enter the new bank account number:\n");
        String input = scanner.nextLine();
        input = input.replaceAll("\\s+","");
        if (input.length() != 9) {
            System.out.println("A bank account number is an integer with exactly 9 digits long!\nreturning to Supplier Menu\n");
            runMenu();
            return;
        }
        int bankNumber;
        try{
            bankNumber=Integer.parseInt(input);
        }
        catch (Exception e){
            System.out.println("A bank account is an integer\nReturning to Supplier Menu\n");
            runMenu();
            return;
        }
        SuperLi.getSuppliers().get(companyID).setBankAccount(bankNumber);
        System.out.println("Bank account number updated successfully!\n");
    }

    public static void runContactMenu(int companyID){
        String[] commands = {"Please choose a function:",
                "1.Add new Contact Person",
                "2.Delete a Contact Person",
                "3.Edit a Contact Person",
                "4.Print all contacts",
                "5.Return to Main Menu"};
        MenuHandler.handleContactMenu(commands,companyID);
    }

    public static void addContact(int companyID){
        System.out.println("Please enter the new contact's name\n");
        String name = scanner.nextLine();
        System.out.println("Please enter the new contact's contact methods in the following format:\n" +
                "method name-method:method name-method:method name-method... (for example email-someone@somewhere.com:phone-054-1111111...\n");
        String input = scanner.nextLine();
        String[] methods = input.split(":");
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < methods.length;i++){
            String[] methodsSplit = methods[i].split("-");
            if (methodsSplit.length != 2){
                System.out.println("Input was of the wrong format\nreturning to Supplier Menu\n");
                runMenu();
                return;
            }
            map.put(methodsSplit[0],methodsSplit[1]);
        }
        if(SuperLi.addContact(companyID, name,map)){
            System.out.println("Contact added successfully!");
        }
        else{
            System.out.println("Failed to add contact, contact already exists!");
        }
    }

    public static void runEditContactMenu(int companyID){
        System.out.println("Please enter the name of the contact person you would like to modify");
        String contactName = scanner.nextLine();

        if (!SuperLi.getSuppliers().get(companyID).getContacts().contains(contactName)){
            System.out.println("error:404 - contact not found!");
            runMenu();
            return;
        }

        String[] commands = {"Please choose a function:",
                "1.Add method to contact",
                "2.Delete method from contact",
                "3.Edit details of method",
                "4.Return to Contact Menu"};
        MenuHandler.handleEditContactMenu(commands,companyID,contactName);
    }

    public static void addMethod(int companyID, String contact){
        System.out.println("Please enter the method you with to add in the following format:\n" +
                "methodName:method (i.e like phone:051-1111111)\n");
        String method = scanner.nextLine();
        String[] details = method.split(":");
        if (details.length != 2){
            System.out.println("Input was of the wrong format\n" +
                    "returning to Supplier menu\n");
            runMenu();
            return;
        }
        else{
            SuperLi.getSuppliers().get(companyID).getContactByName(contact).addToMethods(details[0],details[1]);
            System.out.println("Contact method added successfully\n");
        }
    }

    public static void editMethod(int companyID, String contact){
        System.out.println("Please enter the method you with to edit in the following format:\n" +
                "methodName:method (i.e like phone:051-1111111)\n");
        String method = scanner.nextLine();
        String[] details = method.split(":");
        if (details.length != 2){
            System.out.println("Input was of the wrong format\n" +
                    "returning to Supplier menu\n");
            runMenu();
            return;
        }
        else{
            SuperLi.getSuppliers().get(companyID).getContactByName(contact).editMethod(details[0],details[1]);
            System.out.println("Contact method edited successfully\n");
        }
    }

    public static void deleteMethod(int companyID, String contact){
        System.out.println("Please enter the method you with to delete\n");
        String method = scanner.nextLine();
        SuperLi.getSuppliers().get(companyID).getContactByName(contact).deleteFromMethods(method);
        System.out.println("Contact method deleted successfully\n");
    }

    public static void deleteContact(int companyID){
        System.out.println("Please enter the name of the contact person you would like to delete");
        String contactName;
        contactName = scanner.nextLine();

        if(SuperLi.deleteContact(companyID,contactName))
            System.out.println("Contact deleted successfully");
        else{
            System.out.println("error:404-contact not found\n" +
                    "returning to Supplier menu\n");
            runMenu();
            return;
        }

    }
}


