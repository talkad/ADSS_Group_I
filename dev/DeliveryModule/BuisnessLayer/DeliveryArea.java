package DeliveryModule.BuisnessLayer;

import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.DeliveryAreasDAL;
import org.omg.CORBA.portable.ApplicationException;

public class DeliveryArea {
    protected String Name;

    public DeliveryArea(String Name){
        this.Name = Name;
    }
    
    public String getName() { return Name;}

    public void save() throws ApplicationException {
        DeliveryAreasDAL d = new DeliveryAreasDAL(Name);
        DAL.Insert("Delivery_Areas", d);
    }

}
