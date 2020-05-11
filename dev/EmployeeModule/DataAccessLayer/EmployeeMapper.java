package EmployeeModule.DataAccessLayer;

import java.sql.*;
import java.util.*;

public class EmployeeMapper {
    private static EmployeeMapper instance;
    private static mainData dataInstance;
    private Map<Integer, DALEmployee> employeeMap;

    public static EmployeeModule.DataAccessLayer.EmployeeMapper getInstance(){
        if(instance == null) {
            instance = new EmployeeMapper();
            dataInstance = mainData.getInstance();
            instance.employeeMap = new HashMap<>();
        }
        return instance;
    }

    public void createEmployeeMap(){//todo make all
        String sql = "SELECT * FROM Employees";
        try (Connection conn = dataInstance.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            if(rs != null){
                while (rs.next()) {
                    createEmployee(rs);//todo in while?
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writingEmployee(DALEmployee employee){
        String INSERT_EMPLOYEE = "INSERT INTO Employees(id, firstName, lastName, bankDetails, workConditions, salary, startTime, roles) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataInstance.connect();
             PreparedStatement ps = conn.prepareStatement(INSERT_EMPLOYEE)) {
            ps.setInt(1, employee.getId());
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getBankDetails());
            ps.setString(5, employee.getWorkConditions());
            ps.setInt(6, employee.getSalary());
            ps.setDate(7, new java.sql.Date(employee.getStartTime().getTime()));
            ps.setString(8, employee.getRolesString());
            ps.executeUpdate();
            employeeMap.put(employee.getId(), employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editEmployee(DALEmployee employee) {
        String INSERT_EMPLOYEE = "UPDATE Employees SET firstName = ?, lastName = ?, bankDetails = ?, " +
                "workConditions = ?, salary = ?, startTime = ?, roles = ? WHERE id = ?";
        try (Connection conn = dataInstance.connect();
            PreparedStatement ps = conn.prepareStatement(INSERT_EMPLOYEE)) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getBankDetails());
            ps.setString(4, employee.getWorkConditions());
            ps.setInt(5, employee.getSalary());
            ps.setDate(6, new java.sql.Date(employee.getStartTime().getTime()));
            ps.setString(7, employee.getRolesString());
            ps.setInt(8, employee.getId());
            ps.executeUpdate();
            employeeMap.remove(employee.getId());
            employeeMap.put(employee.getId(), employee);
        } catch (SQLException e) {
            e.printStackTrace();//TODO UPDATE THE MAP
        }
    }

    public boolean searchEmployee(int id) {
        if (!employeeMap.containsKey(id)) {
            String SEARCH_EMPLOYEE = "SELECT * FROM Employees WHERE id = " + id;
            try (Connection conn = dataInstance.connect();
                 Statement stmt  = conn.createStatement();
                 ResultSet rs    = stmt.executeQuery(SEARCH_EMPLOYEE)) {
                if(rs.next()){
                    rs.beforeFirst();
                    System.out.println();
                    createEmployee(rs);
                    return true;
                }
                else return false;
            } catch (SQLException e) {}
        }
        return true;
    }

    public void createEmployee(ResultSet rs) throws SQLException {
        if(!employeeMap.containsKey(rs.getInt("id"))) {
            List<String> roles = Arrays.asList(rs.getString("roles").split(","));
            DALEmployee employee = new DALEmployee(rs.getInt("id"), rs.getString("firstName"),
                    rs.getString("lastName"), rs.getString("bankDetails"), rs.getString("workConditions"),
                    rs.getDate("startTime"), rs.getInt("salary"), roles);
            employeeMap.put(employee.getId(), employee);
        }
    }

    public DALEmployee getEmployee(int id){
        if(employeeMap.containsKey(id))
            return employeeMap.get(id);
        else if(searchEmployee(id)){
            return employeeMap.get(id);
        }
        else return null;
    }

    public boolean hasRole(int id, String role) {
        return this.employeeMap.get(id).getRoles().contains(role);
    }
}
