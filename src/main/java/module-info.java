module com.example.halalsound {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.halalsound to javafx.fxml;
    exports com.example.halalsound;
}