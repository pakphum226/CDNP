import javax.swing.*;
import java.awt.*;

public class BurgerLoginSwing {
    public BurgerLoginSwing(){
        ImageIcon logos = new ImageIcon("logo.png");
        Image scaleImage = logos.getImage().getScaledInstance(222, 120, Image.SCALE_SMOOTH);
        ImageIcon rerizeIcon = new ImageIcon(scaleImage);

        JLabel logoLabel = new JLabel(rerizeIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // 1. สร้างหน้าต่างหลัก (JFrame)
        JFrame frame = new JFrame("CDNP Burger - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 580);
        frame.setLocationRelativeTo(null); // จัดให้อยู่กลางจอ

        // 2. Panel หลัก ใช้จัดระเบียบแนวตั้ง (BoxLayout)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0xF5, 0xEF, 0xE1)); // สีพื้นหลังครีม
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- หัวข้อ ---
        JLabel titleLabel = new JLabel("ยินดีต้อนรับสู่ CDNP Burger");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0x61, 0x27, 0x27));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ช่องกรอกเบอร์โทร ---
        JTextField phoneField = new JTextField("เบอร์โทรศัพท์");
        phoneField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        phoneField.setMaximumSize(new Dimension(300, 40));
        phoneField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ปุ่มลงชื่อเข้าใช้ ---
        JButton loginBtn = new JButton("ลงชื่อเข้าใช้");
        loginBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        loginBtn.setBackground(new Color(0xF0, 0x1E, 0x1E)); // สีแดง
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setMaximumSize(new Dimension(300, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ข้อความ "หรือ" ---
        JLabel dividerLabel = new JLabel("หรือ");
        dividerLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        dividerLabel.setForeground(new Color(0x61, 0x27, 0x27));
        dividerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ปุ่ม Sign In with Google ---
        JButton googleBtn = new JButton("Sign In with Google");
        googleBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        googleBtn.setBackground(Color.WHITE);
        googleBtn.setForeground(new Color(0x44, 0x44, 0x44));
        googleBtn.setFocusPainted(false);
        googleBtn.setMaximumSize(new Dimension(300, 45));
        googleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ข้อความ "ไม่มีบัญชีผู้ใช้" ---
        JLabel noAccountLabel = new JLabel("ไม่มีบัญชีผู้ใช้");
        noAccountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        noAccountLabel.setForeground(new Color(0x61, 0x27, 0x27));
        noAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ปุ่มสมัครบัญชีผู้ใช้ ---
        JButton registerBtn = new JButton("สมัครบัญชีผู้ใช้");
        registerBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        registerBtn.setBackground(new Color(0xF5, 0xEF, 0xE1));
        registerBtn.setForeground(new Color(0xF0, 0x1E, 0x1E));
        registerBtn.setBorder(BorderFactory.createLineBorder(new Color(0xF0, 0x1E, 0x1E), 2));
        registerBtn.setFocusPainted(false);
        registerBtn.setMaximumSize(new Dimension(300, 45));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. ใส่ Component ลงใน Main Panel พร้อมเว้นระยะห่าง (RigidArea)
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(phoneField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(loginBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(dividerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(googleBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(noAccountLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(registerBtn);

        // 4. แสดงผลหน้าจอ
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}