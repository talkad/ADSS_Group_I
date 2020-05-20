package EmployeeModule.Tests;

import EmployeeModule.DataAccessLayer.DALShift;
import EmployeeModule.InterfaceLayer.ILEmployee;
import EmployeeModule.InterfaceLayer.ILShift;
import EmployeeModule.InterfaceLayer.Service;
import EmployeeModule.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.JUnitCore;
import org.omg.CORBA.portable.ApplicationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class employeeTest {
    private Date emp1;
    private Date emp2;
    private Date emp3;
    private Date emp4;
    private Date emp5;
    private Date emp6;
    private Date emp7;
    private Date emp8;
    private Date emp9;
    private Date emp10;
    private Date shift1;
    private Date shift2;
    private Date shift3;
    private boolean[][] freeTime1;
    private boolean[][] freeTime2;
    private EmployeeModule.BusinessLayer.mainBL mainBL;
    private ILEmployee employee1;
    private ILEmployee employee2;
    private ILEmployee employee3;
    private ILEmployee employee4;
    private ILEmployee employee5;
    private ILEmployee employee6;
    private ILEmployee employee7;
    private ILEmployee employee8;
    private ILEmployee employee9;
    private ILEmployee employee10;
    private EmployeeModule.InterfaceLayer.Service service;
    private EmployeeModule.DataAccessLayer.mainData mainData;
    private ILShift ilShift1;
    private ILShift ilShift2;
    private ILShift ilShift3;
    private Scanner inputEmployeeScanner;
    private Scanner idEmployeeScanner;
    private Scanner shiftBadDateScanner;
    private Scanner shiftBadQualificationScanner;
    private Scanner legalShiftScanner;
    private Scanner noManagerShiftScanner;
    private Scanner noTimeShiftScanner;
    private Scanner freeTimeScanner;

    private java.io.ByteArrayOutputStream out;

    public static void main(String[] args){
        JUnitCore.main("EmployeeModule.Tests.employeeTest");
    }

    @Before
    public void InitiateEmployees() throws ApplicationException{
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            out = new java.io.ByteArrayOutputStream();
            System.setOut(new java.io.PrintStream(out));

            mainBL = EmployeeModule.BusinessLayer.mainBL.getInstance();
            mainData = EmployeeModule.DataAccessLayer.mainData.getInstance();
            service = new Service();
            inputEmployeeScanner = new Scanner("1234\nBob\nCohen\nBankDetails\n1000\nconditions\n22/03/2020\n" +
                    "cashier\nstop\n");
            idEmployeeScanner = new Scanner("Not legal id\nAlice\nCohen\nBankDetails\n1000\nconditions\n22/03/2020\n" +
                    "cashier\nstop\n");
            shiftBadDateScanner = new Scanner("22/03/2020 2\n");
            shiftBadQualificationScanner = new Scanner("12/04/2021\n" +
                    "1\n5\ncashier\n1\nstop\n7\ncashier\n3\nshift manager\nstop\n");
            legalShiftScanner = new Scanner("12/04/2021\n" +
                    "1\n5\ncashier\n1\nstop\n1\ncashier\n3\nshift manager\nstop\n");
            noManagerShiftScanner = new Scanner("12/04/2021\n" +
                    "1\n5\ncashier\n1\nstop\n1\ncashier\nstop\n");
            noTimeShiftScanner = new Scanner("12/04/2021\n" +
                    "1\n5\ncashier\n1\nstop\n2\ncashier\n3\nshift manager\nstop\n");
            freeTimeScanner = new Scanner("true\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\n");
            freeTime1 = new boolean[2][7];
            freeTime2 = new boolean[2][7];
            emp1 = formatter.parse("21/06/2018");
            emp2 = formatter.parse("27/11/2012");
            emp3 = formatter.parse("11/07/2020");
            emp4 = formatter.parse("15/06/2019");
            emp5 = formatter.parse("07/02/2000");
            emp6 = formatter.parse("21/02/2019");
            emp7 = formatter.parse("15/02/2019");
            emp8 = formatter.parse("21/02/2014");
            emp9 = formatter.parse("08/03/2018");
            emp10 = formatter.parse("30/06/1999");
            shift1 = formatter.parse("04/04/2021");
            shift2 = formatter.parse("05/04/2021");
            shift3 = formatter.parse("06/04/2021");
            for (int i = 0; i<freeTime1[0].length;i++){
                freeTime1[0][i] = true;
                freeTime2[1][i] = true;
            }

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

            ilShift1 = new ILShift(shift1, 1, 4, 1, reqRoles1, shift1Employees);
            ilShift2 = new ILShift(shift2, 2, 6, 2, reqRoles2, shift2Employees);
            ilShift3 = new ILShift(shift3, 1, 8, 3, reqRoles3, shift3Employees);
            mainBL.createShift(ilShift1.getDate(), ilShift1.getTime(), ilShift1.getBranch(), ilShift1.getShiftId(), ilShift1.getRoles(), ilShift1.getEmployees());
            mainBL.createShift(ilShift2.getDate(), ilShift2.getTime(), ilShift2.getBranch(), ilShift2.getShiftId(), ilShift2.getRoles(), ilShift2.getEmployees());
            mainBL.createShift(ilShift3.getDate(), ilShift3.getTime(), ilShift3.getBranch(), ilShift3.getShiftId(), ilShift3.getRoles(), ilShift3.getEmployees());
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

            service.shiftCounter = 4;

        } catch (ParseException ignored){}
    }

    /*@After
    public void DeleteDataBase(){
        String sql = "DROP TABLE IF EXISTS FreeTime;\n" +
                "DROP TABLE IF EXISTS ShiftEmployees;\n" +
                "DROP TABLE IF EXISTS Employees;\n" +
                "DROP TABLE IF EXISTS Shifts;";
        try (Connection conn = mainData.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    @Test
    public void displayEmployeeTest(){
        service.displayEmployee(7,mainBL);
        assertEquals(out.toString(), "id: 7\n" +
                "first name: Jacob\n" +
                "last name: Jacobian\n" +
                "bank details: ???\n" +
                "work conditions: required conditions?\n" +
                "started working on: 15/02/2019\n" +
                "salary: 50000\n" +
                "roles: [storekeeper, shift manager]\n" +
                "free time:\n" +
                "Shift period: 1, Day: 1 availability: false\n" +
                "Shift period: 1, Day: 2 availability: true\n" +
                "Shift period: 1, Day: 3 availability: false\n" +
                "Shift period: 1, Day: 4 availability: true\n" +
                "Shift period: 1, Day: 5 availability: true\n" +
                "Shift period: 1, Day: 6 availability: true\n" +
                "Shift period: 1, Day: 7 availability: true\n" +
                "Shift period: 2, Day: 1 availability: false\n" +
                "Shift period: 2, Day: 2 availability: false\n" +
                "Shift period: 2, Day: 3 availability: false\n" +
                "Shift period: 2, Day: 4 availability: false\n" +
                "Shift period: 2, Day: 5 availability: false\n" +
                "Shift period: 2, Day: 6 availability: false\n" +
                "Shift period: 2, Day: 7 availability: false\n" +
                System.getProperty("line.separator"));
    }

    /*
    1. Checks that the system doesn't contain an employee with the id 1234
    2. Inserts an employee into the system with id 1234
    3. Checks that the system now contains an employee with the id 1234
    */
    @Test
    public void insertEmployeeTest() throws ApplicationException {
        assertFalse(mainBL.searchEmployee(1234, false)); // employee has not been added with given id
        service.insertEmployee(inputEmployeeScanner,mainBL);
        assertTrue(mainBL.searchEmployee(1234, false)); // an employee has been added with given id
    }

    /*
    1. Attempts to insert an employee with an illegal id in string form (not an integer)
    2. Verifies that the output from the server notifies the user of an error in the id
    */
    @Test
    public void insertEmployeeNonIntegerIdTest(){
        service.insertEmployee(idEmployeeScanner,mainBL);
        assertEquals(out.toString(), "Insert employee's id: Invalid id, must be an integer"+System.getProperty("line.separator"));
    }

    /*
    1. Inserts an employee into the system
    2. Verifies that the starting work date of the employee is indeed the date inserted by the user, and in the same format
    */
    /*@Test
    public void insertEmployeeCorrectDateTest(){
        service.insertEmployee(inputEmployeeScanner,mainBL);
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").format(mainBL.employeeInfo(1234).getStartTime()), "22/03/2020");
    }

    /*
    1. Edits the free time of a pre-added employee to make all the times true (available)
    2. Verifies that the new free time array was indeed updated
    */
    /*@Test
    public void changeEmployeeFreeTimeTest(){
        service.editFreeTime(freeTimeScanner, 7, mainBL); // makes all shift times true for the given employee
        assertArrayEquals(mainBL.employeeInfo(7).getFreeTime(), new boolean[][]{{true,true,true,true,true,true,true},{true,true,true,true,true,true,true}});
    }*/

    /*
    1. Checks that the system doesn't contain a shift with the id 4
    Note: 3 shifts were pre-initialized with the ids 1-3, therefore the next available id is 4
    2. Inserts a new legal shift into the system
    3. Checks that the system now contains a shift with the id 4
    */
    @Test
    public void insertShiftTest() throws ApplicationException {
        assertFalse(mainBL.searchShift("12/04/2021 1", true)); // shift not yet added
        service.insertShift(legalShiftScanner,mainBL);
        assertTrue(mainBL.searchShift("12/04/2021 1", true)); // shift added
    }

    /*
    1. Attempts to insert a shift with an illegal date (date already passed)
    2. Verifies that the output from the server notifies the user of an error with the date
    3. Ensures that the shift was indeed not added to the system
    */
    @Test
    public void insertShiftIllegalDateTest() throws ApplicationException {
        service.insertShift(shiftBadDateScanner,mainBL);
        assertEquals(out.toString(), "Insert shift's date in the format <dd/MM/yyyy>: " + System.getProperty("line.separator") +
                "Invalid date, date must be at least 1 day ahead of current date"+System.getProperty("line.separator"));
        assertFalse(mainBL.searchShift("22/03/2020 2", true)); // shift not added
    }

    /*
    1. Attempts to insert a shift with an illegal job placement (employee not qualified)
    2. Verifies that the output from the server notifies the user of an error with the employee qualification
    3. Ensures that the shift was indeed not added to the system
    */
    @Test
    public void insertShiftEmployeeNotQualifiedTest() throws ApplicationException {
        service.insertShift(shiftBadQualificationScanner,mainBL);
        assertEquals(out.toString(), "Insert shift's date in the format <dd/MM/yyyy>: " + System.getProperty("line.separator") +
                "Insert shift's period - 1 is day, 2 is night: " + System.getProperty("line.separator") +
                "Insert shift's branch number: " + System.getProperty("line.separator") +
                "Insert shift's required role and after that the role's amount, to stop inserting roles type 'stop'" + System.getProperty("line.separator") +
                "Insert role: Insert amount: Insert role: Insert employees id followed by their required role for the shift. type 'stop' to stop adding employees" + System.getProperty("line.separator") +
                "Insert employee's id for the shift: Insert employee's required role for the shift: Error: Employee isn't qualified for the role" + System.getProperty("line.separator") +
                "Insert employee's id for the shift: Insert employee's required role for the shift: Insert employee's id for the shift: Missing roles for shift, shift cannot be assigned"+System.getProperty("line.separator"));
        assertFalse(mainBL.searchShift("12/04/2021 1", true)); // shift not added
    }

    /*
    1. Attempts to insert a shift with no shift manager
    2. Verifies that the output from the server notifies the user of a lack of a shift manager (missing roles)
    3. Ensures that the shift was indeed not added to the system
    */
    @Test
    public void insertShiftNoManagerTest() throws ApplicationException {
        service.insertShift(noManagerShiftScanner,mainBL);
        assertEquals(out.toString(), "Insert shift's date in the format <dd/MM/yyyy>: " +System.getProperty("line.separator")+
                "Insert shift's period - 1 is day, 2 is night: " +System.getProperty("line.separator")+
                "Insert shift's branch number: " +System.getProperty("line.separator")+
                "Insert shift's required role and after that the role's amount, to stop inserting roles type 'stop'" +System.getProperty("line.separator")+
                "Insert role: Insert amount: Insert role: Insert employees id followed by their required role for the shift. type 'stop' to stop adding employees" +System.getProperty("line.separator")+
                "Insert employee's id for the shift: Insert employee's required role for the shift: Insert employee's id for the shift: Missing roles for shift, shift cannot be assigned"+System.getProperty("line.separator"));
        assertFalse(mainBL.searchShift("12/04/2021 1", true)); // shift not added
    }

    /*
    1. Attempts to insert a shift with an employee who isn't available for the shift
    2. Verifies that the output from the server notifies the user of an error with the availability
    3. Ensures that the shift was indeed not added to the system
    */
    @Test
    public void insertShiftEmployeeNotAvailableTest() throws ApplicationException {
        service.insertShift(noTimeShiftScanner,mainBL);
        assertEquals(out.toString(), "Insert shift's date in the format <dd/MM/yyyy>: " +System.getProperty("line.separator")+
                "Insert shift's period - 1 is day, 2 is night: " +System.getProperty("line.separator")+
                "Insert shift's branch number: " +System.getProperty("line.separator")+
                "Insert shift's required role and after that the role's amount, to stop inserting roles type 'stop'" +System.getProperty("line.separator")+
                "Insert role: Insert amount: Insert role: Insert employees id followed by their required role for the shift. type 'stop' to stop adding employees" +System.getProperty("line.separator")+
                "Insert employee's id for the shift: Insert employee's required role for the shift: Error: Employee isn't free during that time" +System.getProperty("line.separator")+
                "Insert employee's id for the shift: Insert employee's required role for the shift: Insert employee's id for the shift: Missing roles for shift, shift cannot be assigned"+System.getProperty("line.separator"));
        assertFalse(mainBL.searchShift("12/04/2021 1", true)); // shift not added
    }
}
