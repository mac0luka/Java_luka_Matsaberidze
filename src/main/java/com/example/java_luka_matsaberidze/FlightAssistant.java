package com.example.java_luka_matsaberidze;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class FlightAssistant extends Application {
   private Text headerText;
    private DatePicker datePicker ;
    private TextField sourceField ;
    private TextField destinationField ;
    private TextField passengerCountField;
    private TextField priceField ;
    private Button validatorButton;

    private Button pieChartButton;
    private Map<String,Integer> sourceCitiesMap = new HashMap<>();
    @Override
    public void start(Stage stage) throws IOException {

        //can be ignored after first initialization
        JDBCUtils.createTable();
        stage.setTitle("flights assistant");
        GridPane gridPane = CreateGrid();

        AddGridUiElements(gridPane);
        SetButtonActions();
        // create a scene
        Scene sc = new Scene(gridPane, 800 , 500);

        // set the scene
        stage.setScene(sc);

        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

    public void Launch()
    {
        launch();
    }

    private GridPane CreateGrid()
    {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);


        gridPane.setPadding(new Insets(40, 40, 40, 40));

        gridPane.setHgap(10);

        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gridPane;
    }

    public void SetButtonActions(){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        validatorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(ValidateButtonPress())
                {
                    SendDataToDatabase(sourceField.getText(),destinationField.getText(),Double.parseDouble(priceField.getText()),Integer.parseInt(passengerCountField.getText()),
                            datePicker.getValue());
                }
                else
                {
                    alert.show();
                    alert.setTitle("invalid data");
                    alert.setHeaderText("missing required fields");
                    alert.setContentText("please fill all fields");
                }
            }
        });

        pieChartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GetCities();
                if(sourceCitiesMap.size()<=0)
                {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.show();
                    alert1.setTitle("not enough data");
                    alert1.setHeaderText("not enough data to show pie chart");
                    alert1.setContentText("input at least 1 data");
                }
                else
                {
                    ShowPieChart();
                }
            }
        });
    }
    private void AddGridUiElements(GridPane gridPane)
    {
         headerText = new Text("Flight Assistant");
        headerText.setStyle(".Bold" +
                "-fx-alignment: right;" +
                "-fx-font-size: 15px;");
         datePicker = new DatePicker();

        datePicker.getEditor().setDisable(true);
        datePicker.setShowWeekNumbers(true);
         sourceField = new TextField();
         destinationField = new TextField();
         passengerCountField= new TextField();
         priceField = new TextField();
         validatorButton = new Button("send to database");
        pieChartButton = new Button("show pie chart");

        priceField.setPrefHeight(40);
        passengerCountField.setPrefHeight(40);
        destinationField.setPrefHeight(40);
        sourceField.setPrefHeight(40);
        datePicker.setPrefHeight(40);

        passengerCountField.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        }));


        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });

        priceField.setTextFormatter(formatter);


        datePicker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if((LocalDate)datePicker.getValue()==null)
                {

                }
                else {
                    ;
                }
            }
        });
        Label dateLabel = new Label("date: ");
        Label sourceLabel = new Label("flight source: ");
        Label destinationLabel = new Label(" flight destination: ");
        Label passengerCountLabel = new Label("passenger count: ");
        Label priceLabel = new Label("flight price: ");
        // add button and label

        gridPane.add(headerText,0,0);
        gridPane.add(dateLabel,0,1);
        gridPane.add(datePicker,1,1);
        gridPane.add(sourceLabel,0,2);
        gridPane.add(sourceField,1,2);
        gridPane.add(destinationLabel,0,3);
        gridPane.add(destinationField,1,3);
        gridPane.add(passengerCountLabel,0,4);
        gridPane.add(passengerCountField,1,4);
        gridPane.add(priceLabel,0,5);
        gridPane.add(priceField,1,5);
        gridPane.add(validatorButton,1,6);
        gridPane.add(pieChartButton,1,7);
    }

    private boolean ValidateButtonPress()
    {
        if(datePicker.getValue()==null)
        {
            return false;
        }
        if(sourceField.getText().isEmpty() || destinationField.getText().isEmpty() || priceField.getText().isEmpty() || passengerCountField.getText().isEmpty())
        {
        return false;
        }
        return true;
    }
    public void SendDataToDatabase(String source,String destination, double price, int passengersCount, LocalDate localDate)
    {
        FlightData flightData = new FlightData(source,destination,passengersCount,localDate,price);
        JDBCUtils.InsertDataToDatabase(flightData);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirmation");
        alert.setHeaderText("Data sent to database");
        alert.setContentText("you may send another data");

        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent()) {
            ClearFields();
        }
        else if(result.get() == ButtonType.OK) {
            ClearFields();
        }
        else if(result.get() == ButtonType.CANCEL) {
            ClearFields();
        }
    }

    private  void ClearFields()
    {
        sourceField.clear();
        destinationField.clear();
        priceField.clear();
        datePicker.setValue(null);
        passengerCountField.clear();
    }

    public void GetCities()
    {
        sourceCitiesMap.clear();
        List<String> sourceCitiesList = JDBCUtils.GetSourceCities();
        System.out.println(sourceCitiesList.size());
        for(int i=0; i < sourceCitiesList.size();i++){
            if(sourceCitiesMap.containsKey(sourceCitiesList.get(i)))
            {
                sourceCitiesMap.put(sourceCitiesList.get(i),sourceCitiesMap.get(sourceCitiesList.get(i))+1);
            }
            else
            {
                sourceCitiesMap.put(sourceCitiesList.get(i),1);
            }
        }
    }

    private void ShowPieChart()
    {
        Scene scene = new Scene(new Group());
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String,Integer> entry : sourceCitiesMap.entrySet())
        {
            pieChartData.add(new PieChart.Data(entry.getKey(),entry.getValue()));
        }

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Flights source Cities");

        ((Group) scene.getRoot()).getChildren().add(chart);
        Stage addStage = new Stage();
        addStage.setTitle("Flights");
        addStage.setScene(scene);
        addStage.show();
    }
}