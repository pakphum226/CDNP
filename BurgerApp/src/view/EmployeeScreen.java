package view;

import model.Order;
import model.OrderItem;
import model.OrderManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class EmployeeScreen extends JPanel {

    private final JPanel centerContainer;
    private final CardLayout centerCardLayout;

    private final JPanel emptyPanel;
    private final JPanel ordersContainerPanel;

    public EmployeeScreen(Runnable onBackToMenu) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // --- Header Bar ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 40, 40));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("หน้าจอพนักงาน / ครัว (Kitchen Display)");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JButton backBtn = new JButton("← กลับหน้าหลัก");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> onBackToMenu.run());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backBtn, BorderLayout.EAST);

        // --- Center Layout (CardLayout เพื่อสลับระหว่าง หน้าว่างกลางจอ กับ หน้าแสดงการ์ด) ---
        centerCardLayout = new CardLayout();
        centerContainer = new JPanel(centerCardLayout);
        centerContainer.setOpaque(false);

        // 1. หน้าแสดงเมื่อไม่มีออเดอร์ (อยู่ตรงกลางจอแบบ GridBagLayout)
        emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setOpaque(false);

        JLabel emptyLabel = new JLabel("ไม่มีออเดอร์ค้างในขณะนี้", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Tahoma", Font.BOLD, 32));
        emptyLabel.setForeground(new Color(140, 140, 140));

        emptyPanel.add(emptyLabel);

        // 2. หน้าแสดงการ์ดออเดอร์
        ordersContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        ordersContainerPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(ordersContainerPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        centerContainer.add(emptyPanel, "Empty");
        centerContainer.add(scrollPane, "Orders");

        add(headerPanel, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);

        // ลงทะเบียน Listener
        OrderManager.setOnOrdersChangedListener(this::refreshOrders);

        // รีเฟรชหน้าจอ
        refreshOrders();
    }

    public void refreshOrders() {
        ordersContainerPanel.removeAll();
        List<Order> orders = OrderManager.getActiveOrders();

        if (orders.isEmpty()) {
            centerCardLayout.show(centerContainer, "Empty");
        } else {
            for (Order order : orders) {
                ordersContainerPanel.add(createOrderCard(order));
            }
            centerCardLayout.show(centerContainer, "Orders");
        }

        centerContainer.revalidate();
        centerContainer.repaint();
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(320, 380));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        // Header การ์ด
        JPanel cardHeader = new JPanel(new BorderLayout());
        boolean isProcessing = "กำลังทำ".equals(order.getStatus());
        cardHeader.setBackground(isProcessing ? new Color(240, 140, 0) : new Color(180, 40, 40));
        cardHeader.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel orderIdLbl = new JLabel(order.getOrderId());
        orderIdLbl.setFont(new Font("Impact", Font.PLAIN, 24));
        orderIdLbl.setForeground(Color.WHITE);

        JLabel statusLbl = new JLabel(order.getStatus());
        statusLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        statusLbl.setForeground(Color.WHITE);

        cardHeader.add(orderIdLbl, BorderLayout.WEST);
        cardHeader.add(statusLbl, BorderLayout.EAST);

        // รายการอาหาร
        JPanel itemListPanel = new JPanel();
        itemListPanel.setLayout(new BoxLayout(itemListPanel, BoxLayout.Y_AXIS));
        itemListPanel.setOpaque(false);
        itemListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (OrderItem item : order.getItems()) {
            JLabel itemLbl = new JLabel(String.format("• %s  x  %d", item.getName(), item.getQuantity()));
            itemLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
            itemLbl.setBorder(new EmptyBorder(4, 0, 4, 0));
            itemListPanel.add(itemLbl);
        }

        JScrollPane itemScroll = new JScrollPane(itemListPanel);
        itemScroll.setBorder(null);

        // ปุ่มควบคุมสถานะ
        JPanel actionPanel = new JPanel(new GridLayout(1, 1, 5, 0));
        actionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        actionPanel.setOpaque(false);

        if ("รอคิว".equals(order.getStatus())) {
            JButton startBtn = new JButton("เริ่มทำออเดอร์");
            startBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
            startBtn.setBackground(new Color(240, 140, 0));
            startBtn.setForeground(Color.WHITE);
            startBtn.setFocusPainted(false);
            startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            startBtn.addActionListener(e -> {
                // *** เรียก OrderManager เพื่อเปลี่ยนสถานะเป็น "กำลังทำ" และบันทึกลงไฟล์ orders_history.txt ***
                OrderManager.startOrder(order);
            });
            actionPanel.add(startBtn);
        } else {
            JButton doneBtn = new JButton("ทำเสร็จแล้ว");
            doneBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
            doneBtn.setBackground(new Color(40, 140, 60));
            doneBtn.setForeground(Color.WHITE);
            doneBtn.setFocusPainted(false);
            doneBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            doneBtn.addActionListener(e -> {
                // เรียก OrderManager เพื่อลบออเดอร์และบันทึกสถานะ "ทำเสร็จแล้ว" ลงไฟล์
                OrderManager.removeOrder(order);
            });
            actionPanel.add(doneBtn);
        }

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(itemScroll, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.SOUTH);

        return card;
    }
}