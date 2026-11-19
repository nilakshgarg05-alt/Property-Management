package customer;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class customerctableontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> customercat;

    @FXML
    private TableView<customertablebean> tabletrainees;
    Connection conn;
    @FXML
    void onexport(ActionEvent event) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Customers");

            int rownum = 0;

            Row header = sheet.createRow(rownum++);
            header.createCell(0).setCellValue("Mobile");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Address");
            header.createCell(3).setCellValue("City");
            header.createCell(4).setCellValue("Email");
            header.createCell(5).setCellValue("Type");

            for (customertablebean data : tabletrainees.getItems()) {

                Row row = sheet.createRow(rownum++);

                row.createCell(0).setCellValue(data.getMobile());
                row.createCell(1).setCellValue(data.getCname());
                row.createCell(2).setCellValue(data.getAddress());
                row.createCell(3).setCellValue(data.getCity());
                row.createCell(4).setCellValue(data.getEmail());
                row.createCell(5).setCellValue(data.getCtype());
            }

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Customers as Excel");
            chooser.setInitialFileName("Customers.xlsx");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

            File file = chooser.showSaveDialog(null);

            if (file == null)
                return;

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);

            out.close();
            workbook.close();

            System.out.println("Excel Created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onfetch(ActionEvent event) {
        ObservableList<customertablebean> list = FXCollections.observableArrayList();

        try {

            PreparedStatement pst;

            if (customercat.getValue() == null || customercat.getValue().equals("All"))
                pst = conn.prepareStatement("select * from customers");
            else {
                pst = conn.prepareStatement("select * from customers where ctype=?");
                pst.setString(1, customercat.getValue().toString());
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                list.add(new customertablebean(
                        rs.getString("mobile"),
                        rs.getString("cname"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("email"),
                        rs.getString("ctype")
                ));

            }

            tabletrainees.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onpdf(ActionEvent event) {

        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save PDF");
            chooser.setInitialFileName("Customers.pdf");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File file = chooser.showSaveDialog(null);

            if (file == null)
                return;

            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            document.add(new Paragraph("Customer Details"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);

            table.addCell("Mobile");
            table.addCell("Name");
            table.addCell("Address");
            table.addCell("City");
            table.addCell("Email");
            table.addCell("Type");

            for (customertablebean data : tabletrainees.getItems()) {

                table.addCell(data.getMobile());
                table.addCell(data.getCname());
                table.addCell(data.getAddress());
                table.addCell(data.getCity());
                table.addCell(data.getEmail());
                table.addCell(data.getCtype());
            }

            document.add(table);

            document.close();

            System.out.println("PDF Created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

        conn = projectdatabaseconnection.doconnecttodb();

        if(conn != null)
            System.out.println("Connected Successfully");
        else
            System.out.println("Connection Failed");
        customercat.getItems().addAll(
                "Buyer",
                "Seller",
                "Both",
                "All"
        );
        customercat.getSelectionModel().select("All");
        TableColumn<customertablebean, String> c1 = new TableColumn<>("Mobile");
        c1.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        TableColumn<customertablebean, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("cname"));

        TableColumn<customertablebean, String> c3 = new TableColumn<>("Address");
        c3.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<customertablebean, String> c4 = new TableColumn<>("City");
        c4.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<customertablebean, String> c5 = new TableColumn<>("Email");
        c5.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<customertablebean, String> c6 = new TableColumn<>("Category");
        c6.setCellValueFactory(new PropertyValueFactory<>("ctype"));

        tabletrainees.getColumns().addAll(c1, c2, c3, c4, c5, c6);
        assert customercat != null : "fx:id=\"customercat\" was not injected: check your FXML file 'browsecustomers.fxml'.";
        assert tabletrainees != null : "fx:id=\"tabletrainees\" was not injected: check your FXML file 'browsecustomers.fxml'.";

    }

}
