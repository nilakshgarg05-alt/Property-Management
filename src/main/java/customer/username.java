package customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class username {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtpassword;

    @FXML
    private TextField txtusername;
    Connection conn;

    @FXML
    void dologin(ActionEvent event) {
        PreparedStatement pst = null;

        try {

            pst = conn.prepareStatement(
                    "select * from login where username=? and password=?");

            pst.setString(1, txtusername.getText());
            pst.setString(2, txtpassword.getText());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful");
                openForm("/customerview/dashboardview.fxml");

                // Open Dashboard Here

            } else {
                System.out.println("Invalid Username or Password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void facelogin(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/customerview/facelogin.fxml"));

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Shree Bala Ji - Face Login");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void doopencam(ActionEvent event) {
        openForm("/customerview/camera.fxml");

    }

    @FXML
    void initialize() {

        conn = projectdatabaseconnection.doconnecttodb();

        if (conn != null)
            System.out.println("Database Connected");
        else
            System.out.println("Connection Failed");
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
