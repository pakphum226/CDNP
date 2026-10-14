package model;

import java.util.List;

public class Order {
    private static int orderCounter = 1;

    private String orderId;
    private List<OrderItem> items;
    private double totalPrice;      // ราคาสุทธิ (หลังหักส่วนลดแล้ว ถ้ามี)
    private String paymentMethod;
    private String status;          // "รอคิว" → "กำลังทำ" → "ทำเสร็จแล้ว"

    // ตั้งค่าตัวนับเลขออเดอร์เริ่มต้น (OrderManager เรียกหลังอ่านไฟล์ประวัติ)
    public static void setOrderCounter(int counter) {
        orderCounter = counter;
    }

    // Constructor ตัวที่ 1: สร้างออเดอร์ใหม่ (เลขรันอัตโนมัติ)
    public Order(List<OrderItem> items, double totalPrice, String paymentMethod) {
        this.orderId = String.format("A-%03d", orderCounter++);
        this.items = items;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = "รอคิว";
    }

    // Constructor ตัวที่ 2: กู้ออเดอร์เดิมจากไฟล์ (คงเลขและสถานะเดิม ไม่เพิ่มตัวนับ)
    public Order(String orderId, List<OrderItem> items, double totalPrice,
                 String paymentMethod, String status) {
        this.orderId = orderId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getPaymentMethod() { return paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}