package EmployeeModule.Tests;

import EmployeeModule.BusinessLayer.mainBL;
import EmployeeModule.InterfaceLayer.ILEmployee;
import EmployeeModule.InterfaceLayer.ILShift;
import EmployeeModule.Pair;
import org.junit.Before;
import org.junit.Test;

import EmployeeModule.BusinessLayer.Employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    @Before
    public void InitiateEmployees(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            mainBL = new mainBL();
            boolean[][] freeTime1 = new boolean[2][7];
            boolean[][] freeTime2 = new boolean[2][7];
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

        } catch (ParseException e){}
    }

    @Test
    public void employeeAddTest(){

    }
}
