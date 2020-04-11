package InterfaceLayer;

import BusinessLayer.Employee;
import BusinessLayer.mainBL;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            System.out.print(menu);
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
                    System.out.print("Please insert the employee's id number: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    service.editFreeTime(scanner, id, mainBL);
                    break;
                case ("6"):
                    quit = true;
            }
        }
    }

    public void insertEmployee(Scanner scanner, BusinessLayer.mainBL mainBL) {
        System.out.print("Insert employee's id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Insert employee's first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Insert employee's last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Insert employee's bank details: ");
        String bankDetails = scanner.nextLine();

        System.out.print("Insert employee's salary: ");
        int salary = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Insert employee's work conditions: ");
        String workCond = scanner.nextLine();

            System.out.print("Insert employee's work starting date in the format <dd/mm/yyyy>: ");
            String startDateStr = scanner.nextLine();
            Date startDate = null;
            try{
            startDate = new SimpleDateFormat("dd/mm/yyyy").parse(startDateStr);
        }catch (ParseException e){}
        List<String> employeeRoles = new LinkedList<>();
            System.out.println("Insert employee's roles, to stop inserting roles type 'stop'");
        String role = scanner.nextLine();
        while(!role.equals("stop")){
            employeeRoles.add(role);
            role = scanner.nextLine();
        }
        if(employeeRoles.size()<1)
            System.out.println("Invalid role list, all employees must have 1 role at least");
        else {
            System.out.println("Successfully added the employee");
            ILEmployee employee = new ILEmployee(id, firstName, lastName, bankDetails, workCond, startDate, salary, employeeRoles);
            createEmployee(mainBL, employee);
        }
    }

    public void createEmployee(BusinessLayer.mainBL mainBL, ILEmployee employee){
        mainBL.createEmployee(employee);
    }

    public void insertShift(Scanner scanner, BusinessLayer.mainBL mainBL, int shiftCounter){
        System.out.println("Insert shift's date in the format <dd/mm/yyyy>: ");
        String shiftDateStr = scanner.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date shiftDate = formatter.parse(shiftDateStr);
            Date currDate = new Date();
            if(currDate.before(shiftDate)){
                System.out.println("Insert shift's period - 1 is day, 2 is night: ");
                int shiftTime = scanner.nextInt();
                while((shiftTime!=1) && (shiftTime !=2)){
                    scanner.nextLine();
                    System.out.println("Invalid shift period, please insert 1 or 2: ");
                    shiftTime = scanner.nextInt();
                }
                scanner.nextLine();
                System.out.println("Insert shift's branch number: ");
                int branch = scanner.nextInt();
                scanner.nextLine();
                String role = "";
                String legalAmount = "";
                int amount = 0;
                System.out.println("Insert shift's required role and after that the role's amount, " +
                        "to stop inserting roles type 'stop'");
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
                System.out.println("Successfully added the shift");
                ILShift shift = new ILShift(shiftDate, shiftTime, branch, shiftCounter, shiftRolesList);
                createShift(mainBL, shift, scanner);
            }
            else{
                System.out.println("Invalid date, date must be at least 1 day ahead of current date");
            }
        } catch (ParseException e) {}
    }

    public void createShift(BusinessLayer.mainBL mainBL, ILShift shift, Scanner scanner){
        mainBL.createShift(shift);
        addEmployeesToShift(scanner, shift.getRoles(), shift.getDate(), shift.getTime(), shift.getShiftId(), mainBL);
    }

    public void editFreeTime(Scanner scanner, int id, BusinessLayer.mainBL mainBL){
        if(mainBL.searchEmployee(id, this)){
            boolean[][] freeTime = new boolean[2][7];
            System.out.println("Insert true or false according to the employee's availability:");
            System.out.print("Sunday day shift: ");
            freeTime[0][0] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Sunday night shift: ");
            freeTime[1][0] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Monday day shift: ");
            freeTime[0][1] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Monday night shift: ");
            freeTime[1][1] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Tuesday day shift: ");
            freeTime[0][2] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Tuesday night shift: ");
            freeTime[1][2] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Wednesday day shift: ");
            freeTime[0][3] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Wednesday night shift: ");
            freeTime[1][3] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Thursday day shift: ");
            freeTime[0][4] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Thursday night shift: ");
            freeTime[1][4] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Friday day shift: ");
            freeTime[0][5] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Friday night shift: ");
            freeTime[1][5] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Saturday day shift: ");
            freeTime[0][6] = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Saturday night shift: ");
            freeTime[1][6] = scanner.nextBoolean();
            scanner.nextLine();
            mainBL.setFreeTime(id, freeTime, this);
            System.out.println("Successfully edited the employee's free time");
        }
    }

    public void addEmployeesToShift(Scanner scanner, List<String> roles, Date date, int time, int shiftID, BusinessLayer.mainBL mainBL){
        List<BusinessLayer.Pair<Integer, String>> shiftList = new LinkedList<>();//TODO WTF CASTING
        System.out.println("Insert employees id followed by their required role for the shift. type 'stop' to stop adding employees");
        while(true) {
            System.out.print("Insert employee's id for the shift: ");
            String empIdStr = scanner.nextLine();
            if(empIdStr.equals("stop"))
                break;
            if(isNumeric(empIdStr)){
                int empId = Integer.parseInt(empIdStr);
                System.out.print("Insert employee's required role for the shift: ");
                String empRole = scanner.nextLine();
                if (mainBL.searchEmployee(empId, this)) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    if(mainBL.isFree(empId, dayOfWeek-1, time-1, this)) {
                        if (mainBL.hasRole(empId, empRole, this)) {
                            roles.remove(empRole);
                            shiftList.add(new Pair<Integer, String>(empId, empRole));
                        }
                    }
                }
            }
            else{
                System.out.println("Invalid id, must be a number");
            }
        }
        if(roles.isEmpty()){
            mainBL.assignEmployees(shiftID, shiftList, this);
        }
        else{
            System.out.println("Missing roles for shift, cannot assign employees for the shift");
        }
    }
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void send(String message){
        System.out.println(message);
    }
}
