package EmployeeModule.DataAccessLayer;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class FreeTimeMapper {
    private static FreeTimeMapper instance;
    private static mainData dataInstance = mainData.getInstance();
    private Map<Integer, boolean[][]> freeTimeMap;

    private FreeTimeMapper(){
        freeTimeMap = new HashMap<>();
    }

    public static FreeTimeMapper getInstance(){
        if(instance == null) {
            instance = new FreeTimeMapper();
        }
        return instance;
    }

    public void writeFreeTime(int id, boolean[][] freeTime) {
        String REPLACE_FREE_TIME = "INSERT OR REPLACE INTO FreeTime (employeeId, day1, day2, day3, day4, day5, day6, day7, " +
                "night1, night2, night3, night4, night5, night6, night7) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(REPLACE_FREE_TIME)) {
            ps.setInt(1, id);
            int sum = 2;
            int val;
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
            ps.executeUpdate();
            freeTimeMap.remove(id);// remove prev if exists and put the new values
            freeTimeMap.put(id, freeTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isFree(int id, int day, int period) {
        if(freeTimeMap.containsKey(id))
            return freeTimeMap.get(id)[period][day];
        else {
            if (searchFreeTime(id)) {
                return freeTimeMap.get(id)[period][day];
            }
            return false;
        }
    }

    public boolean searchFreeTime(int id){
        String sql = "SELECT * FROM FreeTime where employeeId = " + id;
        try (Connection conn = dataInstance.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            if(rs.next()) {
                createFreeTime(rs);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; //todo weird return is irrelevant
    }

    public void createFreeTime(ResultSet rs) throws SQLException {
        boolean[][] freeTime = new boolean[2][7];
        for (int i = 0; i < freeTime[0].length; i++) {
            freeTime[0][i] = (rs.getInt("day" + (i + 1)) == 1);
            freeTime[1][i] = (rs.getInt("night" + (i + 1)) == 1);
        }
        freeTimeMap.put(rs.getInt("employeeId"), freeTime);
    }

    public void writeUpdatedFreeTime(int id, int period, int day, boolean available) {
        String time = "day";
        if(period == 1)
            time = "night";
        time = time+(day+1);
        int updatedValue = 0;
        if (available)
            updatedValue = 1;
        String UPDATE_FREE_TIME = "UPDATE FreeTime SET " + time + " = " + updatedValue + " WHERE employeeId = " + id;
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(UPDATE_FREE_TIME)) {
            ps.executeUpdate();
            searchFreeTime(id);
            freeTimeMap.get(id)[period][day] = available;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String toStringFreeTime(int id){
        StringBuilder str = new StringBuilder();
        if(searchFreeTime(id)){
            boolean[][] freeTime = this.freeTimeMap.get(id);
            for (int i = 0; i < freeTime.length; i++){
                for (int j = 0; j < freeTime[i].length;j++){
                    str.append("Shift period: ").append(i + 1).append(", Day: ").append(j + 1).append(" availability: ").append(freeTime[i][j]).append("\n");
                }
            }
        }
        return str.toString();
    }

    public void createFreeTimeMap(){
        String sql = "SELECT * FROM FreeTime";
        try (Connection conn = dataInstance.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            while (rs.next()) {
                createFreeTime(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeEmployeeFreeTime(int id) {
        String REMOVE_EMPLOYEE_FREE_TIME = "DELETE FROM FreeTime WHERE employeeId = " + id;
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(REMOVE_EMPLOYEE_FREE_TIME)) {
            ps.executeUpdate();
            freeTimeMap.remove(id);
        } catch (SQLException e) {e.printStackTrace();}
    }
}
