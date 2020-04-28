package EmployeeModule.InterfaceLayer;

import EmployeeModule.BusinessLayer.mainBL;
import EmployeeModule.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Service {
    public static int shiftCounter = 1;

    public static void main(String [] args){
        Service service = new Service();
        EmployeeModule.BusinessLayer.mainBL mainBL = new mainBL();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        boolean flag = false;
        String menu = "choose action:\n" + "1) Add employee\n" +
                "2) Edit employee free time\n" + "3) Add shift\n" +
                "4) Get shift history\n" + "5) Get employee details\n"
                + "6) Quit\n" + "7) Load pre-made data\n" + "8) Edit employee's details\n";
        String input;
        while(!quit){
            System.out.print(menu);
            input=scanner.nextLine();
            switch (input){
                case ("1"):
                    flag = true;
                    service.insertEmployee(scanner, mainBL);
                    break;
                case ("2"):
                    flag = true;
                    System.out.print("Please insert the employee's id number: ");
                    int id = isNumeric(scanner.nextLine());
                    if(id != -1){
                        service.editFreeTime(scanner, id, mainBL);
                    }
                    else{
                        System.out.println("Id must be an integer");
                    }
                    break;
                case ("3"):
                    flag = true;
                    service.insertShift(scanner, mainBL);
                    break;
                case ("4"):
                    flag = true;
                    System.out.print("Please insert the shift's id number: ");
                    int shiftId = isNumeric(scanner.nextLine());
                    if(shiftId != -1){
                        service.displayShift(shiftId, mainBL);
                    }
                    else {
                        System.out.println("Id must be an integer");
                    }
                    break;
                case ("5"):
                    flag = true;
                    System.out.print("Please insert the employee's id number: ");
                    int employeeId = isNumeric(scanner.nextLine());
                    if(employeeId != -1){
                        service.displayEmployee(employeeId, mainBL);
                    }
                    else {
                        System.out.println("Id must be an integer");
                    }
                    break;
                case ("6"):
                    quit = true;
                    break;
                case ("7"):
                    if(!flag){
                        service.dataLoad(mainBL);
                    }
                    else {
                        System.out.println("Cannot use this function if it's not the first one to be called");
                    }
                    flag = true;
                    break;
                case ("8"):
                    flag = true;
                    service.editEmployee(scanner, mainBL);
                    break;
            }
        }
    }
    public ILEmployee generateEmployee(Scanner scanner, int id){
        System.out.print("Insert employee's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Insert employee's last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Insert employee's bank details: ");
        String bankDetails = scanner.nextLine();
        System.out.print("Insert employee's salary: ");
        int salary = isNumeric(scanner.nextLine());
        if(salary != -1) {
            System.out.print("Insert employee's work conditions: ");
            String workCond = scanner.nextLine();
            System.out.print("Insert employee's work starting date in the format <dd/MM/yyyy>: ");
            String startDateStr = scanner.nextLine();
            Date startDate;
            try{
                startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
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
                    return new ILEmployee(id, firstName, lastName, bankDetails, workCond, startDate, salary, employeeRoles, new boolean[2][7]);
                }
            } catch (ParseException e){System.out.println("Invalid date");}
        }
        else {
            System.out.println("Invalid salary, must be an integer");
        }
        return null;
    }

    public void insertEmployee(Scanner scanner, EmployeeModule.BusinessLayer.mainBL mainBL) {
        System.out.print("Insert employee's id: ");
        int id = isNumeric(scanner.nextLine());
        if(id != -1){
            if(mainBL.searchEmployee(id, this, false))
                System.out.print("Employee already exists in the system");
            else{
                ILEmployee employee = generateEmployee(scanner, id);
                if(employee!=null){
                    System.out.println("Successfully added the employee");
                    createEmployee(mainBL, employee);
                }
            }
        }
        else
            System.out.println("Invalid id, must be an integer");
    }

    public void editEmployee(Scanner scanner, EmployeeModule.BusinessLayer.mainBL mainBL){
        System.out.print("Insert the id of the employee you wish to edit: ");
        int id = isNumeric(scanner.nextLine());
        if(id != -1){
            if(mainBL.searchEmployee(id, this, true)){
                ILEmployee employee = generateEmployee(scanner, id);
                if(employee!=null){
                    mainBL.removeEmployee(id);
                    System.out.println("Successfully added the employee");
                    createEmployee(mainBL, employee);
                }
            }
        }
        else{
            System.out.println("Id must be an integer");
        }
    }

    private void createEmployee(EmployeeModule.BusinessLayer.mainBL mainBL, ILEmployee employee){
        mainBL.createEmployee(employee);
    }

    public void insertShift(Scanner scanner, EmployeeModule.BusinessLayer.mainBL mainBL){
        System.out.println("Insert shift's date in the format <dd/MM/yyyy>: ");
        String shiftDateStr = scanner.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date shiftDate = formatter.parse(shiftDateStr);
            Date currDate = new Date();
            if(currDate.before(shiftDate)){
                System.out.println("Insert shift's period - 1 is day, 2 is night: ");
                int shiftTime = isNumeric(scanner.nextLine());
                if(shiftTime != -1){
                    if((shiftTime==1) || (shiftTime == 2)){
                        System.out.println("Insert shift's branch number: ");
                        int branch = isNumeric(scanner.nextLine());
                        if(branch != -1){
                            String role;
                            int amount;
                            System.out.println("Insert shift's required role and after that the role's amount, " +
                                    "to stop inserting roles type 'stop'");
                            String inRole = "Insert role: ";
                            String inAmount = "Insert amount: ";
                            List<String> shiftRolesList = new LinkedList<>();
                            shiftRolesList.add("shift manager");
                            while(true){
                                System.out.print(inRole);
                                role = scanner.nextLine();
                                if(role.equals("stop"))
                                    break;
                                else{
                                    System.out.print(inAmount);
                                    amount = isNumeric(scanner.nextLine());
                                    while(amount == -1){
                                        System.out.println("Invalid input, please insert a number: ");
                                        amount = isNumeric(scanner.nextLine());
                                    }
                                    for (int i = 0; i<amount; i++){
                                        shiftRolesList.add(role);
                                    }
                                }
                            }
                            ILShift shift = new ILShift(shiftDate, shiftTime, branch, shiftCounter, shiftRolesList, new LinkedList<>());
                            List<Pair<Integer, String>> addEmployees = addEmployeesToShift(scanner, shift.getRoles(), shift.getDate(), shift.getTime(), mainBL);
                            if(addEmployees!=null) {
                                shift.setEmployees(addEmployees);
                                createShift(mainBL, shift);
                            }
                        }
                        else{
                            System.out.println("Branch id must be an integer");
                        }
                    }
                    else{
                        System.out.println("Invalid shift period, must be 1 or 2");
                    }
                }
                else{
                    System.out.println("Shift's period must be an integer");
                }
            }
            else{
                System.out.println("Invalid date, date must be at least 1 day ahead of current date");
            }
        } catch (ParseException e) {System.out.println("Invalid date format");}
    }

    private void createShift(EmployeeModule.BusinessLayer.mainBL mainBL, ILShift shift){
        mainBL.createShift(shift);
        shiftCounter++;
        System.out.println("Successfully added the shift");
    }

    public void editFreeTime(Scanner scanner, int id, EmployeeModule.BusinessLayer.mainBL mainBL){
        if(mainBL.searchEmployee(id, this, true)){
            boolean[][] freeTime = new boolean[2][7];
            String valueStr;
            String day = "Sunday";
            boolean legal = true;
            System.out.println("Insert 'true' or 'false' according to the employee's availability:");
            for (int i = 0; i < freeTime[0].length && legal; i++) {
                if(i==0){
                    day = "Sunday";
                }
                else if(i == 1){
                    day = "Monday";
                }
                else if(i == 2){
                    day = "Tuesday";
                }
                else if(i == 3){
                    day = "Wednesday";
                }
                else if(i == 4){
                    day = "Thursday";
                }
                else if(i == 5){
                    day = "Friday";
                }
                else if(i == 6){
                    day = "Saturday";
                }
                System.out.print(day + " day shift: ");
                valueStr = scanner.nextLine();
                if(isBoolean(valueStr)){
                    freeTime[0][i] = Boolean.parseBoolean(valueStr);
                    System.out.print(day + " night shift: ");
                    valueStr = scanner.nextLine();
                    if(isBoolean(valueStr)){
                        freeTime[1][i] = Boolean.parseBoolean(valueStr);
                    }
                    else {
                        System.out.println("Input must be a boolean, cannot assign the employee's free time");
                        legal = false;
                    }
                }
                else {
                    System.out.println("Input must be a boolean, cannot assign the employee's free time");
                    legal = false;
                }
            }
            if(legal){
                mainBL.setFreeTime(id, freeTime);
                System.out.println("Successfully edited the employee's free time");
            }
        }
    }

    private List<Pair<Integer, String>> addEmployeesToShift(Scanner scanner, List<String> roles, Date date, int time, EmployeeModule.BusinessLayer.mainBL mainBL){
        List<Pair<Integer, String>> shiftList = new LinkedList<>();
        List<String> checkRoles = new LinkedList<>(roles);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("Insert employees id followed by their required role for the shift. type 'stop' to stop adding employees");
        while(true) {
            System.out.print("Insert employee's id for the shift: ");
            String empIdStr = scanner.nextLine();
            int empId = isNumeric(empIdStr);
            if(empIdStr.equals("stop"))
                break;
            if(empId != -1){
                System.out.print("Insert employee's required role for the shift: ");
                String empRole = scanner.nextLine();
                if (mainBL.searchEmployee(empId, this, true)) {
                    if(mainBL.isFree(empId, dayOfWeek-1, time-1, this)) {
                        if (mainBL.hasRole(empId, empRole, this)) {
                            checkRoles.remove(empRole);
                            shiftList.add(new Pair<>(empId, empRole));
                        }
                    }
                }
            }
            else{
                System.out.println("Invalid id, must be a number");
            }
        }
        if(checkRoles.isEmpty()){
            for (Pair<Integer,String> p: shiftList) {
                mainBL.unFreeTime(p.getFirst(), time-1, dayOfWeek-1);
            }
            return shiftList;
        }
        else{
            System.out.println("Missing roles for shift, shift cannot be assigned");
            return null;
        }
    }

    public static int isNumeric(String str) {
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e){
            return -1;
        }
    }

    public static boolean isBoolean(String str) {
        return str.equals("true") || str.equals("false");
    }

    public void displayEmployee(int id, mainBL mainBL){
        if(mainBL.searchEmployee(id, this, true)){
            System.out.println(mainBL.employeeInfo(id).toString());
        }
    }

    private void displayShift(int shiftId, mainBL mainBL) {
        if(mainBL.searchShift(shiftId, this)){
            System.out.println(mainBL.shiftInfo(shiftId).toString());
        }
    }

    public void send(String message){
        System.out.println(message);
    }

    private void dataLoad(EmployeeModule.BusinessLayer.mainBL mainBL){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date emp1 = formatter.parse("21/06/2018");
            Date emp2 = formatter.parse("27/11/2012");
            Date emp3 = formatter.parse("11/07/2020");
            Date emp4 = formatter.parse("15/06/2019");
            Date emp5 = formatter.parse("07/02/2000");
            Date emp6 = formatter.parse("21/02/2019");
            Date emp7 = formatter.parse("15/02/2019");
            Date emp8 = formatter.parse("21/02/2014");
            Date emp9 = formatter.parse("08/03/2018");
            Date emp10 = formatter.parse("30/06/1999");
            Date shift1 = formatter.parse("04/04/2021");
            Date shift2 = formatter.parse("05/04/2021");
            Date shift3 = formatter.parse("06/04/2021");
            List<String> roles1 = new LinkedList<>();
            roles1.add("cashier");
            roles1.add("storekeeper");
            List<String> roles2 = new LinkedList<>();
            roles2.add("shift manager");
            roles2.add("cashier");
            List<String> roles3 = new LinkedList<>();
            roles3.add("storekeeper");
            roles3.add("shift manager");
            List<String> roles4 = new LinkedList<>();
            roles4.add("cashier");
            boolean[][] freeTime1 = new boolean[2][7];
            boolean[][] freeTime2 = new boolean[2][7];

            for (int i = 0; i<freeTime1[0].length;i++){
                freeTime1[0][i] = true;
                freeTime2[1][i] = true;
            }

            ILEmployee employee1 = new ILEmployee(1, "Ron", "Roni", "???",
                    "required conditions?", emp1, 6000, roles1, freeTime1);
            ILEmployee employee2 = new ILEmployee(2, "Don", "Doni", "???",
                    "required conditions?", emp2, 24352, roles2, freeTime2);
            ILEmployee employee3 = new ILEmployee(3, "John", "Johni", "???",
                    "required conditions?", emp3, 4776, roles3, freeTime1);
            ILEmployee employee4 = new ILEmployee(4, "Tom", "Tomi", "???",
                    "required conditions?", emp4, 56865, roles4, freeTime2);
            ILEmployee employee5 = new ILEmployee(5, "Ben", "Beni", "???",
                    "required conditions?", emp5, 86568, roles1, freeTime1);
            ILEmployee employee6 = new ILEmployee(6, "Tal", "TalTal", "???",
                    "required conditions?", emp6, 12000, roles2, freeTime2);
            ILEmployee employee7 = new ILEmployee(7, "Jacob", "Jacobian", "???",
                    "required conditions?", emp7, 50000, roles3, freeTime1);
            ILEmployee employee8 = new ILEmployee(8, "Shaked", "Shikdoni", "???",
                    "required conditions?", emp8, 100000, roles4, freeTime2);
            ILEmployee employee9 = new ILEmployee(9, "Almog", "Almogi", "???",
                    "required conditions?", emp9, 7000, roles1, freeTime1);
            ILEmployee employee10 = new ILEmployee(10, "Json", "Jr", "???",
                    "required conditions?", emp10, 6000, roles2, freeTime2);
            mainBL.createEmployee(employee1);
            mainBL.createEmployee(employee2);
            mainBL.createEmployee(employee3);
            mainBL.createEmployee(employee4);
            mainBL.createEmployee(employee5);
            mainBL.createEmployee(employee6);
            mainBL.createEmployee(employee7);
            mainBL.createEmployee(employee8);
            mainBL.createEmployee(employee9);
            mainBL.createEmployee(employee10);
            mainBL.setFreeTime(1, freeTime1);
            mainBL.setFreeTime(2, freeTime2);
            mainBL.setFreeTime(3, freeTime1);
            mainBL.setFreeTime(4, freeTime2);
            mainBL.setFreeTime(5, freeTime1);
            mainBL.setFreeTime(6, freeTime2);
            mainBL.setFreeTime(7, freeTime1);
            mainBL.setFreeTime(8, freeTime2);
            mainBL.setFreeTime(9, freeTime1);
            mainBL.setFreeTime(10, freeTime2);

            List<String> reqRoles1 = new LinkedList<>();
            reqRoles1.add("shift manager");
            reqRoles1.add("cashier");
            reqRoles1.add("storekeeper");
            List<String> reqRoles2 = new LinkedList<>();
            reqRoles2.add("shift manager");
            reqRoles2.add("cashier");
            reqRoles2.add("cashier");
            List<String> reqRoles3 = new LinkedList<>();
            reqRoles3.add("shift manager");
            reqRoles3.add("storekeeper");
            reqRoles3.add("cashier");
            reqRoles3.add("cashier");

            List<Pair<Integer, String>> shift1Employees = new LinkedList<>();
            shift1Employees.add(new Pair<>(1, "cashier"));
            shift1Employees.add(new Pair<>(3, "shift manager"));
            shift1Employees.add(new Pair<>(5, "storekeeper"));

            List<Pair<Integer, String>> shift2Employees = new LinkedList<>();
            shift2Employees.add(new Pair<>(6, "cashier"));
            shift2Employees.add(new Pair<>(2, "shift manager"));
            shift2Employees.add(new Pair<>(8, "cashier"));

            List<Pair<Integer, String>> shift3Employees = new LinkedList<>();
            shift3Employees.add(new Pair<>(1, "storekeeper"));
            shift3Employees.add(new Pair<>(3, "shift manager"));
            shift3Employees.add(new Pair<>(5, "cashier"));
            shift3Employees.add(new Pair<>(9, "cashier"));

            ILShift ilShift1 = new ILShift(shift1, 1, 4, 1, reqRoles1, shift1Employees);
            ILShift ilShift2 = new ILShift(shift2, 2, 6, 2, reqRoles2, shift2Employees);
            ILShift ilShift3 = new ILShift(shift3, 1, 8, 3, reqRoles3, shift3Employees);
            mainBL.createShift(ilShift1);
            mainBL.createShift(ilShift2);
            mainBL.createShift(ilShift3);
            mainBL.unFreeTime(1, 0, 0);
            mainBL.unFreeTime(1, 0, 2);
            mainBL.unFreeTime(3, 0, 0);
            mainBL.unFreeTime(3, 0, 2);
            mainBL.unFreeTime(5, 0, 0);
            mainBL.unFreeTime(5, 0, 2);
            mainBL.unFreeTime(6, 1, 1);
            mainBL.unFreeTime(2, 1, 1);
            mainBL.unFreeTime(8, 1, 1);
            mainBL.unFreeTime(9, 0, 2);
            shiftCounter = shiftCounter + 3;
            System.out.println("Successfully loaded pre-made data");
        }catch (ParseException e){}
    }
}
