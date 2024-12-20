package com.example.cafe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;


public class InventoryTableApp  {

InventorySystem i1 = new InventorySystem();

        public void inventory (Stage stage,String user) {
          final  String[] imagePath={null};

            StackPane root = new StackPane();
            root.setId("main-stackpane");

// Background Layer
            StackPane backgroundPane = new StackPane();
            backgroundPane.setId("background-layer");
            backgroundPane.setEffect(new GaussianBlur(10)); // Apply blur only to the background

// Inner StackPane for content
            StackPane contentPane = new StackPane();
            contentPane.setId("content-stackpane");

// Title
            Label titleLabel = new Label("List of Items");
            titleLabel.setId("title-label");
            StackPane.setAlignment(titleLabel, Pos.TOP_LEFT);

// Table
            TableView<Product> table = new TableView<>();
            table.setId("inventory-table");
            TableColumn<Product, String> idCol = new TableColumn<>("ID");
            TableColumn<Product, String> nameCol = new TableColumn<>("Name");
            TableColumn<Product, String> priceCol = new TableColumn<>("Price");
            table.getColumns().addAll(idCol, nameCol, priceCol);


// Form Fields
            TextField idField = new TextField();
            idField.setPromptText("Add ID");
            idField.getStyleClass().add("input-field");

            TextField nameField = new TextField();
            nameField.setPromptText("Add Name");
            nameField.getStyleClass().add("input-field");

            TextField priceField = new TextField();
            priceField.setPromptText("Add Price");
            priceField.getStyleClass().add("input-field");
            VBox imageBox = new VBox(10);
            ImageView imageView = new ImageView();
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.getStyleClass().add("image-preview");



            Button uploadButton = new Button("Upload Picture");
            uploadButton.getStyleClass().add("action-button");
            uploadButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    imageView.setImage(new Image(file.toURI().toString()));
                    imagePath[0] = file.getAbsolutePath(); // Full path of the file

                }
            });

            Button addButton = new Button("Add");
            addButton.getStyleClass().add("action-button");
            addButton.setOnAction(event -> {
                i1.reloadProducts(table);
                int id = Integer.parseInt(idField.getText());
                String name=nameField.getText();
                double price=Double.parseDouble(priceField.getText());
                i1.addProduct(id,name,price,imagePath[0]);
                Product product = new Product(id,name,price,imagePath[0]);
                try {
                    i1.addToTable(id,name,price,idCol,nameCol,priceCol,table,product);
idField.clear();
nameField.clear();
priceField.clear();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                Alert alert =new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Product");
                alert.setHeaderText(null);
                alert.setContentText("Product Added Succesfully");
                alert.showAndWait();
            });

            Button removeButton = new Button("Remove");
            removeButton.getStyleClass().add("action-button");
            removeButton.setOnAction(event -> {
                String name=nameField.getText();
                int id=Integer.parseInt(idField.getText());
                double price=Double.parseDouble(priceField.getText());
                i1.removeProduct(name);
                i1.removeFromTable(name);
                idField.clear();
                nameField.clear();
                priceField.clear();
            });

            Button updateButton = new Button("Update Price");
            updateButton.getStyleClass().add("action-button");
            updateButton.setOnAction(e->{
                String name=nameField.getText();
                Double price=Double.parseDouble(priceField.getText());
               i1.updatePrice(name,price);
               i1.updateFromTable(name,price,table);
                idField.clear();
                nameField.clear();
                priceField.clear();

            });
            Menu menu = new Menu();
            Button backButtononLogin = new Button();
            backButtononLogin.setAlignment(Pos.TOP_LEFT);
            backButtononLogin.getStyleClass().add("backBtn");
            backButtononLogin.setOnAction(e->{
                menu.menu(stage,user);
            });

// Image Upload Section
            imageBox.getChildren().addAll(imageView, uploadButton);

// Layout Organization
            VBox formBox = new VBox(10, idField, nameField, priceField, addButton, removeButton, updateButton);
            formBox.setId("form-container");

            HBox contentBox = new HBox(20, table, imageBox, formBox);
            contentBox.setId("content-container");

// Add content to inner StackPane
            contentPane.getChildren().addAll(contentBox);

// Add layers to root StackPane
            root.getChildren().addAll(backgroundPane, titleLabel, contentPane,backButtononLogin);

// CSS Styling
            Scene scene = new Scene(root, 800, 600);
            var stylesheet = getClass().getResource("/com/example/cafe/style.css");
            if (stylesheet != null) {
                scene.getStylesheets().add(stylesheet.toExternalForm());
            }

            stage.setScene(scene);
            stage.setTitle("Inventory Table");
            stage.show();

        }





    }


