package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final List<Order> activeOrders = new ArrayList<>();
    private static Runnable onOrdersChangedListener;

    // ชื่อไฟล์ที่จะใช้เก็บประวัติออเดอร์
    private static final String FILE_PATH = "orders_history.txt";

    public static List<Order> getActiveOrders() {
        return activeOrders;
    }

    public static void setOnOrdersChangedListener(Runnable listener) {
        onOrdersChangedListener = listener;
    }

    // ฟังก์ชันเพิ่มออเดอร์ใหม่
    public static void addOrder(Order order) {
        activeOrders.add(order);
        
        // บันทึกลงไฟล์ทันทีที่มีออเดอร์ใหม่เข้ามา
        saveOrderToFile(order, "สั่งซื้อสำเร็จ");

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // ฟังก์ชันลบ/ทำเสร็จออเดอร์
    public static void removeOrder(Order order) {
        activeOrders.remove(order);
        
        // บันทึกสถานะว่าออเดอร์นี้ทำเสร็จแล้ว
        saveOrderToFile(order, "ทำเสร็จเรียบร้อย");

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // --- ฟังก์ชันบันทึกข้อมูลลงไฟล์ Text ---
    private static void saveOrderToFile(Order order, String eventType) {
        // ดึงเวลาปัจจุบัน
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = dtf.format(LocalDateTime.now());

        // คำนวณราคารวมของออเดอร์นี้
        double totalPrice = 0;
        StringBuilder itemsDetail = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            totalPrice += item.getTotalPrice();
            itemsDetail.append(item.getName())
                       .append(" x").append(item.getQuantity())
                       .append(" (฿").append(item.getTotalPrice()).append("), ");
        }

        // ตัดเครื่องหมาย , อันสุดท้ายออก
        if (itemsDetail.length() > 2) {
            itemsDetail.setLength(itemsDetail.length() - 2);
        }

        // ใช้ FileWriter แบบ append = true เพื่อเขียนต่อท้ายไฟล์เรื่อยๆ
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String record = String.format("[%s] หมายเลขออเดอร์: %s | สถานะ: %s (%s) | ราคารวม: ฿%.2f | รายการ: [%s]%n",
                    currentTime,
                    order.getOrderId(),
                    order.getStatus(),
                    eventType,
                    totalPrice,
                    itemsDetail.toString()
            );
            
            writer.write(record);
            writer.flush();
        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดในการบันทึกไฟล์: " + e.getMessage());
        }
    }
}