package org.gamezone.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gamezone.model.VideoGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileRepository {

    private List<VideoGame> catalog;
    private ObjectMapper mapper;

    // Define the storage path for the JSON file
    private final String FILE_PATH = "data/catalog.json";

    public JsonFileRepository() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Allow flexibility if model changes in the future
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Ensure data directory exist
        new File("data").mkdirs();
        this.catalog = loadData();
    }

    // Load games from file into memory at startup
    private List<VideoGame> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try {
            // We use an explicit generic type to help Jackson
            return mapper.readValue(file, new TypeReference<List<VideoGame>>() {});
        } catch (IOException e) {
            System.err.println("Archivo corrupto o incompatible. Se iniciará un catálogo vacío.");
            return new ArrayList<>();
        }
    }

    // Persist current catalog state to the JSON file
    private void saveData() {
        try {
            // We use `writerFor` to ensure that polymorphism is preserved when writing
            mapper.writerFor(new TypeReference<List<VideoGame>>() {})
                    .writeValue(new File(FILE_PATH), catalog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save a new game and update file
    public void save(VideoGame game) {
        catalog.add(game);
        saveData(); // Guarda físicamente cada vez que agregas
    }

    // Return a copy of the current catalog
    public List<VideoGame> findAll() {
        return new ArrayList<>(catalog);
    }

    // Find a specific game by its title
    public VideoGame findByTitle(String title) {
        for (VideoGame game : catalog) {
            if (game.getTitle().equalsIgnoreCase(title)) {
                return game;
            }
        }
        return null;
    }

    // Update existing game data and save changes
    public void update(VideoGame updatedGame) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getTitle().equalsIgnoreCase(updatedGame.getTitle())) {
                catalog.set(i, updatedGame);
                saveData();
                return;
            }
        }
    }

    // Delete a game by title and save changes
    public boolean delete(String title) {
        boolean removed = catalog.removeIf(game -> game.getTitle().equalsIgnoreCase(title));
        if (removed) {
            saveData();
        }
        return removed;
    }
}