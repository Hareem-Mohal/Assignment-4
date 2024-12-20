package com.example.cafe;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CustomerPage {

    public void customerPage(Stage stage,String user,ObservableList<Customer> customers) {


        // Create the main StackPane
        StackPane mainPane = new StackPane();

        // StackPane 1: Background Image
        StackPane backgroundStack = new StackPane();
        backgroundStack.setPrefSize(1000, 600);  // Set preferred size of the background pane
        // Set the background image (CSS will handle this)
        backgroundStack.setId("backgroundStack");
        backgroundStack.setEffect(new GaussianBlur(10));

        // StackPane 2: Content (TableView and other UI components)
        StackPane contentStack = new StackPane();
//        contentStack.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);"); // Transparent white background
        contentStack.setPrefSize(1000, 600);

        // TableView for Customer Information
        TableView<Customer> customerTable = new TableView<>();
        customerTable.setId("customerTable");

        // Define columns for the table
        TableColumn<Customer, String> customerIdColumn = new TableColumn<>("Customer ID");
        customerIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        TableColumn<Customer, String> customerNumberColumn = new TableColumn<>("Customer Number");
        customerNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumber()));

        // Add columns to the table
        customerTable.getColumns().addAll(customerIdColumn, customerNumberColumn);

        // Add customer list to the table
        customerTable.setItems(customers);
        customerTable.refresh();
        // Add table to the content stack
        contentStack.getChildren().add(customerTable);
        Button backButtononLogin = new Button();
        backButtononLogin.setAlignment(Pos.TOP_LEFT);
        backButtononLogin.getStyleClass().add("backBtn");
        backButtononLogin.setOnAction(e->{
            Menu menu = new Menu();
            menu.menu(stage,user);
        });
        // Add the background and content stacks to the main pane
        mainPane.getChildren().addAll(backgroundStack, contentStack,backButtononLogin);

        // Create scene and set it to the stage
        Scene scene = new Scene(mainPane, 1000, 600);

        // Apply CSS styles to the scene
        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
            System.out.println("CSS loaded successfully");
        } else {
            System.out.println("CSS not found");
        }

        stage.setTitle("Customer Page");
        stage.setScene(scene);
        stage.show();
    }
}