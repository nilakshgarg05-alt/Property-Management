package shopping;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class shop {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> avitems;

    @FXML
    private ListView<Integer> avprice;

    @FXML
    private Button btnbill;

    @FXML
    private Button btnclearcart;

    @FXML
    private Button btnremitem;

    @FXML
    private ListView<String> cartitems;

    @FXML
    private ListView<Integer> cartprice;

    @FXML
    private ComboBox<String> listitem;

    @FXML
    private Label totalbill;

    @FXML
    void onbill(ActionEvent event) {

        int total = 0;

        for (Integer price : cartprice.getItems()) {
            total += price;
        }

        totalbill.setText("Total Bill = " + total);    }

    @FXML
    void onclearcart(ActionEvent event) {
cartitems.getItems().clear();
        cartprice.getItems().clear();

    }

    @FXML
    void onlistitem(ActionEvent event) {
        String item=listitem.getSelectionModel().getSelectedItem();
        int idx=listitem.getSelectionModel().getSelectedIndex();
        System.out.println(item+" "+idx);
        if(item.equals("Pizza"))
        {
            avitems.getItems().addAll("Margherita","Farmhouse","Cheese Pizza");
            avprice.getItems().addAll(250,350,400);
        }
        else if(item.equals("Book"))
        {
            avitems.getItems().addAll("Java","Python","DBMS");
            avprice.getItems().addAll(500,600,700);
        }
    }

    @FXML
    void onremitem(ActionEvent event) {
int idx=avitems.getSelectionModel().getSelectedIndex();
if(idx!=-1){
    String name=avitems.getItems().get(idx);
    Integer price=avprice.getItems().get(idx);
    cartitems.getItems().add(name);
    cartprice.getItems().add(price);


}
    }

    @FXML
    void initialize() {
avitems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        String     types[]={"Select", "Book","Pizza"};

        listitem.getItems().addAll(types);
        listitem.getSelectionModel().select(0);
    }

}
