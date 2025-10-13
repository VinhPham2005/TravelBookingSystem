package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnectionDAO {
    private static String url = "jdbc:mysql://localhost:3306/TravelBookingSystemApp";
    private static String username = "root";
    private static String password = "Vinh1234@";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return (Connection) DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection con = GetConnectionDAO.getConnection();
        if(con != null) System.out.println("Thành công !");
        else System.out.println("Thất bại !");
    }
}