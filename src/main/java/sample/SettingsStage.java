package sample;

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
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import weather.WeatherInfoGetter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsStage extends Stage {
    int width = 500, height = 300;

    BorderPane borderPane = new BorderPane();
    StackPane logoPane = new StackPane();
    GridPane settingsPane = new GridPane();
    TextField city;
    RadioButton metric, imperial, standard;
    boolean correct_city = false;

    Image image = new Image("file:D:\\WeatherAlert\\src\\main\\resources\\logo.jpg");
    Scene scene = new Scene(borderPane, width, height);

    SettingsStage() {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height / 2.0);
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
                    correct_city = true;
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

        standard = new RadioButton("fahrenheit");
        standard.setToggleGroup(unitSelect);
        standard.setStyle("-fx-text-fill: gold;");

        HBox units = new HBox(metric, imperial, standard);
        units.setSpacing(10);

        settingsPane.add(units, 0, 1);

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

        this.setScene(scene);
        this.initStyle(StageStyle.DECORATED);
        this.setTitle("Weather Alert");
        this.getIcons().add(new Image("file:D:\\WeatherAlert\\src\\main\\resources\\icon.png"));
        this.show();
    }
    private boolean cityExist(String city) {
        try {
            WeatherInfoGetter wig = new WeatherInfoGetter(city, "metric");
            wig.connect();
            return wig.isConnected();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void save() throws IOException {
        if (!correct_city){
            Notifications.create()
                    .darkStyle()
                    .text("Please check if city correct")
                    .position(Pos.CENTER)
                    .hideAfter(new Duration(1500))
                    .show();
        }else {
            File settings = new File(System.getProperty("user.dir") + "\\"+"settings.txt");
            if (settings.exists()) {
                save_settings_to_file(settings);
            }else {
                if (settings.createNewFile()) {
                    save_settings_to_file(settings);
                } else {
                    Notifications.create()
                            .darkStyle()
                            .text("Problem with settings file")
                            .position(Pos.CENTER)
                            .hideAfter(new Duration(1500))
                            .show();
                }
            }
        }
    }

    private void save_settings_to_file(File settings) throws IOException {
        FileWriter writer = new FileWriter(settings);
        writer.write(city.getText());
        writer.write('\n');
        if (metric.isSelected()) {
            writer.write("metric");
        } else if (imperial.isSelected()) {
            writer.write("imperial");
        } else {
            writer.write("standard");
        }
        writer.close();
        this.close();
        new WeatherInfoStage();
    }

}
