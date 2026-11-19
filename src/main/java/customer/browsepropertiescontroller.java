package customer;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class browsepropertiescontroller {
Connection conn;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Integer> comboarea;

    @FXML
    private ComboBox<String> combocity;

    @FXML
    private ComboBox<String> combocons;

    @FXML
    private ComboBox<String> comboprop;

    @FXML
    private TableView<browsepropertiesbean> propertytable;

    @FXML
    private TextField txtmax;

    @FXML
    private TextField txtmin;

    @FXML
    void ongenpdf(ActionEvent event) {
        try{

            FileChooser chooser=new FileChooser();

            chooser.setTitle("Save PDF");

            chooser.setInitialFileName("Properties.pdf");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "PDF Files","*.pdf"));

            File file=chooser.showSaveDialog(null);

            if(file==null)
                return;

            Document document=new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file));

            document.open();

            document.add(new Paragraph("Property Details"));

            document.add(new Paragraph(" "));

            PdfPTable table=new PdfPTable(11);

            table.addCell("Property ID");
            table.addCell("Mobile");
            table.addCell("Location");
            table.addCell("Size");
            table.addCell("Front");
            table.addCell("Rear");
            table.addCell("Left");
            table.addCell("Right");
            table.addCell("Facing");
            table.addCell("Price");
            table.addCell("Approved By");

            for(browsepropertiesbean p:propertytable.getItems()){

                table.addCell(String.valueOf(p.getRid()));
                table.addCell(p.getMobile());
                table.addCell(p.getLocation());
                table.addCell(String.valueOf(p.getSize()));
                table.addCell(String.valueOf(p.getFront()));
                table.addCell(String.valueOf(p.getRear()));
                table.addCell(String.valueOf(p.getLeftt()));
                table.addCell(String.valueOf(p.getRightt()));
                table.addCell(p.getFacing());
                table.addCell(String.valueOf(p.getPrice()));
                table.addCell(p.getApprovedby());

            }

            document.add(table);

            document.close();

            System.out.println("PDF Generated");

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void onshowprop(ActionEvent event) {
        ObservableList<browsepropertiesbean> list =
                FXCollections.observableArrayList();

        try {

            String sql = "SELECT rid,mobile,location,size,front,rear,leftt,rightt,facing,price,approvedby FROM properties WHERE 1=1";

            if (comboprop.getValue() != null)
                sql += " AND proptype=?";

            if (combocons.getValue() != null)
                sql += " AND constype=?";

            if (combocity.getValue() != null)
                sql += " AND city=?";

            if (comboarea.getValue() != null)
                sql += " AND area=?";

            if (!txtmin.getText().trim().isEmpty())
                sql += " AND price>=?";

            if (!txtmax.getText().trim().isEmpty())
                sql += " AND price<=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            int i = 1;

            if (comboprop.getValue() != null)
                pst.setString(i++, comboprop.getValue());

            if (combocons.getValue() != null)
                pst.setString(i++, combocons.getValue());

            if (combocity.getValue() != null)
                pst.setString(i++, combocity.getValue());

            if (comboarea.getValue() != null)
                pst.setInt(i++, comboarea.getValue());

            if (!txtmin.getText().trim().isEmpty())
                pst.setFloat(i++, Float.parseFloat(txtmin.getText()));

            if (!txtmax.getText().trim().isEmpty())
                pst.setFloat(i++, Float.parseFloat(txtmax.getText()));

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                list.add(new browsepropertiesbean(

                        rs.getInt("rid"),
                        rs.getString("mobile"),
                        rs.getString("location"),
                        rs.getFloat("size"),
                        rs.getFloat("front"),
                        rs.getFloat("rear"),
                        rs.getFloat("leftt"),
                        rs.getFloat("rightt"),
                        rs.getString("facing"),
                        rs.getFloat("price"),
                        rs.getString("approvedby")

                ));
            }

            propertytable.setItems(list);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {
        conn = projectdatabaseconnection.doconnecttodb();

        if(conn!=null)
            System.out.println("Connected Successfully");
        else
            System.out.println("Connection Failed");


        comboprop.getItems().addAll(
                "Agricultural",
                "Residential",
                "Commercial"
        );

        combocons.getItems().addAll(
                "Constructed",
                "Unconstructed"
        );


        try {

            PreparedStatement pst = conn.prepareStatement(
                    "select distinct city from properties");

            ResultSet rs = pst.executeQuery();

            while(rs.next())
                combocity.getItems().add(rs.getString("city"));


            pst = conn.prepareStatement(
                    "select distinct area from properties");

            rs = pst.executeQuery();

            while(rs.next())
                comboarea.getItems().add(rs.getInt("area"));

        }
        catch(Exception e){
            e.printStackTrace();
        }



        TableColumn<browsepropertiesbean,Integer> c1 =
                new TableColumn<>("Property ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("rid"));

        TableColumn<browsepropertiesbean,String> c2 =
                new TableColumn<>("Mobile");
        c2.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        TableColumn<browsepropertiesbean,String> c3 =
                new TableColumn<>("Location");
        c3.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<browsepropertiesbean,Float> c4 =
                new TableColumn<>("Size");
        c4.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<browsepropertiesbean,Float> c5 =
                new TableColumn<>("Front");
        c5.setCellValueFactory(new PropertyValueFactory<>("front"));

        TableColumn<browsepropertiesbean,Float> c6 =
                new TableColumn<>("Rear");
        c6.setCellValueFactory(new PropertyValueFactory<>("rear"));

        TableColumn<browsepropertiesbean,Float> c7 =
                new TableColumn<>("Left");
        c7.setCellValueFactory(new PropertyValueFactory<>("leftt"));

        TableColumn<browsepropertiesbean,Float> c8 =
                new TableColumn<>("Right");
        c8.setCellValueFactory(new PropertyValueFactory<>("rightt"));

        TableColumn<browsepropertiesbean,String> c9 =
                new TableColumn<>("Facing");
        c9.setCellValueFactory(new PropertyValueFactory<>("facing"));

        TableColumn<browsepropertiesbean,Float> c10 =
                new TableColumn<>("Price");
        c10.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<browsepropertiesbean,String> c11 =
                new TableColumn<>("Approved By");
        c11.setCellValueFactory(new PropertyValueFactory<>("approvedby"));

        propertytable.getColumns().addAll(
                c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11
        );

        assert comboarea != null : "fx:id=\"comboarea\" was not injected: check your FXML file 'browseproperties.fxml'.";
        assert combocity != null : "fx:id=\"combocity\" was not injected: check your FXML file 'browseproperties.fxml'.";
        assert combocons != null : "fx:id=\"combocons\" was not injected: check your FXML file 'browseproperties.fxml'.";
        assert comboprop != null : "fx:id=\"comboprop\" was not injected: check your FXML file 'browseproperties.fxml'.";
        assert propertytable != null : "fx:id=\"propertytable\" was not injected: check your FXML file 'browseproperties.fxml'.";
        assert txtmax != null : "fx:id=\"txtmax\" was not injected: check your FXML file 'browseproperties.fxml'.";
        assert txtmin != null : "fx:id=\"txtmin\" was not injected: check your FXML file 'browseproperties.fxml'.";

    }

}
