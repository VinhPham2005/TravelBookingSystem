package TourDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TourDBConnection {
    private static String URL = "jdbc:mysql://localhost:3306/TravelBookingSystemApp"; 
    private static String USER = "root";   
    private static String PASSWORD = "12345khongcho";
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        TourDBConnection gcd = new TourDBConnection();
        Connection con = gcd.getConnection();
        if(con != null) System.out.println("Thành công !");
        else System.out.println("Thất bại !");
    }
}