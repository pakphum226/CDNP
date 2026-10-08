package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class WelcomeScreen extends JPanel {

    private Image bgImage;

    public WelcomeScreen(Runnable onStart) {
        setLayout(new GridBagLayout());
        setBackground(new Color(20, 15, 10)); // สีพื้นหลังโทนเข้มเข้ากับแบรนด์
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        bgImage = loadImage("/assets/burger_bg.jpg");
        if (bgImage == null) {
            bgImage = loadImage("/assets/Backgrond.png");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onStart.run();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            // เปิดโหมดวาดภาพเนียน คมชัด ไม่แตก
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelW = getWidth();
            int panelH = getHeight();
            int imgW = bgImage.getWidth(this);
            int imgH = bgImage.getHeight(this);

            if (imgW > 0 && imgH > 0) {
                // คำนวณอัตราส่วน (Aspect Ratio) เพื่อให้ภาพสมส่วนตลอดเวลา
                double scale = Math.min((double) panelW / imgW, (double) panelH / imgH);
                
                int drawW = (int) (imgW * scale);
                int drawH = (int) (imgH * scale);
                
                // จัดตำแหน่งให้อยู่ตรงกลางหน้าจอเสมอ
                int x = (panelW - drawW) / 2;
                int y = (panelH - drawH) / 2;

                g2d.drawImage(bgImage, x, y, drawW, drawH, this);
            }
            g2d.dispose();
        }
    }

    private Image loadImage(String path) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}   