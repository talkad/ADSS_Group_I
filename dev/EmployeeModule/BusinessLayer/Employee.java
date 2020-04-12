package EmployeeModule.BusinessLayer;

import java.util.Date;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String bankDetails;
    private String workConditions;
    private Date startTime;
    private int salary;
    private boolean[][] freeTime;
    private List<String> roles;

    public Employee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankDetails = bankDetails;
        this.workConditions = workConditions;
        this.startTime = startTime;
        this.salary = salary;
        this.freeTime = new boolean[2][7];
        this.roles = roles;
    }

    public void setFreeTime(boolean[][] freeTime){
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

    public boolean[][] getFreeTime() {
        return freeTime;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setUnFreeTime(int period, int day){
        this.freeTime[period][day] = false;
    }
}
