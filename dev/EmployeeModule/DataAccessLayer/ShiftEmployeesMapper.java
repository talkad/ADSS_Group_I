package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftEmployeesMapper {
    private static ShiftEmployeesMapper instance;
    private static mainData dataInstance;
    //private Map<Pair<Integer, Integer>, String> shiftEmployeesMap;//todo remove map probably

    private ShiftEmployeesMapper(){
        dataInstance = mainData.getInstance();
        //shiftEmployeesMap = new HashMap<>();
    }

    public static ShiftEmployeesMapper getInstance(){
        if(instance == null) {
            instance = new ShiftEmployeesMapper();
        }
        return instance;
    }

    protected List<Pair<Integer, String>> generateShiftEmployees(int shiftId){
        List<Pair<Integer, String>> employees = new LinkedList<>();
        String sql = "SELECT * FROM ShiftEmployees where shiftId = " + shiftId;
        try (Connection conn = dataInstance.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(new Pair<>(rs.getInt("employeeId"), rs.getString("role")));
                //shiftEmployeesMap.put(new Pair<>(shiftId, rs.getInt("employeeId")), rs.getString("role"));//add to this map type
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public void writeShiftEmployees(int shiftId, List<Pair<Integer, String>> employees) {
        String EMPLOYEES_IN_SHIFT = "INSERT INTO ShiftEmployees (shiftId, employeeId, role) VALUES(?, ?, ?)";
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(EMPLOYEES_IN_SHIFT)) {
            for (Pair<Integer, String> p: employees) {
                ps.setInt(1, shiftId);
                ps.setInt(2, p.getFirst());
                ps.setString(3, p.getSecond());
                ps.executeUpdate();
                //shiftEmployeesMap.put(new Pair<>(shiftId, p.getFirst()), p.getSecond());//inserting values into shiftEmployeesMap
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
