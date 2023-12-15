module com.example.matrixcalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;


    opens com.example.matrixcalculator to javafx.fxml;
    exports com.example.matrixcalculator;
}