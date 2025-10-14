package View;

import Guide_dao.GuideDAO;
import Main.Main;
import Model.Guide;
import TourDAO.TourDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
            Guide guide = new Guide();
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
            GuideDAO guideDAO = new GuideDAO();
            guide = guideDAO.setGuide(name, birthday, phone, email, Float.parseFloat(exp), langs.toString());
            // Đoạn này sẽ đưa object Guide vào thay vì các giá trị rời rạc
            // new BookinTour(Guide);
            new BookingTour(guide, DB_URL, DB_USER, DB_PASSWORD);
            dispose();
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

    public BookingTour(Guide guide,
            String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Chọn tour dẫn");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        TourDAO tourDao = new TourDAO();

        JLabel lblTour = new JLabel("Tên tour:");
        String[] tours = tourDao.collectTourInfo(DB_URL, DB_USER, DB_PASSWORD);
        JComboBox<String> cbTour = new JComboBox<>(tours);

        JLabel lblDate = new JLabel("Ngày khởi hành:");
        JComboBox<String> cbDate = new JComboBox<>(); // Khởi tạo rỗng

        JLabel lblNumberOfDays = new JLabel("Số ngày: ");
        JComboBox<Double> cbNumberOfDays = new JComboBox<>(); // Khởi tạo rỗng

        JLabel statusLabel = new JLabel("Trạng thái Tour: ");
        JLabel lblTourState = new JLabel("");

        if (cbTour.getItemCount() > 0) {
            String initialTour = (String) cbTour.getSelectedItem();
            String[] initialDates = tourDao.collectTourStartDate(initialTour, DB_URL, DB_USER, DB_PASSWORD);
            cbDate.setModel(new DefaultComboBoxModel<>(initialDates));

            if (cbDate.getItemCount() > 0) {
                String initialDate = (String) cbDate.getSelectedItem();
                Double[] initialDays = tourDao.collectTourDays(initialTour, initialDate, DB_URL, DB_USER, DB_PASSWORD);
                cbNumberOfDays.setModel(new DefaultComboBoxModel<>(initialDays));
            }
            // Cập nhật trạng thái ngay lần đầu tải
            updateTourGuideStatus(cbTour, cbDate, cbNumberOfDays, lblTourState);
        }

        // === THÊM ACTIONLISTENER CHO cbTour ===
        cbTour.addActionListener(e -> {
            String selectedTour = (String) cbTour.getSelectedItem();
            if (selectedTour != null) {
                String[] newDates = tourDao.collectTourStartDate(selectedTour, DB_URL, DB_USER, DB_PASSWORD);
                cbDate.setModel(new DefaultComboBoxModel<>(newDates));

                if (cbDate.getItemCount() > 0) {
                    String firstDate = (String) cbDate.getSelectedItem();
                    Double[] newNumDays = tourDao.collectTourDays(selectedTour, firstDate, DB_URL, DB_USER,
                            DB_PASSWORD);
                    cbNumberOfDays.setModel(new DefaultComboBoxModel<>(newNumDays));
                }
                updateTourGuideStatus(cbTour, cbDate, cbNumberOfDays, lblTourState);
            }
        });

        // === THÊM ACTIONLISTENER CHO cbDate ===
        cbDate.addActionListener(e -> {
            String selectedTour = (String) cbTour.getSelectedItem();
            String selectedDate = (String) cbDate.getSelectedItem();
            if (selectedTour != null && selectedDate != null) {
                Double[] newNumDays = tourDao.collectTourDays(selectedTour, selectedDate, DB_URL, DB_USER, DB_PASSWORD);
                cbNumberOfDays.setModel(new DefaultComboBoxModel<>(newNumDays));
                updateTourGuideStatus(cbTour, cbDate, cbNumberOfDays, lblTourState);
            }
        });

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.addActionListener(e -> {
            String tourName = (String) cbTour.getSelectedItem();
            String date = (String) cbDate.getSelectedItem();
            Double numDays = (Double) cbNumberOfDays.getSelectedItem();
            String tourId = null;

            try {
                tourId = GuideDAO.findTourId(tourName, date, numDays);
                System.out.println("Tour: " + tourName + ", date: " + date + ", numDays: " + numDays);
                System.out.println("Tour ID: " + tourId);

            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            if (tourId == null) {
                JOptionPane.showMessageDialog(this,
                        "❌️ Không tìm thấy tour phù hợp!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tourGuideState = null;
            try {
                tourGuideState = GuideDAO.findTourGuideState(tourId);
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }

            if ("Full".equalsIgnoreCase(tourGuideState)) {
                JOptionPane.showMessageDialog(this, "⚠️ Tour này đã đủ người! Vui lòng chọn tour khác.", "Tour Đã Đầy",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!new GuideDAO().guideCheck(guide, tourId)) {
                JOptionPane.showMessageDialog(this,
                        "❌️ Bạn không đủ điều kiện ngôn ngữ để dẫn tour này!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                JOptionPane.showMessageDialog(this,
                        "✅️ Bạn đủ điều kiện ngôn ngữ để dẫn tour này!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // sau khi đủ điều kiện thì set các thông tin tour cho guide
                guide.setTourBooking(tourId);
                guide.setBookingState("Confirmed");
                guide.setBookingDate(java.time.LocalDate.now().toString());
                // Thêm chức năng add vào db sau
            }

            JOptionPane.showMessageDialog(this,
                    "Họ và tên: " + guide.getName() +
                            "\nNgày sinh: " + guide.getBirthday() +
                            "\nSố điện thoại: " + guide.getPhoneNumber() +
                            "\nEmail: " + guide.getEmail() +
                            "\nKinh nghiệm: " + guide.getGuideExperience() + " năm" +
                            "\nNgoại ngữ: " + guide.getForeignLanguageAsString() +
                            "\nTên tour: " + tourName +
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
        panel.add(statusLabel);
        panel.add(lblTourState);
        panel.add(new JLabel());
        panel.add(btnConfirm);
        setContentPane(panel);
        setVisible(true);
    }

    private void updateTourGuideStatus(JComboBox<String> cbTour, JComboBox<String> cbDate,
            JComboBox<Double> cbNumberOfDays, JLabel lblTourState) {
        String selectedTour = (String) cbTour.getSelectedItem();
        String selectedDate = (String) cbDate.getSelectedItem();
        Double selectedDays = (Double) cbNumberOfDays.getSelectedItem();

        if (selectedTour != null && selectedDate != null && selectedDays != null) {
            String tourId = null;
            try {
                tourId = GuideDAO.findTourId(selectedTour, selectedDate, selectedDays);
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
            if (tourId != null) {
                String tourGuideState = null;
                try {
                    tourGuideState = GuideDAO.findTourGuideState(tourId);
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
                lblTourState.setText(tourGuideState);
                if ("Full".equalsIgnoreCase(tourGuideState)) {
                    lblTourState.setForeground(Color.RED);
                } else {
                    lblTourState.setForeground(new Color(0, 102, 0)); // Màu xanh lá cây đậm
                }
            } else {
                lblTourState.setText("Không tìm thấy");
                lblTourState.setForeground(Color.ORANGE);
            }
        } else {
            lblTourState.setText("");
        }
    }
}