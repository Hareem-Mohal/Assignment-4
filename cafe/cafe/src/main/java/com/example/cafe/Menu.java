package com.example.cafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    List<Customer> c2=new ArrayList<>();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();



    InventoryTableApp i1= new InventoryTableApp();
    InventorySystem i2= new InventorySystem();
    List<Product> p;

    public void menu(Stage stage, String user) {
        // StackPane for background and effects
        Dashboardy d1= new Dashboardy();
        MenuPage m1 = new MenuPage(d1);
        StackPane menupic = new StackPane();
        menupic.setId("menupic");
        menupic.setEffect(new GaussianBlur(10));

        // Text displaying the welcome message
        Text menuUsername = new Text("Welcome  " + user);
        menuUsername.getStyleClass().add("Text");  // Use this ID for styling via CSS

        // Buttons
        Button dashboard = new Button("Dashboard");
        dashboard.getStyleClass().add("button1");
        dashboard.setStyle("dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 2, 2);");
        dashboard.setOnAction(e->{

            d1.dashboardScreen(stage,user);
        });
        Button inventory = new Button("Inventory");
        inventory.getStyleClass().add("button1");
        inventory.setOnAction(event -> {

            i1.inventory(stage,user);
        });
        Button Menubtn = new Button("Menu");
        Menubtn.getStyleClass().add("button1");
        Menubtn.setOnAction(event -> {
            try {
                i2.loadFromFile();
                p=i2.getProducts();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            m1.menuDisplay(stage,user,p);

        });

        Button customers = new Button("Customers");
        customers.getStyleClass().add("button1");
        customers.setOnAction(event -> {
            CustomerPage c1= new CustomerPage();
            m1.readcustomer(c2);
            customerList.addAll(c2);
            c1.customerPage(stage,user,customerList);

        });
        CafeManagementSystem cms = new CafeManagementSystem();
        Button backButtononLogin = new Button();
        backButtononLogin.getStyleClass().add("backBtn");
        backButtononLogin.setAlignment(Pos.TOP_LEFT);
        backButtononLogin.setOnAction(e->{
            cms.loginScreen(stage);
        });

        // VBox for buttons and text
        VBox menuVbox = new VBox();
        menuVbox.setAlignment(Pos.CENTER);
        menuVbox.setId("menuVbox");
        menuVbox.getChildren().addAll(menuUsername, dashboard, inventory,Menubtn, customers);

        // StackPane for the main menu content
        StackPane mainMenuPane = new StackPane();
        mainMenuPane.getChildren().addAll(menupic, menuVbox,backButtononLogin);

        // Scene setup
        Scene mainMenuScene = new Scene(mainMenuPane, 300, 300);

        // Load the stylesheet
        var stylesheet = getClass().getResource("/com/example/cafe/style.css");
        if (stylesheet != null) {
            mainMenuScene.getStylesheets().add(stylesheet.toExternalForm());
        }

        // Set and show the stage
        stage.setScene(mainMenuScene);
        stage.show();
    }
}
