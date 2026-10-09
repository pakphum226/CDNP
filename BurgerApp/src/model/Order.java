package model;

import java.util.List;

public class Order {
    private static int orderCounter = 1;

    private String orderId;
    private List<OrderItem> items;
    private double totalPrice;
    private String paymentMethod;
    private String status; // เพิ่มการเก็บสถานะออเดอร์ (เช่น "รอคิว", "กำลังทำ")

    // ฟังก์ชันสำหรับกำหนดค่า orderCounter เริ่มต้น (ดึงจากไฟล์)
    public static void setOrderCounter(int counter) {
        orderCounter = counter;
    }

    public Order(List<OrderItem> items, double totalPrice, String paymentMethod) {
        this.orderId = String.format("A-%03d", orderCounter++);
        this.items = items;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = "รอคิว"; // ค่าเริ่มต้น
    }

    public String getOrderId() { return orderId; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getPaymentMethod() { return paymentMethod; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}