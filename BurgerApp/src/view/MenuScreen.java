package view;

import model.OrderItem;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.function.Consumer;

public class MenuScreen extends JPanel {

    private JLabel cartCountLabel;
    private JPanel gridPanel;
    private final Consumer<OrderItem> onAddToCart;

    private final Color COLOR_DARK_BROWN = new Color(105, 90, 90);
    private final Color COLOR_TAB_INACTIVE = new Color(210, 200, 195);

    private JButton burgerTab, riceTab, setTab;

    public MenuScreen(Runnable onBackToWelcome, Runnable onOpenCart, Consumer<OrderItem> onAddToCart) {
        this.onAddToCart = onAddToCart;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // คอนเทนเนอร์หลักตรงกลาง
        JPanel menuContainer = new JPanel(new BorderLayout());
        menuContainer.setBackground(Color.WHITE);

        // --- 1. Header (ปุ่มกลับ, โลโก้, ตะกร้า) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 15, 8, 15));

        JButton backBtn = new JButton();
        ImageIcon returnIcon = getResizedIcon("/assets/Return.jpg", 45, 45);
        if (returnIcon != null) {
            backBtn.setIcon(returnIcon);
        } else {
            backBtn.setText("←");
            backBtn.setFont(new Font("Arial", Font.BOLD, 28));
        }
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> onBackToWelcome.run());

        JLabel logoLabel = createIconLabel("/assets/CDNP BURGER.png", 240, 45);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightHeader.setOpaque(false);

        JButton cartBtn = new JButton("🛒");
        cartBtn.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cartBtn.setFocusPainted(false);
        cartBtn.setBorderPainted(false);
        cartBtn.setContentAreaFilled(false);
        cartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartBtn.addActionListener(e -> onOpenCart.run());

        cartCountLabel = new JLabel("0", SwingConstants.CENTER);
        cartCountLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBackground(new Color(190, 0, 0));
        cartCountLabel.setPreferredSize(new Dimension(24, 24));

        rightHeader.add(cartBtn);
        rightHeader.add(cartCountLabel);

        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        headerPanel.add(rightHeader, BorderLayout.EAST);

