package DeliveryModule.BuisnessLayer;


import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.BusinessLayer.mainBL;
import org.omg.CORBA.portable.ApplicationException;

import java.util.List;

public class Driver {
    private Employee employee;
    public List<License> DriverLicense;

    public Driver(Employee employee, List<License> license){
        this.employee = employee;
        this.DriverLicense = license;
    }

    public List<License> getLicense() { return DriverLicense; }
    public String getDriverName() { return employee.getFirstName() + " " + employee.getLastName(); }
    public int getID() { return this.employee.getId(); }
    
    
    public boolean checkShift(String date, int time) throws ApplicationException {
    	String shiftTime = date + " " + time;
    	return mainBL.getInstance().isEmployeeInShift(this.getID(), shiftTime);
    }
    
    
    public void editEmployee(Employee newEmp)
    {
    	this.employee = newEmp;
    }
    
}
