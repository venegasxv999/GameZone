package org.gamezone.model;

public abstract class VideoGame {
    protected String title;
    protected double price;
    protected String platform;
    protected int stock;
    protected String genre;

    // Full Constructor
    public VideoGame(String title, double price, String platform, int stock, String genre) {
        this.title = title;
        this.price = price;
        this.platform = platform;
        this.stock = stock;
        this.genre = genre;
    }

    // Abstract method to be implemented by subclasses (Polymorphism)
    public abstract double calculateFinalPrice();

    // Getters and Setters (Encapsulation)
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getPlatform() { return platform; }
    public int getStock() { return stock; }
    public String getGenre() { return genre; }

    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Título: " + title + " | Plataforma: " + platform + " | Precio Base: $" + price + " | Stock: " + stock;
    }
}