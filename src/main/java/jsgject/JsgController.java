package jsgject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class JsgController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnHi;

    @FXML
    void doHi(ActionEvent event) {
        System.out.println("HI");
    }

    @FXML
    void initialize() {
        assert btnHi != null : "fx:id=\"btnHi\" was not injected: check your FXML file 'JsgView.fxml'.";

    }

}
