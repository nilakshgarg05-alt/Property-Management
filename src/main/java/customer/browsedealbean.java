package customer;

public class browsedealbean {

    private int pid;
    private String seller;
    private String sellercontact;
    private String buyer;
    private String buyercontact;
    private int finalamt;
    private int downpmt;
    private String dtofdeal;
    private String dtofreg;

    public browsedealbean() {
    }

    public browsedealbean(int pid, String seller, String sellercontact,
                          String buyer, String buyercontact,
                          int finalamt, int downpmt,
                          String dtofdeal, String dtofreg) {

        this.pid = pid;
        this.seller = seller;
        this.sellercontact = sellercontact;
        this.buyer = buyer;
        this.buyercontact = buyercontact;
        this.finalamt = finalamt;
        this.downpmt = downpmt;
        this.dtofdeal = dtofdeal;
        this.dtofreg = dtofreg;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellercontact() {
        return sellercontact;
    }

    public void setSellercontact(String sellercontact) {
        this.sellercontact = sellercontact;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyercontact() {
        return buyercontact;
    }

    public void setBuyercontact(String buyercontact) {
        this.buyercontact = buyercontact;
    }

    public int getFinalamt() {
        return finalamt;
    }

    public void setFinalamt(int finalamt) {
        this.finalamt = finalamt;
    }

    public int getDownpmt() {
        return downpmt;
    }

    public void setDownpmt(int downpmt) {
        this.downpmt = downpmt;
    }

    public String getDtofdeal() {
        return dtofdeal;
    }

    public void setDtofdeal(String dtofdeal) {
        this.dtofdeal = dtofdeal;
    }

    public String getDtofreg() {
        return dtofreg;
    }

    public void setDtofreg(String dtofreg) {
        this.dtofreg = dtofreg;
    }
}