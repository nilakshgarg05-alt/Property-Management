package customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class projectdatabaseconnection {
    public static Connection doconnecttodb() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Class Loaded Sucessfullyy");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/javafx", "root", "root");
            System.out.println("database connected");

        } catch (ClassNotFoundException exp) {
            exp.printStackTrace();
        } catch (SQLException exp) {
            exp.printStackTrace();
        }

        return conn;
    }
//        public static void main(String[] args) {
//        doconnecttodb();
//    }
}
