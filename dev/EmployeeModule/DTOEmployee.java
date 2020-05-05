package EmployeeModule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DTOEmployee {
    private int id;
    private String firstName;
    private String lastName;
    private String bankDetails;
    private String workConditions;
    private Date startTime;
    private int salary;
    private List<String> roles;
    private boolean[][] freeTime;

    public DTOEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles, boolean[][] freeTime) {
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
        return new LinkedList<>(roles);
    }

    public boolean[][] getFreeTime() {
        return freeTime;
    }

    public String toStringFreeTime(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i<this.freeTime.length; i++){
            for (int j = 0; j<freeTime[i].length;j++){
                str.append("Shift period: ").append(i + 1).append(", Day: ").append(j + 1).append(" availability: ").append(freeTime[i][j]).append("\n");
            }
        }
        return str.toString();
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
