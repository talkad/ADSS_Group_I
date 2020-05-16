package setUp;//package setUp;
//
//import Buisness.FixedArrangement;
//import Buisness.Item;
//import Buisness.SuperLi;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//
//public class SuperLiTest {
//
//
//    @Test
//    public void testAddSupplier(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().containsKey(222));
//        assertTrue(SuperLi.getSuppliers().get(222).getName().equals("test"));
//        assertTrue(SuperLi.getSuppliers().get(222).getManufacturer().equals("testy"));
//        assertTrue(SuperLi.getSuppliers().get(222).getBankAccount() == 159159159);
//        assertTrue(SuperLi.getSuppliers().get(222).getPaymentConditions().equals("no pay"));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement() instanceof FixedArrangement);
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testAddContact(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().get(222).getContacts().isEmpty());
//        HashMap<String,String> map = new HashMap<>();
//        map.put("email","someone@somewhere.com");
//        map.put("phone","051-1111111");
//        map.put("fax","03-1111111");
//        SuperLi.addContact(222,"no-one",map);
//        assertFalse(SuperLi.getSuppliers().get(222).getContacts().isEmpty());
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testArrangementAdd(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().isEmpty());
//        Item item1 = new Item(12,12,"bla",111);
//        Item item2 = new Item(13,13,"bli",111);
//        Item item3 = new Item(14,14,"ble",111);
//        Item item4 = new Item(15,15,"blo",111);
//        Item item5 = new Item(16,16,"blu",111);
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(item4);items.add(item5);items.add(item2);items.add(item1);items.add(item3);
//        SuperLi.getSuppliers().get(222).addToArrangement(items);
//        assertFalse(SuperLi.getSuppliers().get(222).getArrangement().getItems().isEmpty());
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(12));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(13));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(14));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(15));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(16));
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testOrder(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        HashMap<Integer,Integer> map = new HashMap<>();
//        map.put(12,12);
//        map.put(13,13);
//        map.put(14,14);
//        assertFalse(SuperLi.getSuppliers().get(222).placeOrder(map, LocalDate.now().plusDays(12)));
//        assertTrue(SuperLi.getSuppliers().get(222).getOrders().isEmpty());
//        Item item1 = new Item(12,12,"bla",111);
//        Item item2 = new Item(13,13,"bli",111);
//        Item item3 = new Item(14,14,"ble",111);
//        Item item4 = new Item(15,15,"blo",111);
//        Item item5 = new Item(16,16,"blu",111);
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(item4);items.add(item5);items.add(item2);items.add(item1);items.add(item3);
//        SuperLi.getSuppliers().get(222).addToArrangement(items);
//        assertTrue(SuperLi.getSuppliers().get(222).placeOrder(map, LocalDate.now().plusDays(12)));
//        assertFalse(SuperLi.getSuppliers().get(222).getOrders().isEmpty());
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testDeleteFromArrangement(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().isEmpty());
//        Item item1 = new Item(12,12,"bla",111);
//        Item item2 = new Item(13,13,"bli",111);
//        Item item3 = new Item(14,14,"ble",111);
//        Item item4 = new Item(15,15,"blo",111);
//        Item item5 = new Item(16,16,"blu",111);
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(item4);items.add(item5);items.add(item2);items.add(item1);items.add(item3);
//        SuperLi.getSuppliers().get(222).addToArrangement(items);
//        assertFalse(SuperLi.getSuppliers().get(222).getArrangement().getItems().isEmpty());
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(12));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(13));
//        SuperLi.getSuppliers().get(222).deleteFromArrangement(new ArrayList<>(Arrays.asList(12,13)));
//        assertFalse(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(12));
//        assertFalse(SuperLi.getSuppliers().get(222).getArrangement().getItems().containsKey(13));
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testDeleteContact(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().get(222).getContacts().isEmpty());
//        HashMap<String,String> map = new HashMap<>();
//        map.put("email","someone@somewhere.com");
//        map.put("phone","051-1111111");
//        map.put("fax","03-1111111");
//        SuperLi.addContact(222,"no-one",map);
//        assertFalse(SuperLi.getSuppliers().get(222).getContacts().isEmpty());
//        SuperLi.deleteContact(222,"no-one");
//        assertTrue(SuperLi.getSuppliers().get(222).getContacts().isEmpty());
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testAddAndDeleteQuantity(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        Item item1 = new Item(12,12,"bla",111);
//        Item item2 = new Item(13,13,"bli",111);
//        Item item3 = new Item(14,14,"ble",111);
//        Item item4 = new Item(15,15,"blo",111);
//        Item item5 = new Item(16,16,"blu",111);
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(item4);items.add(item5);items.add(item2);items.add(item1);items.add(item3);
//        SuperLi.getSuppliers().get(222).addToArrangement(items);
//        HashMap<Integer, Map<Integer,Double>> Qmap = new HashMap<>();
//        for (int i = 4; i >= 0; i-=2){
//            HashMap<Integer,Double> pmap = new HashMap<>();
//            pmap.put(items.get(i).getId()*2,items.get(i).getPrice()/2);
//            pmap.put(items.get(i).getId()*4,items.get(i).getPrice()/4);
//            Qmap.put(items.get(i).getId(), pmap);
//        }
//        SuperLi.getSuppliers().get(222).getArrangement().addNewAgreement(Qmap);
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().get_quantityAgreement() != null);
//        SuperLi.getSuppliers().get(222).getArrangement().deleteItemsFromAgreement(new ArrayList<>(Arrays.asList(12,13,14,15,16)));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().get_quantityAgreement().getDiscounts().isEmpty());
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testUpdateArrangement(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().isEmpty());
//        Item item1 = new Item(12,12,"bla",111);
//        Item item2 = new Item(13,13,"bli",111);
//        Item item3 = new Item(14,14,"ble",111);
//        Item item4 = new Item(15,15,"blo",111);
//        Item item5 = new Item(16,16,"blu",111);
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(item4);items.add(item5);items.add(item2);items.add(item1);items.add(item3);
//        SuperLi.getSuppliers().get(222).addToArrangement(items);
//        HashMap<Integer,Double> map = new HashMap<>();
//        map.put(12,13.0);
//        map.put(13,14.0);
//        SuperLi.getSuppliers().get(222).changePriceInAgreement(map);
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().get(12).getPrice() == 13.0);
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().getItems().get(13).getPrice() == 14.0);
//        SuperLi.deleteSupplier(222);
//
//    }
//
//    @Test public void testDelieveryDate(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        HashMap<Integer,Integer> map = new HashMap<>();
//        map.put(12,12);
//        map.put(13,13);
//        map.put(14,14);
//        assertFalse(SuperLi.getSuppliers().get(222).placeOrder(map, LocalDate.now().plusDays(12)));
//        assertTrue(SuperLi.getSuppliers().get(222).getOrders().isEmpty());
//        Item item1 = new Item(12,12,"bla",111);
//        Item item2 = new Item(13,13,"bli",111);
//        Item item3 = new Item(14,14,"ble",111);
//        Item item4 = new Item(15,15,"blo",111);
//        Item item5 = new Item(16,16,"blu",111);
//        ArrayList<Item> items = new ArrayList<Item>();
//        items.add(item4);items.add(item5);items.add(item2);items.add(item1);items.add(item3);
//        SuperLi.getSuppliers().get(222).addToArrangement(items);
//        SuperLi.getSuppliers().get(222).placeOrder(map, LocalDate.now().plusDays(14));
//        assertTrue(SuperLi.getSuppliers().get(222).getArrangement().get_deliveryDates().getDates().containsKey(LocalDate.now().plusDays(14)));
//        assertFalse(SuperLi.getSuppliers().get(222).getArrangement().get_deliveryDates().getDates().get(LocalDate.now().plusDays(14)));
//        SuperLi.deleteSupplier(222);
//    }
//
//    @Test public void testDeleteSupplier(){
//        SuperLi.addSupplier("test", "testy", 222,159159159,"no pay","fixed",true);
//        assertTrue(SuperLi.getSuppliers().containsKey(222));
//        assertFalse(SuperLi.deleteSupplier(333));
//        assertTrue(SuperLi.deleteSupplier(222));
//        assertTrue(SuperLi.getSuppliers().isEmpty());
//        SuperLi.deleteSupplier(222);
//    }
//
//}
