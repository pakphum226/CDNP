package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderManager {

    private static final List<Order> activeOrders = new ArrayList<>();
    private static Runnable onOrdersChangedListener;

    private static final Path FILE_PATH = Paths.get("orders_history.csv");
    private static final String HEADER =
            "Timestamp,OrderId,Status,EventType,TotalPrice,Items,PaymentMethod,ItemsData";
    private static final String STATUS_DONE = "ทำเสร็จแล้ว";

    static {
        loadFromCsv();
    }

    // =====================================================
    // โหลดตอนเปิดโปรแกรม: ตั้งตัวนับเลขออเดอร์ + กู้ออเดอร์ที่ยังค้าง
    // =====================================================
    private static void loadFromCsv() {
        if (!Files.exists(FILE_PATH)) return;

        int maxOrderNum = 0;
        Pattern idPattern = Pattern.compile("A-(\\d+)");
        // เก็บแถวล่าสุดของแต่ละออเดอร์ (คงลำดับตามที่เจอครั้งแรก)
        Map<String, List<String>> lastRowById = new LinkedHashMap<>();

        try {
            for (String line : Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("Timestamp")) continue;

                List<String> f = parseCsvLine(line);
                if (f.size() < 2) continue;

                Matcher m = idPattern.matcher(f.get(1));
                if (m.find()) {
                    maxOrderNum = Math.max(maxOrderNum, Integer.parseInt(m.group(1)));
                }

                // แถวเก่าที่ไม่มีคอลัมน์ ItemsData กู้ไม่ได้ ข้ามไป
                if (f.size() >= 8) {
                    lastRowById.put(f.get(1), f);
                }
            }
        } catch (Exception e) {
            System.err.println("เกิดข้อผิดพลาดในการอ่านไฟล์ประวัติ: " + e.getMessage());
        }

        Order.setOrderCounter(maxOrderNum + 1);

        // ออเดอร์ที่แถวล่าสุดยังไม่ใช่ "ทำเสร็จแล้ว" = ยังค้างอยู่
        for (List<String> f : lastRowById.values()) {
            try {
                String status = f.get(2);
                if (STATUS_DONE.equals(status)) continue;

                List<OrderItem> items = new ArrayList<>();
                double total = 0;
                for (String itemStr : f.get(7).split("\\|")) {
                    if (itemStr.isBlank()) continue;
                    String[] p = itemStr.split("~", -1);
                    OrderItem item = new OrderItem(
                            p[0], p[1],
                            Double.parseDouble(p[2]),
                            Integer.parseInt(p[3]));
                    items.add(item);
                    total += item.getTotalPrice();
                }

                activeOrders.add(new Order(f.get(1), items, total, f.get(6), status));
            } catch (Exception e) {
                System.err.println("ข้ามออเดอร์ที่อ่านไม่ได้: " + e.getMessage());
            }
        }
    }

    // อ่าน 1 บรรทัด CSV (รองรับ "..." และ "" ภายในข้อความ)
    private static List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        sb.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    fields.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
        }
        fields.add(sb.toString());
        return fields;
    }

    // =====================================================
    // API ที่หน้าจอเรียกใช้ (เหมือนเดิม)
    // =====================================================
    public static List<Order> getActiveOrders() {
        return activeOrders;
    }

    public static void setOnOrdersChangedListener(Runnable listener) {
        onOrdersChangedListener = listener;
    }

    public static void addOrder(Order order) {
        order.setStatus("รอคิว");
        activeOrders.add(order);
        saveOrderToFile(order, "สั่งซื้อสำเร็จ");
        notifyChanged();
    }

    public static void startOrder(Order order) {
        order.setStatus("กำลังทำ");
        saveOrderToFile(order, "เริ่มทำออเดอร์");
        notifyChanged();
    }

    public static void updateOrderStatus(Order order, String newStatus, String eventType) {
        order.setStatus(newStatus);
        saveOrderToFile(order, eventType);
        notifyChanged();
    }

    public static void removeOrder(Order order) {
        order.setStatus(STATUS_DONE);
        activeOrders.remove(order);
        saveOrderToFile(order, "ทำเสร็จเรียบร้อย");
        notifyChanged();
    }

    private static void notifyChanged() {
        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }

    // =====================================================
    // บันทึกลง CSV
    // =====================================================
    private static String q(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    private static void saveOrderToFile(Order order, String eventType) {
        boolean isNewFile = !Files.exists(FILE_PATH);
        String currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(LocalDateTime.now());

        double totalPrice = 0;
        StringBuilder itemsDetail = new StringBuilder(); // ข้อความอ่านง่ายสำหรับเปิดใน Excel
        StringBuilder itemsData = new StringBuilder();   // ข้อมูลที่โปรแกรมอ่านกลับ
        for (OrderItem item : order.getItems()) {
            totalPrice += item.getTotalPrice();

            if (itemsDetail.length() > 0) itemsDetail.append("; ");
            itemsDetail.append(item.getName())
                       .append(" x").append(item.getQuantity())
                       .append(" (฿").append(item.getTotalPrice()).append(")");

            if (itemsData.length() > 0) itemsData.append("|");
            itemsData.append(item.getName()).append("~")
                     .append(item.getImagePath()).append("~")
                     .append(item.getPrice()).append("~")
                     .append(item.getQuantity());
        }

        String record = String.join(",",
                q(currentTime),
                q(order.getOrderId()),
                q(order.getStatus()),
                q(eventType),
                String.format(Locale.US, "%.2f", totalPrice),
                q(itemsDetail.toString()),
                q(order.getPaymentMethod()),
                q(itemsData.toString())
        ) + System.lineSeparator();

        try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (isNewFile) {
                writer.write(HEADER + System.lineSeparator());
            }
            writer.write(record);
        } catch (IOException e) {
            System.err.println("เกิดข้อผิดพลาดในการบันทึกไฟล์ CSV: " + e.getMessage());
        }
    }
}