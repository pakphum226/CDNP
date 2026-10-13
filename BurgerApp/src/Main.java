import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        // ตั้งค่าภาษาไทยและ Font ให้ระบบ Swing
        UIManager.put("OptionPane.okButtonText", "ตกลง");
        UIManager.put("OptionPane.cancelButtonText", "ยกเลิก");
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.BOLD, 12));

        // เรียกใช้งานหน้าจอหลัก (MainFrame)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}