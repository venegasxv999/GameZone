package org.gamezone.model;

public class PhysicalVideoGame extends VideoGame implements ISellable, IDisplayable {
    private String condition;
    private String distributor;

    // Required empty constructor for json
    public PhysicalVideoGame() {
    }

    // Constructor for Physical Games
    public PhysicalVideoGame(String title, double price, String platform, int stock, String genre, String condition, String distributor) {
        super(title, price, platform, stock, genre);
        this.condition = condition;
        this.distributor = distributor;
    }

    // Getters and Setters
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getDistributor() { return distributor; }
    public void setDistributor(String distributor) { this.distributor = distributor; }

    @Override
    public double calculateFinalPrice() {
        double finalPrice = this.price;
        // Business Rule: 25% discount if condition is "Usado"
        if ("usado".equalsIgnoreCase(this.condition)) {
            finalPrice -= (finalPrice * 0.25);
        }
        return finalPrice;
    }

    @Override
    public double sell(int qty) throws IllegalArgumentException {
        // Validate stock before selling
        if (qty > this.stock) {
            throw new IllegalArgumentException("Stock insuficiente para: " + this.title);
        }
        this.stock -= qty;
        return calculateFinalPrice() * qty;
    }

    @Override
    public String getDisplayInfo() {
        return "Físico -> " + super.toString() + " | Condición: " + condition + " | Distribuidor: " + distributor;
    }

    @Override
    public Object[] toTableRow() {
        // Prepare data for UI table display
        return new Object[]{title, platform, genre, stock, calculateFinalPrice(), "Físico"};
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}