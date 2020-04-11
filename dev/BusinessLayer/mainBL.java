package BusinessLayer;

import InterfaceLayer.ILEmployee;
import InterfaceLayer.ILShift;

import java.util.HashMap;
import java.util.Map;

public class mainBL {
    public Map<Integer, Employee> employeeMap = new HashMap<>();
    public Map<Integer, Shift> shiftHistory = new HashMap<>();

    public void createEmployee(ILEmployee employee){
        employeeMap.put(employee.getId(), new Employee(employee.getId(),
                employee.getFirstName(), employee.getLastName(), employee.getBankDetails(),
                employee.getWorkConditions(), employee.getStartTime(), employee.getSalary(), employee.getRoles()));
    }

    public void createShift(ILShift shift){
        shiftHistory.put(shift.getShiftId(), new Shift(shift.getDate(), shift.getTime(), shift.getBranch(), shift.getShiftId(), shift.getRoles()));
    }
}
