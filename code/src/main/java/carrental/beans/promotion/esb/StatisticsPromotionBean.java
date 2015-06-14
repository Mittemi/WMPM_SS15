package carrental.beans.promotion.esb;

import carrental.model.reservation.Car;
import carrental.model.reservation.Reservation;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 06.06.2015.
 */
public class StatisticsPromotionBean implements Processor {

    /**
     * determines the least use car
     * @param exchange
     */
    @Override
    public void process(Exchange exchange) {
        List<Car> usedCars = exchange.getIn().getBody(List.class);
        Map.Entry<Car, Long> leastUsedCarEntry = usedCars
                .stream()
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet()
                .stream()
                .min(Comparator.comparing(item -> item.getValue()))
                .get();
        System.out.println("Promotion: Car which is used the least of all: " + leastUsedCarEntry.getKey()
                + ", only reserved: " + leastUsedCarEntry.getValue() +" time(s).");

        //set least used car for further promotion activities
        exchange.getOut().setBody(leastUsedCarEntry.getKey(), Car.class);
    }
}
