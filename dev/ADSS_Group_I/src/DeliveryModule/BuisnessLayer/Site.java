package DeliveryModule.BuisnessLayer;

import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.SiteDAL;
import org.omg.CORBA.portable.ApplicationException;

import java.util.List;


public abstract class Site {
    protected String Address;
    protected String PhoneNumber;
    protected String ContantName;
    protected List<DeliveryArea> DeliveryArea;
    protected int SiteID;

    public Site(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea){
        this.Address = Address;
        this.ContantName = ContantName;
        this.DeliveryArea = DeliveryArea;
        this.PhoneNumber = PhoneNumber;
        this.SiteID = 1;
    }

    public void AddArea(DeliveryArea area){
        DeliveryArea.add(area);
    }
    public String getAddress() { return this.Address; }
    public List<DeliveryArea> getAreas()
    {
    	return DeliveryArea;
    }
    
    public boolean isMe(String Address)
    {
		return this.Address == Address;
    }
    public boolean isMe(int siteID)
    {
		return this.SiteID == siteID;
    }
    
    public void setSiteID(int SiteID) {
    	this.SiteID = SiteID;
    }
    public void save(int isSupplier) throws ApplicationException {
    	if(isSupplier == 1)
    		isSupplier = this.SiteID;
        SiteDAL siteDAL = new SiteDAL(Address, PhoneNumber, ContantName, isSupplier);
        DAL.Insert("Site", siteDAL);
    }
}
