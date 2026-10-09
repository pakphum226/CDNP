package view;

import model.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class PaymentScreen extends JPanel {

    // ปรับให้รับ Order order เข้ามาแทน List<OrderItem> และ double totalPrice
    public PaymentScreen(Order order, Runnable onFinished) {
        // ใช้ GridBagLayout เพื่อล็อคให้อยู่ตรงกลางหน้าจอเสมอ
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // กำหนดขนาดมาตรฐาน และขนาดขั้นต่ำ ป้องกันการโดนบีบย่อจนเสียทรง
        Dimension preferredSize = new Dimension(430, 720);
        Dimension minSize = new Dimension(380, 520);
        container.setPreferredSize(preferredSize);
        container.setMinimumSize(minSize);

        // ==========================================
        // 1. หัวข้อ & ไอคอนด้านบน
        // ==========================================
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- ไอคอนเครื่องหมายถูก ---
        JLabel checkIconLabel = new JLabel();
        checkIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        URL iconURL = getClass().getResource("/assets/Slip_2.png"); 
        if (iconURL != null) {
            Image img = new ImageIcon(iconURL).getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
            checkIconLabel.setIcon(new ImageIcon(img));
        }
        container.add(checkIconLabel);

        container.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- ข้อความชำระเงินสำเร็จ ---
        JLabel successLabel = new JLabel("ชำระเงินสำเร็จ!");
        successLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        successLabel.setForeground(new Color(34, 139, 34));
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(successLabel);

        container.add(Box.createRigidArea(new Dimension(0, 6)));

        // --- หมายเลขออเดอร์ ---
        JLabel orderTitleLabel = new JLabel("หมายเลขออเดอร์");
        orderTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        orderTitleLabel.setForeground(Color.GRAY);
        orderTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(orderTitleLabel);

        // *** ดึงเลขออร์เดอร์จริงจากวัตถุ order ***
        JLabel orderNumLabel = new JLabel(order.getOrderId());
        orderNumLabel.setFont(new Font("Impact", Font.PLAIN, 36));
        orderNumLabel.setForeground(new Color(180, 0, 0));
        orderNumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(orderNumLabel);

        container.add(Box.createRigidArea(new Dimension(0, 15)));

        // ==========================================
        // 2. รายการสินค้า (List Panel)
        // ==========================================
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);

        // ดึงรายการสินค้าจากวัตถุ order
        for (OrderItem item : order.getItems()) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(8, 12, 8, 12));
            row.setPreferredSize(new Dimension(380, 55));
            row.setMaximumSize(new Dimension(380, 55));

            // รูปสินค้า
            JLabel imgLabel = new JLabel();
            imgLabel.setPreferredSize(new Dimension(40, 40));
            URL imgURL = getClass().getResource(item.getImagePath());
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }

            // ชื่อและราคาต่อชิ้น
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(item.getName());
            nameLabel.setFont(new Font("Tahoma", Font.BOLD, 13));

            JLabel qtyPriceLabel = new JLabel(String.format("฿%.0f x %d", item.getPrice(), item.getQuantity()));
            qtyPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
            qtyPriceLabel.setForeground(Color.GRAY);

            infoPanel.add(Box.createVerticalGlue());
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            infoPanel.add(qtyPriceLabel);
            infoPanel.add(Box.createVerticalGlue());

            // ราคารวมชิ้น
            JLabel itemTotalLabel = new JLabel(String.format("฿%.0f", item.getTotalPrice()), SwingConstants.RIGHT);
            itemTotalLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
            itemTotalLabel.setForeground(new Color(180, 0, 0));

            JPanel priceRightPanel = new JPanel(new GridBagLayout());
            priceRightPanel.setOpaque(false);
            priceRightPanel.add(itemTotalLabel);

            row.add(imgLabel, BorderLayout.WEST);
            row.add(infoPanel, BorderLayout.CENTER);
            row.add(priceRightPanel, BorderLayout.EAST);

            itemsPanel.add(row);

            JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
            sep.setMaximumSize(new Dimension(380, 1));
            sep.setForeground(new Color(230, 230, 230));
            itemsPanel.add(sep);
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setPreferredSize(new Dimension(390, 260));
        scrollPane.setMaximumSize(new Dimension(390, 260));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        container.add(scrollPane);

        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // ==========================================
        // 3. ราคารวมทั้งสิ้น & ปุ่ม
        // ==========================================
        // ดึงราคารวมจากวัตถุ order
        JLabel grandTotalLabel = new JLabel(String.format("ราคารวมทั้งสิ้น: ฿%.0f", order.getTotalPrice()));
        grandTotalLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        grandTotalLabel.setForeground(new Color(180, 0, 0));
        grandTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(grandTotalLabel);

        container.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton finishBtn = new JButton("เสร็จสิ้น / สั่งซื้อใหม่");
        finishBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        finishBtn.setForeground(Color.WHITE);
        finishBtn.setBackground(new Color(100, 85, 85));
        finishBtn.setFocusPainted(false);
        finishBtn.setBorderPainted(false);
        finishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        finishBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishBtn.setMaximumSize(new Dimension(210, 42));
        finishBtn.setPreferredSize(new Dimension(210, 42));
        finishBtn.addActionListener(e -> onFinished.run());
        container.add(finishBtn);

        // จัดวาง GridBagConstraints ให้อยู่ตรงกลางเสมอ โดยคงขนาดไว้ไม่ให้โดนบีบย่อ
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        add(container, gbc);
    }
}