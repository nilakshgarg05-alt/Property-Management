package listcombo;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class listcombo {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnaddinput;

    @FXML
    private Button btnclear;

    @FXML
    private Button btnshowsel;

    @FXML
    private Button btnshowallsel;

    @FXML
    private ComboBox<String> comboitems;

    @FXML
    private ListView<String> lstitems;

    @FXML
    void doaddincart(ActionEvent event) {
String item=comboitems.getSelectionModel().getSelectedItem();
        int idx=comboitems.getSelectionModel().getSelectedIndex();
        System.out.println(item+" "+idx);
        lstitems.getItems().add(item);

    }

    @FXML
    void doaddinput(ActionEvent event) {
        String str=doInputUsingDialog();
        if(!(str==null || str.equals("")))
            lstitems.getItems().add(str);    }

    @FXML
    void doclearallitems(ActionEvent event) {

    }

    @FXML
    void dodelondoubleclick(MouseEvent event) {
        if (event.getClickCount() == 2) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Remove Item");
            alert.setContentText("Are you sure you want to remove this item?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                lstitems.getItems().remove(lstitems.getSelectionModel().getSelectedItem());
            }
        }
    }

    @FXML
    void doshowsel(ActionEvent event) {
        System.out.println(lstitems.getSelectionModel().getSelectedItem());
    }

    @FXML
    void doshowallsel(ActionEvent event) {
ObservableList<String> allItems =lstitems.getSelectionModel().getSelectedItems();
        System.out.println(allItems);

        for(String str:allItems)
        {
            System.out.println(str);
        }
    }

    @FXML
    void initialize() {
        lstitems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
     String     types[]={"Select", "Book", "Car", "Laptop", "Mobile"};
     comboitems.getItems().addAll(types);
     comboitems.getSelectionModel().select(0);
//        doInputUsingDialog();

    }

    String doInputUsingDialog() {
        TextInputDialog dialog = new TextInputDialog("");
//        dialog.setTitle("Input Data...");
        dialog.setContentText("Please enter Quantity:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();

        //doAlert(result.get());
        String msg="";
        if (result.isPresent()) {
            msg=result.get();
        }
        return msg;
    }

}
