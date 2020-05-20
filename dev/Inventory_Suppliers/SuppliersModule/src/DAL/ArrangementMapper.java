package DAL;

import Buisness.*;
import BusinessLayer.Product;
import DAL_Connector.DatabaseManager;
import DataAccessLayer.ProductMapper;

import java.lang.reflect.Executable;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO check result changing multiple times
public class ArrangementMapper {
    private static Connection conn = DatabaseManager.getInstance().getConnection();

    //supplierId----List<ProductId>
    private Map<Integer, Integer> identityArrangementMap;
    //supplierId-productId-amount-discount
    private  Map<Map<Integer,Integer>, Map<Integer,Double>> identityQuantityMap;
    //supplierId,date
    private Map<Integer,LocalDate> identityDeliveryMap;

    private static ArrangementMapper instance;

    //contains productId,supplierId
    public ArrangementMapper(){
        identityArrangementMap = new HashMap<>();
        identityArrangementMap = new HashMap<>();
    }

    public static ArrangementMapper getInstance(){
        if(instance == null)
            instance = new ArrangementMapper();
        return instance;
    }

    public Map<Integer,Product> getItems(int supplierId){
        Map<Integer, Product> map = new HashMap<>();
        String sql = "SELECT productId from Arrangement WHERE supplierId = ?";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,supplierId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Product product = ProductMapper.getInstance().getProduct(rs.getInt("productId"));
                map.put(product.getId(),product);
            }
        }
        catch (Exception e){
            return null;
        }
        return map;
    }

    public Arrangement getArrangement(int supplierId){
        int selfPickup = 0,arrangementType = 0;
        Arrangement arrangement;
        PreparedStatement pstmt;

        String sql = "SELECT selfpickup, arrangementType FROM Supplier WHERE companyId = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,supplierId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                selfPickup = rs.getInt("selfPickup");
                arrangementType = rs.getInt("arrangementType");
            }
        }
        catch (Exception e){
            return null;
        }
        if (arrangementType == 1)
            arrangement = new FixedArrangement(LocalDate.now(), (selfPickup == 1), supplierId);
        else
            arrangement = new SingleArrangement(selfPickup == 1,supplierId);
        arrangement.setItems(getItems(supplierId));
        arrangement.set_deliveryDates(getDeliveryDates(supplierId));
        return arrangement;
    }

    public  QuantityAgreement getQuantity(int supplierId){
        Map<Integer,Map<Integer,Double>> qA = new HashMap<>();
        int productId, amount;
        double discount;
        String sql = "SELECT productId, amount, discount FROM QuantityAgreement WHERE supplierId = ? ORDER BY productId";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,supplierId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                productId = rs.getInt("productId");
                amount = rs.getInt("amount");
                discount = rs.getDouble("discount");
                if(!qA.containsKey(productId)) {
                    Map<Integer,Double> qMap = new HashMap<>();
                    qMap.put(amount,discount);
                    qA.put(productId,qMap);
                }
                else{
                    qA.get(productId).put(amount,discount);
                }
            }
        }
        catch (Exception e){
            return null;
        }
        if (qA.size() == 0)
            return null;
        return new QuantityAgreement(qA);
    }
