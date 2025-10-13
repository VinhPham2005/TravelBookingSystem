package Guide_dao;

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

    public boolean guideCheck(Guide g, String tourName, String dayStart) {
        try {
            GetConnectionDAO gcd = new GetConnectionDAO();
            Connection con = gcd.getConnection();
            String sql = "SELECT languageGuideNeed FROM TOUR WHERE tourName = ? AND dayStart = STR_TO_DATE(?, '%d/%m/%Y')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tourName);
            ps.setString(2, dayStart);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String langNeed = rs.getString("languageGuideNeed");
                List<String> listLangNeed = Arrays.asList(langNeed.split(", "));
                return g.getForeignLanguage().containsAll(listLangNeed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}