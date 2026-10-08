import javax.swing.*;
import java.awt.*;

public class Main{
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
