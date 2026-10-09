package model;


public class OrderItem {
    private String name;
    private String imagePath;
    private double price;
    private int quantity;

    public OrderItem(String name, String imagePath, double price, int quantity) {
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getImagePath() { return imagePath; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return price * quantity; }
}