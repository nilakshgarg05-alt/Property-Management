package customer;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class analyticscontroller {

    @FXML
    private PieChart constructionChart;

    @FXML
    private PieChart customerChart;

    @FXML
    private PieChart dealChart;

    @FXML
    private PieChart propertyChart;

    private Connection conn;


    @FXML
    void initialize() {

        // Connect to your existing MySQL database
        conn = projectdatabaseconnection.doconnecttodb();

        if (conn != null) {
            System.out.println("Analytics Database Connected");

            loadCustomerChart();
            loadPropertyChart();
            loadConstructionChart();
            loadDealChart();

        } else {
            System.out.println("Analytics Database Connection Failed");
        }
    }


    // ================= CUSTOMER TYPES =================

    private void loadCustomerChart() {

        String query =
                "SELECT ctype, COUNT(*) " +
                        "FROM customers " +
                        "GROUP BY ctype";

        loadChart(customerChart, query);
    }


    // ================= PROPERTY TYPES =================

    private void loadPropertyChart() {

        String query =
                "SELECT proptype, COUNT(*) " +
                        "FROM properties " +
                        "GROUP BY proptype";

        loadChart(propertyChart, query);
    }


    // ================= CONSTRUCTION TYPES =================

    private void loadConstructionChart() {

        String query =
                "SELECT constype, COUNT(*) " +
                        "FROM properties " +
                        "GROUP BY constype";

        loadChart(constructionChart, query);
    }


    // ================= DEAL STATUS =================

    private void loadDealChart() {

        String query =
                "SELECT status, COUNT(*) " +
                        "FROM deals " +
                        "GROUP BY status";

        loadChart(dealChart, query);
    }


    // ================= COMMON METHOD =================

    private void loadChart(PieChart chart, String query) {

        try {

            PreparedStatement pst =
                    conn.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                String name = rs.getString(1);
                int count = rs.getInt(2);

                if (name == null || name.trim().isEmpty()) {
                    name = "Unknown";
                }

                chart.getData().add(
                        new PieChart.Data(
                                name,
                                count
                        )
                );
            }

            rs.close();
            pst.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}