package org.gamezone.repository;



import org.gamezone.model.DigitalVideoGame;
import org.gamezone.model.PhysicalVideoGame;
import org.gamezone.model.VideoGame;
import java.util.ArrayList;
import java.util.List;

// Simulated JSON/In-Memory Repository for CRUD operations
public class JsonFileRepository {
    private List<VideoGame> catalog;

    public JsonFileRepository() {
        this.catalog = new ArrayList<>();
        // Pre-load dummy data for testing purposes
        catalog.add(new DigitalVideoGame("Cyberpunk", 150000, "PC", 10, "RPG", 70.5, "Steam"));
        catalog.add(new PhysicalVideoGame("Mario Kart", 200000, "Switch", 5, "Racing", "Usado", "Nintendo"));
    }

    public void save(VideoGame game) {
        catalog.add(game);

    }

    public List<VideoGame> findAll() {
        return new ArrayList<>(catalog);
    }

    public VideoGame findByTitle(String title) {
        for (VideoGame game : catalog) {
            if (game.getTitle().equalsIgnoreCase(title)) {
                return game;
            }
        }
        return null;
    }

    public void update(VideoGame updatedGame) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getTitle().equalsIgnoreCase(updatedGame.getTitle())) {
                catalog.set(i, updatedGame);
                return;
            }
        }
    }

    public boolean delete(String title) {
        return catalog.removeIf(game -> game.getTitle().equalsIgnoreCase(title));
    }
}