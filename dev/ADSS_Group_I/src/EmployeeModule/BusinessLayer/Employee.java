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
    private List<String> roles;

    public Employee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankDetails = bankDetails;
        this.workConditions = workConditions;
        this.startTime = startTime;
        this.salary = salary;
        this.roles = roles;
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
        return roles;
    }

}
