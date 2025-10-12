/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Main.Main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GuideForm extends JFrame {
    public GuideForm(String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Xác minh hướng dẫn viên");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblXacMinh = new JLabel("Mã xác minh:");
        JPasswordField txtXacMinh = new JPasswordField();

        JCheckBox showPass = new JCheckBox("Hiện mật khẩu");
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                txtXacMinh.setEchoChar((char) 0); // hiện mật khẩu
            } else {
                txtXacMinh.setEchoChar('*'); // ẩn mật khẩu
            }
        });
        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.addActionListener(e -> {
            String code = new String(txtXacMinh.getPassword());
            if (code.equals("guide123")) {
                new GuideSignIn(DB_URL, DB_USER, DB_PASSWORD);
                dispose();
            } else {
                statusLabel.setText("❌️ Mã xác minh không đúng!");
                statusLabel.setForeground(Color.RED);
            }
        });

        panel.add(lblXacMinh);
        panel.add(txtXacMinh);
        panel.add(showPass);
        panel.add(statusLabel);
        panel.add(new JLabel());
        panel.add(btnSubmit);

        setContentPane(panel);
        setVisible(true);
    }
}

class GuideSignIn extends JFrame {
    public GuideSignIn(String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Thông tin hướng dẫn viên");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblName = new JLabel("Họ và tên:");
        JTextField txtName = new JTextField();

        JLabel lblBirthday = new JLabel("Ngày sinh:");
        JTextField txtBirthday = new JTextField();

        JLabel lblPhone = new JLabel("Số điện thoại:");
        JTextField txtPhone = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        JLabel lblExperience = new JLabel("Kinh nghiệm (năm):");
        JTextField txtExperience = new JTextField();

        JLabel lblLanguages = new JLabel("<html>Ngoại ngữ <br> (Giữ Ctrl để chọn nhiểu ngôn ngữ): </html>");
        JList<String> listLangs = new JList<>(new String[] { "Tiếng Anh", "Tiếng Trung", "Tiếng Nhật", "Tiếng Hàn" });
        listLangs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listLangs);
        scrollPane.setPreferredSize(new Dimension(120, 80));

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        // Tạo Object Guide
        // Các thông số về tour để chuỗi rỗng
        JButton btnSubmit = new JButton("Tiếp tục");
        btnSubmit.addActionListener(e -> {
            String name = txtName.getText();
            String birthday = txtBirthday.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String exp = txtExperience.getText();
            StringBuilder langs = new StringBuilder();
            for (String lang : listLangs.getSelectedValuesList()) {
                if (langs.length() > 0)
                    langs.append(", ");
                langs.append(lang);
            }

            if (name.isEmpty() || birthday.isEmpty() || phone.isEmpty()
                    || email.isEmpty() || exp.isEmpty() || langs.isEmpty()) {
                statusLabel.setText("<html>⚠️ Vui lòng nhập đầy đủ <br>thông tin!</html>");
                statusLabel.setForeground(Color.RED);
                return;
            } else if (!Main.truePhoneNumber(phone)) {
                statusLabel.setText("❌ Số điện thoại không hợp lệ!");
                statusLabel.setForeground(Color.RED);
                return;
            } else if (!email.endsWith("@gmail.com")) {
                statusLabel.setText("❌ Email không hợp lệ!");
                statusLabel.setForeground(Color.RED);
                return;
            } else if (!Main.isDouble(exp) || Double.parseDouble(exp) < 0) {
                statusLabel.setText("❌ Kinh nghiệm không hợp lệ!");
                statusLabel.setForeground(Color.RED);
                return;
            }
            // Gán các giá trị như name, birthday, phone, ...
            // Đoạn này sẽ đưa object Guide vào thay vì các giá trị rời rạc
            // new BookinTour(Guide);
            new BookingTour(name, birthday, phone, email, exp, langs.toString(), DB_URL, DB_USER, DB_PASSWORD);
        });

        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblBirthday);
        panel.add(txtBirthday);
        panel.add(lblPhone);
        panel.add(txtPhone);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblExperience);
        panel.add(txtExperience);

        panel.add(lblLanguages);
        panel.add(scrollPane);

        panel.add(statusLabel);
        panel.add(btnSubmit);

        setContentPane(panel);
        setVisible(true);
    }
}

