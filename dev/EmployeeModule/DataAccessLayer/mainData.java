package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class mainData {
    private static mainData instance;


    public static EmployeeModule.DataAccessLayer.mainData getInstance(){
        if(instance == null) {
            instance = new mainData();
        }
        return instance;
    }

    private static Connection connect() {
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

    private boolean[][] generateFreeTime(int id){
        boolean[][] freeTime = new boolean[2][7];
        String sql = "SELECT * FROM FreeTime where employeeId = " + id;
        try (Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){
            for (int i = 0; i < freeTime[0].length; i++) {
                freeTime[0][i] = (rs.getInt("day" + (i + 1)) == 1);
                freeTime[1][i] = (rs.getInt("night" + (i + 1)) == 1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return freeTime;
    }

    public List<DALEmployee> createEmployeeMap(){
        String sql = "SELECT * FROM Employees";
        List<DALEmployee> empList = new LinkedList<>();
        try (Connection conn = connect();
            Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            while (rs.next()) {
                boolean[][] freeTime = generateFreeTime(rs.getInt("id"));
                List<String> roles = Arrays.asList(rs.getString("roles").split(","));
                DALEmployee employee = new DALEmployee(rs.getInt("id"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("bankDetails"), rs.getString("workConditions"),
                        rs.getDate("startTime"), rs.getInt("salary"), roles, freeTime);
                empList.add(employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return empList;
    }

    private List<Pair<Integer, String>> generateShiftEmployees(int shiftId){
        List<Pair<Integer, String>> employees = new LinkedList<>();
        String sql = "SELECT * FROM ShiftEmployees where shiftId = " + shiftId;
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(new Pair<>(rs.getInt("employeeId"), rs.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public List<DALShift> createShiftMap() {
        String sql = "SELECT * FROM Shifts";
        List<DALShift> shiftList = new LinkedList<>();
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
                List<String> roles = Arrays.asList(rs.getString("roles").split(","));
                List<Pair<Integer, String>> employees = generateShiftEmployees(rs.getInt("shiftId"));
                DALShift shift = new DALShift(rs.getDate("date"), rs.getInt("time"),
                        rs.getInt("branch"), rs.getInt("shiftId"), roles, employees);
                shiftList.add(shift);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return shiftList;
    }

    public int writingEmployee(DALEmployee employee){
        String INSERT_EMPLOYEE = "INSERT INTO Employees(id, firstName, lastName, bankDetails, workConditions, salary, startTime, roles) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        int numRowsInserted = 0;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(INSERT_EMPLOYEE)) {
            ps.setInt(1, employee.getId());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getBankDetails());
            ps.setString(5, employee.getWorkConditions());
            ps.setInt(6, employee.getSalary());
            ps.setDate(7, new java.sql.Date(employee.getStartTime().getTime()));
            ps.setString(8, employee.getRolesString());
            numRowsInserted = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numRowsInserted;
    }
    public int writingShift(DALShift shift){
        String INSERT_SHIFT = "INSERT INTO Shifts(date, time, branch, shiftId, roles) VALUES(?, ?, ?, ?, ?)";
        int numRowsInserted = 0;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(INSERT_SHIFT)) {
            ps.setDate(1, new java.sql.Date(shift.getDate().getTime()));
            ps.setInt(2, shift.getTime());
            ps.setInt(3, shift.getBranch());
            ps.setInt(4, shift.getShiftId());
            ps.setString(5, shift.getRolesString());
            numRowsInserted = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numRowsInserted;
    }

    public int editEmployee(DALEmployee employee) {
        String INSERT_EMPLOYEE = "UPDATE Employees SET firstName = ?, lastName = ?, bankDetails = ?, " +
                "workConditions = ?, salary = ?, startTime = ?, roles = ? WHERE id = ?";
        int numRowsInserted = 0;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(INSERT_EMPLOYEE)) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getBankDetails());
            ps.setString(4, employee.getWorkConditions());
            ps.setInt(5, employee.getSalary());
            ps.setDate(6, new java.sql.Date(employee.getStartTime().getTime()));
            ps.setString(7, employee.getRolesString());
            ps.setInt(8, employee.getId());
            numRowsInserted = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numRowsInserted;
    }

    public int writeFreeTime(int id, boolean[][] freeTime) {
        String REPLACE_FREETIME = "INSERT OR REPLACE INTO FreeTime (employeeId, day1, day2, day3, day4, day5, day6, day7, " +
                "night1, night2, night3, night4, night5, night6, night7) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int numRowsInserted = 0;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(REPLACE_FREETIME)) {
            ps.setInt(1, id);
            int sum = 2;
            int val = 0;
            for (int i = 0; i < freeTime.length; i++) {
                for (int j = 0; j < freeTime[i].length; j++) {
                    if(freeTime[i][j])
                        val = 1;
                    else
                        val = 0;
                    ps.setInt(sum, val);
                    sum++;
                }
            }
            numRowsInserted = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numRowsInserted;
    }

    public int writeShiftEmployees(int shiftId, List<Pair<Integer, String>> employees) {
        String EMPLOYEES_IN_SHIFT = "INSERT OR REPLACE INTO ShiftEmployees (shiftId, employeeId, role) VALUES(?, ?, ?)";
        int numRowsInserted = 0;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(EMPLOYEES_IN_SHIFT)) {
            for (Pair<Integer, String> p: employees) {
                ps.setInt(1, shiftId);
                ps.setInt(2, p.getFirst());
                ps.setString(3, p.getSecond());
                numRowsInserted = ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numRowsInserted;
    }
}
