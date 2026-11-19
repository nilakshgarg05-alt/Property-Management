package customer;

public class browsepropertiesbean {

    private int rid;
    private String mobile;
    private String location;
    private float size;
    private float front;
    private float rear;
    private float leftt;
    private float rightt;
    private String facing;
    private float price;
    private String approvedby;

    public browsepropertiesbean() {
    }

    public browsepropertiesbean(int rid, String mobile, String location,
                                float size, float front, float rear,
                                float leftt, float rightt,
                                String facing, float price,
                                String approvedby) {

        this.rid = rid;
        this.mobile = mobile;
        this.location = location;
        this.size = size;
        this.front = front;
        this.rear = rear;
        this.leftt = leftt;
        this.rightt = rightt;
        this.facing = facing;
        this.price = price;
        this.approvedby = approvedby;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getFront() {
        return front;
    }

    public void setFront(float front) {
        this.front = front;
    }

    public float getRear() {
        return rear;
    }

    public void setRear(float rear) {
        this.rear = rear;
    }

    public float getLeftt() {
        return leftt;
    }

    public void setLeftt(float leftt) {
        this.leftt = leftt;
    }

    public float getRightt() {
        return rightt;
    }

    public void setRightt(float rightt) {
        this.rightt = rightt;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

}