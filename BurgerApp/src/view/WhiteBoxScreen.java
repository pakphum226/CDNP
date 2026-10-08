package view;
import javax.swing.*;
import java.awt.*;

public class WhiteBoxScreen extends JPanel {

    public WhiteBoxScreen(Runnable onBackToMenu) {
        setLayout(new GridBagLayout());
        setBackground(new Color(220, 220, 220));

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
        JButton backBtn = new JButton("← กลับหน้า Menu");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        backBtn.addActionListener(e -> onBackToMenu.run());
        topBar.add(backBtn);

        whiteBox.add(topBar, BorderLayout.NORTH);
        add(whiteBox);
    }
}