class BookingTour extends JFrame {
    static String[] collectTourInfo(String DB_URL, String DB_USER, String DB_PASSWORD) {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "select distinct tourName from TOUR";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tenTour = rs.getString("tourName");
                arr.add(tenTour);
            }
        } catch (SQLException e) {
            System.err.println("Error1");
            e.printStackTrace();
        }
        String[] TourNameArr = arr.toArray(new String[0]);
        return TourNameArr;
    }

    static String[] collectTourStartDate(String tourName, String DB_URL, String DB_USER, String DB_PASSWORD) {
        ArrayList<String> arrStart = new ArrayList<>();
        String sql = "select dayStart from TOUR where tourName = ?";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tourName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tìm startDay
                    Date date = rs.getDate("dayStart");
                    String dayStart = formatter.format(date);
                    arrStart.add(dayStart);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error2");
            e.printStackTrace();
        }
        return arrStart.toArray(new String[0]);
    }

    static Double[] collectTourDays(String tourName, String DB_URL, String DB_USER, String DB_PASSWORD) {
        ArrayList<Double> arr = new ArrayList<>();
        String sql = "select numberOfDays from TOUR where tourName = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tourName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Double numDays = rs.getDouble("numberOfDays");
                    arr.add(numDays);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error2");
            e.printStackTrace();
        }
        return arr.toArray(new Double[0]);
    }

    public BookingTour(String name, String birthday, String phone, String email, String exp, String langs,
            String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Chọn tour dẫn");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Collect Tour
        JLabel lblTour = new JLabel("Tên tour:");
        String[] tours = collectTourInfo(DB_URL, DB_USER, DB_PASSWORD);
        JComboBox<String> cbTour = new JComboBox<>(tours);
        String tour = (String) cbTour.getSelectedItem();

        // Collect StartDates
        JLabel lblDate = new JLabel("Ngày khởi hành:");
        String[] dates = collectTourStartDate(tour, DB_URL, DB_USER, DB_PASSWORD);
        JComboBox<String> cbDate = new JComboBox<>(dates);

        JLabel lblNumberOfDays = new JLabel("Số ngày: ");
        Double[] numberOfDays = collectTourDays(tour, DB_URL, DB_USER, DB_PASSWORD);
        JComboBox<Double> cbNumberOfDays = new JComboBox<>(numberOfDays);

        if (cbTour.getItemCount() > 0) {
            String initialTour = (String) cbTour.getSelectedItem();
            String[] initialDates = collectTourStartDate(initialTour, DB_URL, DB_USER, DB_PASSWORD);
            Double[] initialDays = collectTourDays(initialTour, DB_URL, DB_USER, DB_PASSWORD);
            cbDate.setModel(new DefaultComboBoxModel<>(initialDates));
            cbNumberOfDays.setModel(new DefaultComboBoxModel<>(initialDays));
        }

        // === PHẦN QUAN TRỌNG NHẤT: THÊM ACTIONLISTENER CHO cbTour ===
        cbTour.addActionListener(e -> {
            // 1. Lấy tên tour vừa được chọn
            String selectedTour = (String) cbTour.getSelectedItem();
            if (selectedTour != null) {
                // 2. Gọi lại hàm để lấy dữ liệu mới
                String[] newDates = collectTourStartDate(selectedTour, DB_URL, DB_USER, DB_PASSWORD);
                Double[] newDays = collectTourDays(selectedTour, DB_URL, DB_USER, DB_PASSWORD);

                // 3. Cập nhật lại model cho các ComboBox liên quan
                cbDate.setModel(new DefaultComboBoxModel<>(newDates));
                cbNumberOfDays.setModel(new DefaultComboBoxModel<>(newDays));
            }
        });

        // Từ tên và ngày khởi hành -> tourId
        // Set giá trị cho TourBooking của Object Guide
        // Từ TourBooking(tourId) tìm điều kiện ngôn ngữ của Tour -> Check xem Hướng dẫn
        // viên dủ điều kiện không

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.addActionListener(e -> {
            String date = (String) cbDate.getSelectedItem();

            // Kết nối database
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            } catch (SQLIntegrityConstraintViolationException dupEx) {
            } catch (SQLException ex) {
            }
            JOptionPane.showMessageDialog(this,
                    "Họ và tên: " + name +
                            "\nNgày sinh: " + birthday +
                            "\nSố điện thoại: " + phone +
                            "\nEmail: " + email +
                            "\nKinh nghiệm: " + exp + " năm" +
                            "\nNgoại ngữ: " + langs +
                            "\nTên tour: " + tour +
                            "\nNgày khởi hành: " + date,
                    "Xác nhận dẫn tour",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(lblTour);
        panel.add(cbTour);
        panel.add(lblDate);
        panel.add(cbDate);
        panel.add(lblNumberOfDays);
        panel.add(cbNumberOfDays);
        panel.add(new JLabel());
        panel.add(btnConfirm);
        setContentPane(panel);
        setVisible(true);
    }
}