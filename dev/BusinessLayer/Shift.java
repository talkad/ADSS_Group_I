package BusinessLayer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Shift {
    private Date date;
    private int time;
    private int branch;
    private int shiftId;
    private List<String> roles;

    public Shift(Date date, int time, int branch, int shiftId, List<String> roles) {
        this.date = date;
        this.time = time;
        this.branch = branch;
        this.shiftId = shiftId;
        this.roles = roles;
    }


}
