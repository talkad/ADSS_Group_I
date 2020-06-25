package DeliveryModule.BuisnessLayer;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;

import DeliveryModule.DAL.DAL;

public class TruckManager {
    private static List<Truck> Trucks = new ArrayList<>();

    public static boolean CheckLicense(int truckNumber, int driverId){
        License l1 = null;
        for ( Truck item : Trucks )
        {
            if (item.getNumber() == truckNumber)
                l1 = item.getLicense();
        }
        return DriverManager.CheckLicense(l1, driverId);
    }

    public static boolean CheckWeight(int truckNumber, double NowWeight){
        int weight = -1;
        for (Truck item : Trucks){
            if (item.getNumber() == truckNumber)
                weight = item.getMaxWeight();
        }
        return NowWeight <= weight;
    }

    public static void addTruck(int Trucknumber, String model, int netWeight, int maxWeight, License lic) throws ApplicationException
    {
        Truck newbie = new Truck(Trucknumber, model, netWeight, maxWeight, lic);
        Trucks.add(newbie);
        newbie.Save();
    }
    
    public static Truck getTruckByID(int ID){
    	for (Truck truck : Trucks) {
			if (truck.isMe(ID))
				return truck;
		}
    	return null;
    }
    
    public static List<Truck> getAvailiableTruck(List<Truck> busyTruck){
    	List<Truck> ansTruck = new ArrayList<>();
    	for(Truck truck : Trucks) {
    		boolean flag = true;
    		for(Truck Btruck : busyTruck) {
    			if(truck.getNumber() == Btruck.getNumber())
    				flag = false;
    		}
    		if(flag)
    			ansTruck.add(truck);
    	}
    	return ansTruck;
    }

    public static void Loadall() throws ApplicationException
    {
        List<Object[]> trucks = DAL.LoadAll("Truck");
        for (Object[] truckarr : trucks) {
            Truck truck = new Truck((int)truckarr[0], (String)truckarr[1], (int)truckarr[2], (int)truckarr[3], License.valueOf((String)truckarr[4]));
            Trucks.add(truck);
        }
    }
}
