package carrental.routing.promotion;

import carrental.beans.promotion.esb.StatisticsPromotionBean;
import carrental.model.reservation.Car;
import carrental.model.reservation.Reservation;
import carrental.repository.reservation.ReserveRepository;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Thomas on 06.06.2015.
 */
@Component
public class TwitterPromotionRoute extends RouteBuilder {

    private static int DELAY_5_MINUTES = 1000*60*5;
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MMM dd 'at' HH:mm");
    private static Date DATE_IN_ONE_WEEK = new Date(System.currentTimeMillis() + 1000*60*60*24*7);

    @Autowired
    private ReserveRepository reservationRepository;

    @Override
    public void configure() throws Exception {
       from("jpa:" + Reservation.class.getName()
               + "?consumer.query=select r from carrental.model.reservation.Reservation r" +
               "&consumeLockEntity=false&consumeDelete=false&consumer.delay=" + DELAY_5_MINUTES)
                .aggregate(constant(true), new CarListAggregationStrategy())
                .completionFromBatchConsumer()
                .process(new StatisticsPromotionBean())         //choosing a car for promotion
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd 'at' HH:mm");
                        Date dateInOneWeek = new Date(System.currentTimeMillis() + 1000*60*60*24*7);
                        Car car = exchange.getIn().getBody(Car.class);
                        String tweet = "Hi Carrental lovers! Receive 25% off if you rent the " + car
                                .getDescription() + "! See you at our store in Vienna! :)"
                                + "...valid till: "
                                + DATE_FORMATTER.format(DATE_IN_ONE_WEEK);
                        exchange.getOut().setBody(tweet, String.class);
                    }
                })
                .to("twitter://timeline/user?" +
                        "consumerKey=5GFIG4uvfukiJ09eVOxC0vR07" +
                        "&consumerSecret=SgbFjkDz250k5u6M1PH8RkuoGFS95pr0lgFrnRtUuoQqf5llx9" +
                        "&accessToken=3311764089-SM3VZUVKwWgqgIrQKqHzG6NPYCYU0xY51wPsPcI" +
                        "&accessTokenSecret=8mYqBWOBKrxwfL26izbvlie2vrYsWMfhPRh8OvLWpIlpa");
    }

    /**
     *  Partially taken from http://camel.apache.org/aggregator2
     */
    private static class CarListAggregationStrategy implements AggregationStrategy {

        /**
         * aggregates all cars out of selected reservation and saves it in the newExchange as List<Car>
         * @param oldExchange
         * @param newExchange
         * @return
         */
        @SuppressWarnings("unchecked")
        @Override
        public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
            final Reservation reservation = newExchange.getIn().getBody(Reservation.class);
            ArrayList<Car> list = null;
            if (oldExchange == null) {
                list = new ArrayList<Car>();
                if (reservation != null) {
                    list.add(reservation.getCar());
                }
                newExchange.getIn().setBody(list);
                return newExchange;
            } else {
                list = oldExchange.getIn().getBody(ArrayList.class);
                if (reservation != null) {
                    list.add(reservation.getCar());
                }
                return oldExchange;
            }
        }
    }


   /* @Override
    public void configure() throws Exception {
        from("timer:twitterTimer?period=10000").
                routeId("twitterSearchRoute").
                to("twitter://search?numberOfPages=1&delay=5&keywords=lewis hamilton" +
                        "&consumerKey=5GFIG4uvfukiJ09eVOxC0vR07" +
                        "&consumerSecret=SgbFjkDz250k5u6M1PH8RkuoGFS95pr0lgFrnRtUuoQqf5llx9" +
                        "&accessToken=3311764089-SM3VZUVKwWgqgIrQKqHzG6NPYCYU0xY51wPsPcI" +
                        "&accessTokenSecret=8mYqBWOBKrxwfL26izbvlie2vrYsWMfhPRh8OvLWpIlpa")
        .to("direct:twitterFilter");


        from("direct:twitterFilter").
                routeId("twitterIdempotentFilterRoute").
                split(body()).
                process(new Processor() { // set message header ID
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Status msg = exchange.getIn().getBody(Status.class);
                        exchange.getIn().setHeader("messageId", msg.getId());
                        exchange.setOut(exchange.getIn());
                    }
                }).
                idempotentConsumer(header("messageId"), MemoryIdempotentRepository.memoryIdempotentRepository(1000)).
                skipDuplicate(false).
                filter(property(Exchange.DUPLICATE_MESSAGE).isEqualTo(true)).
                log(LoggingLevel.DEBUG,"duplicate message:").
                to("log:duplicateTwitterMessages?level=DEBUG").stop().end().
                end();
    }*/


}
