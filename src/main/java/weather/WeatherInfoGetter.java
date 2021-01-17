package weather;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.concurrent.CompletionException;

public class WeatherInfoGetter {
    private static String weather_desc;
    private static double feels_temp;
    private static boolean connected;
    String key = "79fea1f9005921f7f52b5d323afaf85d";
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]")
    String city;
    @NotBlank
    @Pattern(regexp = "metric|imperial")
    String units;
    URI uri;
    String last_connection_file = "conn_date.txt";

    public WeatherInfoGetter(String city, String units) throws IOException {
        this.city = city;
        this.units = units;
        uri = URI.create("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=" + units);
    }

    public boolean isConnected() {
        return connected;
    }

    public String getWeather_desc() {
        return weather_desc;
    }

    public double getFeels_temp() {
        return feels_temp;
    }

    public boolean date_check() throws IOException {
        String connection_path = System.getProperty("user.dir") + "\\";
        File file = new File(connection_path + last_connection_file);
        if (file.createNewFile()) {
            if (file.length() > 0) {
                Scanner scanner = new Scanner(file);
                LocalDate last_date = LocalDate.parse(scanner.next());

                if (ChronoUnit.DAYS.between(last_date, LocalDate.now()) > 0) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(String.valueOf(LocalDate.now()));
                    writer.close();
                    return true;
                } else return false;
            } else {
                FileWriter writer = new FileWriter(file);
                writer.write(String.valueOf(LocalDate.now()));
                writer.close();
                return true;
            }
        }else {
            return false;
        }
    }

    public void connect() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(weather.WeatherInfoGetter::parse)
                .join();
    }

    static String parse(String responseBody) {
        try {
            responseBody = "[" + responseBody + "]";
            JSONArray jsonArray = new JSONArray(responseBody);
            JSONObject object = jsonArray.getJSONObject(0);

            if (object.length() > 2) {
                JSONObject weather = object.getJSONArray("weather").getJSONObject(0);
                JSONObject main = object.getJSONObject("main");

                weather_desc = weather.getString("description");
                feels_temp = main.getDouble("feels_like");
                connected = true;
            }else {
                connected = false;
            }
        } catch (CompletionException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
