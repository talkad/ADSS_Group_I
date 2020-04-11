package InterfaceLayer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ILShift {
    private Date date;
    private int time;
    private int branch;
    private int shiftId;
    private List<String> roles;

    public ILShift(Date date, int time, int branch, int shiftId, List<String> roles) {
        this.date = date;
        this.time = time;
        this.branch = branch;
        this.shiftId = shiftId;
        this.roles = roles;
    }

    public Date getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public int getBranch() {
        return branch;
    }

    public int getShiftId() {
        return shiftId;
    }

    public List<String> getRoles() {
        List<String> lst = new LinkedList<>();
        for (String role : roles) {
            lst.add(role);
        }
        return lst;
    }
}
