package DeliveryModule.DAL;

import DeliveryModule.BuisnessLayer.*;

import java.util.Date;

public class DeliveryFormDAL implements iDAL {

    private int Id;
    private int PrevForm;
    private Date date;
    private String DepTime;
    private Site Destiny;
    private Site Source;
    private ItemList itemList;
    private int TruckNumber;
    private int TruckWeight;
    private int DriverID;

    public DeliveryFormDAL(int id, int prevForm, Date date, String depTime, Site destiny, Site source, ItemList itemList, int truckNumber, int truckWeight, int driverID){
        this.Id = id;
        this.PrevForm = prevForm;
        this.date = date;
        this.DepTime = depTime;
        this.Destiny = destiny;
        this.Source = source;
        this.itemList = itemList;
        this.TruckNumber = truckNumber;
        this.TruckWeight = truckWeight;
        this.DriverID = driverID;
    }

    public String getFields(){
        return "FormId, Date, DepTime, TruckWeight, Source, Destination, Driver, Truck, ItemList, PrevForm";
    }

    public String getValues(){
        return Id + ", '" + date.toString() + "', '" + DepTime + "', " + TruckWeight + ", '" + Source.getAddress() + "', '" + Destiny.getAddress() + "', " + DriverID + ", " + TruckNumber + ", '" + itemList.toString() + "', " + PrevForm;
    }
}
