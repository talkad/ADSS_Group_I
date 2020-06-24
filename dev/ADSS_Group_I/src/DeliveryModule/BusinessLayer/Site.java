package DeliveryModule.BusinessLayer;

import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.SiteDAL;
import org.omg.CORBA.portable.ApplicationException;

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
    public String getAddress() { return this.Address; }
    public List<DeliveryArea> getAreas()
    {
    	return DeliveryArea;
    }
    
    public boolean isMe(String Address)
    {
		return this.Address == Address;
    }

    public void save(int isSupplier) throws ApplicationException {
        SiteDAL siteDAL = new SiteDAL(Address, PhoneNumber, ContantName, isSupplier);
        DAL.Insert("Site", siteDAL);
    }
}
