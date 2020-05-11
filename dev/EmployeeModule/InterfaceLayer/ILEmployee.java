package EmployeeModule.InterfaceLayer;

import EmployeeModule.DTOEmployee;

import java.util.Date;
import java.util.List;

public class ILEmployee extends DTOEmployee {

    public ILEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles) {
        super(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
    }
}
