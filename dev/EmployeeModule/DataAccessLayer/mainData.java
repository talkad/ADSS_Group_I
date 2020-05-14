package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;

import java.sql.*;
import java.util.List;

public class mainData {
    private static mainData instance;
    private static EmployeeMapper employeeMapperInstance;
    private static ShiftMapper shiftMapperInstance;
    private static ShiftEmployeesMapper shiftEmployeesMapperInstance;
    private static FreeTimeMapper freeTimeMapperInstance;


    public static EmployeeModule.DataAccessLayer.mainData getInstance(){
        if(instance == null) {
            instance = new mainData();
        }
        return instance;
    }

    private mainData(){
        employeeMapperInstance = EmployeeMapper.getInstance();
        freeTimeMapperInstance = FreeTimeMapper.getInstance();
        shiftEmployeesMapperInstance = ShiftEmployeesMapper.getInstance();
        shiftMapperInstance = ShiftMapper.getInstance();
    }

    protected Connection connect() {
        Connection conn = null;
        try {
        // db parameters
        String url = "jdbc:sqlite:Employees.db";
        // create a connection to the database
        conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
                System.err.println(e.getMessage());
            } /*finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                }catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }*/
        return conn;
    }

    public boolean searchEmployee(int id){
        return employeeMapperInstance.searchEmployee(id);
    }

    public void writeFreeTime(int id, boolean[][] freeTime) {
        freeTimeMapperInstance.writeFreeTime(id, freeTime);
    }

    public void removeEmployee(int id) {
        employeeMapperInstance.removeEmployee(id);
        freeTimeMapperInstance.removeEmployeeFreeTime(id);
    }

    public void writeEmployee(DALEmployee dalEmployee) {
        employeeMapperInstance.writingEmployee(dalEmployee);
    }

    public void editEmployee(DALEmployee dalEmployee) {
        employeeMapperInstance.editEmployee(dalEmployee);
    }

    public void writingShift(DALShift shift) {
        shiftMapperInstance.writingShift(shift);
    }

    public void writeShiftEmployees(int shiftId, List<Pair<Integer, String>> employees) {
        shiftEmployeesMapperInstance.writeShiftEmployees(shiftId, employees);
    }

    public boolean searchShift(String key) {
        return shiftMapperInstance.searchShift(key);
    }

    public void writeUnFreeTime(int id, int period, int day) {
        freeTimeMapperInstance.writeUnFreeTime(id, period, day);
    }

    public String getEmployee(int id) {
        return employeeMapperInstance.getEmployee(id).toString();
    }

    public String toStringFreeTime(int id) {
        return freeTimeMapperInstance.toStringFreeTime(id);
    }

    public void createEmployeeMap() {
        employeeMapperInstance.createEmployeeMap();
    }

    public void createFreeTimeMap() {
        freeTimeMapperInstance.createFreeTimeMap();
    }

    public String displayAllEmployees() {
        StringBuilder display = new StringBuilder();
        for (int id: employeeMapperInstance.employeesKeys()) {
            display.append(getEmployee(id));
            display.append(toStringFreeTime(id));
        }
        return display.toString();
    }

    public String getShift(String key) {
        return shiftMapperInstance.getShift(key).toString();
    }

    public boolean isEmployeeInShift(int id, String shiftTime) {
        return shiftMapperInstance.isEmployeeInShift(id, shiftTime);
    }

    public boolean hasRole(int id, String role) {
        return employeeMapperInstance.hasRole(id, role);
    }

    public boolean isFree(int id, int day, int period) {
        return freeTimeMapperInstance.isFree(id, day, period);
    }
}
