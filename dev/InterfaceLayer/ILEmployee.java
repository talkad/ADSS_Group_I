package InterfaceLayer;

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
    private double salary;
    private List<String> roles;

    public ILEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankDetails = bankDetails;
        this.workConditions = workConditions;
        this.startTime = startTime;
        this.salary = salary;
        this.roles = new LinkedList<>();
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

    public double getSalary() {
        return salary;
    }

    public List<String> getRoles() {
        List<String> lst = new LinkedList<>();
        for (String role : roles) {
            lst.add(role);
        }
        return lst;
    }
}
