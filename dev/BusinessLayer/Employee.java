package BusinessLayer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String bankDetails;
    private String workConditions;
    private Date startTime;
    private double salary;
    private boolean[][] freeTime;
    private List<String> roles;

    public Employee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, double salary, List<String> roles) {
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

    public void addRole(String role){
        this.roles.add(role);
    }

    public void setFreeTime(int day, int shift, boolean isFree){
        freeTime[shift-1][day-1] = isFree;
    }


}
