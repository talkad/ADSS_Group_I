package DeliveryModule.BusinessLayer;


import org.omg.CORBA.portable.ApplicationException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class DeliveryManager {
    private static List<DeliveryForm> Delivery = new ArrayList<DeliveryForm>();

    public static void init() throws ApplicationException {

            List<Object[]> l = DAL.LoadAll("DeliveryForm");
            for (Object[] item : l) {
                CreateForm(Integer.parseInt((String) item[0]), (String) item[1], (String) item[2], SiteManager.FindSite((String) item[5]), SiteManager.FindSite((String) item[4]), Integer.parseInt((String) item[7]), Integer.parseInt((String) item[6]), false, Integer.parseInt((String) item[7]));
            }


    }

    public static void CreateForm(int id, String date,String time, Site Destiny, Site Source, int truckNumber, int driverId, boolean toSave, int orderID) throws ApplicationException
    {
        DeliveryForm deliveryForm = null;
        if(id == -1)
            deliveryForm = new DeliveryForm(date, time, Destiny, Source, truckNumber, driverId, orderID);
        else
            deliveryForm = new DeliveryForm(id, date, time, Destiny, Source, truckNumber, driverId, orderID);
        Delivery.add(deliveryForm);
        int prev = -1;
        if(deliveryForm.getPrevForm() != null)
            prev = deliveryForm.getPrevForm().getID();
        if(toSave)
            DAL.Insert("DeliveryForm", new DeliveryFormDAL(deliveryForm.getID(), prev, date, time, Destiny, Source, truckNumber, -1, driverId, deliveryForm.getOrderID()));
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
                DAL.Update("DeliveryForm", new DeliveryFormDAL(form.getID(), prev, form.getDate(), form.getDepTime(), form.getDestiny(), form.getSource(), form.getTruckNumber(), truckWeight, form.getDriverId(), form.getOrderID()));
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
    
    public static void EditForm(int FormID, String date, String depTime, Site Destiny, Site Source, Map<Integer, Integer> itemList, Integer truckNumber, Integer truckWeight, int driverId)
    {
    	DeliveryForm Editee = FindForm(FormID);
    	DeliveryForm newForm = Editee.CreateEdit(date, depTime, Destiny, Source, itemList, truckNumber, truckWeight, driverId, Editee.getOrderID());
    	Delivery.add(newForm);
    }

    public static void DeleteForm(int order) throws ApplicationException {
        DAL.Delete("DeliveryForm", new DeliveryFormDAL(order), 11);
    }

    public static List<DeliveryForm> getFormByDate(String date, String shift) throws ParseException, ApplicationException {
    	List<DeliveryForm> ans = new ArrayList<>();
    	int[] arr = new int[2];
    	arr[0] = 1;
    	arr[1] = 2;
    	List<Object[]> forms = DAL.Select("DeliveryForm", new DeliveryFormDAL(date), arr);
    	for (Object[] item : forms) {
            String[] items = ((String) item[8]).split("\n");
            ItemList list = new ItemList(Integer.parseInt(items[0].split(": ")[1]));
            for (int i = 1; i < items.length; i++) {
                String[] ma = items[i].split(" X ");
                list.addItem(new Item(ma[0].substring(3), Integer.parseInt(ma[1])));
            }
            ans.add(new DeliveryForm(Integer.parseInt((String) item[0]), (String) item[1], (String) item[2], SiteManager.FindSite((String) item[5]), SiteManager.FindSite((String) item[4]), Integer.parseInt((String) item[7]), Integer.parseInt((String) item[6]), Integer.parseInt((String) item[7])));
    	}
    	return ans;
    }

    public static DeliveryForm getFormByOrderID(int orderID) throws ApplicationException {
    	int[] arr = new int[1];
    	arr[0] = 11;
        List<Object[]> forms = DAL.Select("DeliveryForm", new DeliveryFormDAL(orderID), arr);
        Object[] item = forms.get(0);
            String[] items = ((String) item[8]).split("\n");
            ItemList list = new ItemList(Integer.parseInt(items[0].split(": ")[1]));
            for (int i = 1; i < items.length; i++) {
                String[] ma = items[i].split(" X ");
                list.addItem(new Item(ma[0].substring(3), Integer.parseInt(ma[1])));
            }
        return new DeliveryForm(Integer.parseInt((String) item[0]), (String) item[1], (String) item[2], SiteManager.FindSite((String) item[5]), SiteManager.FindSite((String) item[4]), Integer.parseInt((String) item[6]), Integer.parseInt((String) item[7]), Integer.parseInt((String) item[8]));
    }
    
    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
    		List<Date> datesInRange = new ArrayList<>();
    		Calendar calendar = new GregorianCalendar();
    		calendar.setTime(startDate);
    		Calendar endCalendar = new GregorianCalendar();
    		
    		while (calendar.before(endCalendar)) {
    			Date result = calendar.getTime();
    			datesInRange.add(result);
    			calendar.add(Calendar.DATE, 1);
    			}
    		return datesInRange;
    		}
    
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}




