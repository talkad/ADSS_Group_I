package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ShiftMapper {
    private static ShiftMapper instance;
    private static mainData dataInstance;
    private static ShiftEmployeesMapper shiftEmployeesInstance;
    private Map<String, DALShift> shiftMap;

    private ShiftMapper(){
        dataInstance = mainData.getInstance();
        shiftEmployeesInstance = ShiftEmployeesMapper.getInstance();
        shiftMap = new HashMap<>();
    }

    public static ShiftMapper getInstance(){
        if(instance == null) {
            instance = new ShiftMapper();
        }
        return instance;
    }

    public void writingShift(DALShift shift){
        String INSERT_SHIFT = "INSERT INTO Shifts(date, time, branch, shiftId, roles) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(INSERT_SHIFT)) {
            ps.setString(1, shift.getShiftKey().split(" ")[0]);//get the date part of the shift's key
            ps.setInt(2, shift.getTime());
            ps.setInt(3, shift.getBranch());
            ps.setInt(4, shift.getShiftId());
            ps.setString(5, shift.getRolesString());
            ps.executeUpdate();
            shiftMap.put(shift.getShiftKey(), shift);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean searchShift(String key) {
        if(!shiftMap.containsKey(key)) {
            String[] arr = key.split(" ");
            if (arr.length == 2) {
                String shiftDate = arr[0];
                String shiftTime = arr[1];
                String sql = "SELECT * FROM Shifts where date = " + "'" + shiftDate + "'" + " AND time = " + shiftTime;
                try (Connection conn = dataInstance.connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    if (rs.next()) {
                        List<String> roles = Arrays.asList(rs.getString("roles").split(","));
                        List<Pair<Integer, String>> employees = shiftEmployeesInstance.generateShiftEmployees(rs.getInt("shiftId"));
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date = formatter.parse(rs.getString("date"));
                            DALShift shift = new DALShift((date), rs.getInt("time"),
                                    rs.getInt("branch"), rs.getInt("shiftId"), roles, employees);
                            shiftMap.put(shift.getShiftKey(), shift);
                            return true;
                        } catch (ParseException e) {
                            System.out.println("Invalid date format");
                        }
                    }
                    return false;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            else return false;
        }
        return true;
    }

    public DALShift getShift(String key)  {
        if(shiftMap.containsKey(key))
            return shiftMap.get(key);
        else if(searchShift(key)){ //check if shift is in the database and hasn't been loaded yet
            return shiftMap.get(key);
        }
        else return null;
    }

    public boolean isEmployeeInShift(int id, String shiftTime) {
        for (Pair<Integer, String> p: this.shiftMap.get(shiftTime).getEmployees()) {
            if(p.getFirst() == id)
                return true;
        }
        return false;
    }
}
