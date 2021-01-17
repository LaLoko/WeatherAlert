package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import weather.WeatherInfoGetter;

import javafx.scene.control.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WeatherInfoStage extends Stage {
    String city;
    String units;

    String weather_desc;
    double temp;
    WeatherInfoGetter weatherInfoGetter;
    BorderPane borderPane = new BorderPane();
    GridPane gridPane = new GridPane();
    StackPane logoPane = new StackPane();
    Image image = new Image("file:D:\\WeatherAlert\\src\\main\\resources\\logo.jpg");
    Image icon = new Image("file:D:\\WeatherAlert\\src\\main\\resources\\icon.png");

    int width = 500, height = 300;
    Scene secScene = new Scene(borderPane, width, height);


    WeatherInfoStage(){
        try {
            load_settings();
            weatherInfoGetter = new WeatherInfoGetter(city,units);
            if (weatherInfoGetter.date_check()){
                get_weather_Info();
            }else {
                read_last_data();
            }
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height / 2.0);
            logoPane.getChildren().add(imageView);


            TextField cityText = new TextField(city);
            cityText.setEditable(false);
            gridPane.add(cityText,0,0);
            TextField tempText = new TextField("temperature : "+temp);
            tempText.setEditable(false);
            gridPane.add(tempText,0,1);
            TextField descText = new TextField(weather_desc);
            descText.setEditable(false);
            gridPane.add(descText,0,2);

            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            borderPane.setStyle("-fx-background-color: #000F;");
            borderPane.setTop(logoPane);
            borderPane.setCenter(gridPane);

            this.setScene(secScene);
            this.setTitle("Weather Alert");
            this.getIcons().add(icon);
            this.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void load_settings() throws FileNotFoundException {
        File settings = new File("settings.txt");
        if (settings.exists()) {
            Scanner scanner = new Scanner(settings);
            city = scanner.next();
            units = scanner.next();
        }
        else {
            Notifications.create()
                    .darkStyle()
                    .text("Can't find settings file")
                    .position(Pos.CENTER)
                    .hideAfter(new Duration(1500))
                    .show();
        }
    }
    void get_weather_Info() throws IOException {
        weatherInfoGetter.connect();
        weather_desc = weatherInfoGetter.getWeather_desc();
        temp = weatherInfoGetter.getFeels_temp();
        save_last_data(temp,weather_desc);
    }
    void save_last_data(double temp,String weather_desc) throws IOException {
        File weather_data = new File(System.getProperty("user.dir") + "\\"+ "weather_data.txt");
        FileWriter writer = new FileWriter(weather_data);
        if (!weather_data.exists()) {
            if (!weather_data.createNewFile()){
                Notifications.create()
                        .darkStyle()
                        .text("can't save today weather")
                        .position(Pos.CENTER)
                        .hideAfter(new Duration(1500))
                        .show();
            }
        }
        writer.write(String.valueOf(temp));
        writer.write('\n');
        writer.write(weather_desc);
        writer.close();
    }
    void read_last_data() throws IOException {
        File file = new File(System.getProperty("user.dir") + "\\"+ "weather_data.txt");
        if (file.exists() && file.length()>0){
            Scanner scanner = new Scanner(file);
            temp = Double.parseDouble(scanner.next());
            weather_desc = "";
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()){
                builder.append(scanner.next()).append(" ");
            }
            weather_desc = builder.toString();
            scanner.close();
        }else {
            get_weather_Info();
        }
    }
}
