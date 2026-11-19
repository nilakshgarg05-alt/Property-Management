package jdbcc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.cj.xdevapi.PreparableStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class jdbccontroller {

    @FXML
    private ResourceBundle resources;
    @FXML
    private ImageView picprev;
    @FXML
    private Label lblpath;
    @FXML
    private URL location;

    @FXML
    private ComboBox<String> comboroll;

    @FXML
    private DatePicker dob;

    @FXML
    private TextField textname;

    @FXML
    private TextField txtper;

    @FXML
    void doclear(ActionEvent event) {
comboroll.setValue(null);
textname.setText(null);
txtper.setText(null);
dob.setValue(null);
    }

    @FXML
    void dodelete(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "delete from trainees where rollno=?");
            pst.setInt(1, Integer.parseInt(comboroll.getValue()));
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
    void dosave(ActionEvent event) {
        try {
            PreparedStatement pst =con.prepareStatement("insert into trainees values(?,?,?,?,?)");
            pst.setInt(1,Integer.parseInt(comboroll.getValue()));
            pst.setString(2,textname.getText());
            pst.setFloat(3,Float.parseFloat(txtper.getText()));
            LocalDate local=dob.getValue();
            Date date= Date.valueOf(local);
            pst.setDate(4,date);
            File fileimg=new File(fileref.getAbsolutePath());
            FileInputStream stream=new FileInputStream(fileimg );
            pst.setBinaryStream(5,(InputStream) stream,(int)(fileimg.length()));
            pst.executeUpdate();
            System.out.println("record saved successsfully");
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void dosearch(ActionEvent event) {
try{
    PreparedStatement pst = con.prepareStatement(
            "select * from trainees where rollno=?");
    pst.setInt(1, Integer.parseInt(comboroll.getValue()));
    java.sql.ResultSet rs = pst.executeQuery();
    if(rs.next()){
        textname.setText(rs.getString("sname"));
        txtper.setText(String.valueOf(rs.getFloat("per")));
        Date date = rs.getDate("dob");
        dob.setValue(date.toLocalDate());
        System.out.println("record found");
        InputStream strm=rs.getBinaryStream("pic");
        picprev.setImage(new Image(strm));


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
    void doupdate(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement("update trainees set sname=?,per=?,dob=? where rollno=?");
            pst.setString(1, textname.getText());

            pst.setFloat(2, Float.parseFloat(txtper.getText()));

            LocalDate local = dob.getValue();
            Date date = Date.valueOf(local);

            pst.setDate(3, date);

            pst.setInt(4, Integer.parseInt(comboroll.getValue()));
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
    Connection con;
    public void doConnect(){
        con=databaseconnection.doconnecttodb();
        if(con==null)
            System.out.println("connection error");
        else
            System.out.println("all is well");
    }

    @FXML
    void doshowall(ActionEvent event) {
        try {
            PreparedStatement pst = con.prepareStatement("select * from trainees");
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                int roll = result.getInt("rollno");
                float per = result.getFloat("per");
                String name = result.getString("sname");
                Date dob = result.getDate("dob");
                System.out.println(roll + " " + per + " " + name + " " + dob.toString());
            }
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }
    File fileref;
    @FXML
    void douploadimg(ActionEvent event) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("select profile pic:");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png")
            );
            fileref = chooser.showOpenDialog(null);

            lblpath.setText(fileref.getAbsolutePath());
            picprev.setImage(new Image(new FileInputStream(fileref)));
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }
    @FXML
    void initialize() {
doConnect();
    }

}
