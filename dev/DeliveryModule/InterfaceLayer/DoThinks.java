package DeliveryModule.InterfaceLayer;

import DeliveryModule.BuisnessLayer.DeliveryArea;
import DeliveryModule.BuisnessLayer.DeliveryForm;
import DeliveryModule.BuisnessLayer.DeliveryManager;
import DeliveryModule.BuisnessLayer.DriverManager;
import DeliveryModule.BuisnessLayer.ItemList;
import DeliveryModule.BuisnessLayer.License;
import DeliveryModule.BuisnessLayer.Site;
import DeliveryModule.BuisnessLayer.SiteManager;
import DeliveryModule.BuisnessLayer.TruckManager;
import EmployeeModule.BusinessLayer.Employee;
import org.omg.CORBA.portable.ApplicationException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoThinks {

    public static boolean CreateForm(String date, LocalTime time, String Destiny, String Source, ItemList itemList, int truckNumber, int driverId) throws IOException, ParseException, ApplicationException {
        if (!TruckManager.CheckLicense(truckNumber, driverId))
            return false;

        int shifttype = 0;
        int timehour = time.getHour();
        if(7 <= timehour && timehour < 16)
            shifttype = 1;
        if(16 <= timehour && timehour <= 23)
            shifttype = 2;
        if (!CheckShift(date, shifttype, DriverManager.getDriver(driverId).getID()))
            return false;

        Site dest, source;
        if((dest =SiteManager.FindSite(Destiny)) == null ||(source = SiteManager.FindSite(Source)) == null)
        	return false;
        Date datenew = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        DeliveryManager.CreateForm(datenew, time, dest, source, itemList, truckNumber, driverId);
        return true;
    }
    public static boolean CheckShift(String date, int time, int id) throws ApplicationException {
        return DriverManager.CheckShift(date, time, id);
    }

    public static String Departure(int formID, int truckWeight){
    	DeliveryForm form = DeliveryManager.FindForm(formID);
        if (!TruckManager.CheckWeight(form.getTruckNumber(), truckWeight))
            return null;
        DeliveryManager.addWeghit(truckWeight, LocalTime.now(), form);
     
        return form.getItemList().toString();// print item list
    }

    public static boolean EditForm(int FormID, Date date,  String Destiny, String Source, ItemList itemList, Integer truckNumber,  int driverId)
    {
    	int trucknum;
    	if(truckNumber != null)
    		trucknum = truckNumber;
    	else
    		trucknum = DeliveryManager.FindForm(FormID).getTruckNumber();
    	
    	int driver;
    	if(driverId != -1)
    		driver = driverId;
    	else
    		driver = DeliveryManager.FindForm(FormID).getDriverId();
    	
    	
    	 if (!TruckManager.CheckLicense(trucknum, driver))
             return false;
    	DeliveryManager.EditForm(FormID, date, null, SiteManager.FindSite(Destiny), SiteManager.FindSite(Source), itemList, truckNumber, null, driverId);
    	return true;
    }
  
    public static void EnterDeliveryArea(String Name, List<String> Addresses){
    	List<Site> sites = SiteManager.FindSitesByName(Addresses);
        SiteManager.createDeliveryArea(Name, sites);
    }
    
    
    
    public static void addDriver(Employee employee, List<String> lic)
    {
        List<License> licen = new ArrayList<>();
    	for(String item : lic )
            licen.add(License.valueOf(item));

    	DriverManager.addDriver(employee, licen);
    	
    }
    
    public static void addTruck(int Trucknumber, String model, int netWeight, int maxWeight, String lic)
    {
    	License licen = License.valueOf(lic);
    	TruckManager.addTruck(Trucknumber, model, netWeight, maxWeight, licen);
    	
    }
    
    
    public static void addSite( String Address, String PhoneNumber, String ContantName, List<String> DeliveryArea, boolean isSupplier)
    {
    	List<DeliveryArea> Areas = SiteManager.FindAreasByName(DeliveryArea);
    	SiteManager.addSite(Address, PhoneNumber, ContantName, Areas, isSupplier);
    }
    
    public static void RemoveDriver(int id)
    {
    	DriverManager.RemoveDriver(id);
    }
    
    public static void EditEmployee(int id, Employee newEmp)
    {
    	DriverManager.EditEmployee(id, newEmp);
    }
    
    public static void AddOrEditDriver(int id, Employee newEmp)
    {
    	
    }
    
    
}
