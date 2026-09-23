import javax.swing.*;
import java.awt.*;

public class RegisterForm {
    public RegisterForm (){
        ImageIcon logos = new ImageIcon("logo.png");
        Image scaleImage = logos.getImage().getScaledInstance(222, 120, Image.SCALE_SMOOTH);
        ImageIcon rerizeIcon = new ImageIcon(scaleImage);

        JLabel logoLabel = new JLabel(rerizeIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JFrame frame = new JFrame("CDNP Burger - Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 580);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(0xF5, 0xEF, 0xE1)); // สีพื้นหลังครีม
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleregister = new JLabel("สร้างบัญชี");
        titleregister.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleregister.setForeground(new Color(0x61, 0x27, 0x27));
        titleregister.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField Name = new JTextField("ชื่อจริง");
        Name.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Name.setMaximumSize(new Dimension(300, 40));
        Name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField Lastname = new JTextField("นามสกุล");
        Lastname.setFont(new Font("Tahoma",Font.PLAIN,14));
        Lastname.setMaximumSize(new Dimension(300,40));
        Lastname.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField Address = new JTextField("ที่อยู่");
        Address.setFont(new Font("Tahoma",Font.PLAIN,14));
        Address.setMaximumSize(new Dimension(300,40));
        Address.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField Phone = new JTextField("เบอร์โทรศัพท์");
        Phone.setFont(new Font("Tahoma",Font.PLAIN,14));
        Phone.setMaximumSize(new Dimension(300,40));
        Phone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("สมัครบัญชีผู้ใช้");
        registerBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        registerBtn.setBackground(new Color(0xF5, 0xEF, 0xE1));
        registerBtn.setForeground(new Color(0xF0, 0x1E, 0x1E));
        registerBtn.setBorder(BorderFactory.createLineBorder(new Color(0xF0, 0x1E, 0x1E), 2));
        registerBtn.setFocusPainted(false);
        registerBtn.setMaximumSize(new Dimension(300, 45));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(titleregister);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(Name);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(Lastname);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(Address);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(Phone);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        mainPanel.add(registerBtn);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
}
