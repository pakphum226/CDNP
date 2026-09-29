import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {

    private static final int DESIGN_W = 430;
    private static final int DESIGN_H = 860;
    private static final int CONTENT_TOP_OFFSET = 70;
    private static final int CONTENT_BOTTOM_OFFSET = 18;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("CDNP BURGERS");
        setSize(430, 938);
        setMinimumSize(new Dimension(360, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // เพิ่มหน้าต่างๆ
        mainPanel.add(createWelcomeScreen(), "Welcome");
        mainPanel.add(createLoginScreen(), "Login");
        mainPanel.add(createWhiteBoxScreen(), "WhiteBox"); 
        mainPanel.add(createMenuScreen(), "Menu");

        add(mainPanel);

        // แสดงหน้า Welcome
        cardLayout.show(mainPanel, "Welcome");
    }

    // =====================================================
    // WELCOME SCREEN
    // =====================================================
    private JPanel createWelcomeScreen() {
        JPanel welcomePanel = new JPanel(new BorderLayout()) {
            Image bgImage = new ImageIcon("assets/burger_bg.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int w = getWidth();
                int h = getHeight();

                if (bgImage == null || w <= 0 || h <= 0) return;

                Graphics2D g2 = (Graphics2D) g.create();

                double bgScale = Math.min(
                        (double) w / bgImage.getWidth(this),
                        (double) h / bgImage.getHeight(this)
                );

                int imageW = Math.max(1, (int) Math.round(bgImage.getWidth(this) * bgScale));
                int imageH = Math.max(1, (int) Math.round(bgImage.getHeight(this) * bgScale));

                int imageX = (w - imageW) / 2;
                int imageY = (h - imageH) / 2;

                g2.drawImage(bgImage, imageX, imageY, imageW, imageH, this);

                g2.setColor(new Color(0, 0, 0, 45));
                g2.fillRect(0, 0, w, h);

                g2.dispose();
            }
        };

        welcomePanel.setOpaque(true);

        JPanel composition = new JPanel(new BorderLayout());
        composition.setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(Box.createRigidArea(new Dimension(0, CONTENT_TOP_OFFSET)));

        JLabel titleLabel = new JLabel("Welcome to CDNP BURGERS");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Home of Handcrafted Gold-Standard Burger");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitleLabel.setForeground(new Color(220, 220, 220));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton orderBtn = new JButton("Order Now / เข้าสู่ระบบ");
        orderBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        orderBtn.setBackground(new Color(139, 0, 0));
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setFocusPainted(false);
        orderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderBtn.setPreferredSize(new Dimension(350, 50));
        orderBtn.setMinimumSize(new Dimension(280, 50));
        orderBtn.setMaximumSize(new Dimension(350, 50));
        orderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        orderBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 8)));
        content.add(subTitleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        content.add(orderBtn);

        composition.add(content, BorderLayout.SOUTH);
        welcomePanel.add(composition, BorderLayout.CENTER);

        welcomePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = welcomePanel.getWidth();
                int h = welcomePanel.getHeight();
                if (w <= 0 || h <= 0) return;

                double scale = Math.min((double) w / DESIGN_W, (double) h / DESIGN_H);
                scale = Math.min(scale, 1.0);

                int contentW = Math.max(260, Math.min(350, (int) Math.round(350 * scale)));

                orderBtn.setPreferredSize(new Dimension(contentW, Math.max(46, (int) (50 * scale))));
                orderBtn.setMaximumSize(new Dimension(contentW, Math.max(46, (int) (50 * scale))));

                int titleSize = Math.max(18, Math.min(22, (int) Math.round(22 * scale)));
                int subSize = Math.max(11, Math.min(13, (int) Math.round(13 * scale)));
                int buttonSize = Math.max(15, Math.min(18, (int) Math.round(18 * scale)));

                titleLabel.setFont(new Font("SansSerif", Font.BOLD, titleSize));
                subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, subSize));
                orderBtn.setFont(new Font("Tahoma", Font.BOLD, buttonSize));

                int gap1 = Math.max(5, (int) Math.round(8 * scale));
                int gap2 = Math.max(15, (int) Math.round(25 * scale));

                content.removeAll();

                int topOffset = Math.max(20, Math.min(CONTENT_TOP_OFFSET, (int) Math.round(CONTENT_TOP_OFFSET * scale)));
                content.add(Box.createRigidArea(new Dimension(0, topOffset)));
                content.add(titleLabel);
                content.add(Box.createRigidArea(new Dimension(0, gap1)));
                content.add(subTitleLabel);
                content.add(Box.createRigidArea(new Dimension(0, gap2)));
                content.add(orderBtn);
                content.add(Box.createRigidArea(new Dimension(0, Math.max(6, (int) Math.round(CONTENT_BOTTOM_OFFSET * scale)))));

                content.revalidate();
                content.repaint();
                composition.revalidate();
                composition.repaint();
            }
        });

        return welcomePanel;
    }

    // =====================================================
    // LOGIN SCREEN
    // =====================================================
    private JPanel createLoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(245, 238, 224));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 30, 30, 30));

        // ปุ่มย้อนกลับ
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setOpaque(false);
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("Arial", Font.BOLD, 26));
        backBtn.setForeground(new Color(90, 30, 30));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));
        topPanel.add(backBtn);

        // Logo
        ImageIcon logoIcon = new ImageIcon("assets/logo.png");
        Image originalLogo = logoIcon.getImage();
        int originalW = (originalLogo != null) ? originalLogo.getWidth(null) : 0;
        int originalH = (originalLogo != null) ? originalLogo.getHeight(null) : 0;

        JLabel logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (originalLogo == null || getWidth() <= 0 || getHeight() <= 0) return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.drawImage(originalLogo, 0, 0, getWidth(), getHeight(), this);
                g2.dispose();
            }
        };
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (originalW > 0 && originalH > 0) {
            Dimension exactSize = new Dimension(originalW, originalH);
            logoLabel.setPreferredSize(exactSize);
            logoLabel.setMaximumSize(exactSize);
            logoLabel.setMinimumSize(exactSize);
        }

        // Welcome Text
        JLabel welcomeMsg = new JLabel("ยินดีต้อนรับสู่ CDNP Burger");
        welcomeMsg.setFont(new Font("Tahoma", Font.BOLD, 20));
        welcomeMsg.setForeground(new Color(90, 30, 30));
        welcomeMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Phone Field
        final String PLACEHOLDER = "เบอร์โทรศัพท์";
        JTextField phoneField = new JTextField(PLACEHOLDER);
        phoneField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        phoneField.setForeground(Color.GRAY);
        phoneField.setAlignmentX(Component.CENTER_ALIGNMENT);
        phoneField.setPreferredSize(new Dimension(350, 48));
        phoneField.setMaximumSize(new Dimension(600, 48));
        phoneField.setMinimumSize(new Dimension(220, 48));

        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (phoneField.getText().equals(PLACEHOLDER)) return;

                if (string.matches("\\d+") && (fb.getDocument().getLength() + string.length()) <= 10) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;

                if (text.isEmpty() || text.equals(PLACEHOLDER)) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }

                if (phoneField.getText().equals(PLACEHOLDER)) return;

                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + text.length();

                if (text.matches("\\d+") && newLength <= 10) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                if (phoneField.getText().equals(PLACEHOLDER)) return;
                super.remove(fb, offset, length);
            }
        });

        Runnable clearPlaceholder = () -> {
            if (phoneField.getText().equals(PLACEHOLDER)) {
                phoneField.setText("");
                phoneField.setForeground(Color.BLACK);
            }
        };

        phoneField.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) { clearPlaceholder.run(); }
            @Override
            public void mouseClicked(MouseEvent e) { clearPlaceholder.run(); }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {
                if (!phoneField.hasFocus() && phoneField.getText().trim().isEmpty()) {
                    phoneField.setText(PLACEHOLDER);
                    phoneField.setForeground(Color.GRAY);
                }
            }
        });

        phoneField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { clearPlaceholder.run(); }
            @Override
            public void focusLost(FocusEvent e) {
                if (phoneField.getText().trim().isEmpty()) {
                    phoneField.setText(PLACEHOLDER);
                    phoneField.setForeground(Color.GRAY);
                }
            }
        });

        JButton loginBtn = new JButton("ลงชื่อเข้าใช้");
        loginBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        loginBtn.setBackground(new Color(237, 28, 36));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            String phone = phoneField.getText().trim();

            if (phone.equals(PLACEHOLDER) || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "กรุณากรอกเบอร์โทรศัพท์", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            } else if (phone.length() != 10 || !phone.startsWith("0")) {
                JOptionPane.showMessageDialog(this, "ข้อมูลไม่ถูกต้อง (เบอร์โทรศัพท์ต้องมี 10 หลัก และขึ้นต้นด้วย 0)", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "กำลังเข้าสู่ระบบ...", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainPanel, "Menu");
            }
        });

        JLabel orLabel = new JLabel("หรือ");
        orLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        orLabel.setForeground(new Color(90, 30, 30));
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton googleBtn = new JButton("Sign In with Google");
        googleBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        googleBtn.setBackground(Color.WHITE);
        googleBtn.setForeground(new Color(80, 80, 80));
        googleBtn.setFocusPainted(false);
        googleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        googleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        googleBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "กำลังเข้าสู่ระบบ...", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "Menu");
        });

        JLabel noAccountLabel = new JLabel("ไม่มีบัญชีผู้ใช้");
        noAccountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        noAccountLabel.setForeground(new Color(90, 30, 30));
        noAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("สมัครบัญชีผู้ใช้");
        registerBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        registerBtn.setForeground(new Color(237, 28, 36));
        registerBtn.setBackground(new Color(245, 238, 224));
        registerBtn.setFocusPainted(false);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setBorder(BorderFactory.createLineBorder(new Color(237, 28, 36), 2, true));

        registerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "ไปยังหน้าสมัครสมาชิก", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
        });

        content.add(topPanel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(logoLabel);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(welcomeMsg);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        content.add(phoneField);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(loginBtn);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(orLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(googleBtn);
        content.add(Box.createRigidArea(new Dimension(0, 40)));
        content.add(noAccountLabel);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(registerBtn);

        loginPanel.add(content);

        loginPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int available = loginPanel.getWidth() - 60;
                int width = Math.max(220, Math.min(600, available));

                Dimension fieldSize = new Dimension(width, 48);

                phoneField.setPreferredSize(fieldSize);
                phoneField.setMaximumSize(fieldSize);

                loginBtn.setPreferredSize(fieldSize);
                loginBtn.setMaximumSize(fieldSize);

                googleBtn.setPreferredSize(fieldSize);
                googleBtn.setMaximumSize(fieldSize);

                registerBtn.setPreferredSize(fieldSize);
                registerBtn.setMaximumSize(fieldSize);

                content.revalidate();
                content.repaint();
            }
        });

        return loginPanel;
    }

    // =====================================================
    // WHITE BOX SCREEN
    // =====================================================
    private JPanel createWhiteBoxScreen() {
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(220, 220, 220));

        JPanel whiteBox = new JPanel(new BorderLayout()) {
            Image bgImage = new ImageIcon("assets/ipho14promax.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    int w = getWidth();
                    int h = getHeight();
                    double scale = Math.min((double) w / bgImage.getWidth(this), (double) h / bgImage.getHeight(this));
                    int imgW = (int) (bgImage.getWidth(this) * scale);
                    int imgH = (int) (bgImage.getHeight(this) * scale);
                    int x = (w - imgW) / 2;
                    int y = (h - imgH) / 2;

                    g2.drawImage(bgImage, x, y, imgW, imgH, this);
                    g2.dispose();
                }
            }
        };

        whiteBox.setOpaque(false);
        Dimension boxSize = new Dimension(430, 932);
        whiteBox.setPreferredSize(boxSize);
        whiteBox.setMinimumSize(boxSize);
        whiteBox.setMaximumSize(boxSize);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);
        JButton backBtn = new JButton("← กลับไปหน้า Login");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
        topBar.add(backBtn);

        whiteBox.add(topBar, BorderLayout.NORTH);

        containerPanel.add(whiteBox);
        return containerPanel;
    }

    // =====================================================
    // MENU SCREEN
    // =====================================================
    private JPanel createMenuScreen() {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(Color.WHITE);

        JPanel menuContainer = new JPanel(new BorderLayout());
        Dimension containerSize = new Dimension(430, 860);
        menuContainer.setPreferredSize(containerSize);
        menuContainer.setMinimumSize(containerSize);
        menuContainer.setMaximumSize(containerSize);
        menuContainer.setOpaque(false);

        // HEADER PANEL
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(12, 16, 5, 16));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        ImageIcon logoIcon = new ImageIcon("assets/CDNP BURGER.png");
        JLabel logoLabel = createCenteredImageLabel(logoIcon, 220, 40);

        JLabel userIconHeader = new JLabel("👤", SwingConstants.CENTER);
        userIconHeader.setFont(new Font("SansSerif", Font.PLAIN, 24));
        userIconHeader.setOpaque(true);
        userIconHeader.setBackground(new Color(225, 225, 225));
        userIconHeader.setForeground(Color.WHITE);
        userIconHeader.setPreferredSize(new Dimension(40, 40));

        topRow.add(logoLabel, BorderLayout.WEST);
        topRow.add(userIconHeader, BorderLayout.EAST);

        JLabel titleText = new JLabel("Order your favourite food!");
        titleText.setFont(new Font("Serif", Font.PLAIN, 20));
        titleText.setForeground(new Color(30, 30, 30));
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ปุ่ม MENU
        JPanel menuBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        menuBtnPanel.setOpaque(false);
        JButton menuPillBtn = new JButton("MENU") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(105, 90, 90));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        menuPillBtn.setFont(new Font("Serif", Font.BOLD, 22));
        menuPillBtn.setForeground(Color.WHITE);
        menuPillBtn.setFocusPainted(false);
        menuPillBtn.setContentAreaFilled(false);
        menuPillBtn.setBorderPainted(false);
        menuPillBtn.setPreferredSize(new Dimension(170, 46));
        menuBtnPanel.add(menuPillBtn);

        headerPanel.add(topRow);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(titleText);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(menuBtnPanel);

        // GRID ITEMS PANEL (ระยะห่างช่องปรับเป็น 8px เพื่อรองรับรูปเบอร์เกอร์ขนาดใหญ่ขึ้น)
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 8, 12));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] imagePaths = {
                "assets/burger1.png", "assets/burger2.png", "assets/burger3.png",
                "assets/burger4.png", "assets/burger5.png", "assets/burger6.png",
                "assets/burger7.png", "assets/burger8.png", "assets/burger9.png"
        };

        for (String path : imagePaths) {
            gridPanel.add(createFoodCard(path));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        // BOTTOM NAVIGATION BAR
        JLayeredPane bottomNavContainer = new JLayeredPane();
        bottomNavContainer.setPreferredSize(new Dimension(430, 105));

        JPanel bottomBar = new JPanel(new GridLayout(1, 4, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(75, 65, 65));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bottomBar.setOpaque(false);
        bottomBar.setBounds(0, 25, 430, 80);

        JComponent homeBtn = createHDNavIcon("assets/Home.png", 0);
        JComponent userBtn = createHDNavIcon("assets/User.png", 1);
        JLabel spaceHolder = new JLabel(); 
        JComponent burgerBtn = createHDNavIcon("assets/Burger00.png", 2);

        JPanel userWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        userWrapper.setOpaque(false);
        userWrapper.add(userBtn);

        bottomBar.add(homeBtn);
        bottomBar.add(userWrapper); 
        bottomBar.add(spaceHolder);
        bottomBar.add(burgerBtn);

        JButton bigPlusBtn = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(130, 115, 115));
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bigPlusBtn.setFont(new Font("SansSerif", Font.BOLD, 52));
        bigPlusBtn.setForeground(Color.WHITE);
        bigPlusBtn.setFocusPainted(false);
        bigPlusBtn.setContentAreaFilled(false);
        bigPlusBtn.setBorderPainted(false);
        bigPlusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bigPlusBtn.setBounds(168, 0, 94, 94);

        bigPlusBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "คลิกปุ่มบวกใหญ่ (+)", "แจ้งเตือน", JOptionPane.INFORMATION_MESSAGE);
        });

        bottomNavContainer.add(bottomBar, Integer.valueOf(1));
        bottomNavContainer.add(bigPlusBtn, Integer.valueOf(2));

        menuContainer.add(headerPanel, BorderLayout.NORTH);
        menuContainer.add(scrollPane, BorderLayout.CENTER);
        menuContainer.add(bottomNavContainer, BorderLayout.SOUTH);

        outerPanel.add(menuContainer);
        return outerPanel;
    }

    // =====================================================
    // ฟังก์ชันสร้างการ์ดเบอร์เกอร์ ปรับขนาดเป็น 132 x 122 พิกเซล
    // =====================================================
    private JPanel createFoodCard(String imgPath) {
        JPanel cardContainer = new JPanel(new BorderLayout());
        cardContainer.setOpaque(false);
        
        // กำหนดขนาดการ์ดสินค้าตรงตามที่ระบุ: กว้าง 132 x สูง 122
        cardContainer.setPreferredSize(new Dimension(132, 122));

        ImageIcon icon = new ImageIcon(imgPath);
        JLabel imgLabel = new JLabel() {
            Image img = icon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null && img.getWidth(this) > 0 && img.getHeight(this) > 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    int labelW = getWidth();
                    int labelH = getHeight();

                    double scale = Math.min((double) labelW / img.getWidth(this), (double) labelH / img.getHeight(this));
                    int drawW = (int) (img.getWidth(this) * scale);
                    int drawH = (int) (img.getHeight(this) * scale);

                    int x = (labelW - drawW) / 2;
                    int y = (labelH - drawH) / 2;

                    g2.drawImage(img, x, y, drawW, drawH, this);
                    g2.dispose();
                }
            }
        };
        imgLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cardContainer.add(imgLabel, BorderLayout.CENTER);

        return cardContainer;
    }

    private JComponent createHDNavIcon(String imagePath, int iconType) {
        ImageIcon icon = new ImageIcon(imagePath);
        int targetSize = (iconType == 2) ? 62 : 52;
        
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            label.setIcon(getScaledIcon(icon, targetSize, targetSize));
            return label;
        }

        JPanel customIconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                g2.setColor(Color.WHITE);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;

                if (iconType == 0) { // Home
                    Path2D roof = new Path2D.Double();
                    roof.moveTo(cx - 24, cy + 2);
                    roof.lineTo(cx, cy - 22);
                    roof.lineTo(cx + 24, cy + 2);
                    roof.lineTo(cx + 18, cy + 2);
                    roof.lineTo(cx + 18, cy + 22);
                    roof.lineTo(cx - 18, cy + 22);
                    roof.lineTo(cx - 18, cy + 2);
                    roof.closePath();
                    g2.fill(roof);
                } else if (iconType == 1) { // User
                    g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawOval(cx - 13, cy - 22, 26, 26);
                    Path2D body = new Path2D.Double();
                    body.moveTo(cx - 22, cy + 20);
                    body.curveTo(cx - 22, cy + 5, cx + 22, cy + 5, cx + 22, cy + 20);
                    g2.draw(body);
                } else if (iconType == 2) { // Burger
                    g2.setStroke(new BasicStroke(3.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.fill(new Arc2D.Double(cx - 28, cy - 22, 56, 32, 0, 180, Arc2D.CHORD));
                    g2.setColor(new Color(75, 65, 65));
                    g2.fillOval(cx - 12, cy - 16, 4, 4);
                    g2.fillOval(cx, cy - 18, 4, 4);
                    g2.fillOval(cx + 12, cy - 15, 4, 4);
                    g2.setColor(Color.WHITE);
                    g2.draw(new RoundRectangle2D.Double(cx - 29, cy - 4, 58, 7, 4, 4));
                    g2.draw(new RoundRectangle2D.Double(cx - 26, cy + 6, 52, 6, 4, 4));
                    g2.fill(new RoundRectangle2D.Double(cx - 28, cy + 15, 56, 10, 6, 6));
                }
                g2.dispose();
            }
        };
        customIconPanel.setOpaque(false);
        customIconPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customIconPanel.setPreferredSize(new Dimension(targetSize, targetSize));
        return customIconPanel;
    }

    private ImageIcon getScaledIcon(ImageIcon srcIcon, int w, int h) {
        Image img = srcIcon.getImage();
        Image resized = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    private JLabel createCenteredImageLabel(ImageIcon icon, int targetW, int targetH) {
        JLabel label = new JLabel() {
            Image img = icon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null && img.getWidth(this) > 0 && img.getHeight(this) > 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    
                    double scale = Math.min((double) getWidth() / img.getWidth(this), (double) getHeight() / img.getHeight(this));
                    int drawW = (int) (img.getWidth(this) * scale);
                    int drawH = (int) (img.getHeight(this) * scale);
                    int x = (getWidth() - drawW) / 2;
                    int y = (getHeight() - drawH) / 2;

                    g2.drawImage(img, x, y, drawW, drawH, this);
                    g2.dispose();
                }
            }
        };
        label.setPreferredSize(new Dimension(targetW, targetH));
        return label;
    }

    public static void main(String[] args) {
        UIManager.put("OptionPane.okButtonText", "ตกลง");
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.BOLD, 12));

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}       