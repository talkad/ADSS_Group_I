package DeliveryModule.BuisnessLayer;

import DeliveryModule.DAL.DAL;
import DeliveryModule.DAL.DeliveryFormDAL;
import org.omg.CORBA.portable.ApplicationException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryManager {
    private static List<DeliveryForm> Delivery = new ArrayList<DeliveryForm>();

    public static void init() throws ApplicationException {

            List<Object[]> l = DAL.LoadAll("DeliveryForm");
            for (Object[] item : l) {
                String[] items = ((String) item[8]).split("\n");
                ItemList list = new ItemList(Integer.parseInt(items[0].split(": ")[1]));
                for (int i = 1; i < items.length; i++) {
                    String[] ma = items[i].split(" X ");
                    list.addItem(new Item(ma[0].substring(3), Integer.parseInt(ma[1])));
                }
                CreateForm(Integer.parseInt((String) item[0]), (Date) item[1], (String) item[2], SiteManager.FindSite((String) item[5]), SiteManager.FindSite((String) item[4]), list, Integer.parseInt((String) item[7]), Integer.parseInt((String) item[6]), false);
            }


    }

    public static void CreateForm(int id, Date date,String time, Site Destiny, Site Source, ItemList itemList, int truckNumber, int driverId, boolean toSave) throws ApplicationException
    {
        DeliveryForm deliveryForm = null;
        if(id == -1)
            deliveryForm = new DeliveryForm(date, time, Destiny, Source, itemList, truckNumber, driverId);
        else
            deliveryForm = new DeliveryForm(id, date, time, Destiny, Source, itemList, truckNumber, driverId);
        Delivery.add(deliveryForm);
        int prev = -1;
        if(deliveryForm.getPrevForm() != null)
            prev = deliveryForm.getPrevForm().getID();
        if(toSave)
            DAL.Insert("DeliveryForm", new DeliveryFormDAL(deliveryForm.getID(), prev, date, time, Destiny, Source, itemList, truckNumber, -1, driverId));
    }
    public static void addWeghit(int truckWeight, LocalTime depTime, DeliveryForm form) throws ApplicationException
    {
        for (DeliveryForm item : Delivery)
        {
            if(item.equal(form)) {
                form.Depurture(truckWeight);
                int prev = -1;
                if(form.getPrevForm() != null)
                    prev = form.getPrevForm().getID();
                DAL.Update("DeliveryForm", new DeliveryFormDAL(form.getID(), prev, form.getDate(), form.getDepTime(), form.getDestiny(), form.getSource(), form.getItemList(), form.getTruckNumber(), truckWeight, form.getDriverId()));
            }
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
    
    public static void EditForm(int FormID, Date date, String depTime, Site Destiny, Site Source, ItemList itemList, Integer truckNumber, Integer truckWeight, int driverId)
    {
    	DeliveryForm Editee = FindForm(FormID);
    	DeliveryForm newForm = Editee.CreateEdit(date, depTime, Destiny, Source, itemList, truckNumber, truckWeight, driverId);
    	Delivery.add(newForm);
    }
}
