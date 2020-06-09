package DeliveryModule.DAL;

public class LicenseDAL implements iDAL {
	private int DriverId;
	private String license;
	public LicenseDAL(int id, String license)
	{
		this.DriverId = id;
		this.license = license;
	}
	@Override
	public String getFields() {
		// TODO Auto-generated method stub
		return "DriverID, License";
	}
	@Override
	public String getValues() {
		// TODO Auto-generated method stub
		return "" + this.DriverId +", '" + this.license + "'";
	}

}
