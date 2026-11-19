package customer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class dashboard {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void docustomermanage(ActionEvent event) {
        openForm("/customerview/customer.fxml");
    }

    @FXML
    void docustomerdir(ActionEvent event) {
        openForm("/customerview/browsecustomers.fxml");
    }

    @FXML
    void dopropertymanage(ActionEvent event) {
        openForm("/customerview/propertyview.fxml");
    }

    @FXML
    void dodealmanage(ActionEvent event) {
        openForm("/customerview/dealsview.fxml");
    }

    @FXML
    void dodealexp(ActionEvent event) {
        openForm("/customerview/browsedeals.fxml");
    }

    @FXML
    void dopropcatalog(ActionEvent event) {
        openForm("/customerview/browseproperties.fxml");
    }


    @FXML
    void dosalesstats(ActionEvent event) {
        openForm("/customerview/analytics.fxml");
    }

    @FXML
    void docustomerinsights(ActionEvent event) {
        openForm("/customerview/analytics.fxml");
    }

    @FXML
    void dologout(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/customerview/usernameview.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Shree Bala Ji - Login");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            // Close Dashboard
            Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
            current.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

    }
    private void openForm(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Shree Bala Ji - Property Management");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
