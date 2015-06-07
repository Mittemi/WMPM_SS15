package carrental.routing.promotion;

import carrental.beans.promotion.esb.WeatherPromotionProcessor;
import carrental.model.promotion.Temperature;
import carrental.model.promotion.WeatherForecast;
import com.google.gson.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.DoubleSummaryStatistics;

/**
 * Created by Thomas on 07.06.2015.
 */
@Component
public class WeatherPromotionRoute extends RouteBuilder {

    private static int DELAY_5_MINUTES = 1000*60*5;


    @Override
    public void configure() {
        from("weather:foo?location=Vienna,Austria&period=7 days" +
                "&consumer.delay="+DELAY_5_MINUTES)
                .unmarshal()
                .json(JsonLibrary.Gson, WeatherForecast.class)
                .process(new WeatherPromotionProcessor())
        .to("twitter://timeline/user?" +
                "consumerKey=5GFIG4uvfukiJ09eVOxC0vR07" +
                "&consumerSecret=SgbFjkDz250k5u6M1PH8RkuoGFS95pr0lgFrnRtUuoQqf5llx9" +
                "&accessToken=3311764089-SM3VZUVKwWgqgIrQKqHzG6NPYCYU0xY51wPsPcI" +
                "&accessTokenSecret=8mYqBWOBKrxwfL26izbvlie2vrYsWMfhPRh8OvLWpIlpa");;
    }
}
