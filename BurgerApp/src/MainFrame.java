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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private List<OrderItem> cartItems = new ArrayList<>();

    private MenuScreen menuScreen;
    private CartScreen cartScreen;
    private EmployeeScreen employeeScreen;

    public MainFrame() {
        // ตั้งค่าหน้าต่างหลัก
        setTitle("CDNP BURGERS System");
        setSize(1000, 700);
        setMinimumSize(new Dimension(430, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ==========================================
        // 1. หน้า Welcome & WhiteBox
        // ==========================================
        WelcomeScreen welcomeScreen = new WelcomeScreen(new Runnable() {
            @Override
            public void run() {
                cardLayout.show(mainPanel, "Menu");
            }
        });

        WhiteBoxScreen whiteBoxScreen = new WhiteBoxScreen(new Runnable() {
            @Override
            public void run() {
                cardLayout.show(mainPanel, "Menu");
            }
        });

        // ==========================================
        // 2. หน้า MenuScreen
        // ==========================================
        menuScreen = new MenuScreen(
            new Runnable() { // ปุ่มย้อนกลับ
                @Override
                public void run() {
                    cardLayout.show(mainPanel, "Welcome");
                }
            },
            new Runnable() { // เปิดตะกร้า
                @Override
                public void run() {
                    refreshCartUI();
                    cardLayout.show(mainPanel, "Cart");
                }
            },
            new Consumer<OrderItem>() { // เพิ่มสินค้า
                @Override
                public void accept(OrderItem item) {
                    addToCart(item);
                }
            }
        );

        // ==========================================
        // 3. หน้า CartScreen
        // ==========================================
        cartScreen = new CartScreen(
            new Runnable() { // ย้อนกลับไปหน้า Menu
                @Override
                public void run() {
                    cardLayout.show(mainPanel, "Menu");
                }
            },
            new Consumer<OrderItem>() { // เพิ่มจำนวน
                @Override
                public void accept(OrderItem item) {
                    increaseItem(item);
                }
            },
            new Consumer<OrderItem>() { // ลดจำนวน
                @Override
                public void accept(OrderItem item) {
                    decreaseItem(item);
                }
            },
            new Consumer<OrderItem>() { // ลบสินค้า
                @Override
                public void accept(OrderItem item) {
                    removeItem(item);
                }
            },
            new Runnable() { // ชำระเงิน
                @Override
                public void run() {
                    processCheckout();
                }
            }
        );

        // ==========================================
        // 4. หน้า EmployeeScreen
        // ==========================================
        employeeScreen = new EmployeeScreen(new Runnable() {
            @Override
            public void run() {
                cardLayout.show(mainPanel, "Menu");
            }
        });

        // ใส่หน้าต่างทั้งหมดลงใน mainPanel
        mainPanel.add(welcomeScreen, "Welcome");
        mainPanel.add(whiteBoxScreen, "WhiteBox");
        mainPanel.add(menuScreen, "Menu");
        mainPanel.add(cartScreen, "Cart");
        mainPanel.add(employeeScreen, "Employee");

        // ==========================================
        // 5. เมนูด้านบน (MenuBar)
        // ==========================================
        Font menuFont = new Font("Tahoma", Font.PLAIN, 14);
        JMenuBar menuBar = new JMenuBar();
        JMenu systemMenu = new JMenu("สลับบัญชีใช้งาน");
        systemMenu.setFont(menuFont);

        JMenuItem userItem = new JMenuItem("บัญชีผู้ใช้", getResizedIcon("/assets/User.jpg", 20, 20));
        userItem.setFont(menuFont);
        userItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Welcome");
            }
        });

        JMenuItem employeeItem = new JMenuItem("บัญชีพนักงาน", getResizedIcon("/assets/Employee.png", 20, 20));
        employeeItem.setFont(menuFont);
        employeeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employeeScreen.refreshOrders();
                cardLayout.show(mainPanel, "Employee");
            }
        });

        systemMenu.add(userItem);
        systemMenu.add(employeeItem);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome");
    }

    // ==========================================
    // ฟังก์ชันย่อยการทำงาน
    // ==========================================

    private ImageIcon getResizedIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    private void processCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ไม่มีสินค้าในตะกร้า!", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // คำนวณราคารวมด้วย Loop แบบธรรมดา
        double totalPrice = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            totalPrice += cartItems.get(i).getTotalPrice();
        }

        JPanel dialogContent = new JPanel();
        dialogContent.setLayout(new BoxLayout(dialogContent, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("คุณต้องการยืนยันการสั่งซื้อหรือไม่?");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        dialogContent.add(headerLabel);
        dialogContent.add(Box.createRigidArea(new Dimension(0, 10)));

        StringBuilder itemsText = new StringBuilder();
        itemsText.append("--------------------------------------------------\n");

        for (int i = 0; i < cartItems.size(); i++) {
            OrderItem item = cartItems.get(i);
            itemsText.append("• " + item.getName() + "\n");
            itemsText.append("  จำนวน: " + item.getQuantity() + " ชิ้น ");
            itemsText.append("(฿" + (int)item.getPrice() + " x " + item.getQuantity() + " = ฿" + (int)item.getTotalPrice() + ")\n");
        }
        itemsText.append("--------------------------------------------------\n");

        JTextArea textArea = new JTextArea(itemsText.toString());
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(380, 280));
        scrollPane.setBorder(null);

        dialogContent.add(scrollPane);
        dialogContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel totalLabel = new JLabel("ราคารวมทั้งสิ้น: ฿" + (int)totalPrice);
        totalLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        totalLabel.setForeground(new Color(180, 0, 0));
        dialogContent.add(totalLabel);

        ImageIcon confirmIcon = getResizedIcon("/assets/Slip_2..png", 60, 60);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                dialogContent,
                "ยืนยันคำสั่งซื้อ",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                confirmIcon
        );

        if (confirm == JOptionPane.OK_OPTION) {
            Order newOrder = new Order(new ArrayList<>(cartItems), totalPrice, "ชำระเงินสำเร็จ");
            OrderManager.addOrder(newOrder);

            PaymentScreen paymentScreen = new PaymentScreen(newOrder, new Runnable() {
                @Override
                public void run() {
                    cartItems.clear();
                    updateCartBadge();
                    cardLayout.show(mainPanel, "Welcome");
                }
            });

            mainPanel.add(paymentScreen, "Payment");
            cardLayout.show(mainPanel, "Payment");
        }
    }

    private void addToCart(OrderItem newItem) {
        boolean found = false;
        for (int i = 0; i < cartItems.size(); i++) {
            OrderItem item = cartItems.get(i);
            if (item.getName().equals(newItem.getName())) {
                item.setQuantity(item.getQuantity() + 1);
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

    private void increaseItem(OrderItem item) {
        item.setQuantity(item.getQuantity() + 1);
        updateCartBadge();
        refreshCartUI();
    }

    private void decreaseItem(OrderItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            cartItems.remove(item);
        }
        updateCartBadge();
        refreshCartUI();
    }

    private void removeItem(OrderItem item) {
        cartItems.remove(item);
        updateCartBadge();
        refreshCartUI();
    }

    private void updateCartBadge() {
        int total = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            total += cartItems.get(i).getQuantity();
        }
        menuScreen.updateCartBadge(total);
    }

    private void refreshCartUI() {
        if (cartScreen != null) {
            cartScreen.renderCartList(
                cartItems,
                new Consumer<OrderItem>() {
                    @Override
                    public void accept(OrderItem item) {
                        increaseItem(item);
                    }
                },
                new Consumer<OrderItem>() {
                    @Override
                    public void accept(OrderItem item) {
                        decreaseItem(item);
                    }
                },
                new Consumer<OrderItem>() {
                    @Override
                    public void accept(OrderItem item) {
                        removeItem(item);
                    }
                }
            );
        }
    }
}