package customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class customercontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ComboBox<String> combotype;

    @FXML
    private ImageView prev1;

    @FXML
    private ImageView prev2;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAddr;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtMail;

    @FXML
    private TextField txtMob;
    File customerFile;
    File aadharFile;
    @FXML
    void onBrowse1(ActionEvent event) {
        System.out.println("Browse1 Clicked");
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Customer Photo");
                chooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png")
                );

                customerFile = chooser.showOpenDialog(null);

                if (customerFile != null) {
                    prev1.setImage(new Image(new FileInputStream(customerFile)));
                }

            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }

        @FXML
        void onBrowse2(ActionEvent event) {
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select Aadhaar Photo");
                chooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png")
                );

                aadharFile = chooser.showOpenDialog(null);

                if (aadharFile != null) {
                    prev2.setImage(new Image(new FileInputStream(aadharFile)));
                }

            } catch (Exception exp) {
                exp.printStackTrace();
            }
    }

    @FXML
    void onClear(ActionEvent event) {
        combotype.setValue(null);
        txtAddr.setText(null);
        txtCity.setText(null);
        txtMail.setText(null);
        txtMob.setText(null);

        prev1.setImage(null);
        prev2.setImage(null);

    }
    Connection conn;
    @FXML
    void onDelete(ActionEvent event) {
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "delete from customers where mobile=?");
            pst.setString(1, txtMob.getText());
            int count=pst.executeUpdate();
            if(count>0)
                System.out.println("record deleted");
            else
                System.out.println("record not found");


        }
        catch(Exception exp){
            exp.printStackTrace();
        }

    }

    @FXML
    void onSave(ActionEvent event) {
        try {
//            Connection conn=null;
            PreparedStatement pst =conn.prepareStatement("insert into customers values(?,?,?,?,?,?,?,?)");
            pst.setString(1, txtMob.getText());
            pst.setString(2, txtName.getText());

            pst.setString(3, txtAddr.getText());
            pst.setString(4, txtCity.getText());
            pst.setString(5, txtMail.getText());
            pst.setString(6, combotype.getValue());

            File fileimg = new File(customerFile.getAbsolutePath());
            FileInputStream stream = new FileInputStream(fileimg);
            pst.setBinaryStream(7, (InputStream) stream, (int) fileimg.length());

            File fileimg2 = new File(aadharFile.getAbsolutePath());
            FileInputStream stream2 = new FileInputStream(fileimg2);
            pst.setBinaryStream(8, (InputStream) stream2, (int) fileimg2.length());
            pst.executeUpdate();
            System.out.println("record saved successsfully");
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }
    @FXML
    void doRegisterFace(ActionEvent event) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/customerview/camera.fxml"));

        Parent root = loader.load();

        CameraController controller = loader.getController();

        controller.setCustomerMobile(txtMob.getText());

        Stage stage = new Stage();

        stage.setTitle("Face Registration");

        stage.setScene(new Scene(root));

        stage.show();

    }
    @FXML
    void onSearch(ActionEvent event) {
        try{
            PreparedStatement pst = conn.prepareStatement(
                    "select * from customers where mobile=?");
            pst.setString(1, txtMob.getText());
            java.sql.ResultSet rs = pst.executeQuery();
            if(rs.next()){
                txtName.setText(rs.getString("cname"));
                txtAddr.setText(rs.getString("address"));
                txtCity.setText(rs.getString("city"));
                txtMail.setText(rs.getString("email"));

                combotype.setValue(rs.getString("ctype"));
                InputStream strm1 = rs.getBinaryStream("pic");
                prev1.setImage(new Image(strm1));

                InputStream strm2 = rs.getBinaryStream("acard");
                prev2.setImage(new Image(strm2));

                System.out.println("Record Found");


            }
            else{
                System.out.println("record not found");
            }


        }
catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        try {
            PreparedStatement pst = conn.prepareStatement(
                    "update customers set cname=?, address=?, city=?, email=?, ctype=? where mobile=?");
            pst.setString(1, txtName.getText());

            pst.setString(2, txtAddr.getText());
            pst.setString(3, txtCity.getText());
            pst.setString(4, txtMail.getText());
            pst.setString(5, combotype.getValue());


            pst.setString(6, txtMob.getText());

            int count=pst.executeUpdate();
            if(count>0){
                System.out.println("record updated");
            }
            else{
                System.out.println("not found");
            }
        }
        catch(Exception exp) {
            exp.printStackTrace();
        }

    }


    @FXML
    void initialize() {
        conn = projectdatabaseconnection.doconnecttodb();
        if (conn != null)
            System.out.println("Connected Successfully");
        else
            System.out.println("Connection Failed");

        String     types[]={"Buyer", "Seller","Both"};

        combotype.getItems().addAll(types);
        combotype.getSelectionModel().select(0);

    }




}
