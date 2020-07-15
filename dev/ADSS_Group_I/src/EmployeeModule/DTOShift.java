package EmployeeModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DTOShift {
    private Date date;
    private int time;
    private int branch;
    private int shiftId;
    private List<String> roles;
    private List<Pair<Integer, String>> employees;

    public DTOShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees) {
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
        return new LinkedList<>(roles);
    }

    public List<Pair<Integer, String>> getEmployees() {
        return new LinkedList<>(employees);
    }

    public void setEmployees(List<Pair<Integer, String>> employees) {
        this.employees = employees;
    }

    public String getShiftKey(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(this.date);
        return strDate + " " + this.time;
    }

    public String getRolesString(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.roles.size(); i++) {
            str.append(this.roles.get(i));
            if(i!= this.roles.size()-1)
                str.append(",");
        }
        return str.toString();
    }

    public void setRoles(List<String> roles){
        this.roles = roles;
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
