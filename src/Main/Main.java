package Main;
import View.CustomerForm;
import View.GuideForm;
import View.ManagerForm;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Trang chủ");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] options = { "Khách hàng", "Hướng dẫn viên", "Quản lý" };
        JList<String> list = new JList<>(options);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnOpen = new JButton("Mở");
        btnOpen.addActionListener(e -> {
            String selected = list.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Vui lòng chọn một mục trước!");
                return;
            }
            switch (selected) {
                case "Khách hàng":
                    new CustomerForm();
                    break;
                case "Hướng dẫn viên":
                    new GuideForm();
                    break;
                case "Quản lý":
                    new ManagerForm();
                    break;
            }
        });

        mainPanel.add(new JScrollPane(list), BorderLayout.CENTER);
        mainPanel.add(btnOpen, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // tiện ích dùng chung
    public static boolean isDouble(String str) {
        if (str == null || str.isEmpty())
            return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean truePhoneNumber(String str) {
        if (str.length() != 10 && str.length() != 9)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty())
            return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}