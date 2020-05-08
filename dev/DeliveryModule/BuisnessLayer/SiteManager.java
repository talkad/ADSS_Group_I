package DeliveryModule.BuisnessLayer;
import java.util.ArrayList;
import java.util.List;

public class SiteManager {
    private static List<Site> Sites = new ArrayList<>();
    private static List<DeliveryArea> Area = new ArrayList<>();

    public static void addSite( String Address, String PhoneNumber, String ContantName, List<DeliveryArea> DeliveryArea, boolean isSupplier )
    { 
    	if(isSupplier)
    		Sites.add(new Supplier(Address, PhoneNumber, ContantName, DeliveryArea));
    	else
    		Sites.add(new Shop(Address, PhoneNumber, ContantName, DeliveryArea));
    }
    

    public static void createDeliveryArea( String Name, List<Site> siteList){
        DeliveryArea da = new DeliveryArea(Name);
        Area.add(da);
        for (Site item : siteList){
            int index = Sites.indexOf(item);
            Sites.get(index).AddArea(da);
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
			if(deliveryArea.getName() == name)
				return deliveryArea;
		}
    	return null;
    }
    
    
}
