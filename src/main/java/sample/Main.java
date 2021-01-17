package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application {


    @Override
    public void start(Stage stage) {
        if (is_settings_exist()) new WeatherInfoStage();
        else new SettingsStage();
    }
    boolean is_settings_exist(){
        File file = new File(System.getProperty("user.dir") + "\\"+"settings.txt");
        return file.exists();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