        // --- 2. Title Area + MENU Badge + Category Tabs ---
        JPanel titleArea = new JPanel();
        titleArea.setOpaque(false);
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));

        JLabel titleText = new JLabel("Order your favourite food!");
        titleText.setFont(new Font("Serif", Font.PLAIN, 22));
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel menuPillBtn = new JLabel("MENU", SwingConstants.CENTER);
        menuPillBtn.setFont(new Font("Serif", Font.BOLD, 22));
        menuPillBtn.setForeground(Color.WHITE);
        menuPillBtn.setOpaque(true);
        menuPillBtn.setBackground(COLOR_DARK_BROWN);
        menuPillBtn.setPreferredSize(new Dimension(180, 42));
        menuPillBtn.setMaximumSize(new Dimension(180, 42));
        menuPillBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        categoryPanel.setOpaque(false);

        burgerTab = createCategoryButton("Burger", true);
        riceTab = createCategoryButton("Rice", false);
        setTab = createCategoryButton("SET", false);

        burgerTab.addActionListener(e -> selectTab(burgerTab, "Burger"));
        riceTab.addActionListener(e -> selectTab(riceTab, "Rice"));
        setTab.addActionListener(e -> selectTab(setTab, "SET"));

        categoryPanel.add(burgerTab);
        categoryPanel.add(riceTab);
        categoryPanel.add(setTab);

        titleArea.add(titleText);
        titleArea.add(Box.createRigidArea(new Dimension(0, 8)));
        titleArea.add(menuPillBtn);
        titleArea.add(Box.createRigidArea(new Dimension(0, 10)));
        titleArea.add(categoryPanel);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false);
        topWrapper.add(headerPanel, BorderLayout.NORTH);
        topWrapper.add(titleArea, BorderLayout.CENTER);

        // --- 3. Product Grid ---
        gridPanel = new JPanel(new GridLayout(0, 3, 12, 12));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(12, 15, 15, 15));

        loadProducts("Burger");

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        menuContainer.add(topWrapper, BorderLayout.NORTH);
        menuContainer.add(scrollPane, BorderLayout.CENTER);

        add(menuContainer, BorderLayout.CENTER);
    }

    public void updateCartBadge(int totalCount) {
        if (cartCountLabel != null) {
            cartCountLabel.setText(String.valueOf(totalCount));
        }
    }

    private void selectTab(JButton activeBtn, String category) {
        burgerTab.setBackground(COLOR_TAB_INACTIVE);
        burgerTab.setForeground(new Color(60, 60, 60));
        riceTab.setBackground(COLOR_TAB_INACTIVE);
        riceTab.setForeground(new Color(60, 60, 60));
        setTab.setBackground(COLOR_TAB_INACTIVE);
        setTab.setForeground(new Color(60, 60, 60));

        activeBtn.setBackground(COLOR_DARK_BROWN);
        activeBtn.setForeground(Color.WHITE);

        loadProducts(category);
    }

    private void loadProducts(String category) {
        gridPanel.removeAll();

        Object[][] products;
        if ("SET".equals(category)) {
            products = new Object[][]{
                {"Chubby Kid Set", "/assets/Chubby Kid Set.png", 120.0},
                {"Red and Black", "/assets/Red and Black.png", 110.0},
                {"Super Chicken Set", "/assets/Super Chicken Set.png", 99.0},
                {"Titan Super Burger", "/assets/Titan Super Burger.png", 150.0},
                {"Triple Hamburger", "/assets/Triple Hamburger.png", 130.0},
                {"Great Value Set", "/assets/Great Value Set.png", 89.0},
                {"Black set", "/assets/Black set.png", 95.0},
                {"Eat-to-Die Set", "/assets/Eat-to-Die Set.png", 180.0}
            };
        } else if ("Rice".equals(category)) {
            products = new Object[][]{
                {"Super Chicken Rice", "/assets/Super Chicken Rice.png", 65.0},
                {"Crispy Chicken Basil", "/assets/Crispy Chicken Basil.png", 60.0},
                {"Spicy Chicken Salad", "/assets/Spicy Chicken Salad.png", 65.0},
                {"Grilled Pork Spicy", "/assets/Grilled Pork Spicy.png", 70.0},
                {"Braised Pork Belly", "/assets/Braised Pork Belly.png", 75.0},
                {"Tonkatsu Rice", "/assets/Tonkatsu Rice.png", 65.0},
                {"Rare grilled beef", "/assets/Rare grilled bee.png", 90.0},
                {"Kuro Grilled Beef", "/assets/Kuro Grilled Beef.png", 85.0},
                {"Hamburg Steak Egg", "/assets/Hamburg Steak Egg.png", 80.0}
            };
        } else {
            products = new Object[][]{
                {"Chicken Burger", "/assets/Chicken Burger.png", 50.0},
                {"Fried Chicken", "/assets/Fried Chicken.png", 50.0},
                {"Egg and cheese", "/assets/Egg and cheese.png", 50.0},
                {"Beef + Shrimp", "/assets/Beef + Shrimp.png", 60.0},
                {"Super Fish", "/assets/Super Fish.png", 55.0},
                {"Black Sauce Burger", "/assets/Black Sauce Burger.png", 50.0},
                {"Spicy burger cries", "/assets/Spicy burger cries.png", 50.0},
                {"Monster Triple", "/assets/Monster Triple.png", 100.0},
                {"Veggie Burger", "/assets/Veggie Burger.png", 65.0}
            };
        }

        for (Object[] prod : products) {
            String name = (String) prod[0];
            String path = (String) prod[1];
            double price = (double) prod[2];
            gridPanel.add(createFoodCard(name, path, price));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // สร้างการ์ดอาหารให้รูปยืดขยายตามขนาดของการ์ดอัตโนมัติ
    private JPanel createFoodCard(String name, String resourcePath, double price) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(225, 220, 215)));

        Image rawImg = loadImage(resourcePath);

        // วาดรูปภาพให้อัตราส่วนสวยงามและขยายพอดีตามขนาดพื้นที่ของการ์ด
        JLabel imgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (rawImg != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    int containerW = getWidth();
                    int containerH = getHeight();
                    int imgW = rawImg.getWidth(this);
                    int imgH = rawImg.getHeight(this);

                    if (imgW > 0 && imgH > 0) {
                        double scale = Math.min((double) containerW / imgW, (double) containerH / imgH);
                        int drawW = (int) (imgW * scale);
                        int drawH = (int) (imgH * scale);
                        int x = (containerW - drawW) / 2;
                        int y = (containerH - drawH) / 2;

                        g2.drawImage(rawImg, x, y, drawW, drawH, this);
                    }
                    g2.dispose();
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(120, 120); // กำหนดความสูงขั้นต่ำของโซนรูปภาพ
            }
        };

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 2, 4));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(4, 6, 6, 6));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 13));

        JLabel priceLabel = new JLabel(String.format("฿%.0f", price), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        priceLabel.setForeground(new Color(180, 40, 40));

        JButton addBtn = new JButton("+ ตะกร้า");
        addBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(COLOR_DARK_BROWN);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> onAddToCart.accept(new OrderItem(name, resourcePath, price, 1)));

        bottomPanel.add(nameLabel);
        bottomPanel.add(priceLabel);
        bottomPanel.add(addBtn);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private JButton createCategoryButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 32));

        if (active) {
            btn.setBackground(COLOR_DARK_BROWN);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(COLOR_TAB_INACTIVE);
            btn.setForeground(new Color(60, 60, 60));
        }
        return btn;
    }

    private JLabel createIconLabel(String path, int w, int h) {
        JLabel label = new JLabel();
        ImageIcon icon = getResizedIcon(path, w, h);
        if (icon != null) {
            label.setIcon(icon);
        }
        return label;
    }

    private Image loadImage(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        }
        return null;
    }

    private ImageIcon getResizedIcon(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }
}