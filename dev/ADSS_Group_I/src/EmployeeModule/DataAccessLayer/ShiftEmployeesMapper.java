package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ShiftEmployeesMapper {
    private static ShiftEmployeesMapper instance;
    private static mainData dataInstance = mainData.getInstance();

    private ShiftEmployeesMapper(){}

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeShiftEmployees(int shiftId) {
        String REMOVE_SHIFT_EMPLOYEES = "DELETE FROM ShiftEmployees WHERE shiftId = " + shiftId;
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(REMOVE_SHIFT_EMPLOYEES)) {
            ps.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }
}
