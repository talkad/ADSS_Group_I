package EmployeeModule.BusinessLayer;

import DeliveryModule.InterfaceLayer.DoThinks;
import EmployeeModule.DataAccessLayer.DALEmployee;
import EmployeeModule.DataAccessLayer.DALShift;
import EmployeeModule.InterfaceLayer.ILEmployee;
import EmployeeModule.InterfaceLayer.ILShift;

import EmployeeModule.Pair;
import org.omg.CORBA.portable.ApplicationException;

import java.util.ArrayList;
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
        Employee newEmp = new Employee(employee.getId(),
                employee.getFirstName(), employee.getLastName(), employee.getBankDetails(),
                employee.getWorkConditions(), employee.getStartTime(), employee.getSalary(), employee.getRoles());
        employeeMap.put(newEmp.getId(), newEmp);
        if(!updateFlag && employee.getRoles().contains("driver"))
        {
                DoThinks.addDriver(newEmp, new ArrayList<>());
        }
        else if(employee.getRoles().contains("driver")) {
            DoThinks.AddOrEditDriver(newEmp.getId(), newEmp);
        }
        else {
            DoThinks.RemoveDriver(employee.getId());
        }

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
        mainData.writeShiftEmployees(shift.getShiftId(), shift.getEmployees());
    }

    public void setFreeTime(int id, boolean[][] freeTime){
            employeeMap.get(id).setFreeTime(freeTime);
            mainData.writeFreeTime(id, freeTime);
    }

    public void unFreeTime(int id, int period, int day){
        employeeMap.get(id).setUnFreeTime(period, day);
        mainData.writeFreeTime(id, this.employeeMap.get(id).getFreeTime());
    }

    public boolean searchEmployee(int id, boolean flag) throws ApplicationException {
        if(flag && !this.employeeMap.containsKey(id))
            send("Error: Employee doesn't exist in the system");
        return this.employeeMap.containsKey(id);
    }

    public void removeEmployee(int id){
        this.employeeMap.remove(id);
    }

    public boolean searchShift(String key, boolean flag) throws ApplicationException {
        if(!this.shiftHistory.containsKey(key) && flag)
            send("Error: Shift doesn't exist in the system");
        if(this.shiftHistory.containsKey(key) && !flag){
            send("Error: Shift already exists in the system");
        }
        return this.shiftHistory.containsKey(key);
    }

    public boolean hasRole(int id, String role) throws ApplicationException {
        if(!this.employeeMap.get(id).getRoles().contains(role))
            send("Error: Employee isn't qualified for the role");
        return this.employeeMap.get(id).getRoles().contains(role);
    }

    public boolean isFree(int id, int day, int period) throws ApplicationException {
        if(!employeeMap.get(id).getFreeTime()[period][day])
           send("Error: Employee isn't free during that time");
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

    public boolean isEmployeeInShift(int id, String shiftTime) throws ApplicationException {
        if(searchShift(shiftTime, true)) {
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

    public void initializeEmployeeMap(){
        for (DALEmployee employee: this.mainData.createEmployeeMap()) {
            Employee e = new Employee(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getBankDetails(), employee.getWorkConditions(),
                    employee.getStartTime(), employee.getSalary(), employee.getRoles());
            e.setFreeTime(employee.getFreeTime());
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

    private void send(String msg) throws ApplicationException {
        throw new ApplicationException(msg, null);
    }
}
