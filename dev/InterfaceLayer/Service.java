package InterfaceLayer;

import BusinessLayer.Employee;
import BusinessLayer.mainBL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Service {

    public static void main(String [] args){
        Service service = new Service();
        BusinessLayer.mainBL mainBL = new mainBL();
        Scanner scanner = new Scanner(System.in);
        int shiftCounter = 1;
        boolean quit = false;
        String menu = "choose action:\n" + "1) Add employee\n" +
                "2) Add shift:\n" + "3) Edit employee free time:\n" +
                "4) Get shift history:\n" + "5) Get employee details:\n"
                + "6) Quit\n";
        String input = "";
        while(!quit){
            System.out.println(menu);
            input=scanner.nextLine();
            switch (input){
                case ("1"):
                    service.insertEmployee(scanner, mainBL);
                    break;
                case ("2"):
                    service.insertShift(scanner, mainBL, shiftCounter);
                    shiftCounter++;
                    break;
                case ("3"):

                    break;
            }
        }
    }

    public void insertEmployee(Scanner scanner, BusinessLayer.mainBL mainBL) {
        System.out.println("Insert employee's id: ");
        int id = scanner.nextInt();
        System.out.println("Insert employee's first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Insert employee's last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Insert employee's bank details: ");
        String bankDetails = scanner.nextLine();
        System.out.println("Insert employee's salary: ");
        double salary = scanner.nextDouble();
        System.out.println("Insert employee's work conditions: ");
        String workCond = scanner.nextLine();
        System.out.println("Insert employee's work starting date in the format <dd/mm/yyyy>: ");
        String startDateStr = scanner.nextLine();
        Date startDate = null;
        try{
            startDate = new SimpleDateFormat("dd/mm/yyyy").parse(startDateStr);
        }catch (ParseException e){}

        ILEmployee employee = new ILEmployee(id, firstName, lastName, bankDetails, workCond, startDate, salary);

        createEmployee(mainBL, employee);
    }
    public void createEmployee(BusinessLayer.mainBL mainBL, ILEmployee employee){
        mainBL.createEmployee(employee);
    }

    public void insertShift(Scanner scanner, BusinessLayer.mainBL mainBL, int shiftCounter){
        System.out.println("Insert shift's date in the format <dd/mm/yyyy>: ");
        String shiftDateStr = scanner.nextLine();
        Date shiftDate = null;
        try{
            shiftDate = new SimpleDateFormat("dd/mm/yyyy").parse(shiftDateStr);
        }catch (ParseException e){}

        System.out.println("Insert shift's period - 1 is day, 2 is night: ");
        int shiftTime = scanner.nextInt();
        while((shiftTime!=1) && (shiftTime !=2)){
            System.out.println("Invalid shift period, please insert 1 or 2: ");
            shiftTime = scanner.nextInt();
        }

        System.out.println("Insert shift's branch number: ");
        int branch = scanner.nextInt();

        String role = "";
        String legalAmount = "";
        int amount = 0;
        System.out.println("Insert shift's required role and after that the role's amount, " +
                "to stop inserting roles type 'stop'\n");
        String inRole = "Insert role: ";
        String inAmount = "Insert amount: ";
        List<String> shiftRolesList = new LinkedList<>();
        while(true){
            System.out.print(inRole);
            role = scanner.nextLine();
            if(role.equals("stop"))
                break;
            else{
                System.out.print(inAmount);
                legalAmount = scanner.nextLine();
                while(!isNumeric(legalAmount)){
                    System.out.println("Invalid input, please insert a number: ");
                    legalAmount = scanner.nextLine();
                }
                amount = Integer.parseInt(legalAmount);
                    for (int i = 0; i<amount; i++){
                        shiftRolesList.add(role);
                    }
                }
        }
    ILShift shift = new ILShift(shiftDate, shiftTime, branch, shiftCounter, shiftRolesList);
    createShift(mainBL, shift);
    }

    public void createShift(BusinessLayer.mainBL mainBL, ILShift shift){
        mainBL.createShift(shift);
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
