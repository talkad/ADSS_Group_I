/*package DeliveryModule.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import DeliveryModule.InterfaceLayer.*;
import DeliveryModule.BuisnessLayer.*;


class Tests {
	static LinkedList<String> emptyList = new LinkedList<>();

	@Test
	void test1() {
		assertEquals(null,  DriverManager.getDriver("yossi"));
		DoThinks.addDriver("yossi", "A");
		assertEquals(License.A, DriverManager.getDriver("yossi").DriverLicense);
		
	}
	
	@Test
	void test2() {
		assertEquals(null, SiteManager.FindSite("tel aviv"));
		DoThinks.addSite("tel aviv", "666", "devil", emptyList, false);
		assertEquals(Shop.class, SiteManager.FindSite("tel aviv").getClass());
	}
	
	@Test
	void test3() {
		assertEquals(null, SiteManager.FindArea("israel"));
		DoThinks.EnterDeliveryArea("israel", emptyList);
		assertEquals("israel", SiteManager.FindArea("israel").getName());
	}
	
	@Test
	void test4() {
		DoThinks.addTruck(1, "Taranta", 1, 10, "A");
		assertEquals(false, TruckManager.CheckWeight(1, 40));
		assertEquals(true, TruckManager.CheckWeight(1, 5));
	}
	
	@Test
	void test5() {
		DoThinks.addDriver("or", "B");
		DoThinks.addTruck(2, "", 1, 10, "A");
		DoThinks.addDriver("orly", "A");
		assertEquals(false, TruckManager.CheckLicense(2, "or"));
		assertEquals(true, TruckManager.CheckLicense(2, "orly"));
		
	}
	
	@Test
	void test6() {
		DoThinks.EnterDeliveryArea("Area51", emptyList);
		List<String> areas = new LinkedList<>();
		areas.add("Area51");
		DoThinks.addSite("abc", "123", "ggg", areas, true);
		List<String> sites = new LinkedList<>();
		sites.add("abc");
		DoThinks.EnterDeliveryArea("Area52", sites);
		List<DeliveryArea> actualAreas = SiteManager.FindSite("abc").getAreas();
		assertEquals(2, actualAreas.size());
		if(actualAreas.get(0).getName() == "Area51" && actualAreas.get(1).getName() == "Area52");
		else
			fail("");
		
			
		
	}
	

	@Test
	void test7() {
		DoThinks.addDriver("D1", "A");
		DoThinks.addTruck(5, "a", 1, 10, "A");
		DoThinks.addSite("s1", "asda", "a", emptyList, true);
		DoThinks.addSite("s2", "123", "a", emptyList, false);
		ItemList items = new ItemList(11);
		items.addItem(new Item("bamba", 5));
		DoThinks.CreateForm(new Date(), "s1", "s2", items, 5, "D1");
		assertNotEquals(null, DeliveryManager.FindForm(0));
	}
	
	@Test
	void test8() {
		DoThinks.addDriver("D2", "B");
		DoThinks.addTruck(6, "a", 1, 10, "A");
		DoThinks.addSite("s3", "asda", "a", emptyList, true);
		DoThinks.addSite("s4", "123", "a", emptyList, false);
		ItemList items = new ItemList(11);
		items.addItem(new Item("bamba", 5));
		boolean result = DoThinks.CreateForm(new Date(), "s3", "s4", items, 6, "D2");
		assertEquals(false, result);
	}
	
	@Test
	void test9() {
		DoThinks.addDriver("D3", "A");
		DoThinks.addTruck(7, "a", 1, 10, "A");
		DoThinks.addSite("s5", "asda", "a", emptyList, true);
		DoThinks.addSite("s6", "123", "a", emptyList, false);
		ItemList items = new ItemList(11);
		items.addItem(new Item("bamba", 5));
		boolean result = DoThinks.CreateForm(new Date(), "s5", "s6", items, 7, "D3");
		assertEquals(true, result);
		String actaullist = DoThinks.Departure(0, 7);
		assertEquals(items.toString(), actaullist);
	}
	
	@Test
	void test10() {
		DoThinks.addDriver("D4", "A");
		DoThinks.addDriver("D5", "A");
		DoThinks.addTruck(8, "a", 1, 10, "A");
		DoThinks.addSite("s7", "asda", "a", emptyList, true);
		DoThinks.addSite("s8", "123", "a", emptyList, false);
		ItemList items = new ItemList(11);
		items.addItem(new Item("bamba", 5));
		boolean result = DoThinks.CreateForm(new Date(), "s7", "s8", items, 8, "D4");
		DoThinks.EditForm(0, null, null, null, null, null, "D5");
		assertEquals("D5", DeliveryManager.FindForm(1).getDriverName());
		
	}

}*/
