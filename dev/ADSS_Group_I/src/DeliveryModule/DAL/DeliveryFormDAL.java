package DeliveryModule.DAL;

import DeliveryModule.BuisnessLayer.*;

import java.util.Date;

public class DeliveryFormDAL implements iDAL {

    private int Id;
    private int PrevForm;
    private String date;
    private String DepTime;
    private Site Destiny;
    private Site Source;
    private int TruckNumber;
    private int TruckWeight;
    private int DriverID;
    private int OrderID;

    public DeliveryFormDAL(int id, int prevForm, String date, String depTime, Site destiny, Site source, int truckNumber, int truckWeight, int driverID, int OrderID){
        this.Id = id;
        this.PrevForm = prevForm;
        this.date = date;
        this.DepTime = depTime;
        this.Destiny = destiny;
        this.Source = source;
        this.TruckNumber = truckNumber;
        this.TruckWeight = truckWeight;
        this.DriverID = driverID;
        this.OrderID = OrderID;
    }
    public DeliveryFormDAL(int orderID) {
    	this.OrderID = orderID;
    }
    public DeliveryFormDAL(String date) {
    	this.date = date;
    }
    public String getFields(){
        return "FormId, Date, DepTime, TruckWeight, Source, Destination, Driver, Truck, PrevForm, OrderID";
    }

    public String getValues(){
        return Id + ", '" + date + "', '" + DepTime + "', " + TruckWeight + ", '" + Source.getAddress() + "', '" + Destiny.getAddress() + "', " + DriverID + ", " + TruckNumber + ", '" + /*itemList.toString() +*/ "', " + PrevForm + ", " + OrderID;
    }
}
