package EmployeeModule.DataAccessLayer;

import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.BusinessLayer.mainBL;
import EmployeeModule.InterfaceLayer.ILEmployee;

import java.sql.*;
import java.util.*;

public class mainData {
    private EmployeeModule.BusinessLayer.mainBL mainBL;
    private static mainData instance;

    public static EmployeeModule.DataAccessLayer.mainData getInstance(){
        if(instance == null)
            instance = new mainData();
        return instance;
    }

    private Connection connect() {
        String url = "random.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    private boolean[][] generateFreeTime(int id){
        boolean freeTime[][] = new boolean[2][7];
        String sql = "SELECT * FROM FreeTime where id = " + id;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            for (int i = 0; i < freeTime[0].length; i++) {
                freeTime[0][i] = rs.getBoolean("day"+(i+1));
                freeTime[1][i] = rs.getBoolean("night"+(i+1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return freeTime;
    }

    public Map<Integer, ILEmployee> createEmployeeMap(){
        String sql = "SELECT * FROM Employees";
        Map<Integer, ILEmployee> empMap = new HashMap<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            while (rs.next()) {
                boolean[][] freeTime = generateFreeTime(rs.getInt("id"));
                List<String> roles = Arrays.asList(rs.getString("roles").split(","));
                ILEmployee employee = new ILEmployee(rs.getInt("id"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("bankDetails"), rs.getString("workConditions"),
                        rs.getDate("startTime"), rs.getInt("salary"), roles, freeTime);
                empMap.put(employee.getId(), employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return empMap;
    }

    public void initialize(){

    }
}
