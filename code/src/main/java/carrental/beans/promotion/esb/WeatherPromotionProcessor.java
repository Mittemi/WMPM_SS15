package carrental.beans.promotion.esb;

import carrental.model.promotion.Temperature;
import carrental.model.promotion.WeatherForecast;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thomas on 07.06.2015.
 */
public class WeatherPromotionProcessor implements Processor {

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MMM dd 'at' HH:mm");
    private static Date DATE_IN_ONE_WEEK = new Date(System.currentTimeMillis() + 1000*60*60*24*7);
    private static int NICE_WEATHER_THRESHOLD = 20;
    private static int RAIN_THRESHOLD = 5;
    /**
     * Analyzes the weather and is creating a promotion statement out of it.
     * (e.g. for being posted at Twitter)
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        WeatherForecast forecast = exchange.getIn().getBody(WeatherForecast.class);

        //compute average temperature for the next week
        double averageTemp = forecast.getDayWeatherList()
                .stream()
                .mapToDouble(d -> d.getTemperature().getDay())
                .average()
                .getAsDouble();

        //convert to celsius
        averageTemp = Temperature.fromKelvinToCelsius(averageTemp);

        //compute the average rain intensity
        double averageRain = forecast.getDayWeatherList()
                .stream()
                .mapToDouble(d -> d.getRain())
                .average()
                .getAsDouble();

        String tweet="";

        //bad weather
        if (averageTemp < NICE_WEATHER_THRESHOLD || averageRain > RAIN_THRESHOLD) {
            tweet = "The weather is not going to be that awesome next week. Just take a nice SUV from" +
                    " us and receive 25% off. \n" +
                    "valid till: "
                    + DATE_FORMATTER.format(DATE_IN_ONE_WEEK);
        }

        //nice weather
        if (averageTemp >= NICE_WEATHER_THRESHOLD && averageRain < RAIN_THRESHOLD) {
            tweet = "The weather is going to be awesome next week. Just take a nice convertible" +
                    " from us and receive 25% off. \n" +
                    "valid till: "
                    + DATE_FORMATTER.format(DATE_IN_ONE_WEEK);
        }

        exchange.getOut().setBody(tweet, String.class);
    }
}
