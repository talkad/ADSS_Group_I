import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omg.CORBA.portable.ApplicationException;

import DeliveryModule.InterfaceLayer.DoThinks;

public class Program {
	private static BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws ApplicationException, IOException{
       	DoThinks.Load();
    	System.out.println("please choose which moodule: 1 for Employe, 2 for Delivery");
    	if(buffer.readLine() == "2")
    		DeliveryModule.PresentationLayer.Start.main(args);
    	else
    		EmployeeModule.InterfaceLayer.Service.main(args);
    }
}
