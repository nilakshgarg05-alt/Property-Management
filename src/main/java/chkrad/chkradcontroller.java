package chkrad;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class chkradcontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnoffer;

    @FXML
    private ToggleGroup offer;
    @FXML
    private Button btnselected;

    @FXML
    private CheckBox chkall;

    @FXML
    private CheckBox chkb;

    @FXML
    private CheckBox chkp;

    @FXML
    private Label lblbill;

    @FXML
    private Label lblitems;

    @FXML
    private RadioButton rad10;

    @FXML
    private RadioButton rad20;

    int bill=0;

    @FXML
    void dochkall(ActionEvent event) {
        chkp.setSelected(chkall.isSelected());
        chkb.setSelected(chkall.isSelected());
    }

    @FXML
    void dooffer(ActionEvent event) {
        int offer = 0;
        if (this.rad10.isSelected()) {
            offer = 10;
        } else if (this.rad20.isSelected()) {
            offer = 20;
        }

        this.bill -= this.bill * offer / 100;
        System.out.println("Net Bill=" + this.bill);
        this.bill = 0;
    }

    @FXML
    void doselected(ActionEvent event) {
        String items = "";
        if (this.chkp.isSelected()) {
            this.bill += 100;
            items = items + this.chkp.getText() + ",";
        }

        if (this.chkb.isSelected()) {
            this.bill += 60;
            items = items + this.chkb.getText() + ",";
        }

        this.lblbill.setText("Bill=" + this.bill);
        this.lblitems.setText(items);
    }

    @FXML
    void initialize() {

    }

}
