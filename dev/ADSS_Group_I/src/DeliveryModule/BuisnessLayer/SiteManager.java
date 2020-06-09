package DeliveryModule.BuisnessLayer;
import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.SiteAreaDAL;
import org.omg.CORBA.portable.ApplicationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SiteManager {
    private static List<Site> Sites = new ArrayList<>();
    private static List<DeliveryArea> Area = new ArrayList<>();

    public static void init() throws ApplicationException {

			List<Object[]> l = DAL.LoadAll("Site");
			List<Object[]> l2 = DAL.LoadAll("Site_Areas");
			List<Object[]> l3 = DAL.LoadAll("Delivery_Areas");
			for (Object[] area : l3) {
				createDeliveryArea((String)area[0], null, false);
			}
			for (Object[] site : l) {
				List<DeliveryArea> areas = new ArrayList<>();
				for (Object[] area : l2) {
					if (area[0].equals(site[0]))
						areas.add(FindArea((String) area[1]));
				}
				addSite((String) site[0], (String) site[2], (String) site[1], areas, (int)site[3], false);

			}


	}
    public static void addSite(String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea, int isSupplier, boolean toSave) throws ApplicationException {
		Site s;
    	if (isSupplier == 0)
			s = new Supplier(Address, PhoneNumber, ContantName, DeliveryArea);
    	else
			s = new Shop(Address, PhoneNumber, ContantName, DeliveryArea);
		if(toSave) {
			s.save(isSupplier);
			if(DeliveryArea != null) {
				for (DeliveryArea item : DeliveryArea) {
					int index = Sites.indexOf(item);
					//Sites.get(index).AddArea(da);
					SiteAreaDAL sad = new SiteAreaDAL(Address, item.Name);
					if (toSave)
						DAL.Insert("Site_Areas", sad);
				}
			}
		}
		Sites.add(s);
	}

    public static void createDeliveryArea( String Name, List<Site> siteList, boolean toSave) throws ApplicationException {
        DeliveryArea da = new DeliveryArea(Name);
        Area.add(da);
        if(toSave)
        	da.save();
        if(siteList != null) {
			for (Site item : siteList) {
				int index = Sites.indexOf(item);
				Sites.get(index).AddArea(da);
				SiteAreaDAL sad = new SiteAreaDAL(item.Address, da.Name);
				if (toSave)
					DAL.Insert("Site_Areas", sad);
			}
		}
    }
    public static List<Site> getSite() { return Sites; }
    public static List<DeliveryArea> getArea() { return Area; }
    
    public static List<Site> FindSitesByName(List<String> Addresses)
    {
    	List<Site> ans = new ArrayList<>();
    	for (String address : Addresses) {
    		Site site = FindSite(address);
    		if(site != null)
    			ans.add(site);
		}
    	return ans;
	
    }
    public static Site FindSite(String Address)
    {
		for (Site site : Sites) {
			if(site.isMe(Address))
				return site;
		}
		return null;
    }
    
    public static List<DeliveryArea> FindAreasByName(List<String> Names)
    {
    	List<DeliveryArea> ans = new ArrayList<>();
    	for (String name : Names) {
    		DeliveryArea area = FindArea(name);
    		if(area != null)
    			ans.add(area);
		}
    	return ans;
	
    }
    public static DeliveryArea FindArea(String name)
    {
    	for (DeliveryArea deliveryArea : Area) {
			if(deliveryArea.getName().equals(name))
				return deliveryArea;
		}
    	return null;
    }
    

}
