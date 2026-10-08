package view;

import javax.swing.*;
import java.awt.*;

public class CheckoutScreen extends JPanel {

    public CheckoutScreen(Runnable onBack) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel label = new JLabel("หน้า Checkout (สำรอง)", SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 20));

        JButton backBtn = new JButton("ย้อนกลับ");
        backBtn.addActionListener(e -> onBack.run());

        add(label, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }
}