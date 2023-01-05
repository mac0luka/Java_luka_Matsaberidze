module com.example.java_luka_matsaberidze {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.java_luka_matsaberidze to javafx.fxml;
    exports com.example.java_luka_matsaberidze;
}