import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import weather.WeatherInfoGetter;

import java.io.IOException;

import static org.junit.Assert.*;

public class WeatherGeterTest {
    WeatherInfoGetter weatherInfoGetter;
    @BeforeAll
    public void init() throws IOException {
         weatherInfoGetter = new WeatherInfoGetter("bialystok","metric");
    }

    @Test
    public void baseTest() {
        String desc = weatherInfoGetter.getWeather_desc();
        assertTrue(desc.length() > 0);
    }
}
