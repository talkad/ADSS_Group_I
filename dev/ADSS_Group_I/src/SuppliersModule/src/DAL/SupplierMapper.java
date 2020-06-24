package DAL;

import Buisness.ContactPerson;
import Buisness.Result;
import Buisness.SupplierCard;
import DAL_Connector.DatabaseManager;


import java.sql.*;
import java.util.*;

public class SupplierMapper {
    private static Connection conn = DatabaseManager.getInstance().getConnection();

    private Map<Integer, SupplierCard> identitySupplierMap;
    private Map<Integer, ContactPerson> identityContactMap;

    private static SupplierMapper instance;

    public SupplierMapper(){
        identitySupplierMap = new HashMap<>();
        identityContactMap = new HashMap<>();
    }

    public static SupplierMapper getInstance(){
        if(instance == null)
            instance = new SupplierMapper();
        return instance;
    }

    // ------ insert - update - delete ----------
    /**
     * initiate the product list with the data in DB when the system starts
     * doesn't import items into the system
     */
    private void initSupplierMap(){
        String sql = "SELECT * FROM Supplier";
        String fixed = "fixed", single = "single";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                SupplierCard supplier = new SupplierCard(rs.getString("name"), rs.getString("manufacturer"), rs.getInt("companyId"), rs.getInt("bankAccount"), rs.getString("paymentConditions")
                        , (rs.getInt("arrangementType") == 1 ? fixed : single), (rs.getInt("selfPickup") == 1));

                identitySupplierMap.put(supplier.getCompanyId(), supplier);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * insert a given supplier to DB
     * @param supplier is the product to be stored in DB
     * @return  a Result object with information about the result of the operation
     */
    private Result insert(SupplierCard supplier){
        Result result = new Result();
        int numRowsInserted;

        String insertCommand = "INSERT INTO Supplier(companyId, name, manufacturer, bankAccount, paymentConditions, selfPickup,arrangementType)" +
                "VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            statement.setInt(1, supplier.getCompanyId());
            statement.setString(2, supplier.getName());
            statement.setString(3,supplier.getManufacturer());
            statement.setInt(4, supplier.getBankAccount());
            statement.setString(5, supplier.getPaymentConditions());
            statement.setInt(6, supplier.getArrangement().isSelfPickup() ? 1 : 0);
            statement.setInt(7, supplier.getType().equals("fixed") ? 1 : 0);
            numRowsInserted = statement.executeUpdate();


            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }

        return result;
    }

    /**
     * Update all fields of a given supplier in DB
     * @param supplier is the product to be updated
     * @return  a Result object with information about the result of the operation
     */
    private Result update(SupplierCard supplier){
        Result result = new Result();
        int numRowsUpdated;
        String updateCommand = "UPDATE Supplier SET name = ?, manufacturer = ?, bankAccount = ?, paymentConditions = ?" +
                " WHERE companyId = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(updateCommand);
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getManufacturer());
            statement.setInt(3, supplier.getBankAccount());
            statement.setString(4, supplier.getPaymentConditions());
            statement.setInt(5, supplier.getCompanyId());

            numRowsUpdated = statement.executeUpdate();


            if(numRowsUpdated == 1)
                result.successful();
            else
                result.failure("Failed to update Product");

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }
        return result;
    }

    /**
     * Delete a given supplier from DB
     * @param companyID is the product identifier
     * @return  a Result object with information about the result of the operation
     */
    private Result delete(int companyID){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM Supplier WHERE companyId = ?";

        // delete the product only if there are no items belong to this product
        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, companyID);

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

    // -------- mapper functionality ---------------


    /**
     * given a companyID, returns a SupplierCard with companyId {@param companyID}
     * @param companyId the id of the supplier
     * @return productDTO with id {@param companyId}. or null if no such entry exists
     */
    public SupplierCard getSupplier(int companyId){

        SupplierCard supplier = null;

        if(identitySupplierMap.size() == 0)
            initSupplierMap();

        if(identitySupplierMap.containsKey(companyId))
        {
            supplier = identitySupplierMap.get(companyId);
        }
        return supplier;
    }

    /**
     * checks whether there's already a supplier with the id exists
     * @param companyId is the supplier id
     * @return whether there's already a supplier with the same name and manufacturer name
     */
    public boolean doesSupplierExist(int companyId){

        if(identitySupplierMap.size() == 0)
            initSupplierMap();

        if(identitySupplierMap.containsKey(companyId))
            return true;

        return false;
    }

    /**
     *
     * @return all of the records in a List
     */
    public Map<Integer,SupplierCard> getAll(){
        initSupplierMap();
        return identitySupplierMap;
    }


    /**
     * Add a given item from DB
     * @param supplier is the one to be inserted
     * @return  a Result object with information about the result of the operation
     */
    public Result addMapper(SupplierCard supplier){

        Result result = insert(supplier);

        if(result.isSuccessful())
            identitySupplierMap.put(supplier.getCompanyId(), supplier);

        return result;
    }

    /**
     * Update a given supplier from DB
     * @para supplier is the one to be updated
     * @return  a Result object with information about the result of the operation
     */
    public Result updateMapper(SupplierCard supplier){

        Result result = update(supplier);

        if(result.isSuccessful()){
            identitySupplierMap.remove(supplier.getCompanyId());
            identitySupplierMap.put(supplier.getCompanyId(), supplier);
        }

        return result;
    }


    /**
     * Delete a given supplier from DB
     * @param companyId is the supplier identifier
     * @return  a Result object with information about the result of the operation
     */
    public Result deleteMapper(int companyId){

        Result result = delete(companyId);

        if(result.isSuccessful()){
            identitySupplierMap.remove(companyId);
        }

        return result;
    }

    public Result addContact(int companyId, ContactPerson person){
        Result result = new Result();
        int numRowsInserted = 0;

        String insertCommand = "INSERT INTO ContactPerson(supplierId, name, methodName, method)" +
                "VALUES(?,?,?,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(insertCommand);
            for (String methodName: person.getContactMethods().keySet()) {
                statement.setInt(1, companyId);
                statement.setString(2, person.getName());
                statement.setString(3,methodName);
                statement.setString(4,person.getContactMethods().get(methodName));
                numRowsInserted = statement.executeUpdate();
            }
            if(numRowsInserted == 1)
                result.successful();
            else
                result.failure("Failed to update Product");
            result.successful();

        }catch (java.sql.SQLException e){
            result.failure("Failed to create a statement");
        }
        return result;
    }

    public Result deleteContact(int companyId, ContactPerson person){
        Result result = new Result();
        int numRowsDeleted;
        String deleteCommand = "DELETE FROM ContactPerson WHERE companyId = ? AND name = ?";

        // delete the product only if there are no items belong to this product
        try {
            PreparedStatement statement = conn.prepareStatement(deleteCommand);
            statement.setInt(1, companyId);
            statement.setString(2, person.getName());

            numRowsDeleted = statement.executeUpdate();

            if (numRowsDeleted == 1)
                result.successful();
            else
                result.failure("Failed to delete Contact");

        } catch (java.sql.SQLException e) {
            result.failure("Failed to create a statement");
        }
        return result;
    }



}
