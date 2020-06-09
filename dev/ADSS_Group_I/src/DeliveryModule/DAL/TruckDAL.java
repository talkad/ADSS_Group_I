package DeliveryModule.DAL;


public class TruckDAL implements iDAL {
    private int TruckNumber;
    private String Model;
    private int NetWeight;
    private int  MaxWeight;
    private String license;
    
    public TruckDAL(int trucknum, String model, int netWeight, int maxWeight, String license)
    {
    	this.TruckNumber = trucknum;
    	this.Model = model;
    	this.NetWeight = netWeight;
    	this.MaxWeight = maxWeight;
    	this.license = license;
    }

	@Override
	public String getFields() {
		// TODO Auto-generated method stub
		return "Truck_Number, Model, Net_Weight, Max_Weight, License";
	}

	@Override
	public String getValues() {
		// TODO Auto-generated method stub
		return this.TruckNumber +", '" + this.Model + "', " + this.NetWeight + ", " + this.MaxWeight +", '" + this.license +"'";
	}
    
    

}
