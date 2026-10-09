import view.WelcomeScreen;
import view.WhiteBoxScreen;
import view.MenuScreen;
import view.CartScreen;
import view.EmployeeScreen;
import view.PaymentScreen;

import model.Order;
import model.OrderItem;
import model.OrderManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final List<OrderItem> cartItems = new ArrayList<>();

    private MenuScreen menuScreen;
    private CartScreen cartScreen;
    private EmployeeScreen employeeScreen;

    public MainFrame() {

        setTitle("CDNP BURGERS System");
        setSize(1000, 700);
        setMinimumSize(new Dimension(430, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // =========================
        // Welcome
        // =========================
        WelcomeScreen welcomeScreen =
                new WelcomeScreen(() ->
                        cardLayout.show(mainPanel, "Menu")
                );

        // =========================
        // WhiteBox
        // =========================
        WhiteBoxScreen whiteBoxScreen =
                new WhiteBoxScreen(() ->
                        cardLayout.show(mainPanel, "Menu")
                );

        // =========================
        // Menu
        // =========================
        menuScreen = new MenuScreen(

                // ปุ่มย้อนกลับ
                () -> cardLayout.show(mainPanel, "Welcome"),

                // เปิดตะกร้า
                () -> {
                    refreshCartUI();
                    cardLayout.show(mainPanel, "Cart");
                },

                // เพิ่มสินค้า
                this::addToCart
        );

        // Cart
        cartScreen = new CartScreen(

                // กลับไป Menu
                () -> cardLayout.show(mainPanel, "Menu"),

                // เพิ่มจำนวน
                this::increaseItem,

                // ลดจำนวน
                this::decreaseItem,

                // ลบสินค้า
                this::removeItem,

                // ชำระเงิน
                this::processCheckout
        );

        // =========================
        // Employee
        // =========================
        employeeScreen =
                new EmployeeScreen(() ->
                        cardLayout.show(mainPanel, "Menu")
                );

        // =========================
        // เพิ่มหน้าต่างทั้งหมด
        // =========================
        mainPanel.add(welcomeScreen, "Welcome");
        mainPanel.add(whiteBoxScreen, "WhiteBox");
        mainPanel.add(menuScreen, "Menu");
        mainPanel.add(cartScreen, "Cart");
        mainPanel.add(employeeScreen, "Employee");

        // =========================
        // Menu Bar
        // =========================
        Font menuFont =
                new Font("Tahoma", Font.PLAIN, 14);

        JMenuBar menuBar = new JMenuBar();

        JMenu systemMenu =
                new JMenu("สลับบัญชีใช้งาน");

        systemMenu.setFont(menuFont);

        // -------------------------
        // บัญชีผู้ใช้
        // -------------------------
        JMenuItem userItem =
                new JMenuItem(
                        "บัญชีผู้ใช้",
                        getResizedIcon(
                                "/assets/User.jpg",
                                20,
                                20
                        )
                );

        userItem.setFont(menuFont);

        userItem.addActionListener(e ->
                cardLayout.show(
                        mainPanel,
                        "Welcome"
                )
        );

        // -------------------------
        // บัญชีพนักงาน
        // -------------------------
        JMenuItem employeeItem =
                new JMenuItem(
                        "บัญชีพนักงาน",
                        getResizedIcon(
                                "/assets/Employee.png",
                                20,
                                20
                        )
                );

        employeeItem.setFont(menuFont);

        employeeItem.addActionListener(e -> {

            employeeScreen.refreshOrders();

            cardLayout.show(
                    mainPanel,
                    "Employee"
            );
        });

        systemMenu.add(userItem);
        systemMenu.add(employeeItem);

        menuBar.add(systemMenu);

        setJMenuBar(menuBar);

        // =========================
        // แสดงหน้าเริ่มต้น
        // =========================
        add(mainPanel);

        cardLayout.show(
                mainPanel,
                "Welcome"
        );
    }

    // =====================================================
    // Resize Icon
    // =====================================================

    private ImageIcon getResizedIcon(
            String path,
            int width,
            int height
    ) {

        URL imgURL =
                getClass().getResource(path);

        if (imgURL != null) {

            ImageIcon icon =
                    new ImageIcon(imgURL);

            Image img =
                    icon.getImage()
                            .getScaledInstance(
                                    width,
                                    height,
                                    Image.SCALE_SMOOTH
                            );

            return new ImageIcon(img);
        }

        return null;
    }

    // =====================================================
    // CHECKOUT
    // =====================================================

    private void processCheckout() {

        // -------------------------
        // ไม่มีสินค้า
        // -------------------------
        if (cartItems.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "ไม่มีสินค้าในตะกร้า!",
                    "แจ้งเตือน",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // -------------------------
        // คำนวณราคารวม
        // -------------------------
        double totalPrice =
                cartItems.stream()
                        .mapToDouble(
                                OrderItem::getTotalPrice
                        )
                        .sum();

        // -------------------------
        // สร้าง UI กล่องข้อความสำหรับ Pop-up ยืนยัน (ใส่ Scrollbar)
        // -------------------------
        JPanel dialogContent = new JPanel();
        dialogContent.setLayout(new BoxLayout(dialogContent, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("คุณต้องการยืนยันการสั่งซื้อหรือไม่?");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogContent.add(headerLabel);
        dialogContent.add(Box.createRigidArea(new Dimension(0, 10)));

        // สร้างรายละเอียดรายการสินค้า
        StringBuilder itemsText = new StringBuilder();
        itemsText.append("--------------------------------------------------\n");

        for (OrderItem item : cartItems) {
            itemsText.append(
                    String.format(
                            "• %s\n" +
                            "  จำนวน: %d ชิ้น  (฿%.0f x %d = ฿%.0f)\n",
                            item.getName(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getQuantity(),
                            item.getTotalPrice()
                    )
            );
        }

        itemsText.append("--------------------------------------------------\n");

        JTextArea textArea = new JTextArea(itemsText.toString());
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setOpaque(false);

        // ครอบด้วย JScrollPane และจำกัดความสูงไว้ที่ 280px เพื่อไม่ให้ป๊อปอัปยืดล้นขอบจอ
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(380, 280));
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.setBorder(null);

        dialogContent.add(scrollPane);
        dialogContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel totalLabel = new JLabel(String.format("ราคารวมทั้งสิ้น: ฿%.0f", totalPrice));
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        totalLabel.setForeground(new Color(180, 0, 0));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogContent.add(totalLabel);

        // =================================================
        // หน้าต่างยืนยันคำสั่งซื้อ
        // =================================================

        ImageIcon confirmIcon = 
                getResizedIcon(
                        "/assets/Slip_2..png",
                        60,
                        60
                );

        int confirm;

        if (confirmIcon != null) {

            confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            dialogContent,
                            "ยืนยันคำสั่งซื้อ",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            confirmIcon
                    );

        } else {

            // ถ้าหารูปไม่เจอ ให้ใช้แบบปกติ
            confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            dialogContent,
                            "ยืนยันคำสั่งซื้อ",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
        }

        // =================================================
        // กด OK
        // =================================================

        if (confirm == JOptionPane.OK_OPTION) {

            // -------------------------
            // 1. สร้าง Order ใหม่
            // -------------------------
            Order newOrder =
                    new Order(
                            new ArrayList<>(cartItems),
                            totalPrice,
                            "ชำระเงินสำเร็จ"
                    );

            // 2. บันทึกลง OrderManager
            OrderManager.addOrder(newOrder);            

            // -------------------------
            // 3. เปิดหน้า Payment (ส่งวัตถุ newOrder เข้าไป)
            // -------------------------
            PaymentScreen paymentScreen =
                    new PaymentScreen(
                            newOrder, // <-- ส่งวัตถุ newOrder เข้าไป
                            () -> {

                                // ล้างตะกร้า
                                cartItems.clear();

                                // อัปเดตจำนวนตะกร้า
                                updateCartBadge();

                                // กลับหน้า Welcome
                                cardLayout.show(
                                        mainPanel,
                                        "Welcome"
                                );
                            }
                    );

            mainPanel.add(
                    paymentScreen,
                    "Payment"
            );

            cardLayout.show(
                    mainPanel,
                    "Payment"
            );
        }
    }

    // =====================================================
    // ADD TO CART
    // =====================================================

    private void addToCart(OrderItem newItem) {

        boolean found = false;

        for (OrderItem item : cartItems) {

            if (item.getName()
                    .equals(newItem.getName())) {

                item.setQuantity(
                        item.getQuantity() + 1
                );

                found = true;
                break;
            }
        }

        if (!found) {
            cartItems.add(newItem);
        }

        updateCartBadge();
        refreshCartUI();
    }

    // เพิ่มจำนวนสินค้า
    private void increaseItem(OrderItem item) {

        item.setQuantity(
                item.getQuantity() + 1
        );

        updateCartBadge();
        refreshCartUI();
    }

    // ลดจำนวนสินค้า
    private void decreaseItem(OrderItem item) {

        if (item.getQuantity() > 1) {

            item.setQuantity(
                    item.getQuantity() - 1
            );

        } else {

            cartItems.remove(item);
        }

        updateCartBadge();
        refreshCartUI();
    }

    // ลบสินค้า
    private void removeItem(OrderItem item) {

        cartItems.remove(item);

        updateCartBadge();
        refreshCartUI();
    }

    // อัปเดตตัวเลขบนตะกร้า
    private void updateCartBadge() {

        int total =
                cartItems.stream()
                        .mapToInt(
                                OrderItem::getQuantity
                        )
                        .sum();

        menuScreen.updateCartBadge(total);
    }

    // Refresh Cart
    private void refreshCartUI() {

        if (cartScreen != null) {

            cartScreen.renderCartList(
                    cartItems,
                    this::increaseItem,
                    this::decreaseItem,
                    this::removeItem
            );
        }
    }
}