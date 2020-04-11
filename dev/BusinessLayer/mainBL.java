package BusinessLayer;

import InterfaceLayer.ILEmployee;
import InterfaceLayer.ILShift;
import InterfaceLayer.Service;

import java.util.HashMap;
import java.util.List;
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
                shift.getTime(), shift.getBranch(), shift.getShiftId(), shift.getRoles()));
    }

    public void setFreeTime(int id, boolean[][] freeTime, Service service){
            employeeMap.get(id).setFreeTime(freeTime);
    }

    public boolean searchEmployee(int id, Service service){
        if(!this.employeeMap.containsKey(id))
            send("Error: Employee doesn't exist in the system", service);
        return this.employeeMap.containsKey(id);
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

    public void assignEmployees(int id, List<Pair<Integer, String>> employees, Service service){
        shiftHistory.get(id).setEmployees(employees);
    }

    public void send(String message, Service service){
        service.send(message);
    }
}
