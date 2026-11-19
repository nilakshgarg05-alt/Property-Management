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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class browsedealcontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dates;

    @FXML
    private TableView<browsedealbean> dealstable;

    @FXML
    private TextField dosearch;

    @FXML
    private ToggleGroup status;
Connection conn;
    @FXML
    void doexcel(ActionEvent event) {
        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Deals");


            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Property ID");
            header.createCell(1).setCellValue("Seller");
            header.createCell(2).setCellValue("Seller Contact");
            header.createCell(3).setCellValue("Buyer");
            header.createCell(4).setCellValue("Buyer Contact");
            header.createCell(5).setCellValue("Final Amount");
            header.createCell(6).setCellValue("Down Payment");
            header.createCell(7).setCellValue("Deal Date");
            header.createCell(8).setCellValue("Registry Date");


            int rownum = 1;


            for(browsedealbean d : dealstable.getItems()) {

                Row row = sheet.createRow(rownum++);

                row.createCell(0).setCellValue(d.getPid());
                row.createCell(1).setCellValue(d.getSeller());
                row.createCell(2).setCellValue(d.getSellercontact());
                row.createCell(3).setCellValue(d.getBuyer());
                row.createCell(4).setCellValue(d.getBuyercontact());
                row.createCell(5).setCellValue(d.getFinalamt());
                row.createCell(6).setCellValue(d.getDownpmt());
                row.createCell(7).setCellValue(d.getDtofdeal());
                row.createCell(8).setCellValue(d.getDtofreg());

            }


            FileChooser chooser = new FileChooser();

            chooser.setTitle("Save Deals Excel");

            chooser.setInitialFileName("Deals.xlsx");

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Excel Files",
                            "*.xlsx"
                    )
            );


            File file = chooser.showSaveDialog(null);


            if(file == null)
                return;


            FileOutputStream out = new FileOutputStream(file);

            workbook.write(out);


            out.close();

            workbook.close();


            System.out.println("Excel Generated");


        }
        catch(Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    void dopdf(ActionEvent event) {
        try {


            FileChooser chooser = new FileChooser();

            chooser.setTitle("Save Deals PDF");

            chooser.setInitialFileName("Deals.pdf");


            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "PDF Files",
                            "*.pdf"
                    )
            );


            File file = chooser.showSaveDialog(null);


            if(file == null)
                return;



            Document document = new Document();


            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file)
            );


            document.open();


            document.add(
                    new Paragraph("Deal Details")
            );


            document.add(
                    new Paragraph(" ")
            );



            PdfPTable table = new PdfPTable(9);



            table.addCell("Property ID");
            table.addCell("Seller");
            table.addCell("Seller Contact");
            table.addCell("Buyer");
            table.addCell("Buyer Contact");
            table.addCell("Final Amount");
            table.addCell("Down Payment");
            table.addCell("Deal Date");
            table.addCell("Registry Date");



            for(browsedealbean d : dealstable.getItems()) {


                table.addCell(
                        String.valueOf(d.getPid())
                );

                table.addCell(
                        d.getSeller()
                );

                table.addCell(
                        d.getSellercontact()
                );

                table.addCell(
                        d.getBuyer()
                );

                table.addCell(
                        d.getBuyercontact()
                );

                table.addCell(
                        String.valueOf(d.getFinalamt())
                );

                table.addCell(
                        String.valueOf(d.getDownpmt())
                );

                table.addCell(
                        d.getDtofdeal()
                );

                table.addCell(
                        d.getDtofreg()
                );

            }


            document.add(table);


            document.close();


            System.out.println("PDF Generated");


        }
        catch(Exception e) {

            e.printStackTrace();

        }
    }

    @FXML
    void doshowdeals(ActionEvent event) {
        ObservableList<browsedealbean> list = FXCollections.observableArrayList();

        try {

            PreparedStatement pst;

            if (dosearch.getText().trim().isEmpty()) {

                pst = conn.prepareStatement("SELECT * FROM deals");

            } else {

                pst = conn.prepareStatement("SELECT * FROM deals WHERE pid=?");
                pst.setInt(1, Integer.parseInt(dosearch.getText()));
            }

            ResultSet rs = pst.executeQuery();


            while (rs.next()) {

                browsedealbean obj = new browsedealbean(
                        rs.getInt("pid"),
                        rs.getString("seller"),
                        rs.getString("sellercontact"),
                        rs.getString("buyer"),
                        rs.getString("buyercontact"),
                        rs.getInt("finalamt"),
                        rs.getInt("downpmt"),
                        rs.getDate("dtofdeal").toString(),
                        rs.getDate("dtofreg").toString()
                );

                list.add(obj);
            }

            dealstable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {

        conn = projectdatabaseconnection.doconnecttodb();

        TableColumn<browsedealbean,Integer> c1 = new TableColumn<>("Property ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("pid"));

        TableColumn<browsedealbean,String> c2 = new TableColumn<>("Seller");
        c2.setCellValueFactory(new PropertyValueFactory<>("seller"));

        TableColumn<browsedealbean,String> c3 = new TableColumn<>("Seller Contact");
        c3.setCellValueFactory(new PropertyValueFactory<>("sellercontact"));

        TableColumn<browsedealbean,String> c4 = new TableColumn<>("Buyer");
        c4.setCellValueFactory(new PropertyValueFactory<>("buyer"));

        TableColumn<browsedealbean,String> c5 = new TableColumn<>("Buyer Contact");
        c5.setCellValueFactory(new PropertyValueFactory<>("buyercontact"));

        TableColumn<browsedealbean,Integer> c6 = new TableColumn<>("Final Amount");
        c6.setCellValueFactory(new PropertyValueFactory<>("finalamt"));

        TableColumn<browsedealbean,Integer> c7 = new TableColumn<>("Down Payment");
        c7.setCellValueFactory(new PropertyValueFactory<>("downpmt"));

        TableColumn<browsedealbean,String> c8 = new TableColumn<>("Deal Date");
        c8.setCellValueFactory(new PropertyValueFactory<>("dtofdeal"));

        TableColumn<browsedealbean,String> c9 = new TableColumn<>("Registry Date");
        c9.setCellValueFactory(new PropertyValueFactory<>("dtofreg"));

        dealstable.getColumns().clear();
        dealstable.getColumns().addAll(c1,c2,c3,c4,c5,c6,c7,c8,c9);

        if(conn!=null)
            System.out.println("Connected Successfully");
        else
            System.out.println("Connection Failed");
    }

}
