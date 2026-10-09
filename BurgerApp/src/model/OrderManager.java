package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderManager {

    private static final List<Order> activeOrders = new ArrayList<>();
    private static Runnable onOrdersChangedListener;

    private static final String FILE_PATH = "orders_history.txt";

    static {
        loadLastOrderCounter();
    }

    public static void loadLastOrderCounter() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        int maxOrderNum = 0;
        Pattern pattern = Pattern.compile("หมายเลขออเดอร์:\\s*A-(\\d+)");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int num = Integer.parseInt(matcher.group(1));
                    if (num > maxOrderNum) {
                        maxOrderNum = num;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดในการอ่านไฟล์ประวัติ: " + e.getMessage());
        }

        Order.setOrderCounter(maxOrderNum + 1);
    }

    public static List<Order> getActiveOrders() {
        return activeOrders;
    }

    public static void setOnOrdersChangedListener(Runnable listener) {
        onOrdersChangedListener = listener;
    }

    // 1. เพิ่มออเดอร์ใหม่
    public static void addOrder(Order order) {
        order.setStatus("รอคิว");
        activeOrders.add(order);
        
        saveOrderToFile(order, "สั่งซื้อสำเร็จ");

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // 2. ฟังก์ชันเมื่อกด "เริ่มทำออเดอร์"
    public static void startOrder(Order order) {
        order.setStatus("กำลังทำ");
        
        // บันทึกลงไฟล์พร้อมระบุ event ว่า "เริ่มทำออเดอร์"
        saveOrderToFile(order, "เริ่มทำออเดอร์");

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // 3. ฟังก์ชันอัปเดตสถานะทั่วไป
    public static void updateOrderStatus(Order order, String newStatus, String eventType) {
        order.setStatus(newStatus);
        saveOrderToFile(order, eventType);

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // 4. ฟังก์ชันทำเสร็จออเดอร์ / ลบออกจากรายการ
    public static void removeOrder(Order order) {
        order.setStatus("ทำเสร็จแล้ว");
        activeOrders.remove(order);
        
        saveOrderToFile(order, "ทำเสร็จเรียบร้อย");

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // --- ฟังก์ชันบันทึกข้อมูลลงไฟล์ Text ---
    private static void saveOrderToFile(Order order, String eventType) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = dtf.format(LocalDateTime.now());

        double totalPrice = 0;
        StringBuilder itemsDetail = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            totalPrice += item.getTotalPrice();
            itemsDetail.append(item.getName())
                       .append(" x").append(item.getQuantity())
                       .append(" (฿").append(item.getTotalPrice()).append("), ");
        }

        if (itemsDetail.length() > 2) {
            itemsDetail.setLength(itemsDetail.length() - 2);
        }

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