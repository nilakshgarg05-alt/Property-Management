package customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class finalizedealcontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnfetch;

    @FXML
    private Button btnmodify;

    @FXML
    private Button btnsave;

    @FXML
    private DatePicker dateofdeal;

    @FXML
    private ToggleGroup dealstatus;

    @FXML
    private DatePicker registrydate;

    @FXML
    private TextField tcxtid;

    @FXML
    private TextField txtadvrec;

    @FXML
    private TextField txtbal;

    @FXML
    private TextField txtbuyercontact;

    @FXML
    private TextField txtbuyername;

    @FXML
    private TextField txtdownpay;

    @FXML
    private TextField txtfinalamt;

    @FXML
    private TextField txtsellercontact;

    @FXML
    private TextField txtsellername;

    @FXML
    private TextField txttotalcomm;
    Connection conn;

    @FXML
    void onfetch(ActionEvent event) {
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT c.cname,c.mobile " +
                            "FROM properties p " +
                            "INNER JOIN customers c ON p.mobile=c.mobile " +
                            "LEFT JOIN deals d ON p.rid=d.pid " +
                            "WHERE p.rid=? AND d.pid IS NULL");

            pst.setInt(1, Integer.parseInt(tcxtid.getText()));

            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                txtsellername.setText(rs.getString("cname"));
                txtsellercontact.setText(rs.getString("mobile"));
                System.out.println("Record Found");

            }
            else{
                System.out.println("property not found sorryyy......");
            }

        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void onmodify(ActionEvent event) {
        try {

            PreparedStatement pst = conn.prepareStatement(
                    "UPDATE deals SET buyer=?, buyercontact=?, finalamt=?, downpmt=?, dtofdeal=?, dtofreg=?, totalcom=?, adv=?, bal=?, status=? WHERE pid=?");

            pst.setString(1, txtbuyername.getText());
            pst.setString(2, txtbuyercontact.getText());

            pst.setInt(3, Integer.parseInt(txtfinalamt.getText()));
            pst.setInt(4, Integer.parseInt(txtdownpay.getText()));

            pst.setDate(5, java.sql.Date.valueOf(dateofdeal.getValue()));
            pst.setDate(6, java.sql.Date.valueOf(registrydate.getValue()));

            pst.setInt(7, Integer.parseInt(txttotalcomm.getText()));
            pst.setInt(8, Integer.parseInt(txtadvrec.getText()));
            pst.setInt(9, Integer.parseInt(txtbal.getText()));

            pst.setString(10,
                    ((RadioButton) dealstatus.getSelectedToggle()).getText());

            pst.setInt(11, Integer.parseInt(tcxtid.getText()));

            int count = pst.executeUpdate();

            if (count > 0) {
                System.out.println("Record Updated Successfully.");
            } else {
                System.out.println("Deal Not Found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onsave(ActionEvent event) throws SQLException {
        PreparedStatement pst = null;
        try {

            pst = conn.prepareStatement(
                    "INSERT INTO deals(pid,seller,sellercontact,buyer,buyercontact,finalamt,downpmt,dtofdeal,dtofreg,totalcom,adv,bal,status) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst.setInt(1, Integer.parseInt(tcxtid.getText()));

            pst.setString(2, txtsellername.getText());
            pst.setString(3, txtsellercontact.getText());

            pst.setString(4, txtbuyername.getText());
            pst.setString(5, txtbuyercontact.getText());

            pst.setInt(6, Integer.parseInt(txtfinalamt.getText()));
            pst.setInt(7, Integer.parseInt(txtdownpay.getText()));

            pst.setDate(8, java.sql.Date.valueOf(dateofdeal.getValue()));
            pst.setDate(9, java.sql.Date.valueOf(registrydate.getValue()));

            pst.setInt(10, Integer.parseInt(txttotalcomm.getText()));
            pst.setInt(11, Integer.parseInt(txtadvrec.getText()));
            pst.setInt(12, Integer.parseInt(txtbal.getText()));

            pst.setString(13,
                    ((RadioButton) dealstatus.getSelectedToggle()).getText());

            int count = pst.executeUpdate();

            if (count > 0)
                System.out.println("Deal Saved Successfully.");
            else
                System.out.println("Failed to Save Deal.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {
        conn = projectdatabaseconnection.doconnecttodb();
        if (conn != null)
            System.out.println("Connected Successfully");
        else
            System.out.println("Connection Failed");


    }

}
