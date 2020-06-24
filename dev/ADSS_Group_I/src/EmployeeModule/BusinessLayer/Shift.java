package EmployeeModule.BusinessLayer;




import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Shift {
    private Date date;
    private int time;
    private int branch;
    private int shiftId;
    private List<String> roles;
    private List<Pair<Integer, String>> employees;

    public Shift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees) {
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

    public String getShiftKey(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(this.date);
        return strDate + " " + this.time;
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
