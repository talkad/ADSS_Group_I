import DTO.ItemDTO;
import DTO.ProductDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HardCodeInitializer {

    /**
     * This function takes care over illegal date inputs
     * @param date is a string which have to be in the pre-defined format
     * @return a new date by the given string. if the string invalid return null.
     */
    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private static class SingletonHolder {
        private static HardCodeInitializer instance = new HardCodeInitializer();
    }

    public static HardCodeInitializer getInstance() {
        return SingletonHolder.instance;
    }

    //milk items:
    private ItemDTO milk1= new ItemDTO(1,false,parseDate("1-1-2030"),"Store");
    private ItemDTO milk2= new ItemDTO(2,false,parseDate("1-1-2000"),"Inventory");
    private ItemDTO milk3= new ItemDTO(3,true,parseDate("1-1-2000"),"Store");
    private ItemDTO milk4= new ItemDTO(4,false,parseDate("1-1-2030"),"Store");
    private ItemDTO milk5= new ItemDTO(5,false,parseDate("1-1-2000"),"Inventory");
    private ItemDTO milk6= new ItemDTO(6,true,parseDate("1-1-2000"),"Store");

    private List<String> milkCategories= new LinkedList<>(Arrays.asList("milky","500ml"));
    private List<ItemDTO> items1=new LinkedList<>(Arrays.asList(milk1,milk2,milk3,milk4,milk5,milk6));
    private ProductDTO milk= new ProductDTO("milk","Tnuva",3, 10,15,
            4,2,milkCategories,items1);

    //shampoo items:
    private ItemDTO shampoo1= new ItemDTO(7, false, parseDate("1-1-2030"),"Store");
    private ItemDTO shampoo2= new ItemDTO(8, true, parseDate("1-1-2030"),"Inventory");
    private ItemDTO shampoo3= new ItemDTO(9, false, parseDate("1-1-2030"),"Store");
    private ItemDTO shampoo4= new ItemDTO(10, true, parseDate("1-1-2030"),"Inventory");
    private ItemDTO shampoo5= new ItemDTO(11, false, parseDate("1-1-1999"),"Store");
    private ItemDTO shampoo6= new ItemDTO(12, true, parseDate("1-1-1999"),"Inventory");

    private List<String> shampooCategories= new LinkedList<>(Arrays.asList("bathing","clean"));
    private List<ItemDTO> items2=new LinkedList<>(Arrays.asList(shampoo1,shampoo2,shampoo3,shampoo4,shampoo5,shampoo6));
    private ProductDTO shampoo= new ProductDTO("shampoo","head&shoulders",3, 10,15,
            4,2,shampooCategories,items2);

    //cheese items:
    private ItemDTO cheese1= new ItemDTO(13, false, parseDate("1-1-2030"),"Store");
    private ItemDTO cheese2= new ItemDTO(14, true, parseDate("1-1-2030"),"Inventory");
    private ItemDTO cheese3= new ItemDTO(15, false, parseDate("1-1-2030"),"Store");
    private ItemDTO cheese4= new ItemDTO(16, true, parseDate("1-1-2030"),"Inventory");
    private ItemDTO cheese5= new ItemDTO(17, false, parseDate("1-1-1999"),"Store");
    private ItemDTO cheese6= new ItemDTO(18, true, parseDate("1-1-1999"),"Inventory");

    private List<String> cheeseCategories= new LinkedList<>(Arrays.asList("milky","salty"));
    private List<ItemDTO> items3=new LinkedList<>(Arrays.asList(cheese1,cheese2,cheese3,cheese4,cheese5,cheese6));
    private ProductDTO cheese= new ProductDTO("cheese","Tara",3, 10,15,
            4,2,cheeseCategories,items3);


    public void initialize(){
        //add the products
    }

}
