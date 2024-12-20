//
//package com.example.cafe;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.effect.GaussianBlur;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//
//public class Dashboardy {
//
//private Label customersLabel;
//    private Label incomeLabel;
//    private Label productsLabel;
//
//    // Variables to track metrics
//    private int totalCustomers = 0;
//    private double totalIncome = 0;
//    private int totalSoldProducts = 0;
//
//    public void dashboardScreen(Stage stage, String user) {
//        // Main Pane (Root StackPane)
//        StackPane mainPane = new StackPane();
//
//        // Secondary StackPane for background image
//        StackPane backgroundPane = new StackPane();
//        backgroundPane.setId("backgroundPane");
//        backgroundPane.setEffect(new GaussianBlur(10));
//
//        // Back button
//        Button backButtononLogin = new Button();
//        backButtononLogin.setAlignment(Pos.TOP_LEFT);
//        backButtononLogin.getStyleClass().add("backBtn");
//        backButtononLogin.setOnAction(e -> {
//            Menu menu = new Menu();
//            menu.menu(stage, user);
//        });
//
//        // Horizontal box for the containers
//        HBox containerBox = new HBox(20);
//        containerBox.setAlignment(Pos.CENTER);
//
//        // Add three containers for displaying metrics
//        VBox customersContainer = createContainer("Number of Customers", "0");
//        VBox incomeContainer = createContainer("Total Income", "$0.00");
//        VBox productsContainer = createContainer("Number of Sold Products", "0");
//
//        // Save references to labels for updates
//        customersLabel = (Label) customersContainer.getChildren().get(1);
//        incomeLabel = (Label) incomeContainer.getChildren().get(1);
//        productsLabel = (Label) productsContainer.getChildren().get(1);
//
//        containerBox.getChildren().addAll(customersContainer, incomeContainer, productsContainer);
//
//        // Add the background pane and content to the main pane
//        mainPane.getChildren().addAll(backgroundPane, containerBox, backButtononLogin);
//
//        // Set up the scene
//        Scene scene = new Scene(mainPane, 800, 500);
//        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
//        if (stylesheet != null) {
//            scene.getStylesheets().add(stylesheet.toExternalForm());
//        }
//
//        // Set up the stage
//        stage.setTitle("Cafe Management Dashboard");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private VBox createContainer(String labelText, String valueText) {
//        VBox container = new VBox(10);
//        container.setAlignment(Pos.CENTER);
//        container.getStyleClass().add("containerBox");
//
//        // Title Text
//        Text title = new Text(labelText);
//        title.getStyleClass().add("title");
//        title.getStyleClass().add("container-label");
//
//        // Value Label
//        Label valueLabel = new Label(valueText);
//        valueLabel.setStyle("-fx-font-size: 18px;");
//
//        container.getChildren().addAll(title, valueLabel);
//        return container;
//    }
//
//    // Methods to update metrics
//    public void updateTotalCustomers(int count) {
//        totalCustomers += count;
//        customersLabel.setText(String.valueOf(totalCustomers));
//    }
//
//    public void updateTotalIncome(double income) {
//        totalIncome += income;
//        incomeLabel.setText(String.format("$%.2f", totalIncome));
//    }
//
//    public void updateTotalSoldProducts(int count) {
//        totalSoldProducts += count;
//        productsLabel.setText(String.valueOf(totalSoldProducts));
//    }
//
//
//}
package com.example.cafe;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Dashboardy {
    private static final String DASHBOARD_FILE = "dashboard_data.txt";

    // Fields to hold metrics
    private double totalIncome = 0.0;
    private int totalCustomers = 0;
    private int totalSoldProducts = 0;
    private double finalIncome;
    private int finalCustomers;
    private int finalProducts;

    public void dashboardScreen(Stage stage, String user) {
        // Load data from file
        loadDashboardData();

        // VBox for layout
        VBox dashboardBox = new VBox(20);
        dashboardBox.setAlignment(Pos.CENTER);
        dashboardBox.setPadding(new Insets(20));
        dashboardBox.setId("dashboardBox");

        // Text for metrics
        Text incomeText = new Text("Total Income: $" + String.format("%.2f", totalIncome));
        incomeText.setId("income-text");

        Text customersText = new Text("Total Customers: " + totalCustomers);
        customersText.setId("customers-text");

        Text soldProductsText = new Text("Total Sold Products: " + totalSoldProducts);
        soldProductsText.setId("sold-products-text");

        // Back to Menu button
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("button1");
        backButton.setOnAction(e -> {
            Menu menu = new Menu();
            menu.menu(stage, user);
        });

        // Add components to the VBox
        dashboardBox.getChildren().addAll(incomeText, customersText, soldProductsText, backButton);

        // Scene setup
        Scene dashboardScene = new Scene(dashboardBox, 400, 300);

        // Add CSS
        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
        if (stylesheet != null) {
            dashboardScene.getStylesheets().add(stylesheet.toExternalForm());
        }

        // Set and show the stage
        stage.setScene(dashboardScene);
        stage.setTitle("Dashboard");
        stage.show();
    }

    // Method to load data from the file
    private void loadDashboardData() {
        File file = new File(DASHBOARD_FILE);
        if (!file.exists()) {
            saveDashboardData(); // Create the file with initial values if not exists
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {

                    if ("TotalIncome".equals(parts[0])) {
                        totalIncome = finalIncome(Double.parseDouble(parts[1]));
                    } else if ("TotalCustomers".equals(parts[0])) {
                        totalCustomers = finalCustomers(Integer.parseInt(parts[1]));
                    } else if ("TotalSoldProducts".equals(parts[0])) {
                        totalSoldProducts = finalProducts(Integer.parseInt(parts[1]));
                    }
                    }


            }
        } catch (IOException e) {
            System.err.println("Error reading dashboard data: " + e.getMessage());
        }
    }

    // Method to save data to the file
    public void saveDashboardData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DASHBOARD_FILE,true))) {
            writer.write("TotalIncome:" + totalIncome + "\n");
            writer.write("TotalCustomers:" + totalCustomers + "\n");
            writer.write("TotalSoldProducts:" + totalSoldProducts + "\n");
        } catch (IOException e) {
            System.err.println("Error saving dashboard data: " + e.getMessage());
        }
    }

    // Methods to update metrics
    public void updateTotalIncome(double income) {
        totalIncome += income;
//        saveDashboardData();
    }

    public void updateTotalCustomers(int count) {
        totalCustomers += count;
//        saveDashboardData();
    }

    public void updateTotalSoldProducts(int count) {
        totalSoldProducts += count;
        saveDashboardData();
    }
    public double finalIncome(double income){
         finalIncome+=income;
         return finalIncome;
    }
    public Integer finalCustomers(int customers){
finalCustomers += customers;
return finalCustomers;
}
public Integer finalProducts(int products){
    finalProducts += products;
    return finalProducts;
}
    }
