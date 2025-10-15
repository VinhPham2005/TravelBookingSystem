package DAO;

import Model.Guide;
import java.util.*;
import java.sql.*;

public class GuideDAO {

    public Guide setGuide(String Name, String Birthday, String PhoneNumber, String Email, float exp, String langs) {
        Guide g = new Guide();
        g.setName(Name);
        g.setBirthday(Birthday);
        g.setPhoneNumber(PhoneNumber);
        g.setEmail(Email);
        g.setGuideExperience(exp);
        ArrayList<String> listLangs = new ArrayList<>(Arrays.asList(langs.split(", ")));
        g.setForeignLanguage(listLangs);     
        g.chuanHoaTenVaNgaySinh();   
        return g;
    }

    // Tìm tourId khi biết tourName và dayStart và numberOfDay
    public static String findTourId(String tourName, String dayStart, double numberOfDay) throws ClassNotFoundException {
        String tourId = null;
        try { 
            Connection con = GetConnectionDAO.getConnection();
            String sql = "SELECT tourId FROM tour WHERE tourName = ? AND dayStart = STR_TO_DATE(?, '%d/%m/%Y') AND numberOfDays = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tourName);
            stmt.setString(2, dayStart);
            stmt.setDouble(3, numberOfDay);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tourId = rs.getString("tourId");
            } else {
                System.out.println("Không tìm thấy tour phù hợp.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi truy vấn findTourId: " + e.getMessage());
        }
        return tourId;
    }

    public static String findTourGuideState(String tourId) throws ClassNotFoundException {
        String tourGuideState = null;
        try {
            Connection con = GetConnectionDAO.getConnection();
            String sql = "SELECT tourGuideState FROM tour WHERE tourId = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tourId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tourGuideState = rs.getString("tourGuideState");
            } else {
                System.out.println("Không tìm thấy tour phù hợp.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi truy vấn findTourId: " + e.getMessage());
        }
        return tourGuideState;
    }

    public boolean guideCheck(Guide g, String tourId) {
        try {
            Connection con = GetConnectionDAO.getConnection();
            String sql = "SELECT languageGuideNeed FROM TOUR WHERE tourId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tourId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String langNeed = rs.getString("languageGuideNeed");
                List<String> listLangNeed = Arrays.asList(langNeed.split(", "));

                // Kiểm tra xem hướng dẫn viên có biết ít nhất một ngôn ngữ cần thiết không
                for (String lang : g.getForeignLanguage()) {
                    if (listLangNeed.contains(lang)) {
                        return true; 
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}