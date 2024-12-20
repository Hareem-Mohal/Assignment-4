module com.example.cafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.cafe to javafx.fxml;
    exports com.example.cafe;
}