
package customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Blob;
import java.sql.Types;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class listpropertycontroller {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private ComboBox<String> approvedby;
    @FXML private Button btnremove;
    @FXML private Button btnsave;
    @FXML private Button btnsearch;
    @FXML private Button btnupdate;

    @FXML private ToggleGroup constype;
    @FXML private ComboBox<Integer> listprop;
    @FXML private ImageView prev1;
    @FXML private ImageView prev2;
    @FXML private ToggleGroup proptype;

    @FXML private TextField txtarea;
    @FXML private TextField txtcity;
    @FXML private TextField txtdirection;
    @FXML private TextField txtfront;
    @FXML private TextField txtleft;
    @FXML private TextField txtlocality;
    @FXML private TextField txtmobile;
    @FXML private TextField txtotherinfo;
    @FXML private TextField txtpropno;
    @FXML private TextField txtrear;
    @FXML private TextField txtright;
    @FXML private TextField txttotalprice;

    private File PlotFile1;
    private File PlotFile2;

    private Connection conn;

    // ------------------------------------------------
    // COMMON HELPER METHODS
    // ------------------------------------------------

    private void showMessage(String title, String message,
                             Alert.AlertType type) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getMobile() {
        return txtmobile.getText().trim();
    }

    private boolean validateDatabase() {

        if (conn == null) {
            showMessage("Database Error",
                    "MySQL connection is not available.",
                    Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validateForm() {

        if (getMobile().isEmpty()) {
            showMessage("Validation",
                    "Please enter customer mobile number.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (txtlocality.getText().trim().isEmpty()
                || txtcity.getText().trim().isEmpty()) {

            showMessage("Validation",
                    "Please enter property locality and city.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (txtarea.getText().trim().isEmpty()
                || txtpropno.getText().trim().isEmpty()
                || txtfront.getText().trim().isEmpty()
                || txtrear.getText().trim().isEmpty()
                || txtleft.getText().trim().isEmpty()
                || txtright.getText().trim().isEmpty()
                || txttotalprice.getText().trim().isEmpty()) {

            showMessage("Validation",
                    "Please fill in all numeric property fields.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (proptype.getSelectedToggle() == null
                || constype.getSelectedToggle() == null) {

            showMessage("Validation",
                    "Please select property type and construction type.",
                    Alert.AlertType.WARNING);
            return false;
        }

        if (approvedby.getValue() == null) {
            showMessage("Validation",
                    "Please select Approved By.",
                    Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(txtarea.getText().trim());

            new BigDecimal(txtpropno.getText().trim());
            new BigDecimal(txtfront.getText().trim());
            new BigDecimal(txtrear.getText().trim());
            new BigDecimal(txtleft.getText().trim());
            new BigDecimal(txtright.getText().trim());
            new BigDecimal(txttotalprice.getText().trim());

        } catch (NumberFormatException e) {

            showMessage("Invalid Input",
                    "Area must be a whole number. Dimensions and price must be numeric.",
                    Alert.AlertType.WARNING);

            return false;
        }

        return true;
    }

    private boolean customerExists(String mobile) throws Exception {

        String sql = "SELECT mobile FROM customers WHERE mobile = ?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, mobile);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String getSelectedRadio(ToggleGroup group) {

        if (group.getSelectedToggle() == null) {
            return null;
        }

        return ((RadioButton) group.getSelectedToggle()).getText();
    }

    // ------------------------------------------------
    // BROWSE PROPERTY PHOTO 1
    // ------------------------------------------------

    @FXML
    void dobrowse1(ActionEvent event) {

        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Property Photo 1");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Image Files", "*.jpg", "*.jpeg", "*.png"
                    )
            );

            File selected = chooser.showOpenDialog(null);

            if (selected != null) {

                PlotFile1 = selected;

                try (InputStream is = new FileInputStream(PlotFile1)) {
                    // Photo 1 is displayed in the first image panel (prev2).
                    prev2.setImage(new Image(is));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            showMessage("Image Error",
                    "Unable to load Photo 1.",
                    Alert.AlertType.ERROR);
        }
    }

    // ------------------------------------------------
    // BROWSE PROPERTY PHOTO 2
    // ------------------------------------------------

    @FXML
    void dobrowse2(ActionEvent event) {

        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Property Photo 2");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Image Files", "*.jpg", "*.jpeg", "*.png"
                    )
            );

            File selected = chooser.showOpenDialog(null);

            if (selected != null) {

                PlotFile2 = selected;

                try (InputStream is = new FileInputStream(PlotFile2)) {
                    // Photo 2 is displayed in the second image panel (prev1).
                    prev1.setImage(new Image(is));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            showMessage("Image Error",
                    "Unable to load Photo 2.",
                    Alert.AlertType.ERROR);
        }
    }

    // ------------------------------------------------
    // SAVE PROPERTY
    // ------------------------------------------------

    @FXML
    void dosave(ActionEvent event) {

        if (!validateDatabase() || !validateForm()) {
            return;
        }

        try {

            String mobile = getMobile();

            // Check customer before inserting property.
            if (!customerExists(mobile)) {

                showMessage("Customer Not Found",
                        "Register this customer first or enter an existing customer mobile number.",
                        Alert.AlertType.WARNING);

                return;
            }

            String sql =
                    "INSERT INTO properties " +
                            "(mobile, location, area, city, size, front, rear, " +
                            "leftt, rightt, facing, proptype, constype, approvedby, " +
                            "price, otherinfo, pic1, pic2) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, mobile);
                pst.setString(2, txtlocality.getText().trim());

                // Your MySQL area column is INT.
                pst.setInt(3, Integer.parseInt(txtarea.getText().trim()));

                pst.setString(4, txtcity.getText().trim());

                pst.setBigDecimal(5,
                        new BigDecimal(txtpropno.getText().trim()));

                pst.setBigDecimal(6,
                        new BigDecimal(txtfront.getText().trim()));

                pst.setBigDecimal(7,
                        new BigDecimal(txtrear.getText().trim()));

                pst.setBigDecimal(8,
                        new BigDecimal(txtleft.getText().trim()));

                pst.setBigDecimal(9,
                        new BigDecimal(txtright.getText().trim()));

                pst.setString(10, txtdirection.getText().trim());
                pst.setString(11, getSelectedRadio(proptype));
                pst.setString(12, getSelectedRadio(constype));
                pst.setString(13, approvedby.getValue());

                pst.setBigDecimal(14,
                        new BigDecimal(txttotalprice.getText().trim()));

                pst.setString(15, txtotherinfo.getText().trim());

                // Images are optional.
                if (PlotFile1 != null) {
                    pst.setBytes(16, Files.readAllBytes(PlotFile1.toPath()));
                } else {
                    pst.setNull(16, Types.BLOB);
                }

                if (PlotFile2 != null) {
                    pst.setBytes(17, Files.readAllBytes(PlotFile2.toPath()));
                } else {
                    pst.setNull(17, Types.BLOB);
                }

                int count = pst.executeUpdate();

                if (count > 0) {

                    showMessage("Success",
                            "Property saved successfully!",
                            Alert.AlertType.INFORMATION);

                    System.out.println("Property saved successfully.");

                    // Refresh IDs for this customer.
                    fetchProperties(mobile, false);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            showMessage("Save Error",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // ------------------------------------------------
    // FETCH PROPERTY IDS
    // ------------------------------------------------

    @FXML
    void dosearch(ActionEvent event) {

        if (!validateDatabase()) {
            return;
        }

        String mobile = getMobile();

        if (mobile.isEmpty()) {

            showMessage("Validation",
                    "Enter customer mobile number first.",
                    Alert.AlertType.WARNING);

            return;
        }

        fetchProperties(mobile, true);
    }

    private void fetchProperties(String mobile, boolean autoLoad) {

        try {

            listprop.getItems().clear();

            String sql =
                    "SELECT rid FROM properties WHERE mobile = ? ORDER BY rid";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, mobile);

                try (ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {
                        listprop.getItems().add(rs.getInt("rid"));
                    }
                }
            }

            if (listprop.getItems().isEmpty()) {

                showMessage("No Properties",
                        "No properties found for mobile number: " + mobile,
                        Alert.AlertType.INFORMATION);

                return;
            }

            System.out.println(
                    "Fetched property IDs: " + listprop.getItems()
            );

            if (autoLoad) {

                listprop.getSelectionModel().selectFirst();

                loadProperty(listprop.getValue());
            }

        } catch (Exception e) {

            e.printStackTrace();

            showMessage("Fetch Error",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // ------------------------------------------------
    // SELECTED PROPERTY FROM COMBOBOX
    // ------------------------------------------------

    @FXML
    void onlistprop(ActionEvent event) {

        Integer rid = listprop.getValue();

        if (rid == null) {
            return;
        }

        loadProperty(rid);
    }

    private void loadProperty(int rid) {

        if (!validateDatabase()) {
            return;
        }

        String sql = "SELECT * FROM properties WHERE rid = ?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, rid);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    txtmobile.setText(rs.getString("mobile"));
                    txtlocality.setText(rs.getString("location"));
                    txtarea.setText(rs.getString("area"));
                    txtcity.setText(rs.getString("city"));

                    txtpropno.setText(rs.getString("size"));
                    txtfront.setText(rs.getString("front"));
                    txtrear.setText(rs.getString("rear"));
                    txtleft.setText(rs.getString("leftt"));
                    txtright.setText(rs.getString("rightt"));

                    txtdirection.setText(rs.getString("facing"));

                    selectRadio(proptype, rs.getString("proptype"));
                    selectRadio(constype, rs.getString("constype"));

                    approvedby.setValue(rs.getString("approvedby"));

                    txttotalprice.setText(rs.getString("price"));
                    txtotherinfo.setText(rs.getString("otherinfo"));

                    // Load first photo.
                    Blob b1 = rs.getBlob("pic1");

                    if (b1 != null) {

                        try (InputStream is = b1.getBinaryStream()) {
                            prev2.setImage(new Image(is));
                        }

                    } else {
                        prev2.setImage(null);
                    }

                    // Load second photo.
                    Blob b2 = rs.getBlob("pic2");

                    if (b2 != null) {

                        try (InputStream is = b2.getBinaryStream()) {
                            prev1.setImage(new Image(is));
                        }

                    } else {
                        prev1.setImage(null);
                    }

                    // Clear previous selected image files.
                    PlotFile1 = null;
                    PlotFile2 = null;

                    System.out.println("Property loaded: " + rid);

                } else {

                    showMessage("Not Found",
                            "Property ID not found.",
                            Alert.AlertType.WARNING);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            showMessage("Loading Error",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void selectRadio(ToggleGroup group, String value) {

        if (value == null) {
            group.selectToggle(null);
            return;
        }

        for (Toggle toggle : group.getToggles()) {

            RadioButton rb = (RadioButton) toggle;

            if (rb.getText().equalsIgnoreCase(value)) {
                group.selectToggle(toggle);
                return;
            }
        }

        group.selectToggle(null);
    }

    // ------------------------------------------------
    // UPDATE PROPERTY
    // ------------------------------------------------

    @FXML
    void doupdate(ActionEvent event) {

        if (!validateDatabase() || !validateForm()) {
            return;
        }

        Integer rid = listprop.getValue();

        if (rid == null) {

            showMessage("Select Property",
                    "Fetch and select a property before updating.",
                    Alert.AlertType.WARNING);

            return;
        }

        try {

            String mobile = getMobile();

            if (!customerExists(mobile)) {

                showMessage("Customer Not Found",
                        "The entered customer mobile does not exist.",
                        Alert.AlertType.WARNING);

                return;
            }

            String sql =
                    "UPDATE properties SET " +
                            "mobile=?, location=?, area=?, city=?, size=?, " +
                            "front=?, rear=?, leftt=?, rightt=?, facing=?, " +
                            "proptype=?, constype=?, approvedby=?, price=?, " +
                            "otherinfo=?, pic1=COALESCE(?,pic1), " +
                            "pic2=COALESCE(?,pic2) WHERE rid=?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, mobile);
                pst.setString(2, txtlocality.getText().trim());
                pst.setInt(3,
                        Integer.parseInt(txtarea.getText().trim()));
                pst.setString(4, txtcity.getText().trim());

                pst.setBigDecimal(5,
                        new BigDecimal(txtpropno.getText().trim()));
                pst.setBigDecimal(6,
                        new BigDecimal(txtfront.getText().trim()));
                pst.setBigDecimal(7,
                        new BigDecimal(txtrear.getText().trim()));
                pst.setBigDecimal(8,
                        new BigDecimal(txtleft.getText().trim()));
                pst.setBigDecimal(9,
                        new BigDecimal(txtright.getText().trim()));

                pst.setString(10, txtdirection.getText().trim());
                pst.setString(11, getSelectedRadio(proptype));
                pst.setString(12, getSelectedRadio(constype));
                pst.setString(13, approvedby.getValue());

                pst.setBigDecimal(14,
                        new BigDecimal(txttotalprice.getText().trim()));

                pst.setString(15, txtotherinfo.getText().trim());

                if (PlotFile1 != null) {
                    pst.setBytes(16,
                            Files.readAllBytes(PlotFile1.toPath()));
                } else {
                    pst.setNull(16, Types.BLOB);
                }

                if (PlotFile2 != null) {
                    pst.setBytes(17,
                            Files.readAllBytes(PlotFile2.toPath()));
                } else {
                    pst.setNull(17, Types.BLOB);
                }

                pst.setInt(18, rid);

                int count = pst.executeUpdate();

                if (count > 0) {

                    showMessage("Updated",
                            "Property updated successfully!",
                            Alert.AlertType.INFORMATION);

                    fetchProperties(mobile, false);

                } else {

                    showMessage("Not Found",
                            "No property was updated.",
                            Alert.AlertType.WARNING);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            showMessage("Update Error",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // ------------------------------------------------
    // REMOVE PROPERTY
    // ------------------------------------------------

    @FXML
    void doremove(ActionEvent event) {

        if (!validateDatabase()) {
            return;
        }

        Integer rid = listprop.getValue();

        if (rid == null) {

            showMessage("Select Property",
                    "Please fetch and select a property first.",
                    Alert.AlertType.WARNING);

            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Property ID: " + rid);
        confirm.setContentText(
                "Are you sure you want to permanently delete this property?"
        );

        if (confirm.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {

            String sql = "DELETE FROM properties WHERE rid = ?";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setInt(1, rid);

                int count = pst.executeUpdate();

                if (count > 0) {

                    showMessage("Deleted",
                            "Property deleted successfully!",
                            Alert.AlertType.INFORMATION);

                    fetchProperties(getMobile(), false);

                    clearPropertyForm();

                } else {

                    showMessage("Not Found",
                            "Property does not exist.",
                            Alert.AlertType.WARNING);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            showMessage("Delete Error",
                    "Could not delete this property. It may be linked to an existing deal.\n"
                            + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // ------------------------------------------------
    // CLEAR FORM
    // ------------------------------------------------

    private void clearPropertyForm() {

        txtlocality.clear();
        txtarea.clear();
        txtcity.clear();
        txtpropno.clear();
        txtfront.clear();
        txtrear.clear();
        txtleft.clear();
        txtright.clear();
        txtdirection.clear();
        txttotalprice.clear();
        txtotherinfo.clear();

        proptype.selectToggle(null);
        constype.selectToggle(null);

        approvedby.getSelectionModel().selectFirst();

        prev1.setImage(null);
        prev2.setImage(null);

        PlotFile1 = null;
        PlotFile2 = null;
    }

    // ------------------------------------------------
    // COMBOBOX CLICK
    // ------------------------------------------------

    @FXML
    void clicklistprop(MouseEvent event) {
        // IDs are populated only after Fetch.
        // Do not load all properties here.
    }

    // ------------------------------------------------
    // APPROVED BY
    // ------------------------------------------------

    @FXML
    void onapprovedby(ActionEvent event) {
        // Selection handled by ComboBox.
    }

    // ------------------------------------------------
    // INITIALIZE
    // ------------------------------------------------

    @FXML
    void initialize() {

        conn = projectdatabaseconnection.doconnecttodb();

        if (conn != null) {
            System.out.println(
                    "Property Controller: Database connected successfully."
            );
        } else {
            System.out.println(
                    "Property Controller: Database connection failed."
            );
        }

        approvedby.getItems().setAll(
                "Buyer", "Seller", "Both"
        );

        approvedby.getSelectionModel().selectFirst();

        listprop.getItems().clear();
    }
}
