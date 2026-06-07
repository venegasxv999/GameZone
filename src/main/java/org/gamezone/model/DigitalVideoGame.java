package org.gamezone.model;

// Inheritance and Multiple Interfaces implementation
public class DigitalVideoGame extends VideoGame implements ISellable, IDisplayable {
    private double sizeGB;
    private String downloadPlatform;

    public DigitalVideoGame(String title, double price, String platform, int stock, String genre, double sizeGB, String downloadPlatform) {
        super(title, price, platform, stock, genre);
        this.sizeGB = sizeGB;
        this.downloadPlatform = downloadPlatform;
    }

    public double getSizeGB() { return sizeGB; }
    public void setSizeGB(double sizeGB) { this.sizeGB = sizeGB; }

    public String getDownloadPlatform() { return downloadPlatform; }
    public void setDownloadPlatform(String downloadPlatform) { this.downloadPlatform = downloadPlatform; }

    @Override
    public double calculateFinalPrice() {
        double finalPrice = this.price;
        // Business Rule: If size > 50GB, add $5000
        if (this.sizeGB > 50) {
            finalPrice += 5000;
        }
        return finalPrice;
    }

    @Override
    public double sell(int qty) throws IllegalArgumentException {
        if (qty > this.stock) {
            throw new IllegalArgumentException("Stock insuficiente para: " + this.title);
        }
        this.stock -= qty;
        return calculateFinalPrice() * qty;
    }

    @Override
    public String getDisplayInfo() {
        return "Digital -> " + super.toString() + " | Tamaño: " + sizeGB + "GB | Tienda: " + downloadPlatform;
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{title, platform, genre, stock, calculateFinalPrice(), "Digital"};
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}