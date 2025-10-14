package View;

import Main.Main;
import Model.Customer;
import TourDAO.TourDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Customer_dao.CustomerDAO;

import java.awt.*;
import java.math.BigDecimal;

public class CustomerForm extends JFrame {
    public CustomerForm(String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Nhập thông tin khách hàng");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblName = new JLabel("Họ và tên:");
        JTextField txtName = new JTextField();

        JLabel lblBirthday = new JLabel("Ngày sinh:");
        JTextField txtBirthday = new JTextField();

        JLabel lblPhone = new JLabel("Số điện thoại:");
        JTextField txtPhone = new JTextField();

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);

        // Tạo Customer với các chuỗi rỗng hoặc số là 0
        // Vd: Customer cus = new Customer("", "", 0.0, ...)
        JButton btnSubmit = new JButton("Nhập");
        btnSubmit.addActionListener(e -> {
            String name = txtName.getText();
            String birthday = txtBirthday.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            // Set các giá trị cho Customer
            if (name.isEmpty() || birthday.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                statusLabel.setText("<html>⚠️ Vui lòng nhập đầy đủ <br>thông tin!</html>");
                statusLabel.setForeground(Color.RED);
                return;
            } else if (!Main.truePhoneNumber(phone)) {
                statusLabel.setText("❌ Số điện thoại không hợp lệ!");
                statusLabel.setForeground(Color.RED);
                return;
            } else if (!email.contains("@gmail.com") && !email.contains("@yahoo.com")) {
                statusLabel.setText("❌ Email không hợp lệ!");
                statusLabel.setForeground(Color.RED);
                return;
            }
            // Truyền Object vào
            // new TourWindow(cus);
            Customer customer = new Customer();

            customer.setName(name);
            customer.setBirthday(birthday);
            customer.setPhoneNumber(phone);
            customer.setEmail(email);
            customer.chuanHoaTenVaNgaySinh();

            new TourWindow(customer, DB_URL, DB_USER, DB_PASSWORD);
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
        panel.add(statusLabel);
        panel.add(btnSubmit);

        setContentPane(panel);
        setVisible(true);

    }
}

class TourWindow extends JFrame {
    public TourWindow(Customer customer, String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Đăng ký tour");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        TourDAO tourDao = new TourDAO();

        JLabel lblTour = new JLabel("Tên tour:");
        String[] tours = tourDao.collectTourInfo(DB_URL, DB_USER, DB_PASSWORD);
        JComboBox<String> cbTour = new JComboBox<>(tours);

        JLabel lblDate = new JLabel("Ngày khởi hành:");
        JComboBox<String> cbDate = new JComboBox<>(); // Khởi tạo rỗng

        JLabel lblNumberOfPeople = new JLabel("Số người đi:");
        Integer[] number = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        JComboBox<Integer> cbNumber = new JComboBox<>(number);

        JLabel lblNumberOfDays = new JLabel("Số ngày: ");
        JComboBox<Double> cbNumberOfDays = new JComboBox<>(); // Khởi tạo rỗng

        JLabel statusLabel = new JLabel("Trạng thái Tour: ");
        JLabel lblTourState = new JLabel("");

        // Tải dữ liệu lần đầu cho tour được chọn mặc định
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
            updateTourStatus(cbTour, cbDate, cbNumberOfDays, lblTourState);
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
                updateTourStatus(cbTour, cbDate, cbNumberOfDays, lblTourState);
            }
        });

        // === THÊM ACTIONLISTENER CHO cbDate ===
        cbDate.addActionListener(e -> {
            String selectedTour = (String) cbTour.getSelectedItem();
            String selectedDate = (String) cbDate.getSelectedItem();
            if (selectedTour != null && selectedDate != null) {
                Double[] newNumDays = tourDao.collectTourDays(selectedTour, selectedDate, DB_URL, DB_USER, DB_PASSWORD);
                cbNumberOfDays.setModel(new DefaultComboBoxModel<>(newNumDays));
                updateTourStatus(cbTour, cbDate, cbNumberOfDays, lblTourState);
            }
        });

        // Bỏ ActionListener của cbNumberOfDays

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.addActionListener(e -> {
            String tourName = (String) cbTour.getSelectedItem();
            String date = (String) cbDate.getSelectedItem();
            String numPeople = cbNumber.getSelectedItem().toString();
            Double numDays = (Double) cbNumberOfDays.getSelectedItem();

            String tourId = CustomerDAO.findTourId(tourName, date, numDays);
            String tourState = CustomerDAO.findTourState(tourId);

            if ("Full".equalsIgnoreCase(tourState)) {
                JOptionPane.showMessageDialog(this, "⚠️ Tour này đã đủ người! Vui lòng chọn tour khác.", "Tour Đã Đầy",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            customer.setTourBooking(tourId);
            customer.setBookingDate(java.time.LocalDate.now().toString());
            customer.setNumberOfCustomers(Integer.parseInt(numPeople));
            JOptionPane.showMessageDialog(this,
                    "Họ và tên: " + customer.getName() +
                            "\nNgày sinh: " + customer.getBirthday() +
                            "\nSố điện thoại: " + customer.getPhoneNumber() +
                            "\nEmail: " + customer.getEmail() +
                            "\nTên tour: " + tourName +
                            "\nNgày khởi hành: " + date +
                            "\nSố ngày: " + numDays +
                            "\nSố người đi: " + numPeople + " người",
                    "Xác nhận đặt tour",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnPay = new JButton("Thanh toán");
        btnPay.addActionListener(e -> {
            // Cập nhật lại thông tin mới nhất trước khi thanh toán
            String tourName = (String) cbTour.getSelectedItem();
            String date = (String) cbDate.getSelectedItem();
            Double numDays = (Double) cbNumberOfDays.getSelectedItem();
            String tourId = CustomerDAO.findTourId(tourName, date, numDays);
            String tourState = CustomerDAO.findTourState(tourId);

            if ("Full".equalsIgnoreCase(tourState)) {
                JOptionPane.showMessageDialog(this, "⚠️ Tour này đã đủ người! Vui lòng chọn tour khác.", "Tour Đã Đầy",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            customer.setTourBooking(tourId);
            customer.setBookingDate(date);
            customer.setNumberOfCustomers((Integer) cbNumber.getSelectedItem());

            new XacNhanThanhToan(customer, customer.getNumberOfCustomers(), DB_URL, DB_USER, DB_PASSWORD);
            dispose();
        });

        panel.add(lblTour);
        panel.add(cbTour);
        panel.add(lblDate);
        panel.add(cbDate);
        panel.add(lblNumberOfPeople);
        panel.add(cbNumber);
        panel.add(lblNumberOfDays);
        panel.add(cbNumberOfDays);
        panel.add(statusLabel);
        panel.add(lblTourState);
        panel.add(btnConfirm);
        panel.add(btnPay);

        setContentPane(panel);
        setVisible(true);

    }

    // <<< PHƯƠNG THỨC MỚI ĐƯỢC THÊM VÀO >>>
    private void updateTourStatus(JComboBox<String> cbTour, JComboBox<String> cbDate, JComboBox<Double> cbNumberOfDays,
            JLabel lblTourState) {
        String selectedTour = (String) cbTour.getSelectedItem();
        String selectedDate = (String) cbDate.getSelectedItem();
        Double selectedDays = (Double) cbNumberOfDays.getSelectedItem();

        if (selectedTour != null && selectedDate != null && selectedDays != null) {
            String tourId = CustomerDAO.findTourId(selectedTour, selectedDate, selectedDays);
            if (tourId != null) {
                String tourState = CustomerDAO.findTourState(tourId);
                lblTourState.setText(tourState);
                if ("Full".equalsIgnoreCase(tourState)) {
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

class XacNhanThanhToan extends JFrame {
    public XacNhanThanhToan(Customer customer, int n, String DB_URL, String DB_USER, String DB_PASSWORD) {
        setTitle("Xác nhận thanh toán");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        BigDecimal pricePerPerson = CustomerDAO.findPrice(customer.getTourBooking());
        BigDecimal total = pricePerPerson.multiply(BigDecimal.valueOf(n));
        customer.setPrice(total);

        JLabel lblTotal = new JLabel("Tổng tiền thanh toán: ");
        JLabel lblTotalAmount = new JLabel(String.format("%.2f VND", total));

        JLabel lblInfo = new JLabel(
                "<html>Nhập mã xác minh: <br> Mã xác minh bằng <br> Tên + chuyển khoản + số người</html>");
        JTextField txtCode = new JTextField(); // Mã xác minh bằng Id + số người đăng ký (chuyển về chuỗi)

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);

        JButton btnNotPay = new JButton("Để sau");
        btnNotPay.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Xác nhận chưa thanh toán");
            customer.setBookingState("Pending");
            CustomerDAO.addCustomer(customer);
            dispose();
        });
        JButton btnPay = new JButton("Xác nhận thanh toán");
        btnPay.addActionListener(e -> {
            String code = txtCode.getText();
            if (customer.getNumberOfCustomers() == 1) {
                if (!code.equals(customer.getName() + " person ")) {
                    statusLabel.setText("❌ Mã xác nhận không dúng");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
            } else {
                if (!code.equals(customer.getName() + " group ")) {
                    statusLabel.setText("❌ Mã xác nhận không dúng");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nCảm ơn bạn đã sử dụng dịch vụ.");
            customer.setBookingState("Confirmed");
            CustomerDAO.addCustomer(customer);
            dispose();
        });

        panel.add(lblTotal);
        panel.add(lblTotalAmount);
        panel.add(lblInfo);
        panel.add(txtCode);
        panel.add(statusLabel);
        panel.add(new JLabel());
        panel.add(btnNotPay);
        panel.add(btnPay);

        setContentPane(panel);
        setVisible(true);
    }
}