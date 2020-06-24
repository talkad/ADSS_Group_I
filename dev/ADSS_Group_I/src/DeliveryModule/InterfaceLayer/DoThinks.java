package DeliveryModule.InterfaceLayer;

import DeliveryModule.BusinessLayer.*;
import EmployeeModule.BusinessLayer.*;
import org.omg.CORBA.portable.ApplicationException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DoThinks {

	// create form and save it on the Delivery manager and DB
    public static boolean CreateForm(String date, String time, String Destiny, String Source, int truckNumber, int driverId, int orderID) throws IOException, ParseException, ApplicationException {
        if (!TruckManager.CheckLicense(truckNumber, driverId))
            return false;
        
        if (!CheckShift(date, time, DriverManager.getDriver(driverId).getID()))
            return false;

        Site dest, source;
        if((dest =SiteManager.FindSite(Destiny)) == null ||(source = SiteManager.FindSite(Source)) == null)
        	return false;
        // save to delivery manager
        DeliveryManager.CreateForm(-1, date, time, dest, source, truckNumber, driverId, true, orderID);
        return true;
    }
    
    // check if the driver is in shift
    public static boolean CheckShift(String date, String time, int id) throws ApplicationException {
        return DriverManager.CheckShift(date, time, id);
    }
    
    // the Departure action
    public static String Departure(int formID, int truckWeight) throws ApplicationException {
    	DeliveryForm form = DeliveryManager.FindForm(formID);
    	// check if the truck can take all the items
    	if (!TruckManager.CheckWeight(form.getTruckNumber(), truckWeight))
            return null;
        DeliveryManager.addWeghit(truckWeight, LocalTime.now(), form);
        // send OrderID
        // _Inventory_.receiveOrder(form.getOrderID());
        return ""; //form.getItemList().toString();// print item list to driver
    } // ????????????????????????????????????????????????
    
    // if the delivery can happened, you need to change something 
    public static boolean EditForm(int FormID, String date,  String Destiny, String Source, Map<Integer, Integer> itemList, Integer truckNumber,  int driverId)
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
  
    // enter new delivery area answer save it
    public static void EnterDeliveryArea(String Name, List<String> Addresses) throws ApplicationException {
    	List<Site> sites = SiteManager.FindSitesByName(Addresses);
        SiteManager.createDeliveryArea(Name, sites, true);
    }
    
    
    // add new driver
    public static void addDriver(Employee employee, List<String> lic) throws ApplicationException
    {
        List<License> licen = new ArrayList<>();
    	for(String item : lic )
            licen.add(License.valueOf(item));

    	DriverManager.addDriver(employee, licen);
    	
    }
    
    // add new truck
    public static void addTruck(int Trucknumber, String model, int netWeight, int maxWeight, String lic)
    {
    	License licen = License.valueOf(lic);
    	TruckManager.addTruck(Trucknumber, model, netWeight, maxWeight, licen);
    	
    }
    
    // add new site and save it
    public static void addSite(String Address, String PhoneNumber, String ContantName, List<String> DeliveryArea, int isSupplier) throws ApplicationException
    {
    	List<DeliveryArea> Areas = SiteManager.FindAreasByName(DeliveryArea);
    	SiteManager.addSite(Address, PhoneNumber, ContantName, Areas, isSupplier, true);
    }
    
    // remove driver
    public static void RemoveDriver(int id) throws ApplicationException
    {
    	DriverManager.RemoveDriver(id);
    }
    
    // edit employee
    public static void EditEmployee(int id, Employee newEmp)
    {
    	DriverManager.EditEmployee(id, newEmp);
    }
    
    // add or edit driver
    public static void AddOrEditDriver(int id, Employee newEmp) throws ApplicationException
    {
    	DriverManager.AddOrEditDriver(id, newEmp);
    }
    
    // add license
    public static void addLicense(int id, String license) throws ApplicationException
    {
    	DriverManager.AddLicense(id, License.valueOf(license));
    }

    // delete form
    public static void DeleteForm(int OrderID) throws ApplicationException {
        DeliveryManager.DeleteForm(OrderID);
    }

    // set delivery for waiting order
    public static void DetermineDelivery()throws ApplicationException, ParseException, IOException{
    	List<Integer> FailOrder = new ArrayList<>(); // all the order we can't delivery
    	// get all the order we need to set
        Map<Integer, String> orderToSet = null;//SuppliersModules.DAL.OrderMapper.getInstance().getOrdersToSet();
        
        for(int orderID : orderToSet.keySet()){ // get the order
        	Order order = null;//SuppliersModules.DAL.OrderMapper.getInstance().GetOrder(orderID);
            
        	double weightItemList = 0; // get weight of item list
            Map<Integer, Integer> itemList = order.getItemList();
            int storeID = -1;//Connector.getInstance().getOrder(orderID).getStoreID();
            // get weight of the item list
            for(Integer itemID : itemList.keySet()) {
            	weightItemList += Connector.getInstance().getWeightItem(itemID, storeID) * itemList.get(itemID);
            }
            
            String date = orderToSet.get(orderID);
            String keepSearchEmployee = (checkOrderToDelivery(date, weightItemList, orderID, "1", true))[0];
            
            if(keepSearchEmployee.length() == 0)
            	keepSearchEmployee = (checkOrderToDelivery(date, weightItemList, orderID, "2", true))[0];
            if(keepSearchEmployee.length() == 0) { // can't set a delivery
            	FailOrder.add(orderID);
            }
            
        }
        // send Suppliers all the order we can't set delivery
        //postponeOrders(FailOrder);
    }// ????????????????????????????????????????????????
    
    public static String[] checkOrderToDelivery(String date, double weightItemList, int orderID, String shift, boolean toSave) throws ApplicationException, ParseException, IOException {
    	String[] DeliveryInfo = new String[4];
    	String dateShift = date + " " + shift;
    	
    	List<String> employees = Connector.getInstance().getDriversInShift(dateShift);
        // get all the drivers in shift that day
        List<DeliveryForm> forms = DeliveryManager.getFormByDate(date, shift);
        List<Truck> busyTruck = new ArrayList<>();
        // search for the available trucks
        for(DeliveryForm form : forms) {
        	busyTruck.add(TruckManager.getTruckByID(form.getTruckNumber()));
        }
        List<Truck> trucks = TruckManager.getAvailiableTruck(busyTruck);
        
        // check witch employees are available
        boolean keepSearchEmployee = true;
        for(String employeeID : employees) { // check witch driver is available for delivery
        	if(keepSearchEmployee) {
        		boolean employeeFlag = true;
        		int truckID = -1;
            	for(DeliveryForm form : forms) { // and check if there is an available truck for him that fit to his license
            		if(employeeFlag && form.getDriverId() ==  Integer.valueOf(employeeID))
            			employeeFlag = false;
            	}
            	
            	if(employeeFlag) // found employee
            	{
            		Map<Integer, Double> differenceWeight = new HashMap<>();
            		for(Truck truck : trucks) { // found truck with same license
        				if(truck.getMaxWeight() >= weightItemList ) {
        					if(DriverManager.getDriver(Integer.valueOf(employeeID)).isLicenseSet(truck.getLicense()))
        						differenceWeight.put(truck.getNumber(), truck.getMaxWeight() - weightItemList); // save all the weight of the available truck
        				}	
        			}
            		
            		// check who is the Lightest weight from the truck
            		double min = Integer.MAX_VALUE;
            		for(Integer keyTruckID : differenceWeight.keySet()) {
            			if(differenceWeight.get(keyTruckID) < min) {
            				min = differenceWeight.get(keyTruckID);
            				truckID = keyTruckID;
            			}
            		}
            		if(toSave) {// create the form
            			if(truckID != -1) {  // check if the truck can delivery items
                			keepSearchEmployee = !CreateForm(date, shift, "dest", "source", truckID, Integer.valueOf(employeeID), orderID);
                			DeliveryInfo[0] = date;
            				DeliveryInfo[1] = shift;
            				DeliveryInfo[2] = truckID + "";
            				DeliveryInfo[3] = employeeID;
            			} else { // can't find date
            				DeliveryInfo[0] = "";
            				DeliveryInfo[1] = shift;
            				DeliveryInfo[2] = truckID + "";
            				DeliveryInfo[3] = employeeID;
            			}	
            		}
            		else
            			{ // save the information for later 
            				if(truckID != -1)
            					DeliveryInfo[0] = date;
            				else
            					DeliveryInfo[0] = "";
            				DeliveryInfo[1] = shift;
            				DeliveryInfo[2] = truckID + "";
            				DeliveryInfo[3] = employeeID;
            			}
            	}
        	}
        }
        return DeliveryInfo;
    }
    public static boolean isPossibleUpdateForm(Map<Integer, Integer> newItems, int orderID) throws ApplicationException {
        DeliveryForm form = DeliveryManager.getFormByOrderID(orderID);
        int storeID = -1;//Connector.getInstance().getOrder(orderID).getStoreID();
        // get weight of the item list
        double weight = 0;
        for(int itemID : newItems.keySet()) {
        	weight += Connector.getInstance().getWeightItem(itemID, storeID) * newItems.get(itemID);
        }
        return TruckManager.CheckWeight(form.getTruckNumber(), weight);
    }// ????????????????????????????????????????????????

    public static String[] getPreviousDateForDelivery(Map<Integer, Integer> itemList, int storeID)throws ApplicationException, ParseException, IOException{
    	String[] setDelivery = new String[4];
    	// get weight of the item list
    	int itemID = -1;
    	for(int i : itemList.keySet())
    		itemID = i;
    	double weightItemList = Connector.getInstance().getWeightItem(itemID, storeID) * itemList.get(itemID); // something with item list
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	ZoneId defaultZoneId = ZoneId.systemDefault();
    	LocalDate nowtemp = java.time.LocalDate.now();
    	Date now = Date.from(nowtemp.atStartOfDay(defaultZoneId).toInstant());
    	// get all dates in this week
    	List<Date> dates = DeliveryManager.getDatesBetween(now, DeliveryManager.addDays(now, 7));
    	for(Date setDate : dates) { // search previous date
    		String date = dateFormat.format(setDate);
    		setDelivery = checkOrderToDelivery(date, weightItemList, -1, "1", false); // set appropriate delivery
    		if(setDelivery[0].length() != 0) // if the delivery Succeeded
        		return setDelivery;
    		else
    			setDelivery = checkOrderToDelivery(date, weightItemList, -1, "2", false);
    		if(setDelivery[0].length() != 0)
    			return setDelivery;
    	}
    	
    	// check the possibility the employees add driver
    	for(Date setDate : dates) {
    		List<String> AvailableEmployee = Connector.getInstance().getAvailableDrivers(setDate + " 1");
    		String date = dateFormat.format(setDate);
    		setDelivery = checkOrderToDelivery(date, weightItemList, -1, "1", false);
    		if(setDelivery[0].length() != 0) { // try to change the employee to add driver to shift
    			setDelivery = checkOrderToDelivery(date, weightItemList, -1, "2", false);
    			if( Connector.getInstance().addDriverToShift(setDelivery[0] + " " + setDelivery[1], Integer.parseInt(setDelivery[3]))) {
    				setDelivery[0] = date;
    				return setDelivery;
    			}
    		}
    		else {
    			if( Connector.getInstance().addDriverToShift(setDelivery[0] + " " + setDelivery[1], Integer.parseInt(setDelivery[3]))) {
    				setDelivery[0] = date;
    				return setDelivery;
    			}
    		}
    	}
    	setDelivery[0] = dateFormat.format(DeliveryManager.addDays(now, 8)); // tell suppliers that the delivery can happens only in next week
        return setDelivery; // delivery don't Succeeded
    }
    
    public static boolean isAddressExist(String Address) {
    	return (SiteManager.FindSite(Address) != null);
    }
    public static void Load() throws ApplicationException
    {
    	DriverManager.Loadall();
    	TruckManager.Loadall();
    	SiteManager.init();
    	DeliveryManager.init();
    }
    
    
    
    
}
