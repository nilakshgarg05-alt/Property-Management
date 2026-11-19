package jsg1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class jsg1controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnclear;

    @FXML
    private Button btnmarks;

    @FXML
    private Button btnper;

    @FXML
    private Button btntotal;

    @FXML
    private TextField textD;

    @FXML
    private TextField txtA;

    @FXML
    private TextField txtB;

    @FXML
    private TextField txtC;

    @FXML
    private TextField txtE;

    @FXML
    private TextField txtF;

    @FXML
    void doclear(ActionEvent event) {
txtA.setText("");
        txtB.setText("");

        txtC.setText("");

    }

    @FXML
    void domarks(ActionEvent event) {
        String strA = txtA.getText().trim();
        String strB = txtB.getText().trim();
        String strC = txtC.getText().trim();
        if (dovalidate(strA)==true && dovalidate(strB)==true &&dovalidate(strC)==true) {

            try {
            if(Integer.parseInt(strA)>Integer.parseInt(strB) && Integer.parseInt(strA)>Integer.parseInt(strC) ){
                txtF.setText(String.valueOf(Integer.parseInt(strA)));
            }
                else if(Integer.parseInt(strB)>Integer.parseInt(strC) && Integer.parseInt(strB)>Integer.parseInt(strA) ){
                    txtF.setText(String.valueOf(Integer.parseInt(strB)));
                }
                else{
                    txtF.setText(String.valueOf(Integer.parseInt(strC)));
                }
            } catch (Exception exp) {
                showMsg("Fill nuemric vlaue");
                txtA.setText("");
                txtB.setText("");

                txtC.setText("");

            }
        }
        else{
            showMsg("empty");
            txtA.setText("");
            txtB.setText("");

            txtC.setText("");


        }
    }

    @FXML
    void doper(ActionEvent event) {
        String strA = txtA.getText().trim();
        String strB = txtB.getText().trim();
        String strC = txtC.getText().trim();
        if (dovalidate(strA)==true && dovalidate(strB)==true &&dovalidate(strC)==true) {

            try {

                String sum = String.valueOf(((Integer.parseInt(strA) + Integer.parseInt(strB) + Integer.parseInt(strC))*100)/300);

                txtE.setText(sum);
            } catch (Exception exp) {
                showMsg("Fill nuemric vlaue");
                txtA.setText("");
                txtB.setText("");

                txtC.setText("");

            }
        }
        else{
            showMsg("empty");
            txtA.setText("");
            txtB.setText("");

            txtC.setText("");


        }
    }

    @FXML
    void dototal(ActionEvent event) {
        String strA = txtA.getText().trim();
        String strB = txtB.getText().trim();
        String strC = txtC.getText().trim();
        if (dovalidate(strA)==true && dovalidate(strB)==true &&dovalidate(strC)==true) {

            try {
                String sum = String.valueOf(Integer.parseInt(strA) + Integer.parseInt(strB) + Integer.parseInt(strC));


                textD.setText(sum);
            } catch (Exception exp) {
                showMsg("Fill nuemric vlaue");
                txtA.setText("");
                txtB.setText("");

                txtC.setText("");

            }
        }
        else{
showMsg("empty");
        txtA.setText("");
            txtB.setText("");

            txtC.setText("");


        }
    }
    boolean dovalidate(String data){
        if(data=="")
        {
            return false;
        }
        else{
return true;
        }
    }

    void showMsg(String msg) {
        //Alert alert = new Alert(AlertType.INFORMATION);
        Alert alert = new Alert(Alert.AlertType.ERROR);
//Alert alert = new Alert(AlertType.WARNING);

        alert.setTitle("Information Dialog");
//or
//alert.setTitle(null);


        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    @FXML
    void initialize() {
        assert btnclear != null : "fx:id=\"btnclear\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert btnmarks != null : "fx:id=\"btnmarks\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert btnper != null : "fx:id=\"btnper\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert btntotal != null : "fx:id=\"btntotal\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert textD != null : "fx:id=\"textD\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert txtA != null : "fx:id=\"txtA\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert txtB != null : "fx:id=\"txtB\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert txtC != null : "fx:id=\"txtC\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert txtE != null : "fx:id=\"txtE\" was not injected: check your FXML file 'jsg1vieww.fxml'.";
        assert txtF != null : "fx:id=\"txtF\" was not injected: check your FXML file 'jsg1vieww.fxml'.";

    }

}
