package EmployeeModule.InterfaceLayer;

import EmployeeModule.Pair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ILShift {
    private Date date;
    private int time;
    private int branch;
    private int shiftId;
    private List<String> roles;
    private List<Pair<Integer, String>> employees;

    public ILShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees) {
        this.date = date;
        this.time = time;
        this.branch = branch;
        this.shiftId = shiftId;
        this.roles = roles;
        this.employees = employees;
    }

    public Date getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public int getBranch() {
        return branch;
    }

    public int getShiftId() {
        return shiftId;
    }

    public List<String> getRoles() {
        List<String> lst = new LinkedList<>();
        for (String role : roles) {
            lst.add(role);
        }
        return lst;
    }

    public List<Pair<Integer, String>> getEmployees() {
        List<Pair<Integer, String>> lst = new LinkedList<>();
        for (Pair<Integer, String> employee : employees) {
            lst.add(employee);
        }
        return lst;
    }

    public void setEmployees(List<Pair<Integer, String>> employees) {
        this.employees = employees;
    }

    public String toString(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String period = "day";
        if(this.time==2)
            period = "night";
        return "Shift date: " + formatter.format(this.date) + "\n" +
                "Shift period: " + period  + "\n" +
                "Shift branch number: " + this.branch + "\n" +
                "Shift id: " + this.shiftId + "\n" +
                "Shift required roles: " + this.roles.toString() + "\n" +
                "Shift assigned employees: " + this.employees.toString() + "\n";
    }
}
