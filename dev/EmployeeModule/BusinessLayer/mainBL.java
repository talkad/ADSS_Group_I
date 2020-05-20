package EmployeeModule.BusinessLayer;

import DeliveryModule.InterfaceLayer.DoThinks;
import EmployeeModule.DataAccessLayer.*;

import EmployeeModule.Pair;
import org.omg.CORBA.portable.ApplicationException;

import java.util.*;

public class mainBL {
    private static mainBL instance;
    private static mainData mainDataInstance;

    private mainBL(){
        mainDataInstance = EmployeeModule.DataAccessLayer.mainData.getInstance();
    }
    public static mainBL getInstance(){
        if(instance == null)
            instance = new mainBL();
        return instance;
    }

    public void createEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles, boolean updateFlag) throws ApplicationException{
        Employee newEmp = new Employee(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
        if(!updateFlag && newEmp.getRoles().contains("driver"))
        {
            try {
                DoThinks.addDriver(newEmp, new ArrayList<>());
            }
            catch(Exception e)
            {
                send(e.getMessage());
            }
        }
        else if(newEmp.getRoles().contains("driver")) {
            DoThinks.AddOrEditDriver(newEmp.getId(), newEmp);
        }
        else {
            DoThinks.RemoveDriver(newEmp.getId());
        }

        DALEmployee dalEmployee = new DALEmployee(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
        if(!updateFlag) {
            mainDataInstance.writeEmployee(dalEmployee);
            mainDataInstance.writeFreeTime(id, new boolean[2][7]);
        }
        else
            mainDataInstance.editEmployee(dalEmployee);
    }

    public void createShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees){
        mainDataInstance.writingShift(new DALShift(date, time, branch, shiftId, roles, employees));
        mainDataInstance.writeShiftEmployees(shiftId, employees);
    }

    public boolean searchEmployee(int id, boolean flag) throws ApplicationException {
        boolean found = mainBL.mainDataInstance.searchEmployee(id);
        if(flag && !found) {
            send("Error: Employee doesn't exist in the system");
            return false;
        }
        return found;
    }

    public void removeEmployee(int id) throws ApplicationException {
        if(searchEmployee(id, true)) {
            mainDataInstance.removeEmployee(id);
        }
        else
            send("Error: Employee doesn't exist in the system");
    }

    public boolean searchShift(String key, boolean flag) throws ApplicationException {
        boolean found = mainDataInstance.searchShift(key);
        if(!found && flag)
            send("Error: Shift doesn't exist in the system");
        if(found && !flag){
            send("Error: Shift already exists in the system");
        }
        return found;
    }

    public boolean hasRole(int id, String role) throws ApplicationException {
        boolean found = mainDataInstance.hasRole(id, role);
        if(!found) {
            send("Error: Employee isn't qualified for the role");
        }
        return found;
    }

    public boolean isFree(int id, int day, int period) throws ApplicationException {
        boolean free = mainDataInstance.isFree(id, day, period);
        if(!free)
            send("Error: Employee isn't free during that time");
        return free;
    }

    public void setFreeTime(int id, boolean[][] freeTime){
        mainDataInstance.writeFreeTime(id, freeTime);
    }

    public void unFreeTime(int id, int period, int day){
        mainDataInstance.writeUnFreeTime(id, period, day);
    }

    public String employeeInfo(int id){
        String display = mainDataInstance.getEmployee(id);
        display += mainDataInstance.toStringFreeTime(id);
        return display;
    }

    public String displayAllEmployees(){
        mainDataInstance.createEmployeeMap();
        mainDataInstance.createFreeTimeMap();
        return mainDataInstance.displayAllEmployees();
    }

    public String shiftInfo(String key){
        return mainDataInstance.getShift(key);
    }

    public boolean isEmployeeInShift(int id, String shiftTime) throws ApplicationException {
        if(searchShift(shiftTime, true)) {
            return mainDataInstance.isEmployeeInShift(id, shiftTime);
        }
        return false;
    }

    private void send(String msg) throws ApplicationException {
        throw new ApplicationException(msg, null);
    }

    public Employee GetEmployee(int id)
    {
        return mainData.getInstance().GetEmployee(id);
    }
}
