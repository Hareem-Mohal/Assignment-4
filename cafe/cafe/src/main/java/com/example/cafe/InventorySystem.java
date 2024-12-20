package com.example.cafe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventorySystem {

    public static final String USER_FILE="inventory.txt";
    List<Product> products=new ArrayList<>();



    public List<Product> getProducts() {
        return products;
    }

    ObservableList<Product> observableProducts= FXCollections.observableArrayList();


    public void addProduct( int id,String name, double price,String imagePath){

        products.add(new Product(id,name,price,imagePath));

        saveInfoToFile();
    }


public void saveInfoToFile() {
    try {
        // Create a new ObjectOutputStream to overwrite the file
        ObjectOutputStream fileObject = new ObjectOutputStream(new FileOutputStream(USER_FILE));

        // Write the updated list of products to the file
        fileObject.writeObject(products);

        fileObject.close();
    } catch (IOException e) {
        throw new RuntimeException("Error saving to file", e);
    }
}


public void removeProduct(String name) {
    try {
        loadFromFile();  // Load products from the file

        // Find the product to remove
        Product productToRemove = null;
        for (Product p : products) {
            if (p.getName().equals(name)) {
                productToRemove = p;
                break;  // Exit the loop once the product is found
            }
        }

        // If the product is found, remove it
        if (productToRemove != null) {
            products.remove(productToRemove);  // Remove the product from the list

            // Display confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Product Deleted Successfully");
            alert.showAndWait();

            // Save the updated list to the file
            saveInfoToFile();
        } else {
            // If the product was not found, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Product not found");
            alert.showAndWait();
        }

    } catch (FileNotFoundException e) {
        throw new RuntimeException("File not found", e);
    }
}
    public void loadFromFile() throws FileNotFoundException {
        products.clear();

        ObjectInputStream fileObject= null;
        try {
            fileObject = new ObjectInputStream(new FileInputStream(USER_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            try {
                try {
                    products = (List<Product>) fileObject.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                fileObject.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public void reloadProducts(TableView<Product> table) {
        try {
            // Load products from the file
            loadFromFile();
            // Clear and update the observable list
            observableProducts.clear();
            observableProducts.addAll(products);

            // Update the table with the observable list
            table.setItems(observableProducts);

        } catch (Exception e) {
            throw new RuntimeException("Error reloading products", e);
        }
    }


    public void updatePrice(String name,double price) {
        try {
            loadFromFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Product productToUpdate=null;
        for(Product p2 : products) {
            if(p2.getName().equals(name)) {
                productToUpdate=p2;
            }
        }
        if(productToUpdate != null) {
            productToUpdate.setPrice(price);
            saveInfoToFile();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Product Updated Successfully");
            alert.showAndWait();
        }
       else{
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText(null);
           alert.setContentText("Product not found");
           alert.showAndWait();
        }


    }
    public void addToTable(int id, String name, double price, TableColumn<Product, String> idCol, TableColumn<Product,String> nameCol, TableColumn<Product, String> priceCol, TableView<Product> table,Product product) throws FileNotFoundException {

        observableProducts.add(product);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        table.setItems(observableProducts);
    }
    public void removeFromTable(String name) {
        for(Product o:observableProducts){
            if(o.getName().equals(name)) {
                observableProducts.remove(o);
                break;
            }
        }
    }
    public void updateFromTable(String name,double price, TableView<Product> table) {
        for(Product o:observableProducts){
            if(o.getName().equals(name)) {
                o.setPrice(price);
                break;
            }

        }
        table.refresh();
    }


}
