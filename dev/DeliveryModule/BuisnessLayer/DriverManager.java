package DeliveryModule.BuisnessLayer;
import EmployeeModule.BusinessLayer.Employee;
import org.omg.CORBA.portable.ApplicationException;

import java.util.ArrayList;
import java.util.List;

public class DriverManager {
    private static List<Driver> Drivers = new ArrayList<Driver>();

    public static boolean CheckLicense(License license, int driverId){
        List<License> l1 = getDriver(driverId).getLicense();


        if(l1 != null )
            for ( License license1 : l1 )
                if( license1 == license )
                    return true;
        return false;
    }
    
    
    public static void addDriver(Employee employee, List<License> lic)
    {
    	Drivers.add(new Driver(employee, lic));
    }
    
    public static Driver getDriver(int id)
    {
    	for (Driver driver : Drivers) {
			if(driver.getID() == id)
				return driver;
		}
    	return null;
    }
    
    public static void RemoveDriver(int id)
    {
    	Driver removed = getDriver(id);
		Drivers.remove(removed);
    
    }
    
    public static boolean CheckShift(String date, int time, int id) throws ApplicationException {
    	return getDriver(id).checkShift(date, time);
    }
    
    
    public static void EditEmployee(int id, Employee newEmp)
    {
    	getDriver(id).editEmployee(newEmp);
    }
    
    public static void AddOrEditDriver(int id, Employee newEmp)
    {
    	if(getDriver(id) != null)
    		EditEmployee(id, newEmp);
    	else
    		addDriver(newEmp, null);
    }

}
