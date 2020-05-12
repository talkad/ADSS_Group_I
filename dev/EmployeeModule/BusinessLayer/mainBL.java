package EmployeeModule.BusinessLayer;

import DeliveryModule.InterfaceLayer.DoThinks;
import EmployeeModule.DataAccessLayer.*;

import EmployeeModule.Pair;
import org.omg.CORBA.portable.ApplicationException;

import java.util.*;

public class mainBL {
    private static mainBL instance;
    private static EmployeeMapper employeeMapperInstance;//todo needs to be static?
    private static ShiftMapper shiftMapperInstance;
    private static ShiftEmployeesMapper shiftEmployeesMapperInstance;
    private static FreeTimeMapper freeTimeMapperInstance;

    private mainBL(){
        employeeMapperInstance = EmployeeModule.DataAccessLayer.EmployeeMapper.getInstance();
        shiftMapperInstance = EmployeeModule.DataAccessLayer.ShiftMapper.getInstance();
        shiftEmployeesMapperInstance = EmployeeModule.DataAccessLayer.ShiftEmployeesMapper.getInstance();
        freeTimeMapperInstance = EmployeeModule.DataAccessLayer.FreeTimeMapper.getInstance();
    }
    public static mainBL getInstance(){
        if(instance == null)
            instance = new mainBL();
        return instance;
    }

    public void createEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles, boolean updateFlag){
        Employee newEmp = new Employee(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
        if(!updateFlag && newEmp.getRoles().contains("driver"))
        {
                DoThinks.addDriver(newEmp, new ArrayList<>());
        }
        else if(newEmp.getRoles().contains("driver")) {
            DoThinks.AddOrEditDriver(newEmp.getId(), newEmp);
        }
        else {
            DoThinks.RemoveDriver(newEmp.getId());
        }

        DALEmployee dalEmployee = new DALEmployee(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
        if(!updateFlag) {
            employeeMapperInstance.writingEmployee(dalEmployee);
            freeTimeMapperInstance.writeFreeTime(id, new boolean[2][7]);
        }
        else
            employeeMapperInstance.editEmployee(dalEmployee);
    }

    public void createShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees){
        shiftMapperInstance.writingShift(new DALShift(date, time, branch, shiftId, roles, employees));
        shiftEmployeesMapperInstance.writeShiftEmployees(shiftId, employees);
    }

    public boolean searchEmployee(int id, boolean flag) throws ApplicationException {
        boolean found = mainBL.employeeMapperInstance.searchEmployee(id);
        if(flag && !found) {
            send("Error: Employee doesn't exist in the system");
            return false;
        }
        return found;
    }

    public void removeEmployee(int id) throws ApplicationException {
        if(searchEmployee(id, true)) {
            employeeMapperInstance.removeEmployee(id);
            freeTimeMapperInstance.removeEmployeeFreeTime(id);
        }
        else
            send("Error: Employee doesn't exist in the system");
    }

    public boolean searchShift(String key, boolean flag) throws ApplicationException {
        boolean found = shiftMapperInstance.searchShift(key);
        if(!found && flag)
            send("Error: Shift doesn't exist in the system");
        if(found && !flag){
            send("Error: Shift already exists in the system");
        }
        return found;
    }

    public boolean hasRole(int id, String role) throws ApplicationException {
        boolean found = employeeMapperInstance.hasRole(id, role);
        if(!found) {
            send("Error: Employee isn't qualified for the role");
        }
        return found;
    }

    public boolean isFree(int id, int day, int period) throws ApplicationException {
        boolean free = freeTimeMapperInstance.isFree(id, day, period);
        if(!free)
           send("Error: Employee isn't free during that time");
        return free;
    }

    public void setFreeTime(int id, boolean[][] freeTime){
        freeTimeMapperInstance.writeFreeTime(id, freeTime);
    }

    public void unFreeTime(int id, int period, int day){
        freeTimeMapperInstance.writeUnFreeTime(id, period, day);
    }


    public String employeeInfo(int id){
        String display = employeeMapperInstance.getEmployee(id).toString();
        display += freeTimeMapperInstance.toStringFreeTime(id);
        return display;
    }

    public String displayAllEmployees(){
        employeeMapperInstance.createEmployeeMap();
        freeTimeMapperInstance.createFreeTimeMap();
        StringBuilder display = new StringBuilder();
        for (int id: employeeMapperInstance.employeesKeys()) {
            display.append(employeeMapperInstance.getEmployee(id).toString());
            display.append(freeTimeMapperInstance.toStringFreeTime(id));
        }
        return display.toString();
    }

    public String shiftInfo(String key){
        return shiftMapperInstance.getShift(key).toString();
    }

    public boolean isEmployeeInShift(int id, String shiftTime) throws ApplicationException {
        if(searchShift(shiftTime, true)) {
            return shiftMapperInstance.isEmployeeInShift(id, shiftTime);
        }
        return false;
    }

    private void send(String msg) throws ApplicationException {
        throw new ApplicationException(msg, null);
    }
}
