module com.example.java_luka_matsaberidze {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.java_luka_matsaberidze to javafx.fxml;
    exports com.example.java_luka_matsaberidze;
}