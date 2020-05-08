package DeliveryModule.BuisnessLayer;

import java.util.List;


public abstract class Site {
    protected String Address;
    protected String PhoneNumber;
    protected String ContantName;
    protected List<DeliveryArea> DeliveryArea;

    public Site(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea){
        this.Address = Address;
        this.ContantName = ContantName;
        this.DeliveryArea = DeliveryArea;
        this.PhoneNumber = PhoneNumber;
    }

    public void AddArea(DeliveryArea area){
        DeliveryArea.add(area);
    }
    
    public List<DeliveryArea> getAreas()
    {
    	return DeliveryArea;
    }
    
    public boolean isMe(String Address)
    {
		return this.Address == Address;
    }

}
