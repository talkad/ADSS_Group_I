package EmployeeModule.InterfaceLayer;

import EmployeeModule.BusinessLayer.mainBL;
import EmployeeModule.Pair;
import org.omg.CORBA.portable.ApplicationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Service {
    private static Service instance;
    private static EmployeeModule.BusinessLayer.mainBL mainBL;
    public static int shiftCounter;

    public static Service getInstance(){
        if(instance == null){
            instance = new Service();
            mainBL = EmployeeModule.BusinessLayer.mainBL.getInstance();
            shiftCounter = mainBL.getShiftCounter()+1;
        }
        return instance;
    }

    public static void displayMenu(String[] options){
        System.out.println("\nchoose action:");
        for(int i=0; i<options.length; i++)
            System.out.println((i+1) +") "+ options[i]);

        System.out.print("Selection: ");
    }

    public void display(Scanner scanner) throws ApplicationException {
        EmployeeModule.BusinessLayer.mainBL mainBL = EmployeeModule.BusinessLayer.mainBL.getInstance();
        boolean quit = false;
        String[] options = new String[] {"Add employee", "Edit employee free time", "Add shift", "Get shift history", "Get employee details", "Quit", "Edit employee's details", "Display all employees", "Remove employee", "Edit Shift"};
        String input;
        while(!quit){
            displayMenu(options);
            input=scanner.nextLine();
            switch (input){
                case ("1"):
                    instance.insertEmployee(scanner, mainBL);
                    break;
                case ("2"):
                    System.out.print("Please insert the employee's id number: ");
                    int id = isNumeric(scanner.nextLine());
                    if(id != -1){
                        instance.editFreeTime(scanner, id, mainBL);
                    }
                    else{
                        System.out.println("Id must be an integer");
                    }
                    break;
                case ("3"):
                    instance.insertShift(scanner, mainBL);
                    break;
                case ("4"):
                    instance.displayShift(scanner, mainBL);
                    break;
                case ("5"):
                    System.out.print("Please insert the employee's id number: ");
                    int employeeId = isNumeric(scanner.nextLine());
                    if(employeeId != -1){
                        instance.displayEmployee(employeeId, mainBL);
                    }
                    else {
                        System.out.println("Id must be an integer");
                    }
                    break;
                case ("6"):
                    quit = true;
                    break;
                case ("7"):
                    instance.editEmployee(scanner, mainBL);
                    break;
                case ("8"):
                    instance.displayAllEmployees(mainBL);
                    break;
                case ("9"):
                    System.out.print("Please insert the employee's id number: ");
                    int empId = isNumeric(scanner.nextLine());
                    if(empId != -1){
                        if(empId != 1) {
                            instance.removeEmployee(empId, mainBL);
                        }
                        else System.out.println("You may not remove the Main HR Manager from the database");
                    }
                    else {
                        System.out.println("Id must be an integer");
                    }
                    break;
                case("10"):
                    instance.editShift(scanner, mainBL);
                    break;
                default:
                    System.out.println ("Unrecognized option");
                    break;
            }
        }
    }

    private void editShift(Scanner scanner, mainBL mainBL) {
        try {
            System.out.print("Please insert the shift's date in the format <dd/MM/yyyy> " +
                    "followed by a space and time of the shift (1 for day and 2 for night):\n");
            String shiftTime = scanner.nextLine();
                if (mainBL.searchShift(shiftTime, true)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        int time = Integer.parseInt(shiftTime.split(" ")[1]);
                        Date shiftDate = formatter.parse(shiftTime.split(" ")[0]);
                        Date currDate = new Date();
                        if (currDate.before(shiftDate)) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(shiftDate);
                            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                            for (int id: mainBL.getEmployeesInShift(shiftTime)) {
                                mainBL.writeUpdatedFreeTime(id, time-1, dayOfWeek - 1, true);
                            }
                            ILShift shift = generateShift(scanner, shiftTime, mainBL);
                            if (shift != null) {
                                System.out.println("Successfully edited the shift's details");
                                removeShift(shiftTime, mainBL);
                                createShift(mainBL, shift);
                            }
                        }
                        else{
                            System.out.println("shift already took place or is taking place tomorrow, and cannot be edited");
                        }
                    }catch (ParseException e) {
                        System.out.println("Invalid date format");
                    }
                }
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
    }

    private ILShift generateShift(Scanner scanner, String shiftTime, mainBL mainBL) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date shiftDate = formatter.parse(shiftTime.split(" ")[0]);
            System.out.println("Insert shift's branch number: ");
            int branch = isNumeric(scanner.nextLine());
            if (branch != -1) {
                String role;
                int amount;
                System.out.println("Insert shift's required role and after that the role's amount, " +
                        "to stop inserting roles type 'stop'");
                String inRole = "Insert role: ";
                String inAmount = "Insert amount: ";
                List<String> shiftRolesList = new LinkedList<>();
                shiftRolesList.add("shift manager");
                while (true) {
                    System.out.print(inRole);
                    role = scanner.nextLine();
                    if (role.equals("stop"))
                        break;
                    else {
                        System.out.print(inAmount);
                        amount = isNumeric(scanner.nextLine());
                        while (amount == -1) {
                            System.out.println("Invalid input, please insert a number: ");
                            amount = isNumeric(scanner.nextLine());
                        }
                        for (int i = 0; i < amount; i++) {
                            shiftRolesList.add(role);
                            if (role.equals("driver"))
                                shiftRolesList.add("storekeeper");
                        }
                    }
                }
                ILShift shift = new ILShift(shiftDate, Integer.parseInt(shiftTime.split(" ")[1]), branch, shiftCounter, shiftRolesList, new LinkedList<>());
                List<Pair<Integer, String>> addEmployees = addEmployeesToShift(scanner, shift.getRoles(), shift.getDate(), shift.getTime(), mainBL);
                if (addEmployees != null) {
                    shift.setEmployees(addEmployees);
                    return shift;
                }
                else return null;
            } else {
                System.out.println("Branch id must be an integer");
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format");
        }
        return null;
    }

    private void removeEmployee(int id, mainBL mainBL) {
        try{
            mainBL.removeEmployee(id);
            System.out.println("Successfully removed employee");
        } catch (ApplicationException e){ System.out.println(e.getId());}
    }

    private void removeShift(String shiftTime, mainBL mainBL){
        mainBL.removeShift(shiftTime);
    }

    private void displayAllEmployees(mainBL mainBL) {
        System.out.println(mainBL.displayAllEmployees());
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
                    return new ILEmployee(id, firstName, lastName, bankDetails, workCond, startDate, salary, employeeRoles);
                }
            } catch (ParseException e){System.out.println("Invalid date");}
        }
        else {
            System.out.println("Invalid salary, must be an integer");
        }
        return null;
    }

    public void insertEmployee(Scanner scanner, mainBL mainBL) {
        try {
            System.out.print("Insert employee's id: ");
            int id = isNumeric(scanner.nextLine());
            if (id != -1) {
                if (mainBL.searchEmployee(id, false))
                    System.out.println("Employee already exists in the system");
                else {
                    ILEmployee employee = generateEmployee(scanner, id);
                    if (employee != null) {
                        System.out.println("Successfully added the employee");
                        createEmployee(mainBL, employee, false);
                    }
                }
            } else
                System.out.println("Invalid id, must be an integer");
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
    }

    public void editEmployee(Scanner scanner, mainBL mainBL){
        try {
            System.out.print("Insert the id of the employee you wish to edit: ");
            int id = isNumeric(scanner.nextLine());
            if (id != -1) {
                if(id != 1){
                    if (mainBL.searchEmployee(id, true)) {
                        ILEmployee employee = generateEmployee(scanner, id);
                        if (employee != null) {
                            System.out.println("Successfully edited the employee's details");
                            createEmployee(mainBL, employee, true);
                        }
                    }
                }
                else System.out.println("You may not edit the Main HR Manager details");
            } else {
                System.out.println("Id must be an integer");
            }
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
    }

    private void createEmployee(mainBL mainBL, ILEmployee employee, boolean updateFlag) throws ApplicationException{
        mainBL.createEmployee(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getBankDetails(), employee.getWorkConditions(), employee.getStartTime(), employee.getSalary(), employee.getRoles(), updateFlag);
    }

    public void insertShift(Scanner scanner, mainBL mainBL){
        try {
            System.out.println("Insert shift's date in the format <dd/MM/yyyy>: ");
            String shiftDateStr = scanner.nextLine();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date shiftDate = formatter.parse(shiftDateStr);
                Date currDate = new Date();
                if (currDate.before(shiftDate)) {
                    System.out.println("Insert shift's period - 1 is day, 2 is night: ");
                    int shiftTime = isNumeric(scanner.nextLine());
                    if (shiftTime != -1) {
                        if ((shiftTime == 1) || (shiftTime == 2)) {
                            if (!mainBL.searchShift(shiftDateStr + " " + shiftTime, false)) {
                                ILShift shift = generateShift(scanner, shiftDateStr, mainBL);
                                if(shift != null) {
                                    System.out.println("Successfully added the shift");
                                    createShift(mainBL, shift);
                                }
                            }
                        } else {
                            System.out.println("Invalid shift period, must be 1 or 2");
                        }
                    } else {
                        System.out.println("Shift's period must be an integer");
                    }
                } else {
                    System.out.println("Invalid date, date must be at least 1 day ahead of current date");
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format");
            }
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
    }

    private void createShift(mainBL mainBL, ILShift shift){
        mainBL.createShift(shift.getDate(), shift.getTime(), shift.getBranch(), shift.getShiftId(), shift.getRoles(), shift.getEmployees());
        shiftCounter++;
    }

    public void editFreeTime(Scanner scanner, int id, mainBL mainBL){
        try {
            if (mainBL.searchEmployee(id, true)) {
                boolean[][] freeTime = new boolean[2][7];
                String valueStr;
                String day = "Sunday";
                boolean legal = true;
                System.out.println("Insert 'true' or 'false' according to the employee's availability:");
                for (int i = 0; i < freeTime[0].length && legal; i++) {
                    if (i == 0) {
                        day = "Sunday";
                    } else if (i == 1) {
                        day = "Monday";
                    } else if (i == 2) {
                        day = "Tuesday";
                    } else if (i == 3) {
                        day = "Wednesday";
                    } else if (i == 4) {
                        day = "Thursday";
                    } else if (i == 5) {
                        day = "Friday";
                    } else if (i == 6) {
                        day = "Saturday";
                    }
                    System.out.print(day + " day shift: ");
                    valueStr = scanner.nextLine();
                    if (isBoolean(valueStr)) {
                        freeTime[0][i] = Boolean.parseBoolean(valueStr);
                        System.out.print(day + " night shift: ");
                        valueStr = scanner.nextLine();
                        if (isBoolean(valueStr)) {
                            freeTime[1][i] = Boolean.parseBoolean(valueStr);
                        } else {
                            System.out.println("Input must be a boolean, cannot assign the employee's free time");
                            legal = false;
                        }
                    } else {
                        System.out.println("Input must be a boolean, cannot assign the employee's free time");
                        legal = false;
                    }
                }
                if (legal) {
                    mainBL.setFreeTime(id, freeTime);
                    System.out.println("Successfully edited the employee's free time");
                }
            }
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
    }

    private List<Pair<Integer, String>> addEmployeesToShift(Scanner scanner, List<String> roles, Date date, int time, mainBL mainBL){
        try {
            List<Pair<Integer, String>> shiftList = new LinkedList<>();
            List<String> checkRoles = new LinkedList<>(roles);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            System.out.println("Insert employees id followed by their required role for the shift. type 'stop' to stop adding employees");
            while (true) {
                System.out.print("Insert employee's id for the shift: ");
                String empIdStr = scanner.nextLine();
                int empId = isNumeric(empIdStr);
                if (empIdStr.equals("stop"))
                    break;
                if (empId != -1) {
                    System.out.print("Insert employee's required role for the shift: ");
                    String empRole = scanner.nextLine();
                    if (mainBL.searchEmployee(empId, true)) {
                        if (mainBL.isFree(empId, dayOfWeek - 1, time - 1)) {
                            if (mainBL.hasRole(empId, empRole)) {
                                checkRoles.remove(empRole);
                                shiftList.add(new Pair<>(empId, empRole));
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid id, must be a number");
                }
            }
            if (checkRoles.isEmpty()) {
                for (Pair<Integer, String> p : shiftList) {
                    mainBL.writeUpdatedFreeTime(p.getFirst(), time - 1, dayOfWeek - 1, false);
                }
                return shiftList;
            } else {
                System.out.println("Missing roles for shift, shift cannot be assigned");
            }
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
        return null;
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
        try{
            mainBL.searchEmployee(id,true);
                System.out.println(mainBL.employeeInfo(id));
            } catch (ApplicationException e) {
            System.out.println(e.getId());
        }
    }

    private void displayShift(Scanner scanner, mainBL mainBL) {
        try {
            System.out.print("Please insert the shift's date in the format <dd/MM/yyyy> " +
                    "followed by a space and time of the shift (1 for day and 2 for night):\n");
            String shiftTime = scanner.nextLine();
            if (mainBL.searchShift(shiftTime, true)) {
                System.out.println(mainBL.shiftInfo(shiftTime));
            }
        } catch (ApplicationException e){
            System.out.println(e.getId());
        }
    }

    public boolean hasRole(int id, String role) throws ApplicationException {
        return mainBL.hasRole(id, role);
    }

    public boolean searchEmployee(int id) throws ApplicationException {
        return mainBL.searchEmployee(id, true);
    }

    private void dataLoad(mainBL mainBL) throws ApplicationException{
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
                    "required conditions?", emp1, 6000, roles1);
            ILEmployee employee2 = new ILEmployee(2, "Don", "Doni", "???",
                    "required conditions?", emp2, 24352, roles2);
            ILEmployee employee3 = new ILEmployee(3, "John", "Johni", "???",
                    "required conditions?", emp3, 4776, roles3);
            ILEmployee employee4 = new ILEmployee(4, "Tom", "Tomi", "???",
                    "required conditions?", emp4, 56865, roles4);
            ILEmployee employee5 = new ILEmployee(5, "Ben", "Beni", "???",
                    "required conditions?", emp5, 86568, roles1);
            ILEmployee employee6 = new ILEmployee(6, "Tal", "TalTal", "???",
                    "required conditions?", emp6, 12000, roles2);
            ILEmployee employee7 = new ILEmployee(7, "Jacob", "Jacobian", "???",
                    "required conditions?", emp7, 50000, roles3);
            ILEmployee employee8 = new ILEmployee(8, "Shaked", "Shikdoni", "???",
                    "required conditions?", emp8, 100000, roles4);
            ILEmployee employee9 = new ILEmployee(9, "Almog", "Almogi", "???",
                    "required conditions?", emp9, 7000, roles1);
            ILEmployee employee10 = new ILEmployee(10, "Json", "Jr", "???",
                    "required conditions?", emp10, 6000, roles2);
            mainBL.createEmployee(employee1.getId(), employee1.getFirstName(), employee1.getLastName(), employee1.getBankDetails(), employee1.getWorkConditions(), employee1.getStartTime(), employee1.getSalary(), employee1.getRoles(), false);
            mainBL.createEmployee(employee2.getId(), employee2.getFirstName(), employee2.getLastName(), employee2.getBankDetails(), employee2.getWorkConditions(), employee2.getStartTime(), employee2.getSalary(), employee2.getRoles(), false);
            mainBL.createEmployee(employee3.getId(), employee3.getFirstName(), employee3.getLastName(), employee3.getBankDetails(), employee3.getWorkConditions(), employee3.getStartTime(), employee3.getSalary(), employee3.getRoles(), false);
            mainBL.createEmployee(employee4.getId(), employee4.getFirstName(), employee4.getLastName(), employee4.getBankDetails(), employee4.getWorkConditions(), employee4.getStartTime(), employee4.getSalary(), employee4.getRoles(), false);
            mainBL.createEmployee(employee5.getId(), employee5.getFirstName(), employee5.getLastName(), employee5.getBankDetails(), employee5.getWorkConditions(), employee5.getStartTime(), employee5.getSalary(), employee5.getRoles(), false);
            mainBL.createEmployee(employee6.getId(), employee6.getFirstName(), employee6.getLastName(), employee6.getBankDetails(), employee6.getWorkConditions(), employee6.getStartTime(), employee6.getSalary(), employee6.getRoles(), false);
            mainBL.createEmployee(employee7.getId(), employee7.getFirstName(), employee7.getLastName(), employee7.getBankDetails(), employee7.getWorkConditions(), employee7.getStartTime(), employee7.getSalary(), employee7.getRoles(), false);
            mainBL.createEmployee(employee8.getId(), employee8.getFirstName(), employee8.getLastName(), employee8.getBankDetails(), employee8.getWorkConditions(), employee8.getStartTime(), employee8.getSalary(), employee8.getRoles(), false);
            mainBL.createEmployee(employee9.getId(), employee9.getFirstName(), employee9.getLastName(), employee9.getBankDetails(), employee9.getWorkConditions(), employee9.getStartTime(), employee9.getSalary(), employee9.getRoles(), false);
            mainBL.createEmployee(employee10.getId(), employee10.getFirstName(), employee10.getLastName(), employee10.getBankDetails(), employee10.getWorkConditions(), employee10.getStartTime(), employee10.getSalary(), employee10.getRoles(), false);
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
            mainBL.createShift(ilShift1.getDate(), ilShift1.getTime(), ilShift1.getBranch(), ilShift1.getShiftId(), ilShift1.getRoles(), ilShift1.getEmployees());
            mainBL.createShift(ilShift2.getDate(), ilShift2.getTime(), ilShift2.getBranch(), ilShift2.getShiftId(), ilShift2.getRoles(), ilShift2.getEmployees());
            mainBL.createShift(ilShift3.getDate(), ilShift3.getTime(), ilShift3.getBranch(), ilShift3.getShiftId(), ilShift3.getRoles(), ilShift3.getEmployees());
            mainBL.writeUpdatedFreeTime(1, 0, 0, false);
            mainBL.writeUpdatedFreeTime(1, 0, 2, false);
            mainBL.writeUpdatedFreeTime(3, 0, 0, false);
            mainBL.writeUpdatedFreeTime(3, 0, 2, false);
            mainBL.writeUpdatedFreeTime(5, 0, 0, false);
            mainBL.writeUpdatedFreeTime(5, 0, 2, false);
            mainBL.writeUpdatedFreeTime(6, 1, 1, false);
            mainBL.writeUpdatedFreeTime(2, 1, 1, false);
            mainBL.writeUpdatedFreeTime(8, 1, 1, false);
            mainBL.writeUpdatedFreeTime(9, 0, 2, false);
            shiftCounter = shiftCounter + 3;
            System.out.println("Successfully loaded pre-made data");
        }catch (ParseException ignored){}
    }
}