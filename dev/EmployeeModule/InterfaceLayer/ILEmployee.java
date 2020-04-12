package EmployeeModule.InterfaceLayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ILEmployee {
    private int id;
    private String firstName;
    private String lastName;
    private String bankDetails;
    private String workConditions;
    private Date startTime;
    private int salary;
    private List<String> roles;
    private boolean[][] freeTime;

    public ILEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles, boolean[][] freeTime) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankDetails = bankDetails;
        this.workConditions = workConditions;
        this.startTime = startTime;
        this.salary = salary;
        this.roles = roles;
        this.freeTime = freeTime;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public String getWorkConditions() {
        return workConditions;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getSalary() {
        return salary;
    }

    public List<String> getRoles() {
        List<String> lst = new LinkedList<>();
        for (String role : roles) {
            lst.add(role);
        }
        return lst;
    }

    public boolean[][] getFreeTime() {
        return freeTime;
    }

    public String toStringFreeTime(){
        String str = "";
        for (int i = 0; i<this.freeTime.length; i++){
            for (int j = 0; j<freeTime[i].length;j++){
                str = str + "Shift period: " + (i+1) + ", Day: " + (j+1) + " availability: " + freeTime[i][j] + "\n";
            }
        }
        return str;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return "id: " + id + "\n" + "first name: " + firstName + "\n" +
                "last name: " + lastName + "\n" + "bank details: " + bankDetails + "\n" +
                "work conditions: " + workConditions + "\n" +"started working on: " + formatter.format(this.startTime) + "\n" +
                "salary: " + salary + "\n" + "roles: " + this.roles.toString() + "\n"
                + "free time:\n" + toStringFreeTime();
    }
}
