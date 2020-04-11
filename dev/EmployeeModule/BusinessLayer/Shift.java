package EmployeeModule.BusinessLayer;

import EmployeeModule.Pair;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Shift {
    private Date date;
    private int time;
    private int branch;
    private int shiftId;
    private List<String> roles;
    private List<Pair<Integer, String>> employees;

    public Shift(Date date, int time, int branch, int shiftId, List<String> roles) {
        this.date = date;
        this.time = time;
        this.branch = branch;
        this.shiftId = shiftId;
        this.roles = roles;
        this.employees = new LinkedList<>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Pair<Integer, String>> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Pair<Integer, String>> employees) {
        this.employees = employees;
    }
}
