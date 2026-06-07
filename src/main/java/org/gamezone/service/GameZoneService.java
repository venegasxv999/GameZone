package org.gamezone.service;

import org.gamezone.model.*;
import org.gamezone.repository.JsonFileRepository;
import java.util.ArrayList;
import java.util.List;

public class GameZoneService {
    private JsonFileRepository repository;
    private List<Sale> salesHistory;

    public GameZoneService() {
        this.repository = new JsonFileRepository();
        this.salesHistory = new ArrayList<>();
    }

    // CREATE
    public void addVideoGame(VideoGame game) throws Exception {
        if (game.getTitle() == null || game.getTitle().trim().isEmpty()) {
            throw new Exception("El título no puede estar vacío.");
        }
        if (game.getPrice() <= 0) {
            throw new Exception("El precio debe ser mayor a 0.");
        }
        if (game.getStock() < 0) {
            throw new Exception("El stock no puede ser negativo.");
        }
        if (repository.findByTitle(game.getTitle()) != null) {
            throw new Exception("El video juego ya existe en el catálogo.");
        }
        repository.save(game);
    }

    // READ (All)
    public List<VideoGame> getAllVideoGames() {
        return repository.findAll();
    }

    // READ (By Title)
    public VideoGame searchByTitle(String title) {
        return repository.findByTitle(title);
    }

    // READ (By Platform)
    public List<VideoGame> searchByPlatform(String platform) {
        List<VideoGame> result = new ArrayList<>();
        for (VideoGame game : repository.findAll()) {
            if (game.getPlatform().equalsIgnoreCase(platform)) {
                result.add(game);
            }
        }
        return result.isEmpty() ? null : result;
    }

    // UPDATE
    public void updateVideoGame(VideoGame game) {
        repository.update(game);
    }

    // DELETE
    public void deleteVideoGame(String title) throws Exception {
        boolean deleted = repository.delete(title);
        if (!deleted) {
            throw new Exception("No se encontró el juego para eliminar.");
        }
    }

    // BUSINESS RULE: SELL GAME
    public Sale sellVideoGame(String title, int quantity) throws Exception {
        VideoGame game = repository.findByTitle(title);

        if (quantity <= 0) {
            throw new Exception("La cantidad a vender debe ser mayor a cero");
        }


        if (game == null) {
            throw new Exception("El juego no existe en el catálogo.");
        }

        // Polymorphic call. Both subclasses implement ISellable
        double totalSaleValue;
        if (game instanceof ISellable) {
            totalSaleValue = ((ISellable) game).sell(quantity);
        } else {
            throw new Exception("Este artículo no está disponible para la venta.");
        }



        repository.update(game); // Save new stock

        Sale newSale = new Sale(null, game, quantity, game.calculateFinalPrice());
        salesHistory.add(newSale);
        return newSale;
    }

    public List<Sale> getSalesHistory() {
        return salesHistory;
    }
}