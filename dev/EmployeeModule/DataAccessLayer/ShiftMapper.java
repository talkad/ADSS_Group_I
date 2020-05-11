package EmployeeModule.DataAccessLayer;

import EmployeeModule.Pair;

import java.sql.*;
import java.util.*;

public class ShiftMapper {
    private static ShiftMapper instance;
    private static mainData dataInstance;
    private static ShiftEmployeesMapper shiftEmployeesInstance;
    private Map<String, DALShift> shiftMap;

    public static EmployeeModule.DataAccessLayer.ShiftMapper getInstance(){
        if(instance == null) {
            instance = new ShiftMapper();
            dataInstance = mainData.getInstance();
            shiftEmployeesInstance = ShiftEmployeesMapper.getInstance();
            instance.shiftMap = new HashMap<>();
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
            e.printStackTrace();//TODO ADD TO MAP PROPERLY
        }
    }

    public boolean searchShift(String key)  {
        if(!shiftMap.containsKey(key)) {
            String[] arr = key.split(" ");
            String shiftDate = arr[0];
            int shiftTime = Integer.parseInt(arr[1]);
            String sql = "SELECT * FROM Shifts where date = " + shiftDate + " AND time = " + shiftTime;//todo this may be wrong date sql and date java are problematic, PROBABLY NEED TO BE JAVA BUT CURRENTLY SQL
            try (Connection conn = dataInstance.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs != null) {
                    List<String> roles = Arrays.asList(rs.getString("roles").split(","));
                    List<Pair<Integer, String>> employees = shiftEmployeesInstance.generateShiftEmployees(rs.getInt("shiftId"));
                    DALShift shift = new DALShift(rs.getDate("date"), rs.getInt("time"),
                            rs.getInt("branch"), rs.getInt("shiftId"), roles, employees);
                    shiftMap.put(shift.getShiftKey(), shift);
                    return true;
                }
                return false;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;
    }

    public DALShift getShift(String key)  {
        if(shiftMap.containsKey(key))
            return shiftMap.get(key);
        else if(searchShift(key)){
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
