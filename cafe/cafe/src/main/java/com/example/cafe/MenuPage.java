

package com.example.cafe;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPage {
    Menu  m5=new Menu();
    public static final String USER_FILE="customer.txt";
    private double totalIncome = 0; // Tracks total income
    private int totalSoldProducts = 0; // Tracks total sold products
    public Dashboardy dashboardy; // Reference to Dashboardy for updates

    public MenuPage(Dashboardy dashboardy) {
        this.dashboardy = dashboardy;
    }

    private void printReceipt(String customerNumber, double total, double amountPaid, double change) {
        // Create a StringBuilder for the receipt content
        StringBuilder receipt = new StringBuilder();

        receipt.append("********************************\n");
        receipt.append("          Cafe Receipt          \n");
        receipt.append("********************************\n\n");

        receipt.append("Customer Number: ").append(customerNumber).append("\n");
        receipt.append("--------------------------------\n");

        receipt.append(String.format("%-10s %-15s %-8s %-10s\n", "ID", "Name", "Price", "Qty"));
        receipt.append("--------------------------------\n");

        // Add each product's details to the receipt
        for (Product product : cart) {
            receipt.append(String.format("%-10s %-15s $%-7.2f %-10d\n",
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity()
            ));
        }

        receipt.append("--------------------------------\n");
        receipt.append(String.format("Total Bill:       $%.2f\n", total));
        receipt.append(String.format("Amount Paid:      $%.2f\n", amountPaid));
        receipt.append(String.format("Change:           $%.2f\n", change));
        receipt.append("********************************\n");
        receipt.append("       Thank You for Visiting!  \n");
        receipt.append("********************************\n");

        // Show the receipt in a TextArea inside an Alert dialog
        TextArea receiptArea = new TextArea(receipt.toString());
        receiptArea.setEditable(false);

        Alert receiptAlert = new Alert(Alert.AlertType.INFORMATION);
        receiptAlert.setTitle("Receipt");
        receiptAlert.setHeaderText("Customer Receipt");
        receiptAlert.getDialogPane().setContent(receiptArea);
        receiptAlert.getButtonTypes().clear();
        receiptAlert.getButtonTypes().add(ButtonType.CLOSE);
        receiptAlert.showAndWait();
    }

    // Observable list for managing cart items
    private final ObservableList<Product> cart = FXCollections.observableArrayList();
    List<Customer> c1 = new ArrayList<>();


    private final ObservableList<Customer> customerList = FXCollections.observableArrayList(); // New list for customers
    private final SimpleDoubleProperty totalPrice = new SimpleDoubleProperty(0.0); // Tracks total price

    public ObservableList<Customer> getcustomerList() {
        return customerList;
    }

    public void menuDisplay(Stage stage, String user, List<Product> p1) {
        TableView<Product> orderTable = new TableView<>();
        ;
        BorderPane borderPane = new BorderPane();
        borderPane.setId("mainback");

        // Menu Section
        VBox menuSection = new VBox(10);
        menuSection.setId("menu-section");
        menuSection.setPadding(new Insets(10));
        menuSection.setPrefHeight(600); // Set preferred height
        menuSection.setMaxHeight(Double.MAX_VALUE);
        menuSection.setAlignment(Pos.TOP_CENTER);

        Label menuLabel = new Label("Menu");
        menuLabel.setId("menu-label");

        // Menu items with scroll
        ScrollPane menuScrollPane = new ScrollPane();
        VBox menuItems = new VBox(10);
        menuItems.setAlignment(Pos.TOP_CENTER);

        for (Product product : p1) {
            VBox menuItem = new VBox(10);
            menuItem.setId("menu-item");
            menuItem.setAlignment(Pos.CENTER);
            menuItem.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 10;");

            // Product name label
            Label productName = new Label(product.getName());
            productName.getStyleClass().add("product-name");

            // Product price label
            Label productPrice = new Label("Price: $" + product.getPrice());
            productPrice.getStyleClass().add("product-price");

            // Product image
            String imagePath = product.getImagePath();
            ImageView productImage;
            try {
                File imageFile = new File(imagePath);
                productImage = new ImageView(new Image(imageFile.toURI().toString()));
                productImage.setId("imageMenu");
            } catch (Exception e) {
                System.out.println("Failed to load image: " + imagePath);
                productImage = new ImageView();
            }

            productImage.setFitWidth(80);
            productImage.setFitHeight(80);

            // Add to Order button
            Button addToOrder = new Button("Add to Order");
            addToOrder.getStyleClass().add("add-to-order");

            // Add action to "Add to Order" button
            addToOrder.setOnAction(event -> {
                addProductToCart(product, orderTable); // Add product to cart
                updateTotalPrice(); // Update total price
            });

            // Add elements to menuItem VBox
            menuItem.getChildren().addAll(productImage, productName, productPrice, addToOrder);

            // Add menuItem to menuItems VBox
            menuItems.getChildren().add(menuItem);
        }

        // Set menuItems VBox as content of ScrollPane
        menuScrollPane.setContent(menuItems);
        menuScrollPane.setFitToWidth(true);

        // Add menuScrollPane to menuSection
        menuSection.getChildren().addAll(menuLabel, menuScrollPane);

        // Order Table Section

        orderTable.setId("order-table");
        orderTable.setPlaceholder(new Label("No items in the order"));

        TableColumn<Product, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        orderTable.getColumns().addAll(productNameCol, quantityCol, priceCol);

        // Bind cart to the table
        orderTable.setItems(cart);


        // Payment Section
        VBox paymentSection = new VBox(10);
        paymentSection.setId("payment-section");
        paymentSection.setPadding(new Insets(10));
        paymentSection.setAlignment(Pos.CENTER_RIGHT);

        Label totalLabel = new Label("Total:");
        totalLabel.setId("total-label");

        TextField totalField = new TextField();
        totalField.setId("total-field");
        totalField.setEditable(false);
        totalField.textProperty().bind(totalPrice.asString("%.2f")); // Bind total price to the text field

        Label amountPaidLabel = new Label("Amount paid by customer:");
        amountPaidLabel.setId("amount-paid-label");

        TextField amountPaidField = new TextField();
        amountPaidField.setId("amount-paid-field");

        Label changeLabel = new Label("Change:");
        changeLabel.setId("change-label");

        TextField changeField = new TextField();
        changeField.setId("change-field");
        changeField.setEditable(false);
        Label customernum = new Label("Customer Number:");
        customernum.setId("customer-num");
        TextField customerNumber = new TextField();
        customerNumber.setId("customer-number");

        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("button1");

        // Remove button action
        removeButton.setOnAction(event -> {
            Product selectedProduct = orderTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                cart.remove(selectedProduct);
                updateTotalPrice();
            }
        });

        Button payButton = new Button("Pay");
        payButton.getStyleClass().add("button1");

        // Pay button action
        payButton.setOnAction(event -> {
            String customerNumber1 = customerNumber.getText();
            double total = totalPrice.getValue();
            double paid;
            try {
                paid = Double.parseDouble(amountPaidField.getText());
                if (paid >= total) {
                    double change = paid - total;
                    changeField.setText(String.format("%.2f", change));

                    c1.add(new Customer(customerNumber1));
                    writecustomer(c1);
                    // Update metrics
                    customerList.addAll(c1);
                    dashboardy.updateTotalIncome(total); // Update income
                    dashboardy.updateTotalCustomers(1); // Increment customers
                    dashboardy.updateTotalSoldProducts(cart.stream().mapToInt(Product::getQuantity).sum());
                    cart.clear(); // Clear the cart after payment
                    updateTotalPrice();
//
                    // Update sold products
                    // Clear the cart and reset UI
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Insufficient Payment!", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid payment amount!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button printReceiptButton = new Button("Print Bill Receipt");
        printReceiptButton.getStyleClass().add("button1");
        printReceiptButton.setOnAction(event -> {
            String customerNumber1 = customerNumber.getText();
            double total = totalPrice.get();
            double paid;
            paid = Double.parseDouble(amountPaidField.getText());
            if (paid >= total) {
                double change = paid - total;
                printReceipt(customerNumber1, total, paid, change);
            }


        });

        Button backbutton = new Button("Back");
        backbutton.getStyleClass().add("button1");
        backbutton.setOnAction(actionEvent -> {

            m5.menu(stage, user);

        });

        actionButtons.getChildren().addAll(removeButton, payButton, printReceiptButton, backbutton);
        paymentSection.getChildren().addAll(totalLabel, totalField, amountPaidLabel, amountPaidField, changeLabel, changeField, customernum, customerNumber, actionButtons);

        // Layout the sections
        borderPane.setLeft(menuSection);
        borderPane.setRight(orderTable);
        borderPane.setBottom(paymentSection);

        // Set widths dynamically
        menuSection.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.4));
        orderTable.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.6));

        // Create scene
        Scene scene4 = new Scene(borderPane, 1000, 600);
        stage.setScene(scene4);

        // Adding Stylesheet
        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
        if (stylesheet != null) {
            scene4.getStylesheets().add(stylesheet.toExternalForm());
            System.out.println("CSS loaded successfully");
        } else {
            System.out.println("CSS not found");
        }

        stage.setTitle("Cafe Management System");
        stage.show();
    }

    // Helper method to add a product to the cart
    private void addProductToCart(Product product, TableView<Product> orderTable) {
        for (Product p : cart) {
            if (p.getName().equals(product.getName())) {
                p.setQuantity(p.getQuantity() + 1);
                orderTable.refresh();// Increment quantity
                return;
            }
        }
        product.setQuantity(1); // Set initial quantity if not already in cart
        cart.add(product);
    }

    // Helper method to update the total price
    private void updateTotalPrice() {
        double total = 0;
        for (Product product : cart) {
            total += product.getPrice() * product.getQuantity();
        }
        totalIncome(total);
        totalPrice.set(total);
    }

    public void totalIncome(double total) {

        totalIncome = totalIncome + total;
    }
    public void writecustomer(List<Customer> customers) {

        try {
            BufferedWriter r1=new BufferedWriter(new FileWriter(USER_FILE,true));
           for(Customer customer : customers) {
               r1.write(customer.getId()+";"+customer.getNumber()+"\n");
           }
           r1.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Customer> readcustomer(List<Customer> customers) {
        customers.clear(); // Clear the list before loading
        int maxId = 0; // To track the highest ID value
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    String id = parts[0];
                    String number = parts[1];
                    // Add customer to the list
                    customers.add(new Customer(id, number));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Update the static counter in the Customer class
//        Customer.updateCounter(maxId);

        return customers;
    }

}