/*
    private void initQuantityMap(){
        String sql = "SELECT * FROM QuantityAgreement";
        Map<Integer,Integer> map1;
        Map<Integer,Double> map2;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                map1 = new HashMap<>();
                map1.put(rs.getInt("supplierId"),rs.getInt("productId"));
                map2 = new HashMap<>();
                map2.put(rs.getInt("amount"),rs.getDouble("discount"));
                identityQuantityMap.put(map1,map2);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    public DeliveryDates getDeliveryDates(int supplierId){
        String sql = "SELECT date, orderNum FROM DeliveryDates WHERE supplierId = ?";
        LocalDate date;
        int orderNum;
        DeliveryDates dDates = new DeliveryDates();
        Map <LocalDate,Integer> map = new HashMap<>();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,supplierId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                date = stringToDate(rs.getString("date"));
                orderNum = rs.getInt("orderNum");
                dDates.addDate(date,orderNum);
            }
        }
        catch (Exception e){
            return null;
        }
        return dDates;
    }

/*
    private void initArrangementMap(){
        String sql = "SELECT * FROM Arrangement";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                identityArrangementMap.put(rs.getInt("supplierId"),rs.getInt("productId"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
*/

    public boolean insertArrangement(Arrangement arrangement, int supplierId){
        //add items to arrangement table
        Map<Integer, Product>  items = (arrangement.getItems());
        if (items != null)
            for(Integer id:items.keySet()){
                if(!insertProduct(supplierId,id).isSuccessful())return false;
            }

        //add items to Quantity arrangement
        if(arrangement.get_quantityAgreement()!=null){
            insertToQuantity(arrangement.get_quantityAgreement(),supplierId);
        }

        //add items to delivery dates
        insertToDelivery(arrangement.get_deliveryDates().getDates(),supplierId);
        return true;
    }

    public boolean deleteArrangement(int supplierId){
        Map<Integer, Product> map  = getItems(supplierId);
        if (map != null)
            for (int productId:map.keySet()){
                deleteProduct(supplierId, productId);
            }
        if(getQuantity(supplierId)!=null){
            deleteFromQuantity(getQuantity(supplierId),supplierId);
        }
        List<Integer> list = new ArrayList<Integer>(getDeliveryDates(supplierId).getDates().keySet());
        deleteFromDelivery(list,supplierId);
        return true;
    }

    public boolean updateArrangement(int supplierId, Arrangement arrangement){
        deleteArrangement(supplierId);
        insertArrangement(arrangement,supplierId);
        return true;
    }


    public void insertToDelivery(Map<Integer,LocalDate>map,int supplierId){
        for(int order:map.keySet()){
            insertItemToDelivery(map.get(order),supplierId,order);
        }
    }

    public void deleteFromDelivery(List<Integer> orders, int supplierId){
        for(int order:orders){
            deleteItemFromDelivery(order, supplierId);
        }
    }

    public void updateDelivery(Map<Integer,LocalDate>map,int supplierId){
        for(int order:map.keySet()){
            updateItemInDelivery(order,supplierId,map.get(order));
        }
    }

    public Result insertProduct(int supplierId, int productId){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO Arrangement(supplierId, productId)" +
                "VALUES(?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, supplierId);
            statement.setInt(2, productId);
            numRowsInserted = statement.executeUpdate();


            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Arrangement");

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }

    public Result deleteProduct(int supplierId, int productId){
        Result result = new Result();
        int numRowsDeleted;

        String deleteCommand = "DELETE FROM Arrangement WHERE supplierId = ? AND productId = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, supplierId);
            statement.setInt(2, productId);
            numRowsDeleted = statement.executeUpdate();

            if(numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to update Arrangement");
            result.successful();
        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }
        result = deleteProductFromQuantity(supplierId,productId);
        return result;
    }

    public Result updateProduct(int supplierId, int productId){
        Result result = new Result();
        result = deleteProduct(supplierId, productId);
        if (!result.isSuccessful())
            return result;
        result = insertProduct(supplierId, productId);
        return result;
    }


    public Result insertToQuantity(QuantityAgreement qa, int supplierId){
        Result result = new Result();
        Result tempResult = new Result();
        tempResult.successful();
        double discount;
        Map<Integer, Map<Integer,Double>> quantity = qa.getDiscounts();
        for(Integer id : quantity.keySet()){ //each item id
           for (Integer amount : quantity.get(id).keySet()){ //each amount
               discount = quantity.get(id).get(amount);
               tempResult = insertProductToQuantity(supplierId,id,amount,discount);
               if(result.isSuccessful())
                   result = tempResult;
           }
        }
        return result;
    }

    public Result deleteFromQuantity(QuantityAgreement qa, int supplierId){
        Result result = new Result();
        Result tempResult = new Result();
        tempResult.successful();
        double discount;
        Map<Integer, Map<Integer,Double>> quantity = qa.getDiscounts();
        for(Integer id : quantity.keySet()){ //each item id
            tempResult = deleteProductFromQuantity(supplierId,id);
            if(result.isSuccessful())
                result = tempResult;
        }
        return result;
    }

    public Result updateQuantity(QuantityAgreement qa, int supplierId){
        Result result = new Result();
        result = deleteFromQuantity(qa,supplierId);
        if (!result.isSuccessful())
            return result;
        result = insertToQuantity(qa,supplierId);
        return result;
    }

    public Result insertItemToDelivery(LocalDate date ,int supplierId, int orderNum){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO DeliveryDates(supplierId, date, orderNum)" +
                "VALUES(?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, supplierId);
            statement.setString(2, date.toString());
            statement.setInt(3,orderNum);
            numRowsInserted = statement.executeUpdate();


            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Arrangement");

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }

    public Result deleteItemFromDelivery(int orderNum, int supplierId){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM DeliveryDates WHERE supplierId = ? AND orderNum = ?";

        // delete the product only if there are no items belong to this product
        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, supplierId);
            statement.setInt(2,orderNum);

            numRowsDeleted = statement.executeUpdate();

            if (numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to delete Product");

        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        return result;
    }

    public Result updateItemInDelivery(int orderNum, int supplierId, LocalDate date){
        Result result = new Result();
        result = deleteItemFromDelivery(orderNum,supplierId);
        if (!result.isSuccessful())
            return result;
        result = insertItemToDelivery(date,supplierId,orderNum);
        return result;
    }

    public Result updateItemInQuantity(int supplierId, int productId, Map<Integer,Double> discounts){
        Result result = new Result();
        result = deleteProductFromQuantity(supplierId, productId);
        if (!result.isSuccessful())
            return result;
        for (int amount:discounts.keySet())
            result = insertProductToQuantity(supplierId, productId, amount, discounts.get(amount));
        return result;
    }

    public Result insertProductToQuantity(int supplierId, int productId, int amount, double discount){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO QuantityAgreement(supplierId, productId, amount, discount)" +
                "VALUES(?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, supplierId);
            statement.setInt(2, productId);
            statement.setInt(3, amount);
            statement.setDouble(4, discount);
            numRowsInserted = statement.executeUpdate();

            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Arrangement");

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }

    public Result deleteProductFromQuantity(int supplierId, int productId){
        Result result = new Result();
        int numRowsDeleted;

        String insertCommand = "DELETE FROM QuantityAgreement WHERE supplierId = ? AND productId =?";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, supplierId);
            statement.setInt(2, productId);
            numRowsDeleted = statement.executeUpdate();

            if(numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to update Arrangement");

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }




    private LocalDate stringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //String date = "16/08/2016";
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
}
