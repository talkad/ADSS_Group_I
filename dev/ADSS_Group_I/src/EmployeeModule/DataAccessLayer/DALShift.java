package EmployeeModule.DataAccessLayer;

import EmployeeModule.DTOShift;
import EmployeeModule.Pair;

import java.util.Date;
import java.util.List;

public class DALShift extends DTOShift {
    public DALShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees) {
        super(date, time, branch, shiftId, roles, employees);
    }
}
