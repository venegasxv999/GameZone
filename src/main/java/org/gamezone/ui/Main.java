package org.gamezone.ui;

import org.gamezone.model.*;
import org.gamezone.service.GameZoneService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



public class Main extends Application {

    private GameZoneService service;

    @Override
    public void init() {
        service = new GameZoneService();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SISTEMA DE GESTIÓN - GAMEZONE");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4; -fx-font-family: 'Segoe UI';");

        Label titleLabel = new Label("SISTEMA DE GESTIÓN - GAMEZONE");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnAddCrud = new Button("1. Agregar/Gestionar Videojuego");
        Button btnListAll = new Button("2. Listar todos los videojuegos");
        Button btnSearchTitle = new Button("3. Buscar por título");
        Button btnSearchPlatform = new Button("4. Buscar por plataforma");
        Button btnSell = new Button("5. Realizar venta");
        Button btnShowSales = new Button("6. Mostrar ventas");
        Button btnExit = new Button("7. Salir");

        // Styling buttons to fill width
        Button[] buttons = {btnAddCrud, btnListAll, btnSearchTitle, btnSearchPlatform, btnSell, btnShowSales, btnExit};
        for (Button btn : buttons) {
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        }

        // Action Events
        btnAddCrud.setOnAction(e -> showCrudMenu());
        btnListAll.setOnAction(e -> listAllGames());
        btnSearchTitle.setOnAction(e -> searchByTitle());
        btnSearchPlatform.setOnAction(e -> searchByPlatform());
        btnSell.setOnAction(e -> performSale());
        btnShowSales.setOnAction(e -> showSales());
        btnExit.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(titleLabel, btnAddCrud, btnListAll, btnSearchTitle, btnSearchPlatform, btnSell, btnShowSales, btnExit);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void showTableWindow(String title, TableView<?> table) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(table, 600, 400));
        stage.show();
    }
    private void showCrudMenu() {
        // Simplified CRUD via Dialog. In a full app, this would be a new Scene.
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Crear", "Crear", "Eliminar");
        dialog.setTitle("Gestión de Videojuegos");
        dialog.setHeaderText("Seleccione la operación a realizar");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals("Crear")) {
                createGameProcess();
            } else if (result.get().equals("Eliminar")) {
                deleteGameProcess();
            }
        }
    }

    private void createGameProcess() {
        Dialog<VideoGame> dialog = new Dialog<>();
        dialog.setTitle("Agregar Videojuego");
        dialog.setHeaderText("Complete los datos del nuevo videojuego");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Common fields
        TextField titleField = new TextField();
        TextField priceField = new TextField();
        TextField platformField = new TextField();
        TextField stockField = new TextField();
        TextField genreField = new TextField();
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Digital", "Físico");

        // Dynamic fields
        TextField extra1 = new TextField(); // Tamaño (Digital) o Condición (Físico)
        TextField extra2 = new TextField(); // Tienda (Digital) o Distribuidor (Físico)
        Label label1 = new Label("Extra 1:");
        Label label2 = new Label("Extra 2:");

        // Logic for changing labels based on type
        typeBox.setOnAction(e -> {
            if ("Digital".equals(typeBox.getValue())) {
                label1.setText("Tamaño (GB):");
                label2.setText("Plataforma Descarga:");
            } else {
                label1.setText("Condición:");
                label2.setText("Distribuidor:");
            }
        });

        grid.add(new Label("Título:"), 0, 0); grid.add(titleField, 1, 0);
        grid.add(new Label("Precio:"), 0, 1); grid.add(priceField, 1, 1);
        grid.add(new Label("Plataforma:"), 0, 2); grid.add(platformField, 1, 2);
        grid.add(new Label("Stock:"), 0, 3); grid.add(stockField, 1, 3);
        grid.add(new Label("Género:"), 0, 4); grid.add(genreField, 1, 4);
        grid.add(new Label("Tipo:"), 0, 5); grid.add(typeBox, 1, 5);
        grid.add(label1, 0, 6); grid.add(extra1, 1, 6);
        grid.add(label2, 0, 7); grid.add(extra2, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    // Data conversion
                    String title = titleField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int stock = Integer.parseInt(stockField.getText());
                    String platform = platformField.getText();
                    String genre = genreField.getText();

                    if ("Digital".equals(typeBox.getValue())) {
                        return new DigitalVideoGame(title, price, platform, stock, genre,
                                Double.parseDouble(extra1.getText()), extra2.getText());
                    } else {
                        return new PhysicalVideoGame(title, price, platform, stock, genre,
                                extra1.getText(), extra2.getText());
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Verifique que los campos numéricos sean correctos.");
                }
            }
            return null;
        });

        Optional<VideoGame> result = dialog.showAndWait();
        result.ifPresent(game -> {
            try {
                service.addVideoGame(game);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Videojuego agregado.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });
    }

    private void deleteGameProcess() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Eliminar Videojuego");
        dialog.setHeaderText("Seleccione el juego que desea eliminar:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Get the current catalog
        List<VideoGame> juegos = service.getAllVideoGames();

        // If there are no games, notify the user and close
        if (juegos.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Sin juegos", "No hay videojuegos en el catálogo para eliminar.");
            return;
        }

        ComboBox<String> gameSelector = new ComboBox<>();
        for (VideoGame g : juegos) {
            gameSelector.getItems().add(g.getTitle());
        }

        grid.add(new Label("Juego a eliminar:"), 0, 0);
        grid.add(gameSelector, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String titleToDelete = gameSelector.getValue();
                if (titleToDelete != null) {
                    try {
                        service.deleteVideoGame(titleToDelete);
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "El juego '" + titleToDelete + "' ha sido eliminado.");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un juego de la lista.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Initializes and displays a TableView containing the complete catalog of video games.
     * This method binds the model attributes (title, price, stock) to the table columns
     * using PropertyValueFactory, ensuring the UI remains synced with the game repository.
     */

    private void listAllGames() {
        TableView<VideoGame> table = new TableView<>();

        TableColumn<VideoGame, String> colTitle = new TableColumn<>("Título");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<VideoGame, Double> colPrice = new TableColumn<>("Precio");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<VideoGame, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(colTitle, colPrice, colStock);
        table.getItems().addAll(service.getAllVideoGames());

        showTableWindow("Catálogo de Videojuegos", table);
    }

    /**
     * Triggers a modal dialog that allows the user to select a video game from the current catalog.
     * Once a selection is made, it queries the service layer to retrieve and display
     * the specific details of the chosen game.
     */

    private void searchByTitle() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Buscar por Título");
        dialog.setHeaderText("Seleccione el juego para ver sus detalles:");

        // Configuring the form layout
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        // Populating the ComboBox with existing game titles from the repository
        List<VideoGame> juegos = service.getAllVideoGames();
        ComboBox<String> gameSelector = new ComboBox<>();
        for (VideoGame g : juegos) gameSelector.getItems().add(g.getTitle());

        grid.add(new Label("Juego:"), 0, 0);
        grid.add(gameSelector, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Result handling: execute search via service layer if the user confirms selection
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && gameSelector.getValue() != null) {
                VideoGame game = service.searchByTitle(gameSelector.getValue());
                showAlert(Alert.AlertType.INFORMATION, "Detalles del Juego", game.toString());
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void searchByPlatform() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Buscar por Plataforma");
        dialog.setHeaderText("Seleccione la plataforma:");


        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        // Get unique platforms using Streams
        List<String> plataformas = service.getAllVideoGames().stream()
                .map(VideoGame::getPlatform)
                .distinct()
                .toList();

        ComboBox<String> platformSelector = new ComboBox<>();
        platformSelector.getItems().addAll(plataformas);

        grid.add(new Label("Plataforma:"), 0, 0);
        grid.add(platformSelector, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && platformSelector.getValue() != null) {
                List<VideoGame> juegos = service.searchByPlatform(platformSelector.getValue());
                StringBuilder sb = new StringBuilder("Juegos en " + platformSelector.getValue() + ":\n\n");
                for (VideoGame g : juegos) sb.append("- ").append(g.getTitle()).append("\n");
                showAlert(Alert.AlertType.INFORMATION, "Resultados", sb.toString());
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void performSale() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Realizar Venta");
        dialog.setHeaderText("Seleccione el juego y la cantidad");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Retrieve existing games from the service
        List<VideoGame> juegos = service.getAllVideoGames();
        ComboBox<String> gameSelector = new ComboBox<>();

        for (VideoGame g : juegos) {
            gameSelector.getItems().add(g.getTitle());
        }

        TextField qtyField = new TextField();
        qtyField.setPromptText("Cantidad");

        grid.add(new Label("Seleccionar juego:"), 0, 0);
        grid.add(gameSelector, 1, 0);
        grid.add(new Label("Cantidad:"), 0, 1);
        grid.add(qtyField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String title = gameSelector.getValue(); // Retrieves the value from the drop-down
                    int qty = Integer.parseInt(qtyField.getText());

                    if (title == null) throw new Exception("Debe seleccionar un juego.");

                    Sale sale = service.sellVideoGame(title, qty);
                    showAlert(Alert.AlertType.INFORMATION, "Venta Exitosa", "Venta registrada:\n" + sale.toString());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error en la Venta", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    //Display the data for the “Mostrar ventas” option

    private void showSales() {
        TableView<Sale> table = new TableView<>();

        TableColumn<Sale, String> colId = new TableColumn<>("ID Venta");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Sale, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Sale, Double> colPrice = new TableColumn<>("Precio");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Sale, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Sale, LocalDateTime> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));

        // Format the date so it is readable
        colDate.setCellFactory(column -> new TableCell<Sale, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });

        table.getColumns().addAll(colId, colTotal, colName, colPrice, colDate);
        table.getItems().addAll(service.getSalesHistory());

        showTableWindow("Historial de Ventas", table);
    }

    // UI Helper Method
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}