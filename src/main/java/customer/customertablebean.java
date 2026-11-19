package customer;

public class customertablebean {

    private String mobile;
    private String cname;
    private String address;
    private String city;
    private String email;
    private String ctype;

    public customertablebean(String mobile, String cname, String address,
                             String city, String email, String ctype) {
        this.mobile = mobile;
        this.cname = cname;
        this.address = address;
        this.city = city;
        this.email = email;
        this.ctype = ctype;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }
}