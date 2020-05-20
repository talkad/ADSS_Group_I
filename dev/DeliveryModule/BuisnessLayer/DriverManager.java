package DeliveryModule.BuisnessLayer;
import EmployeeModule.BusinessLayer.Employee;
import org.omg.CORBA.portable.ApplicationException;

import com.sun.javafx.collections.MappingChange.Map;

import DeliveryModule.DAL.DAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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


    public static void addDriver(Employee employee, List<License> lic) throws ApplicationException
    {
        Driver newDriver = new Driver(employee, lic);
        Drivers.add(newDriver);
        newDriver.saveDriver();

    }

    public static Driver getDriver(int id)
    {
        for (Driver driver : Drivers) {
            if(driver.getID() == id)
                return driver;
        }
        return null;
    }

    public static void RemoveDriver(int id) throws ApplicationException
    {
        Driver removed = getDriver(id);
        Drivers.remove(removed);
        removed.DeleteDriver();

    }

    public static boolean CheckShift(String date, int time, int id) throws ApplicationException {
        return getDriver(id).checkShift(date, time);
    }


    public static void EditEmployee(int id, Employee newEmp)
    {
        getDriver(id).editEmployee(newEmp);
    }

    public static void AddOrEditDriver(int id, Employee newEmp) throws ApplicationException
    {
        if(getDriver(id) != null)
            EditEmployee(id, newEmp);
        else
            addDriver(newEmp, null);
    }

    public static void AddLicense(int DriverID, List<License> licenses) throws ApplicationException
    {
        Driver driver = getDriver(DriverID);
        driver.addLicenses(licenses);
    }
    public static void AddLicense(int DriverID, License license) throws ApplicationException
    {
        Driver driver = getDriver(DriverID);
        driver.addLicense(license);
    }


    public static void Loadall() throws ApplicationException
    {

        List<Object[]> licenses = DAL.LoadAll("DriverLicense");
        List<Object[]> drivers = DAL.LoadAll("Driver");
        HashMap<Integer, List<License>> mapper = new HashMap<Integer, List<License>>();
        for (Object[] driverValues : drivers) {
            mapper.put((int)driverValues[0], new ArrayList<License>());
        }
        for (Object[] licenseArr : licenses) {
            mapper.get((int)licenseArr[0]).add(License.valueOf(((String)licenseArr[1])));
        }

        for (Integer Driverid : mapper.keySet()) {
            Driver driver = new Driver(EmployeeModule.BusinessLayer.mainBL.getInstance().GetEmployee(Driverid), mapper.get(Driverid)  );
            Drivers.add(driver);
        }
    }





}
