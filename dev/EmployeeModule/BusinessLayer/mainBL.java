package EmployeeModule.BusinessLayer;

import EmployeeModule.DataAccessLayer.DALEmployee;
import EmployeeModule.DataAccessLayer.DALShift;
import EmployeeModule.InterfaceLayer.ILEmployee;
import EmployeeModule.InterfaceLayer.ILShift;
import EmployeeModule.InterfaceLayer.Service;

import EmployeeModule.Pair;

import java.util.HashMap;
import java.util.Map;

public class mainBL {
    private Map<Integer, Employee> employeeMap;
    private Map<String, Shift> shiftHistory;
    private EmployeeModule.DataAccessLayer.mainData mainData;
    private static mainBL instance;
    private mainBL(){
        this.employeeMap = new HashMap<>();
        this.shiftHistory = new HashMap<>();
        this.mainData = EmployeeModule.DataAccessLayer.mainData.getInstance();
        initialize();
    }
    public static mainBL getInstance(){
        if(instance == null)
            instance = new mainBL();
        return instance;
    }

    public void createEmployee(ILEmployee employee, boolean updateFlag){
        employeeMap.put(employee.getId(), new Employee(employee.getId(),
                employee.getFirstName(), employee.getLastName(), employee.getBankDetails(),
                employee.getWorkConditions(), employee.getStartTime(), employee.getSalary(), employee.getRoles()));

        DALEmployee dalEmployee = new DALEmployee(employee.getId(),
                employee.getFirstName(), employee.getLastName(), employee.getBankDetails(),
                employee.getWorkConditions(), employee.getStartTime(), employee.getSalary(), employee.getRoles(), employee.getFreeTime());
        if(!updateFlag)
            mainData.writingEmployee(dalEmployee);
        else
            mainData.editEmployee(dalEmployee);
    }

    public void createShift(ILShift shift){
        shiftHistory.put(shift.getShiftKey(), new Shift(shift.getDate(), shift.getTime(),
                shift.getBranch(), shift.getShiftId(), shift.getRoles(), shift.getEmployees()));

        mainData.writingShift(new DALShift(shift.getDate(), shift.getTime(),
                shift.getBranch(), shift.getShiftId(), shift.getRoles(), shift.getEmployees()));
    }

    public void setFreeTime(int id, boolean[][] freeTime){
            employeeMap.get(id).setFreeTime(freeTime);
            mainData.writeFreeTime(id, freeTime);
    }

    public void unFreeTime(int id, int period, int day){
        employeeMap.get(id).setUnFreeTime(period, day);
        mainData.writeFreeTime(id, this.employeeMap.get(id).getFreeTime());
    }

    public boolean searchEmployee(int id, Service service, boolean flag){
        if(flag && !this.employeeMap.containsKey(id))
            send("Error: Employee doesn't exist in the system", service);
        return this.employeeMap.containsKey(id);
    }

    public void removeEmployee(int id){
        this.employeeMap.remove(id);
    }

    public boolean searchShift(String key, Service service, boolean flag){
        if(!this.shiftHistory.containsKey(key) && flag)
            send("Error: Shift doesn't exist in the system", service);
        if(this.shiftHistory.containsKey(key) && !flag){
            send("Error: Shift already exists in the system", service);
        }
        return this.shiftHistory.containsKey(key);
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

    public ILShift shiftInfo(String key){
        Shift shift = shiftHistory.get(key);
        return new ILShift(shift.getDate(), shift.getTime(), shift.getBranch(), shift.getShiftId(), shift.getRoles(), shift.getEmployees());
    }

    public boolean isEmployeeInShift(int id, String shiftTime, Service service){
        if(searchShift(shiftTime, service, true)) {
            for (Pair<Integer, String> p: shiftHistory.get(shiftTime).getEmployees()) {
                if(p.getFirst() == id)
                    return true;
            }
        }
        return false;
    }

    public boolean[][] freeTime(int id){
        return employeeMap.get(id).getFreeTime();
    }

    public void send(String message, Service service){
        service.send(message);
    }

    public void initializeEmployeeMap(){
        for (DALEmployee employee: this.mainData.createEmployeeMap()) {
            Employee e = new Employee(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getBankDetails(), employee.getWorkConditions(),
                    employee.getStartTime(), employee.getSalary(), employee.getRoles());
            this.employeeMap.put(employee.getId(), e);
        }
    }
    public void initializeShiftMap(){
        for (DALShift shift: this.mainData.createShiftMap()) {
            Shift s = new Shift(shift.getDate(), shift.getTime(), shift.getBranch(),
                    shift.getShiftId(), shift.getRoles(), shift.getEmployees());
            this.shiftHistory.put(shift.getShiftKey(), s);
        }
    }

    private void initialize(){
        this.initializeEmployeeMap();
        this.initializeShiftMap();
    }
}
