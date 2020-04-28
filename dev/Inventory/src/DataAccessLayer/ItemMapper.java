package DataAccessLayer;

import BusinessLayer.Result;
import DTO.ItemDTO;

import java.sql.Connection;
import java.sql.Statement;

public class ItemMapper {

    private Connection conn = Connector.getInstance().getConnection();

    public Result addItem(ItemDTO itemdto){
        Result result = new Result();


        return result;
    }
}
