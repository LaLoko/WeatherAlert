import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import sample.Main;
import weather.WeatherInfoGetter;

import java.io.IOException;

import static org.junit.Assert.*;

public class WeatherGeterTest {
    static WeatherInfoGetter weatherInfoGetter;

    @BeforeClass
    public static void init() throws IOException {
        weatherInfoGetter = new WeatherInfoGetter("warsaw", "metric");
    }

    @Test
    public void correnctCityTest() throws IOException {
        WeatherInfoGetter pom = new WeatherInfoGetter("ddc", "metric");
        pom.connect();
        assertFalse(pom.isConnected());
    }

    @Test
    public void defaultUnitsTest() throws IOException {
        WeatherInfoGetter pom1 = new WeatherInfoGetter("London", "mskdm");
        WeatherInfoGetter pom2 = new WeatherInfoGetter("London", "standard");

        pom1.connect();
        pom2.connect();

        assertEquals(pom1.getFeels_temp(), pom2.getFeels_temp(), 0.0);
    }

    @Test
    public void connectionTest() {
        weatherInfoGetter.connect();
        assertTrue(weatherInfoGetter.isConnected());
    }

    @Test
    public void descGetTest() {
        weatherInfoGetter.connect();
        String desc = weatherInfoGetter.getWeather_desc();
        assertTrue(desc.length() > 0);
    }

    @Test
    public void tempGetTest() {
        weatherInfoGetter.connect();
        double temp = weatherInfoGetter.getFeels_temp();
        assertTrue(temp > -30 && temp < 50);
    }

    @Test
    public void MainInitTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                new Main().start(new Stage());
            });
        });
        thread.start();
        Thread.sleep(2000);
    }

}
