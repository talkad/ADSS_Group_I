package EmployeeModule.InterfaceLayer;

import EmployeeModule.DTOShift;
import EmployeeModule.Pair;

import java.util.Date;
import java.util.List;

public class ILShift extends DTOShift {

    public ILShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees) {
        super(date, time, branch, shiftId, roles, employees);
    }
}
