/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;
import Main.Main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ManagerForm extends JFrame {
    public ManagerForm() {
        setTitle("Xác minh quản lý");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
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

        JButton btnSubmit = new JButton("Xác nhận");
        btnSubmit.addActionListener(e -> {
            String code = new String(txtXacMinh.getPassword());
            if (code.equals("iammanager")) {
                new TourInput();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Mã xác minh không đúng!");
            }
        });

        panel.add(lblXacMinh);
        panel.add(txtXacMinh);
        panel.add(showPass);
        panel.add(btnSubmit);
        setContentPane(panel);
        setVisible(true);
    }
}

class TourInput extends JFrame {
    public TourInput() {
        setTitle("Nhập thông tin chuyến đi");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblStart = new JLabel("Điểm xuất phát:");
        JTextField txtStart = new JTextField();

        JLabel lblDestination = new JLabel("Điểm đến:");
        JTextField txtDestination = new JTextField();

        JLabel lblDayStart = new JLabel("Ngày khởi hành:");
        JTextField txtDayStart = new JTextField();

        JLabel lblNumberOfDays = new JLabel("Số ngày:");
        JTextField txtNumberOfDays = new JTextField();

        JLabel lblPrice = new JLabel("Giá/người:");
        JTextField txtPrice = new JTextField();

        JLabel lblNumberOfPassengers = new JLabel("Số khách tối đa:");
        JTextField txtNumberOfPassengers = new JTextField();

        JLabel lblNumberOfGuides = new JLabel("Số hướng dẫn viên:");
        JTextField txtNumberOfGuides = new JTextField();

        JButton btnSubmit = new JButton("Nhập");
        btnSubmit.addActionListener(e -> {
            String tourName = txtStart.getText() + " - " + txtDestination.getText();
            String dayStart = txtDayStart.getText();
            String numberOfDaysStr = txtNumberOfDays.getText();
            String numberOfPassengersStr = txtNumberOfPassengers.getText();
            String numberOfGuidesStr = txtNumberOfGuides.getText();
            String price = txtPrice.getText();

            if (tourName.isEmpty() || price.isEmpty() || numberOfDaysStr.isEmpty() || dayStart.isEmpty()
                    || numberOfPassengersStr.isEmpty() || numberOfGuidesStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            } else if (!Main.isDouble(price)) {
                JOptionPane.showMessageDialog(this, "Giá không hợp lệ!");
                return;
            } else if (!Main.isInteger(numberOfPassengersStr)) {
                JOptionPane.showMessageDialog(this, "Số hành khách tối đa không hợp lệ!");
                return;
            } else if (!Main.isInteger(numberOfGuidesStr)) {
                JOptionPane.showMessageDialog(this, "Số hướng dẫn viên không hợp lệ!");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Tour : " + tourName + "\nGiá: " + price + " VND / người\n" + "Thời gian: " + numberOfDaysStr +
                            "\nSố lượng hành khách: " + numberOfPassengersStr + " người" + "\nSố lượng hướng dẫn viên: "
                            + numberOfGuidesStr + " người",
                    "Thông tin chuyến đi", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(lblStart);
        panel.add(txtStart);
        panel.add(lblDestination);
        panel.add(txtDestination);
        panel.add(lblDayStart);
        panel.add(txtDayStart);
        panel.add(lblNumberOfDays);
        panel.add(txtNumberOfDays);
        panel.add(lblPrice);
        panel.add(txtPrice);
        panel.add(lblNumberOfPassengers);
        panel.add(txtNumberOfPassengers);
        panel.add(lblNumberOfGuides);
        panel.add(txtNumberOfGuides);
        panel.add(new JLabel());
        panel.add(btnSubmit);

        setContentPane(panel);
        setVisible(true);
    }
}