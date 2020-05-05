package EmployeeModule.DataAccessLayer;

import EmployeeModule.DTOEmployee;

import java.util.Date;
import java.util.List;

public class DALEmployee extends DTOEmployee {
    public DALEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles, boolean[][] freeTime) {
        super(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles, freeTime);
    }
}
