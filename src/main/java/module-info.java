module com.example.halalsound {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.halalsound to javafx.fxml;
    exports com.example.halalsound;
}