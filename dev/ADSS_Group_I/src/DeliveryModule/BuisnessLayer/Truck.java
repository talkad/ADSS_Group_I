package DeliveryModule.BuisnessLayer;

import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.TruckDAL;
import org.omg.CORBA.portable.ApplicationException;

public class Truck {
    private int TruckNumber;
    private String Model;
    private int NetWeight;
    private int  MaxWeight;
    private License license;

    public Truck (int truckNumber, String model, int netWeight, int maxWeight, License license){
        TruckNumber = truckNumber;
        Model = model;
        NetWeight = netWeight;
        MaxWeight = maxWeight;
        this.license = license;
    }

    public License getLicense(){ return license; }
    public int getNumber() { return TruckNumber; }
    public int getMaxWeight() { return MaxWeight; }
    public boolean isMe(int Id)
    {
    	return this.TruckNumber == Id;
    }

    public void Save() throws ApplicationException {
        DAL.Insert("Truck", new TruckDAL(this.TruckNumber, this.Model, this.NetWeight, this.MaxWeight, this.license.toString()));
    }
}
