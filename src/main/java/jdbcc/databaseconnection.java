package jdbcc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection {
public static Connection doconnecttodb(){
    Connection con=null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Class Loaded Sucessfullyy");
        con=DriverManager.getConnection("jdbc:mysql://localhost/java2026","root","root");
        System.out.println("database connected");

    }
catch (ClassNotFoundException exp)

        {
            exp.printStackTrace();
        }
    catch(SQLException exp){
        exp.printStackTrace();
    }

return con;
}
//
//    public static void main(String[] args) {
//        doconnecttodb();
//    }
}
