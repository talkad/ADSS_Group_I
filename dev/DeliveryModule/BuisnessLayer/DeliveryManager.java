package DeliveryModule.BuisnessLayer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryManager {
    private static List<DeliveryForm> Delivery = new ArrayList<DeliveryForm>();

    public static void CreateForm(Date date,LocalTime time, Site Destiny, Site Source, ItemList itemList, int truckNumber, int driverId)
    {
        DeliveryForm deliveryForm = new DeliveryForm(date, time, Destiny, Source, itemList, truckNumber, driverId);
        Delivery.add(deliveryForm);
    }
    public static void addWeghit(int truckWeight, LocalTime depTime, DeliveryForm form)
    {
        for (DeliveryForm item : Delivery)
        {
            if(item.equal(form))
                form.Depurture(truckWeight);
        }
    }
    
    public static DeliveryForm FindForm(int ID)
    {
    	for (DeliveryForm deliveryForm : Delivery) {
			if (deliveryForm.isMe(ID))
				return deliveryForm;
		}
    	return null;
    }

    public static DeliveryForm Clone(DeliveryForm form){
        //DeliveryForm newform = new DeliveryForm();
        return null;
    }
    
    public static void EditForm(int FormID, Date date, LocalTime depTime, Site Destiny, Site Source, ItemList itemList, Integer truckNumber, Integer truckWeight, int driverId)
    {
    	DeliveryForm Editee = FindForm(FormID);
    	DeliveryForm newForm = Editee.CreateEdit(date, depTime, Destiny, Source, itemList, truckNumber, truckWeight, driverId);
    	Delivery.add(newForm);
    }
}
