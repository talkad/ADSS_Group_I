package EmployeeModule.BusinessLayer;

import EmployeeModule.InterfaceLayer.ILEmployee;
import EmployeeModule.InterfaceLayer.ILShift;
import EmployeeModule.InterfaceLayer.Service;

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
        shiftHistory.put(shift.getShiftId(), new Shift(shift.getDate(),
                shift.getTime(), shift.getBranch(), shift.getShiftId(), shift.getRoles(), shift.getEmployees()));
    }

    public void setFreeTime(int id, boolean[][] freeTime){
            employeeMap.get(id).setFreeTime(freeTime);
    }

    public void unFreeTime(int id, int period, int day){
        employeeMap.get(id).setUnFreeTime(period, day);
    }

    public boolean searchEmployee(int id, Service service, boolean flag){
        if(flag && !this.employeeMap.containsKey(id))
            send("Error: Employee doesn't exist in the system", service);
        return this.employeeMap.containsKey(id);
    }

    public void removeEmployee(int id){
        this.employeeMap.remove(id);
    }
    public boolean searchShift(int id, Service service){
        if(!this.shiftHistory.containsKey(id))
            send("Error: Shift doesn't exist in the system", service);
        return this.shiftHistory.containsKey(id);
    }

    public boolean hasRole(int id, String role, Service service){
        if(!this.employeeMap.get(id).getRoles().contains(role))
            send("Error: Employee isn't qualified for the role", service);
        return this.employeeMap.get(id).getRoles().contains(role);
    }

    public boolean isFree(int id, int day, int period, Service service){
        if(!employeeMap.get(id).getFreeTime()[period][day])
            send("Error: Employee isn't free during that time", service);
        return employeeMap.get(id).getFreeTime()[period][day];
    }

    public ILEmployee employeeInfo(int id){
        Employee employee = employeeMap.get(id);
        return new ILEmployee(employee.getId(), employee.getFirstName(), employee.getLastName(),
                employee.getBankDetails(), employee.getWorkConditions(), employee.getStartTime(), employee.getSalary(),
                employee.getRoles(), employee.getFreeTime());
    }

    public ILShift shiftInfo(int id){
        Shift shift = shiftHistory.get(id);
        return new ILShift(shift.getDate(), shift.getTime(), shift.getBranch(), shift.getShiftId(), shift.getRoles(), shift.getEmployees());
    }

    public void send(String message, Service service){
        service.send(message);
    }
}
