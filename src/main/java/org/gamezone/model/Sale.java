package org.gamezone.model;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Represents a sales transaction within the GameZone system.
 * This class encapsulates the details of a purchase, including the item sold,
 * the quantity, the total calculated price, and the timestamp of the operation.
 */
public class Sale {
    private String id;
    private VideoGame videoGame;
    private int quantity;
    private double unitPrice;
    private double total;
    private LocalDateTime saleDate;

    //Constructs a new Sale instance.
    //If no ID is provided, a unique UUID is automatically generated/ /
    public Sale(String id, VideoGame videoGame, int quantity, double unitPrice) {
        this.id = (id == null || id.isEmpty()) ? UUID.randomUUID().toString() : id;
        this.videoGame = videoGame;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = unitPrice * quantity;
        this.saleDate = LocalDateTime.now();
    }

    // Getters for standard attributes
    public String getId() { return id; }
    public VideoGame getVideoGame() { return videoGame; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }
    public LocalDateTime getSaleDate() { return saleDate; }

    //Display the name and value of the game in the table that is generated when the option is selected: “6. Mostrar ventas”
    public String getName() { return videoGame.getTitle(); }
    public double getPrice() { return videoGame.getPrice(); }
    @Override
    public String toString() {
        return "Venta ID: " + id + " | Juego: " + videoGame.getTitle() +
                " | Cantidad: " + quantity + " | Total: $" + total + " | Fecha: " + saleDate;
    }
}