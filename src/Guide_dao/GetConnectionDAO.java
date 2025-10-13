package Guide_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnectionDAO {
    private String url = "jdbc:mysql://localhost:3306/TravelBookingSystemApp";
    private String username = "root";
    private String password = "12345khongcho";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return (Connection) DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        GetConnectionDAO gcd = new GetConnectionDAO();
        Connection con = gcd.getConnection();
        if(con != null) System.out.println("Thành công !");
        else System.out.println("Thất bại !");
    }
}