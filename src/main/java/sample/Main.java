package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import weather.WeatherInfoGetter;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main extends Application {

    int width = 500, height = 300;

    BorderPane borderPane = new BorderPane();
    StackPane logoPane = new StackPane();
    GridPane settingsPane = new GridPane();
    TextField city;
    RadioButton metric,imperial;
    boolean corrext_city = false;

    Image image = new Image("file:D:\\WeatherAlert\\src\\main\\resources\\logo.jpg");
    Scene scene = new Scene(borderPane, width, height);


    @Override
    public void start(Stage stage) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height / 2);
        logoPane.getChildren().add(imageView);

        //city pane
        GridPane gridPane = new GridPane();
        city = new TextField();
        city.setPromptText("city");
        city.setFocusTraversable(false);
        Button cityButton = new Button("Confirm");

        gridPane.add(city, 0, 0);
        gridPane.add(cityButton, 1, 0);

        cityButton.setOnAction(event -> {
            if (city.getText().equals("")){
                Notifications.create()
                        .darkStyle()
                        .text("Please enter text")
                        .position(Pos.CENTER)
                        .hideAfter(new Duration(1500))
                        .show();

            }else{
                if (cityExist(city.getText())) {
                    Notifications.create()
                            .darkStyle()
                            .text("Correct City")
                            .position(Pos.CENTER)
                            .hideAfter(new Duration(1500))
                            .show();
                    corrext_city = true;
                }else {
                    Notifications.create()
                            .darkStyle()
                            .text("Can't find this city")
                            .position(Pos.CENTER)
                            .hideAfter(new Duration(1500))
                            .show();
                }
            }
        });
        settingsPane.add(gridPane,0,0);
        //units select
        ToggleGroup unitSelect = new ToggleGroup();

        metric = new RadioButton("metric");
        metric.setStyle("-fx-text-fill: gold;");
        metric.setToggleGroup(unitSelect);
        metric.setSelected(true);

        imperial = new RadioButton("imperial");
        imperial.setToggleGroup(unitSelect);
        imperial.setStyle("-fx-text-fill: gold;");

        HBox units = new HBox(metric, imperial);
        units.setSpacing(10);

        settingsPane.add(units,0,1);

        Button saveSettings = new Button("save settings");

        saveSettings.setOnAction(event -> {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        settingsPane.add(saveSettings,0,2);

        settingsPane.setAlignment(Pos.CENTER);
        settingsPane.setHgap(10);
        settingsPane.setVgap(10);


        borderPane.setStyle("-fx-background-color: #000F;");

        borderPane.setTop(logoPane);
        borderPane.setCenter(settingsPane);

        stage.setTitle("Weather Alert");
        stage.getIcons().add(new Image("file:icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    private boolean cityExist(String city) {
        try {
            WeatherInfoGetter wig = new WeatherInfoGetter(city, "metirc");
            wig.connect();
            return wig.isConnected();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void save() throws IOException {
        if (!corrext_city){
            Notifications.create()
                    .darkStyle()
                    .text("Please check if city correct")
                    .position(Pos.CENTER)
                    .hideAfter(new Duration(1500))
                    .show();
        }else {
            File settings = new File(System.getProperty("user.dir") + "\\"+"settings.txt");
            if (settings.createNewFile()) {
                FileWriter writer = new FileWriter(settings);
                writer.write(city.getText());
                if (metric.isSelected()){
                    writer.write("metric");
                }else {
                    writer.write("imperial");
                }
                writer.close();

            }else {
                Notifications.create()
                        .darkStyle()
                        .text("Problem with settings file")
                        .position(Pos.CENTER)
                        .hideAfter(new Duration(1500))
                        .show();
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
