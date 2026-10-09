package model;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static final List<Order> activeOrders = new ArrayList<>();
    private static Runnable onOrdersChangedListener;

    // ตั้งค่า Listener เพื่อแจ้งเตือนหน้าแอปพนักงานเมื่อมีออเดอร์ใหม่เข้ามา
    public static void setOnOrdersChangedListener(Runnable listener) {
        onOrdersChangedListener = listener;
    }

    // เพิ่มออเดอร์ใหม่เข้าคิว
    public static void addOrder(Order order) {
        activeOrders.add(order);
        notifyListener();
    }

    // ดึงรายการออเดอร์ทั้งหมดที่ยังทำไม่เสร็จ
    public static List<Order> getActiveOrders() {
        return activeOrders;
    }

    // ลบออเดอร์ออกจากคิวเมื่อทำเสร็จแล้ว
    public static void removeOrder(Order order) {
        activeOrders.remove(order);
        notifyListener();
    }

    private static void notifyListener() {
        if (onOrdersChangedListener != null) {
            onOrdersChangedListener.run();
        }
    }
}