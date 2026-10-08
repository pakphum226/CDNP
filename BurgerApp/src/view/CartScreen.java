package view;

import model.OrderItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class CartScreen extends JPanel {

    private final JPanel itemsListPanel;
    private final JLabel totalPriceLabel;
    private List<OrderItem> currentItems;

    public CartScreen(Runnable onBackToMenu, Consumer<OrderItem> onIncrease, Consumer<OrderItem> onDecrease, Consumer<OrderItem> onRemove, Runnable onCheckout) {
        // ใช้ GridBagLayout เพื่อล็อค Container ให้อยู่ตรงกลางหน้าจอเสมอ
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setOpaque(false);

        // กำหนดขนาดมาตรฐาน ขนาดขั้นต่ำ และขนาดสูงสุด เพื่อป้องกันการล้นหรือย่อจนเบียดกัน
        Dimension preferredSize = new Dimension(430, 750);
        Dimension minSize = new Dimension(380, 500);
        
        cartContainer.setPreferredSize(preferredSize);
        cartContainer.setMinimumSize(minSize);

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton backBtn = new JButton();
        URL backURL = getClass().getResource("/assets/Return.jpg");
        if (backURL != null) {
            Image backImage = new ImageIcon(backURL).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            backBtn.setIcon(new ImageIcon(backImage));
        }
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> onBackToMenu.run());

        JLabel titleLabel = new JLabel("ตะกร้าสินค้า", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));

        JLabel dummyRight = new JLabel("   ");
        dummyRight.setPreferredSize(new Dimension(40, 40));

        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(dummyRight, BorderLayout.EAST);

        // --- List Panel ---
        itemsListPanel = new JPanel();
        itemsListPanel.setLayout(new BoxLayout(itemsListPanel, BoxLayout.Y_AXIS));
        itemsListPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(itemsListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        // ซ่อน ScrollBar แนวระนาบ ป้องกัน thanh scroll ล้น
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        // --- Bottom Bar ---
        JPanel bottomBar = new JPanel(new BorderLayout(10, 0));
        bottomBar.setBackground(new Color(245, 245, 245));
        bottomBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pricePanel.setOpaque(false);

        JLabel totalTextLabel = new JLabel("ราคารวม: ");
        totalTextLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        totalTextLabel.setForeground(new Color(180, 40, 40));

        totalPriceLabel = new JLabel("฿0");
        totalPriceLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        totalPriceLabel.setForeground(new Color(180, 40, 40));

        pricePanel.add(totalTextLabel);
        pricePanel.add(totalPriceLabel);

        // --- Checkout Button ---
        JButton checkoutBtn = new JButton("สั่งซื้อ");
        checkoutBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setBackground(new Color(105, 90, 90));
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setBorderPainted(false);
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutBtn.setPreferredSize(new Dimension(130, 45));

        checkoutBtn.addActionListener(e -> {
            if (currentItems == null || currentItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ไม่มีสินค้าในตะกร้า", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                return;
            }
            onCheckout.run();
        });

        bottomBar.add(pricePanel, BorderLayout.WEST);
        bottomBar.add(checkoutBtn, BorderLayout.EAST);

        cartContainer.add(headerPanel, BorderLayout.NORTH);
        cartContainer.add(scrollPane, BorderLayout.CENTER);
        cartContainer.add(bottomBar, BorderLayout.SOUTH);

        // ตั้งค่า GridBagConstraints ให้รักษารูปทรงและอยู่ตรงกลางเสมอ
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL; // รักษาสัดส่วนแนวตั้งและคงความกว้างไม่ให้โดนบีบเบียด
        gbc.anchor = GridBagConstraints.CENTER;

        add(cartContainer, gbc);
    }

    public void renderCartList(List<OrderItem> items, Consumer<OrderItem> onIncrease, Consumer<OrderItem> onDecrease, Consumer<OrderItem> onRemove) {
        this.currentItems = items;
        itemsListPanel.removeAll();
        double total = 0;

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("ไม่มีสินค้าในตะกร้า", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemsListPanel.add(Box.createRigidArea(new Dimension(0, 50)));
            itemsListPanel.add(emptyLabel);
        } else {
            for (OrderItem item : items) {
                total += item.getPrice() * item.getQuantity();
                itemsListPanel.add(createItemRow(item, onIncrease, onDecrease, onRemove));

                JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
                sep.setMaximumSize(new Dimension(410, 1));
                sep.setForeground(new Color(230, 230, 230));
                itemsListPanel.add(sep);
            }
        }

        totalPriceLabel.setText(String.format("฿%.0f", total));
        itemsListPanel.revalidate();
        itemsListPanel.repaint();
    }

    private JPanel createItemRow(OrderItem item, Consumer<OrderItem> onIncrease, Consumer<OrderItem> onDecrease, Consumer<OrderItem> onRemove) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 10, 10, 10));
        row.setMaximumSize(new Dimension(410, 75));
        row.setPreferredSize(new Dimension(410, 75));

        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(50, 50));
        URL imgURL = getClass().getResource(item.getImagePath());
        if (imgURL != null) {
            Image scaled = new ImageIcon(imgURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaled));
        }
        row.add(imgLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

        double itemTotal = item.getPrice() * item.getQuantity();
        JLabel priceLabel = new JLabel(String.format("฿%.0f x %d = ฿%.0f", item.getPrice(), item.getQuantity(), itemTotal));
        priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        priceLabel.setForeground(Color.GRAY);

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalGlue());

        row.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 10));
        actionPanel.setOpaque(false);

        JButton minusBtn = createCircleButton("-");
        minusBtn.addActionListener(e -> onDecrease.accept(item));

        JLabel qtyLabel = new JLabel(String.valueOf(item.getQuantity()), SwingConstants.CENTER);
        qtyLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        qtyLabel.setPreferredSize(new Dimension(20, 25));

        JButton plusBtn = createCircleButton("+");
        plusBtn.addActionListener(e -> onIncrease.accept(item));

        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        deleteBtn.setForeground(new Color(180, 40, 40));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> onRemove.accept(item));

        actionPanel.add(minusBtn);
        actionPanel.add(qtyLabel);
        actionPanel.add(plusBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        actionPanel.add(deleteBtn);

        row.add(actionPanel, BorderLayout.EAST);

        return row;
    }

    private JButton createCircleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(26, 26));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(230, 225, 220));
        btn.setForeground(Color.BLACK);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}