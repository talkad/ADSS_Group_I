package DeliveryModule.BusinessLayer;


import EmployeeModule.BusinessLayer.Employee;
import EmployeeModule.BusinessLayer.mainBL;
import org.omg.CORBA.portable.ApplicationException;

import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.DriverDAL;
import DeliveryModule.DAL.LicenseDAL;

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


    public boolean checkShift(String date, String time) throws ApplicationException {
        String shiftTime = date + " " + time;
        return mainBL.getInstance().isEmployeeInShift(this.getID(), shiftTime);
    }
    
    public boolean isLicenseSet(License l) {
    	for(License license : DriverLicense) {
    		if(l.equals(license))
    			return true;
    	}
    	return false;
    }

    public void editEmployee(Employee newEmp)
    {
        this.employee = newEmp;
    }


    public void addLicenses(List<License> liceneses) throws ApplicationException
    {
        for (License license : liceneses) {
            addLicense(license);
        }
    }

    public void addLicense(License licenese) throws ApplicationException
    {
        this.DriverLicense.add(licenese);
        LicenseDAL licensedal = new LicenseDAL(getID(), licenese.name());
        DAL.Insert("DriverLicses", licensedal);
    }
    public void saveDriver() throws ApplicationException
    {
        DriverDAL driver = new DriverDAL(this.getID());
        DAL.Insert("Driver", driver);
        for (License license : DriverLicense) {
            LicenseDAL driver_license = new LicenseDAL(getID(), license.name());
            DAL.Insert("DriverLicense", driver_license);
        }
    }

    public void DeleteDriver() throws ApplicationException
    {
        DriverDAL driver = new DriverDAL(this.getID());
        DAL.Delete("Driver", driver);
        for (License license : DriverLicense) {
            LicenseDAL driver_license = new LicenseDAL(getID(), license.name());
            DAL.Delete("DriverLicense", driver_license);
        }
    }
}
