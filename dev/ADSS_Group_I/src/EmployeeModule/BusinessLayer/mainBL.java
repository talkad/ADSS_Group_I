package EmployeeModule.BusinessLayer;

import EmployeeModule.DataAccessLayer.*;

import EmployeeModule.Pair;
import Interface.Bussiness_Connector.Connector;
import org.omg.CORBA.portable.ApplicationException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class mainBL {
    private static mainBL instance;
    private static mainData mainDataInstance;

    private mainBL() {
        mainDataInstance = EmployeeModule.DataAccessLayer.mainData.getInstance();
    }

    public static mainBL getInstance() {
        if (instance == null)
            instance = new mainBL();
        return instance;
    }

    public void createEmployee(int id, String firstName, String lastName, String bankDetails, String workConditions, Date startTime, int salary, List<String> roles, boolean updateFlag) throws ApplicationException {
        Employee newEmp = new Employee(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
        if (!updateFlag && newEmp.getRoles().contains("driver")) {
            try {
                Connector.getInstance().addDriver(newEmp);
            } catch (Exception e) {
                send(e.getMessage());
            }
        } else if (newEmp.getRoles().contains("driver")) {
            Connector.getInstance().AddOrEditDriver(newEmp.getId(), newEmp);
        }
        DALEmployee dalEmployee = new DALEmployee(id, firstName, lastName, bankDetails, workConditions, startTime, salary, roles);
        if (!updateFlag) {
            mainDataInstance.writeEmployee(dalEmployee);
            mainDataInstance.writeFreeTime(id, new boolean[2][7]);
        } else
            mainDataInstance.editEmployee(dalEmployee);
    }

    public void createShift(Date date, int time, int branch, int shiftId, List<String> roles, List<Pair<Integer, String>> employees) throws ParseException, IOException, ApplicationException {
        Connector.DetermineDelivery();
        mainDataInstance.writingShift(new DALShift(date, time, branch, shiftId, roles, employees));
        mainDataInstance.writeShiftEmployees(shiftId, employees);
    }

    public boolean searchEmployee(int id, boolean flag) throws ApplicationException {
        boolean found = mainBL.mainDataInstance.searchEmployee(id);
        if (flag && !found) {
            send("Error: Employee doesn't exist in the system");
            return false;
        }
        return found;
    }

    public void removeEmployee(int id) throws ApplicationException {
        if (searchEmployee(id, true)) {
            Connector.getInstance().RemoveDriver(id);
            mainDataInstance.removeEmployee(id);
        } else
            send("Error: Employee doesn't exist in the system");
    }

    public boolean searchShift(String key, boolean flag) throws ApplicationException {
        boolean found = mainDataInstance.searchShift(key);
        if (!found && flag)
            send("Error: Shift doesn't exist in the system");
        if (found && !flag) {
            send("Error: Shift already exists in the system");
        }
        return found;
    }

    public boolean hasRole(int id, String role) throws ApplicationException {
        boolean found = mainDataInstance.hasRole(id, role);
        if (!found) {
            send("Error: Employee isn't qualified for the role");
        }
        return found;
    }

    public boolean isFree(int id, int day, int period) throws ApplicationException {
        boolean free = mainDataInstance.isFree(id, day, period);
        if (!free)
            send("Error: Employee isn't free during that time");
        return free;
    }

    public void setFreeTime(int id, boolean[][] freeTime) {
        mainDataInstance.writeFreeTime(id, freeTime);
    }

    public void writeUpdatedFreeTime(int id, int period, int day, boolean available) {
        mainDataInstance.writeUpdatedFreeTime(id, period, day, available);
    }

    public String employeeInfo(int id) {
        String display = mainDataInstance.getEmployee(id);
        display += mainDataInstance.toStringFreeTime(id);
        return display;
    }

    public String displayAllEmployees() {
        mainDataInstance.createEmployeeMap();
        mainDataInstance.createFreeTimeMap();
        return mainDataInstance.displayAllEmployees();
    }

    public String shiftInfo(String key) {
        return mainDataInstance.getShift(key);
    }

    public List<Integer> getEmployeesInShift(String key) {
        return mainDataInstance.getEmployeesInShift(key);
    }

    public boolean isEmployeeInShift(int id, String shiftTime) throws ApplicationException {
        if (searchShift(shiftTime, true)) {
            return mainDataInstance.isEmployeeInShift(id, shiftTime);
        }
        return false;
    }

    private void send(String msg) throws ApplicationException {
        throw new ApplicationException(msg, null);
    }

    public Employee GetEmployee(int id) {
        return mainDataInstance.GetEmployee(id);
    }

    public void removeShift(String shiftTime) {
        mainDataInstance.removeShift(shiftTime);
    }

    public int getShiftCounter() {
        return mainDataInstance.getShiftIdCounter();
    }

    public List<String> getAvailableDrivers(String shiftTime) throws ApplicationException {
        if(searchShift(shiftTime, true))
            return mainDataInstance.getAvailableRoles(shiftTime, "driver");
        return null;
    }

    public List<String> getDriversInShift(String shiftTime) {
        if(mainDataInstance.searchShift(shiftTime))
            return mainDataInstance.getDriversInShift(shiftTime);
        else return null;
    }

    public boolean addDriverToShift(String shiftTime, int id) throws ApplicationException {
        if(searchShift(shiftTime, true) && searchEmployee(id, true)){
            String shiftDate = shiftTime.split(" ")[0];
            String time = shiftTime.split(" ")[1];
            if(mainDataInstance.canAddDriver(shiftTime)){
               if(mainDataInstance.hasRole(id, "driver")) {
                   if(mainDataInstance.isFree(id, mainDataInstance.dayOfTheShift(shiftDate)-1, Integer.parseInt(time)-1)) {
                       mainDataInstance.writeUpdatedFreeTime(id, Integer.parseInt(time) - 1, mainDataInstance.dayOfTheShift(shiftDate) - 1, false);
                       return mainDataInstance.addRole(shiftTime, id, "driver");
                   }
               }
           }
           else{
               List<String> storekeeperList = mainDataInstance.getAvailableRoles(shiftTime, "storekeeper");
                if (storekeeperList != null){
                    if(Integer.parseInt(storekeeperList.get(0)) != id) {
                        mainDataInstance.addRole(shiftTime, Integer.parseInt(storekeeperList.get(0)), "storekeeper");
                        mainDataInstance.writeUpdatedFreeTime(Integer.parseInt(storekeeperList.get(0)), Integer.parseInt(time)-1, mainDataInstance.dayOfTheShift(shiftDate)-1, false);
                        mainDataInstance.writeUpdatedFreeTime(id, Integer.parseInt(time)-1, mainDataInstance.dayOfTheShift(shiftDate)-1, false);
                        return mainDataInstance.addRole(shiftTime, id, "driver");
                    }
                    else if(storekeeperList.size() > 1){
                        mainDataInstance.addRole(shiftTime, Integer.parseInt(storekeeperList.get(1)), "storekeeper");    //If the first available storekeeper is also the driver given, choose the next storekeeper if one such exists.
                        mainDataInstance.writeUpdatedFreeTime(Integer.parseInt(storekeeperList.get(1)), Integer.parseInt(time)-1, mainDataInstance.dayOfTheShift(shiftDate)-1, false);
                        mainDataInstance.writeUpdatedFreeTime(id, Integer.parseInt(time)-1, mainDataInstance.dayOfTheShift(shiftDate)-1 , false);
                        return mainDataInstance.addRole(shiftTime, id, "driver");
                    }
                    return false; //if no available storekeeper was found, return false
                }
           }
        }
        return false;
    }
}
