package DeliveryModule.BuisnessLayer;

import DeliveryModule.DAL.DeliveryFormDAL;

import java.util.Date;
import java.util.Map;
import java.time.*;

public class DeliveryForm {
	private static int IDCounter = 0;
	
	private final int Id;
    private DeliveryForm PrevForm;
    private String date;
    private String DepTime;
    private Site Destiny;
    private Site Source;
    private int TruckNumber;
    private int TruckWeight;
    private int DriverID;
    private int OrderID;



    public DeliveryForm(String date, String depTime, Site Destiny, Site Source, int truckNumber, int driverId, int orderID){
    	this.Id = IDCounter;
    	IDCounter++;
    	this.date = date;
    	this.DepTime = depTime;
        this.Destiny = Destiny;
        this.DriverID = driverId;
        this.Source = Source;
        this.TruckNumber = truckNumber;
        this.TruckWeight = -1;
        this.OrderID = orderID;
    }

    public DeliveryForm(int Id, String date, String depTime, Site Destiny, Site Source, int truckNumber, int driverId, int orderID){
        this.Id = Id;
        if(Id >= IDCounter)
            IDCounter = Id + 1;
        this.date = date;
        this.DepTime = depTime;
        this.Destiny = Destiny;
        this.DriverID = driverId;
        this.Source = Source;
        this.TruckNumber = truckNumber;
        this.TruckWeight = -1;
        this.OrderID = orderID;
    }
    
    private DeliveryForm(DeliveryForm prevForm, String date, String depTime, Site Destiny, Site Source, int truckNumber, Integer truckWeight, int driverId, int orderID){
        this.Id = IDCounter;
        IDCounter++;
    	this.date = date;
    	this.DepTime = depTime;
        this.Destiny = Destiny;
        this.PrevForm = prevForm;
        this.Source = Source;
        this.TruckNumber = truckNumber;
        this.TruckWeight = -1;
        this.DriverID = driverId;
        this.OrderID = orderID;
    }

    public void Depurture(int truckWeight){
        this.TruckWeight = truckWeight;
    }
    public void SetTruckNum(int truckNumber) { this.TruckNumber = truckNumber; }
    public void SetSource( Site source ) { this.Source = source; }
    public Site getDestiny() { return this.Destiny; }
    public Site getSource() { return this.Source; }
    public int getTruckNumber() { return TruckNumber; }
    public String getDepTime() { return DepTime; }
    public int getDriverId() { return DriverID; }
    public String getDate() {return date;}
    public int getID() { return this.Id; }
    public DeliveryForm getPrevForm() { return this.PrevForm; }
    public int getOrderID() { return this.OrderID; }

    public boolean equal(DeliveryForm other){
       return this.Id == other.Id;
    }

    
    public boolean isMe(int Id)
    {
    	return this.Id == Id;
    }
    
    
    public DeliveryForm CreateEdit(String date, String depTime, Site Destiny, Site Source, Map<Integer, Integer> itemList, Integer truckNumber, Integer truckWeight, int driverId, int orderID)
    {
    	String newDate;
    	String newDepTime;
    	Site newDestiny, newSource;
    	int newTruck;
    	int newWeight; 
    	int newDriver;
    	if(date != null)
    		newDate = date;
    	else
    		newDate = this.date;
    	if(depTime != null)
    		newDepTime = depTime;
    	else
    		newDepTime = this.DepTime;
    	if(Destiny != null)
    		newDestiny = Destiny;
    	else
    		newDestiny = this.Destiny;
    	if(Source != null)
    		newSource = Source;
    	else
    		newSource = this.Source;
    	/*
    	if(itemList != null)
    		newList = itemList;
    	else
    		newList = this.itemList;	
    		*/
    	if(truckNumber != null)
    		newTruck = truckNumber;
    	else
    		newTruck = this.TruckNumber;
    	if(truckWeight != null)
    		newWeight= truckWeight;
    	else
    		newWeight = this.TruckWeight;
    	if(driverId != -1)
    		newDriver = driverId;
    	else
    		newDriver = this.DriverID;
    	
    	
    	return new DeliveryForm(this, newDate, newDepTime, newDestiny, newSource, newTruck, newWeight, newDriver, orderID);
    	
    }

}



