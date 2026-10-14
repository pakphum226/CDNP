package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Discount {

    // ===================== 1. ตั้งค่าตัวแปร =====================
    private static final double MIN_SUBTOTAL   = 300;  // ยอดขั้นต่ำ (บาท)
    private static final double DISCOUNT_RATE  = 10;   // ส่วนลด 10%

    // ชื่อเมนูสำหรับเงื่อนไขข้อ 3 (เปลี่ยนชื่อเมนูตรงนี้ได้เลย)
    private static final String SPECIFIC_MENU_NAME = "Chicken Burger"; 

    // รายชื่อเมนูแต่ละหมวด
    private static final Set<String> SET_MENU = Set.of(
            "Chubby Kid Set", "Red and Black", "Super Chicken Set", "Titan Super Burger",
            "Triple Hamburger", "Great Value Set", "Black set", "Eat-to-Die Set");

    private static final Set<String> RICE_MENU = Set.of(
            "Super Chicken Rice", "Crispy Chicken Basil", "Spicy Chicken Salad",
            "Grilled Pork Spicy", "Braised Pork Belly", "Tonkatsu Rice",
            "Rare grilled beef", "Kuro Grilled Beef", "Hamburg Steak Egg");

    private static final Set<String> BURGER_MENU = Set.of(
            "Chicken Burger", "Fried Chicken", "Egg and cheese",
            "Beef + Shrimp", "Super Fish", "Black Sauce Burger",
            "Spicy burger cries", "Monster Triple", "Veggie Burger");

    private Discount() {}

    // ===================== 2. เมธอดที่หน้าจอเรียกใช้ =====================

    public static double subtotal(List<OrderItem> items) {
        double sum = 0;
        for (OrderItem it : items) sum += it.getTotalPrice();
        return sum;
    }

    public static double calculate(List<OrderItem> items) {
        return apply(items, new ArrayList<>());
    }

    public static List<String> describe(List<OrderItem> items) {
        List<String> notes = new ArrayList<>();
        apply(items, notes);
        return notes;
    }

    // ===================== 3. เมธอดคำนวณกฎส่วนลด =====================

    private static double apply(List<OrderItem> items, List<String> notes) {
        double discount = 0;
        double subtotal = subtotal(items);

        // =========================================================================
        // [เงื่อนไข 1.1] ลดเฉพาะหมวด SET รวมกันในหมวดขั้นต่ำ 300 บาท ลด 10%
        // =========================================================================
        /*
        double setTotal = 0;
        for (OrderItem it : items) {
            if (SET_MENU.contains(it.getName())) {
                setTotal += it.getTotalPrice();
            }
        }
        if (setTotal >= MIN_SUBTOTAL) {
            discount = setTotal * DISCOUNT_RATE / 100.0;
            double netPrice = setTotal - discount;
            notes.add(String.format("[หมวด SET ซื้อครบ ฿%.0f] ราคาก่อนลด ฿%.2f (ลด %.0f%% = -฿%.2f) เหลือ ฿%.2f", 
                    MIN_SUBTOTAL, setTotal, DISCOUNT_RATE, discount, netPrice));
        }
        */

        // =========================================================================
        // [เงื่อนไข 1.2] ลดเฉพาะหมวด RICE รวมกันในหมวดขั้นต่ำ 300 บาท ลด 10%
        // =========================================================================
        /*
        double riceTotal = 0;
        for (OrderItem it : items) {
            if (RICE_MENU.contains(it.getName())) {
                riceTotal += it.getTotalPrice();
            }
        }
        if (riceTotal >= MIN_SUBTOTAL) {
            discount = riceTotal * DISCOUNT_RATE / 100.0;
            double netPrice = riceTotal - discount;
            notes.add(String.format("[หมวด RICE ซื้อครบ ฿%.0f] ราคาก่อนลด ฿%.2f (ลด %.0f%% = -฿%.2f) เหลือ ฿%.2f", 
                    MIN_SUBTOTAL, riceTotal, DISCOUNT_RATE, discount, netPrice));
        }
        */

        // =========================================================================
        // [เงื่อนไข 1.3] ลดเฉพาะหมวด BURGER รวมกันในหมวดขั้นต่ำ 300 บาท ลด 10%
        // =========================================================================
        /*
        double burgerTotal = 0;
        for (OrderItem it : items) {
            if (BURGER_MENU.contains(it.getName())) {
                burgerTotal += it.getTotalPrice();
            }
        }
        if (burgerTotal >= MIN_SUBTOTAL) {
            discount = burgerTotal * DISCOUNT_RATE / 100.0;
            double netPrice = burgerTotal - discount;
            notes.add(String.format("[หมวด BURGER ซื้อครบ ฿%.0f] ราคาก่อนลด ฿%.2f (ลด %.0f%% = -฿%.2f) เหลือ ฿%.2f", 
                    MIN_SUBTOTAL, burgerTotal, DISCOUNT_RATE, discount, netPrice));
        }
        */

        // =========================================================================
        // [เงื่อนไขที่ 2] ซื้ออะไรก็ได้ทุกเมนูรวมกันทั้งตะกร้าขั้นต่ำ 300 บาท ลด 10%
        // =========================================================================
        
        if (subtotal >= MIN_SUBTOTAL) {
            double d = subtotal * DISCOUNT_RATE / 100.0;
            double netPrice = subtotal - d;
            discount += d;
            notes.add(String.format("[ซื้อครบ ฿%.0f ทั้งตะกร้า] ราคาก่อนลด ฿%.2f (ลด %.0f%% = -฿%.2f) เหลือ ฿%.2f", 
                    MIN_SUBTOTAL, subtotal, DISCOUNT_RATE, d, netPrice));
        }
        

        // =========================================================================
        // [เงื่อนไขที่ 3] ลดแค่ชื่อรายการอาหารรายการหนึ่ง (SPECIFIC_MENU_NAME) ลด 10% ต่อ 1 ชิ้น
        // =========================================================================
        /*
        for (OrderItem it : items) {
            if (it.getName().equals(SPECIFIC_MENU_NAME)) {
                double originalPrice = it.getPrice();
                double d = originalPrice * DISCOUNT_RATE / 100.0;
                double netPrice = originalPrice - d;
                discount += d;
                notes.add(String.format("[%s (ต่อ 1 ชิ้น)] ราคาก่อนลด ฿%.2f (ลด %.0f%% = -฿%.2f) เหลือ ฿%.2f", 
                        it.getName(), originalPrice, DISCOUNT_RATE, d, netPrice));
            }
        }
        */

        return discount;
    }
}