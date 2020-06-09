package DeliveryModule.DAL;

import java.util.List;

public class DriverDAL implements iDAL {
	private int ID;
	
	public DriverDAL(int id )
	{
		this.ID = id;
	}

	@Override
	public String getFields() {
		// TODO Auto-generated method stub
		return "DriverID";
	}

	@Override
	public String getValues() {
		// TODO Auto-generated method stub
		return ID +"";
	}
	
	

}
