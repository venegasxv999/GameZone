package org.gamezone.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Configure Jackson to handle polymorphism using a "tipoJuego" property
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoJuego")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DigitalVideoGame.class, name = "Digital"),
        @JsonSubTypes.Type(value = PhysicalVideoGame.class, name = "Physical")
})

public abstract class VideoGame {
    protected String title;
    protected double price;
    protected String platform;
    protected int stock;
    protected String genre;

    // Configure Jackson to handle polymorphism using a "tipoJuego" property
    public VideoGame() {
    }

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