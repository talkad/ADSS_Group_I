package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;
import EmployeeModule.BusinessLayer.Employee;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
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

    public void writeUpdatedFreeTime(int id, int period, int day, boolean available) {
        freeTimeMapperInstance.writeUpdatedFreeTime(id, period, day, available);
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

    public Employee GetEmployee(int id)
    {
        return employeeMapperInstance.getEmployee(id).getEmployee();
    }

    public List<String> getDriversInShift(String shiftTime) {
        return shiftMapperInstance.getDriversInShift(shiftTime);
    }

    public List<Integer> getEmployeesInShift(String shiftTime) {
        return shiftMapperInstance.getEmployeesInShift(shiftTime);
    }

    public void removeShift(String shiftTime) {
        shiftMapperInstance.removeShift(shiftTime);
    }

    public int getShiftIdCounter(){
       return shiftMapperInstance.getShiftIdCounter();
    }

    public List<String> getAvailableRoles(String shiftTime, String role){
        if(shiftMapperInstance.searchShift(shiftTime)) {
            int shiftId = shiftMapperInstance.getShift(shiftTime).getShiftId();
            String shiftDay = "day";
            if(shiftMapperInstance.getShift(shiftTime).getTime() == 1)
                shiftDay = "night";
            shiftDay+=dayOfTheShift(shiftTime.split(" ")[0]);
            String sql = "SELECT Employees.id FROM (Employees JOIN FreeTime ON Employees.id = FreeTime.employeeId) WHERE " + shiftDay + " = 1 AND Employees.roles LIKE '%" + role + "%' AND Employees.id NOT IN \n" +
                    "(SELECT Employees.id FROM Employees JOIN ShiftEmployees on Employees.id = ShiftEmployees.employeeId WHERE ShiftEmployees.shiftId = " + shiftId + ")";
            try (Connection conn = connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // loop through the result set
                List<String> employeesId = new LinkedList<>();
                while (rs.next()) {
                    employeesId.add(""+rs.getInt(1));
                }
                if(!employeesId.isEmpty())
                    return employeesId;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public int dayOfTheShift(String strDate){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(strDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_WEEK);
            } catch (ParseException ignored) {}
        return -1;
    }

    public boolean canAddDriver(String shiftTime){
        return shiftMapperInstance.canAddDriver(shiftTime);
    }

    public boolean addRole(String shiftTime, int id, String role){
        return shiftMapperInstance.addRole(shiftTime, id, role);
    }
}
