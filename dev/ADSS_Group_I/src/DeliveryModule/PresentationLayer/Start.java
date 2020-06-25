package DeliveryModule.PresentationLayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DeliveryModule.BuisnessLayer.*;
import DeliveryModule.DAL.DAL;
import DeliveryModule.InterfaceLayer.DoThinks;
import org.omg.CORBA.portable.ApplicationException;

public class Start {
	
	private static BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
 
    public static void StartDelivery() {
    	while(true) {
    		try {
				System.out.println("Welcome to Delivery !");
				System.out.println(
						"1. Add Delivery Area\n" +
								"2. Order Stock Completion\n" +
								"3. Create Delivery Form\n" +
								"4. Departure for delivery\n" +
								"5. Add Truck\n" +
								"6. Add Driver (does nothing)\n" +
								"7. Add Site\n" +
								"8. Add License\n" +
								"9. Exit");
				//String input =

				String line = buffer.readLine();
				switch (line) {
					case ("1"):
						InsertDeliveryArea();
						break;
					case ("2"):
						OrderStock();
						break;
					case ("3"):
						CreateFrom();
						break;
					case ("4"):
						Departure();
						break;
					case ("5"):
						AddTruck();
						break;
					case ("6"):
						AddDriver();
						break;
					case ("7"):
						AddSite();
						break;
					case ("8"):
						AddLicense();
						break;
					case("9"):
						return;
					

				}
			}
    		catch (ApplicationException e){
				System.out.println(e.getId());

			}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
    public static void main(String[] args){
    	StartDelivery();
    }
    
    
    
    private static void InsertDeliveryArea() throws IOException, ApplicationException
    {
    	System.out.println();
    	System.out.println("please enter the name of the new Delivery Area");
    	String name = buffer.readLine();
		List<String> Areas = new ArrayList<String>();
    	System.out.println("would you like to enter sites in the Area? Y/N");
    	while( buffer.readLine() == "Y")
    	{
    		System.out.println("Please enter the site address:");
    		Areas.add(buffer.readLine());
    		System.out.println("would you like to enter more sites? Y/N");
    	}
    	DoThinks.EnterDeliveryArea(name, Areas);
    	
    	
    }
    
    private static void OrderStock() throws IOException, ParseException, ApplicationException {
    	CreateFrom();
    }
    
    private static void CreateFrom() throws IOException, ParseException, ApplicationException {
    	
    	System.out.println("\n Delivery Form Creation page \n");
    	
    	
		System.out.println("please enter departure date");
		String date = buffer.readLine();

		System.out.println("please enter departure time, 1 for morning delivery and 2 for evening delivery");
		String time = buffer.readLine();
		//String[] arr = timestring.split(":");
		//LocalTime time = LocalTime.of(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),Integer.parseInt(arr[2]));
		
		System.out.println("please enter destiny");	
		String destinyAdd = buffer.readLine();
		
		System.out.println("please enter Source");	
		String sourceAdd = buffer.readLine();
		/*
		System.out.println("please enter item list: ");
		ItemList items = getItemList();
	*/
		System.out.println("please enter truck number");
		Integer	truck = Integer.parseInt(buffer.readLine());

		System.out.println("please enter driver id");
		int driverId = Integer.parseInt(buffer.readLine());

		System.out.println("please enter order ID");
		Integer	orderID = Integer.parseInt(buffer.readLine());
		
		boolean succes = DoThinks.CreateForm(date, time, destinyAdd, sourceAdd, truck, driverId, orderID);
		if(succes)
			System.out.println("Form Created succesfully");
		else
			System.out.println("Creation faild");
		System.out.println("\n");
    	
    	
    }
    
    private static void Departure() throws IOException, ParseException, ApplicationException
    {
    	System.out.println();
    	System.out.println("please enter Form number");
    	int form = Integer.parseInt( buffer.readLine());
    	System.out.println("Please enter truck Weight");
    	int weight = Integer.parseInt(buffer.readLine());
    	String result = DoThinks.Departure(form, weight);
    	if(result == null)
    		ProccedToEdit(form);
    	else
    	{
    		System.out.println();
    		System.out.println("Item List detils:");
    		System.out.println(result);
    		System.out.println("/n please Confirm by pressing any button \n\n");
    		buffer.read();
    	}
    	
    	
    }

    private static void ProccedToEdit(int Form) throws IOException, ParseException
    {
    	System.out.println("/n the Truck is OverWeight and the delivery may not commence");
    	System.out.println("would you like to edit the Form? Y/N");
    	if(buffer.readLine().equals("Y"))
    	{
    		String dep;
    		System.out.println("would you like to Edit departure date? Y/N");
    		if(buffer.readLine().equals("Y"))
    			dep = buffer.readLine();
    		else
    			dep = null;
    		
    		String destinyAdd;
    		System.out.println("would you like to Edit destiny? Y/N");
    		if(buffer.readLine().equals("Y"))
    			destinyAdd = buffer.readLine();
    		else
    			destinyAdd = null;
    		
    		String sourceAdd;
    		System.out.println("would you like to Edit source? Y/N");
    		if(buffer.readLine().equals("Y"))
    			sourceAdd = buffer.readLine();
    		else
    			sourceAdd = null;
    		
    		Map<Integer, Integer> items;
    		System.out.println("would you like to Edit items list? Y/N");
    		if(buffer.readLine().equals("Y"))
    			items = getItemList();
    		else
    			items = null;
    		
    		Integer truck;
    		System.out.println("would you like to Edit truck number? Y/N");
    		if(buffer.readLine().equals("Y"))
    			truck = Integer.parseInt(buffer.readLine());
    		else
    			truck = null;
    		
    		int driver;
    		System.out.println("would you like to Edit Driver? Y/N");
    		if(buffer.readLine().equals("Y"))
    			driver = Integer.parseInt(buffer.readLine());
    		else
    			driver = -1;

    		DoThinks.EditForm(Form, dep, destinyAdd, sourceAdd, items, truck, driver);
    		
    	}
    	System.out.println(); System.out.println();
    }
    
    
    
    private static Map<Integer, Integer> getItemList() throws IOException
    {
    	Map<Integer, Integer> ans = new HashMap<Integer, Integer>();
    	System.out.println("please enter Item id");
    	Integer id = Integer.parseInt(buffer.readLine());
    	System.out.println("please Enter Quantity");
    	Integer quantity = Integer.parseInt(buffer.readLine());
    	ans.put(id, quantity);
    	System.out.println("would you like to enter another item? Y/N");
    	while(buffer.readLine().equals("Y"))
    	{
    		System.out.println("please enter Item id");
        	id = Integer.parseInt(buffer.readLine());
        	System.out.println("please Enter Quantity");
        	quantity = Integer.parseInt(buffer.readLine());
        	ans.put(id, quantity);
    		System.out.println("would you like to enter another item? Y/N");
    	}

    	return ans;
    }
    
    

    
    
    private static void AddTruck() throws IOException
    {
    	System.out.println("please enter truck number");
    	int num = Integer.parseInt(buffer.readLine());
    	System.out.println("please enter model");
    	String model = buffer.readLine();
    	System.out.println("please enter net weight");
    	int netWeight = Integer.parseInt(buffer.readLine());
    	System.out.println("please enter max weight");
    	int maxWeight = Integer.parseInt(buffer.readLine());
    	System.out.println("please enter the required license for the truck");
    	String license = buffer.readLine();
    	DoThinks.addTruck(num, model, netWeight, maxWeight, license);
    }
    
    
    
    
    private static void AddDriver() throws IOException
    {
    	/*
    	System.out.println("please enter Driver name");
    	String name = buffer.readLine();
		System.out.println("please enter Driver ID");
		String id = buffer.readLine();
    	System.out.println("please enter licenses");
    	String license = buffer.readLine();
    	String[] arr = license.split(", ");
    	List<String> lic = new ArrayList<>();
    	for(int i=0; i<arr.length;i++)
    		lic.add(arr[i]);
    	DoThinks.addDriver(name, id, lic);
    	*/
    
    }
    
    private static void AddSite() throws IOException, ApplicationException
    {
    	System.out.println("would you like to add a Supplier or a Shop? for Suplier enter Y. for Shop enter N");
    	String name = buffer.readLine();
    	boolean isSupplie = name == "Y";
    	int isSupplier = 0;
    	if(isSupplie)
    		isSupplier = 1;
    	System.out.println("please enter the site Address");
    	String address = buffer.readLine();
    	System.out.println("please enter phone number");
    	String phone = buffer.readLine();
    	System.out.println("pleae enter the contact name");
    	String contact = buffer.readLine();
    	System.out.println("would you like to add a Delivery Area? Y/N");
    	List<String> areas = new ArrayList<>();
    	while(buffer.readLine().equals("Y"))
    	{
    		System.out.println("please enter the Area name");
    		areas.add(buffer.readLine());
    		System.out.println("would you like to enter another Delivery Area? Y/N");
    	}
    	DoThinks.addSite(address, phone, contact, areas, isSupplier);
    }
    
    private static void AddLicense() throws IOException, ApplicationException
    {
    	System.out.println("please enter Driver ID");
    	int ID = Integer.parseInt(buffer.readLine());
    	
    	System.out.println("please enter the Licnese");
    	String Licnese = buffer.readLine();
    	
    	DoThinks.addLicense(ID, Licnese);
    }
}
