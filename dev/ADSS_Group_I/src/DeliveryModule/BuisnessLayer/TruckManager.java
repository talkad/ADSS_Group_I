package DeliveryModule.BuisnessLayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;

import DeliveryModule.DAL.DAL;

public class TruckManager {
    private static List<Truck> Trucks = new ArrayList<Truck>();

    public static boolean CheckLicense(int truckNumber, int driverId){
        License l1 = null;
        for ( Truck item : Trucks )
        {
            if (item.getNumber() == truckNumber)
                l1 = item.getLicense();
        }
        return DriverManager.CheckLicense(l1, driverId);
    }

    public static boolean CheckWeight(int truckNumber, int NowWeight){
        int weight = -1;
        for (Truck item : Trucks){
            if (item.getNumber() == truckNumber)
                weight = item.getMaxWeight();
        }
        return NowWeight <= weight;
    }


    public static void addTruck(int Trucknumber, String model, int netWeight, int maxWeight, License lic)
    {
        Trucks.add(new Truck(Trucknumber, model, netWeight, maxWeight, lic));
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
