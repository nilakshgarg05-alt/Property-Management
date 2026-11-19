module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.security.jgss;
    requires org.apache.poi.poi;
    requires com.github.librepdf.openpdf;
    requires org.apache.poi.ooxml;
    requires java.management;
    requires opencv;
    requires javafx.swing;
    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
//    opens jsgject to javafx.fxml;
//    exports jsgject;
//    opens jsg1 to javafx.fxml;
//    exports jsg1;
//    opens listcombo to javafx.fxml;
//    exports listcombo;
//    opens shopping to javafx.fxml;
//    exports shopping;
//    opens chkrad to javafx.fxml;
//    exports chkrad;
//    opens jdbcc to javafx.fxml;
//    exports jdbcc;
    opens customer to javafx.fxml;
    exports customer;

}