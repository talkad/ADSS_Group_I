package DeliveryModule.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPConnection;

import org.omg.CORBA.portable.ApplicationException;

public class DAL
{

    private static final String DB = "jdbc:sqlite:src/Delivery.db";
    private static final String Connection_String = "Data Source=DataName.db;Version=3;";

    public static Connection connection = null;

    public static void OpenConnect() throws ApplicationException
    {
        try {
            //	Class.forName("com.mysql.jdbc.Driver");
            if (connection == null)
                connection = DriverManager.getConnection(DB);

        }
        catch(SQLException e) {
            throw new ApplicationException("database exception", null);
        }

    }
    public static void CloseConnect() throws ApplicationException
    {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new ApplicationException("database exception", null);
        }
    }

    public static List<Object[]> LoadAll(String TableName) throws ApplicationException
    {
        OpenConnect();
        List<Object[]> ans = new ArrayList<>();
        String sql = "SELECT * FROM " + TableName;
        try (Connection conn = connection;
             PreparedStatement pstmt  = conn.prepareStatement(sql)){


            ResultSet rs  = pstmt.executeQuery();
            int columnnum = rs.getMetaData().getColumnCount();

            // loop through the result set
            while (rs.next()) {
                Object[] arr = new Object[columnnum];
                for(int i = 0; i< columnnum; i++)
                    arr[i] = rs.getObject(i+1);
                ans.add(arr);
            }
        } catch (SQLException e) {
            throw new ApplicationException("database exception", null);
        }
        CloseConnect();
        return ans;

    }

    public static void Insert(String TableName, iDAL obj) throws ApplicationException
    {
        OpenConnect();
        String str = "INSERT INTO " + TableName + " (" + obj.getFields() + ") VALUES (" + obj.getValues() + ");";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(str)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Failed to insert to " + TableName + ", " + e.getMessage());
        }
        CloseConnect();
    }

    public static void Update(String TableName, iDAL obj) throws ApplicationException
    {
        OpenConnect();
        String str = "INSERT OR REPLACE INTO " + TableName + "(" + obj.getFields() + ") VALUES( " + obj.getValues() +")";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(str)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Failed to update to " + TableName + ", " + e.getMessage());
        }
        CloseConnect();
    }
    
    public static void Update(String TableName, iDAL obj, String condition) throws ApplicationException
    {
        OpenConnect();
        String[] fields = obj.getFields().split(", ");
        String[] values = obj.getValues().split(", ");
        String str = "UPDATE " + TableName + " SET " + fields[0] + " = " + values[0];
        for(int i=1; i < fields.length; i++) {
        	str += ", " + fields[i] + " = " + values[i];
        }
        str += " WHERE " + condition;
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(str)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Failed to update to " + TableName + ", " + e.getMessage());
        }
        CloseConnect();
    }

    public static void Delete(String TableName, iDAL obj, int key) throws ApplicationException
    {
        OpenConnect();
        String[] Fieldsarr = obj.getFields().split(", "), ValuesArr = obj.getValues().split(", ");
        String str = "DELETE FROM " + TableName + " WHERE (";
        str += Fieldsarr[key] + " = " + ValuesArr[key] + " )";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(str)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Failed to delete row from " + TableName + ", " + e.getMessage());
        }
        CloseConnect();
    }
    public static void Delete(String TableName, iDAL obj) throws ApplicationException
    {
        OpenConnect();
        String[] Fieldsarr = obj.getFields().split(", "), ValuesArr = obj.getValues().split(", ");
        String str = "DELETE FROM " + TableName + " WHERE (";
        str += Fieldsarr[0] + " = " + ValuesArr[0] ;
        for (int i = 1; i < ValuesArr.length; i++) {
            str += " AND " + Fieldsarr[i] + " = " + ValuesArr[i] ;
        }
        str += " )";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(str)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Failed to delete row from " + TableName + ", " + e.getMessage());
        }
        CloseConnect();
    }
    
    public static List<Object[]> Select(String TableName, iDAL obj, int[] key) throws ApplicationException
    {
        OpenConnect();
        String[] Fieldsarr = obj.getFields().split(", "), ValuesArr = obj.getValues().split(", ");
        List<Object[]> ans = new ArrayList<>();
        String sql = "SELECT * FROM " + TableName + " WHERE (";
        sql += Fieldsarr[key[0]] + " = " + ValuesArr[key[0]];
        int j = 1;
        while(j + 1 <= key.length) {
        	sql += " AND " + Fieldsarr[key[j]] + " = " + ValuesArr[key[j]];
        	j++;
        }
        sql += " )";
        try (Connection conn = connection;
             PreparedStatement pstmt  = conn.prepareStatement(sql)){


            ResultSet rs  = pstmt.executeQuery();
            int columnnum = rs.getMetaData().getColumnCount();

            // loop through the result set
            while (rs.next()) {
                Object[] arr = new Object[columnnum];
                for(int i = 0; i< columnnum; i++)
                    arr[i] = rs.getObject(i+1);
                ans.add(arr);
            }
        } catch (SQLException e) {
            throw new ApplicationException("database exception", null);
        }
        CloseConnect();
        return ans;

    }
    
    public static List<Object[]> Select(String TableName, iDAL obj, String date) throws ApplicationException
    {
        OpenConnect();
        List<Object[]> ans = new ArrayList<>();
        String sql = "SELECT * FROM " + TableName + " WHERE Date = " + "'" + date + "'";
        try (Connection conn = connection;
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            ResultSet rs  = pstmt.executeQuery();
            int columnnum = rs.getMetaData().getColumnCount();

            // loop through the result set
            while (rs.next()) {
                Object[] arr = new Object[columnnum];
                for(int i = 0; i< columnnum; i++)
                    arr[i] = rs.getObject(i+1);
                ans.add(arr);
            }
        } catch (SQLException e) {
            throw new ApplicationException("database exception", null);
        }
        CloseConnect();
        return ans;

    }


}