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
        this.SiteID = 0;
    }

    public Site(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea, int siteID){
        this.Address = Address;
        this.ContantName = ContantName;
        this.DeliveryArea = DeliveryArea;
        this.PhoneNumber = PhoneNumber;
        this.SiteID = siteID;
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
		return this.Address.equals(Address);
    }
    public boolean isMe(int siteID)
    {
		return this.SiteID == siteID;
    }
    
    public void setSiteID(int SiteID) {
    	this.SiteID = SiteID;
    }

    public void save(int isSupplier) throws ApplicationException {
        SiteDAL siteDAL = new SiteDAL(Address, PhoneNumber, ContantName, isSupplier);
        if(isSupplier == 1 || isSupplier == -1){
            DAL.Insert("Site", siteDAL);
        }
        else
            DAL.Update("Site", siteDAL, "Address = " + "'" + Address + "'");
    }
}
