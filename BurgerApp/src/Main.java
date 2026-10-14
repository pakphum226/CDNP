import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args) {
        UIManager.put("OptionPane.okButtonText", "ตกลง");
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.BOLD, 12));

        //Swing ไม่ปลอดภัยต่อ thread (not thread-safe) ต้องสร้างและแก้ไข UI บน Event Dispatch Thread (EDT) เท่านั้น
        //invokeLater จะโยนงานนี้ไปรันบน EDT ซึ่งป้องกันปัญหา UI ค้างหรือแสดงผลเพี้ยนแบบสุ่ม
        SwingUtilities.invokeLater(() -> {
            //MainFrame เป็นหน้าต่าง (JFrame) ส่วน Main ทำหน้าที่ "ตั้งค่าแล้วเปิดแอป" เท่านั้น ไม่มีตรรกะอื่นปนอยู่ ถือเป็นการแยกหน้าที่ให้อ่านง่าย
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
