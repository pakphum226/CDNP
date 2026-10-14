package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderManager {

    private static final List<Order> activeOrders = new ArrayList<>();
    private static Runnable onOrdersChangedListener;

    private static final String FILE_PATH = "orders_history.csv";

    // ไฟล์เก็บออเดอร์ที่ยังค้างอยู่ (ใช้กู้คืนตอนเปิดโปรแกรมใหม่)
    private static final Path ACTIVE_FILE = Paths.get("active_orders.txt");

    static {
        loadLastOrderCounter();
        loadActiveOrders();
    }

    public static void loadLastOrderCounter() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        int maxOrderNum = 0;
        Pattern pattern = Pattern.compile("A-(\\d+)");

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
        saveActiveOrders();

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // 2. เริ่มทำออเดอร์
    public static void startOrder(Order order) {
        order.setStatus("กำลังทำ");

        saveOrderToFile(order, "เริ่มทำออเดอร์");
        saveActiveOrders();

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // 3. อัปเดตสถานะทั่วไป
    public static void updateOrderStatus(Order order, String newStatus, String eventType) {
        order.setStatus(newStatus);
        saveOrderToFile(order, eventType);
        saveActiveOrders();

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // 4. ทำเสร็จ / ลบออกจากรายการ
    public static void removeOrder(Order order) {
        order.setStatus("ทำเสร็จแล้ว");
        activeOrders.remove(order);

        saveOrderToFile(order, "ทำเสร็จเรียบร้อย");
        saveActiveOrders();

        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // =====================================================
    // บันทึก / โหลดออเดอร์ที่ยังค้าง
    // รูปแบบ 1 บรรทัดต่อ 1 ออเดอร์ (คั่นด้วย TAB):
    // orderId \t status \t totalPrice \t paymentMethod \t name~imagePath~price~qty|name~imagePath~price~qty
    // =====================================================

    private static void saveActiveOrders() {
        List<String> lines = new ArrayList<>();

        for (Order o : activeOrders) {
            StringBuilder items = new StringBuilder();
            for (OrderItem it : o.getItems()) {
                if (items.length() > 0) items.append("|");
                items.append(it.getName()).append("~")
                     .append(it.getImagePath()).append("~")
                     .append(it.getPrice()).append("~")
                     .append(it.getQuantity());
            }
            lines.add(o.getOrderId() + "\t" + o.getStatus() + "\t"
                    + o.getTotalPrice() + "\t" + o.getPaymentMethod() + "\t" + items);
        }

        try {
            Files.write(ACTIVE_FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดในการบันทึกออเดอร์ที่ค้าง: " + e.getMessage());
        }
    }

    private static void loadActiveOrders() {
        if (!Files.exists(ACTIVE_FILE)) return;

        try {
            for (String line : Files.readAllLines(ACTIVE_FILE, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;

                String[] p = line.split("\t", -1);
                if (p.length < 5) continue;

                List<OrderItem> items = new ArrayList<>();
                for (String itemStr : p[4].split("\\|")) {
                    if (itemStr.isBlank()) continue;
                    String[] f = itemStr.split("~", -1);
                    items.add(new OrderItem(
                            f[0], f[1],
                            Double.parseDouble(f[2]),
                            Integer.parseInt(f[3])));
                }

                activeOrders.add(new Order(
                        p[0], items,
                        Double.parseDouble(p[2]),
                        p[3], p[1]));
            }
        } catch (Exception e) {
            System.err.println("เกิดข้อผิดพลาดในการโหลดออเดอร์ที่ค้าง: " + e.getMessage());
        }
    }

    // --- บันทึกประวัติลงไฟล์ CSV (ของเดิม) ---
    private static void saveOrderToFile(Order order, String eventType) {
        File file = new File(FILE_PATH);
        boolean isNewFile = !file.exists();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = dtf.format(LocalDateTime.now());

        double totalPrice = 0;
        StringBuilder itemsDetail = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            totalPrice += item.getTotalPrice();
            itemsDetail.append(item.getName())
                       .append(" x").append(item.getQuantity())
                       .append(" (฿").append(item.getTotalPrice()).append("); ");
        }

        if (itemsDetail.length() > 2) {
            itemsDetail.setLength(itemsDetail.length() - 2);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (isNewFile) {
                writer.write("Timestamp,OrderId,Status,EventType,TotalPrice,Items\n");
            }

            String record = String.format("\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\"%n",
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
            System.err.println("เกิดข้อผิดพลาดในการบันทึกไฟล์ CSV: " + e.getMessage());
        }
    }
}