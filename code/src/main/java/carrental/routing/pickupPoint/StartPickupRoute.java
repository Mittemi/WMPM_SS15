package carrental.routing.pickupPoint;

import carrental.CarRentalConfig;
import carrental.Constants;
import carrental.beans.pickupPoint.PickupBean;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.Reservation;
import carrental.model.reservation.CarState;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Michael on 19.05.2015.
 */
@Component
/**
 * Takes care about the protocol creation. Stores the protocol inside the mongodb
 *
 * INPUT:
 *  - direct:pickupPoint.PickupProtocol (expects: pickupPoint.Reservation
 * OUTPUT:
 *  - seda:pickupPoint.CarReturn.internal (reservation.Reservation.id)
 */
public class StartPickupRoute extends RouteBuilder {

    @Autowired
    private CarRentalConfig config;


    @Override
    public void configure() throws Exception {

        String mongoEndpointString = "mongodb:mongo?database=" + config.getPickupPoint().getMongo().getName() + "&collection=pickupProtocols&operation=save&writeResultAsHeader=true";

        // Application -> ESB
        from("activemq:queue:createPickupProtocolDoneQueue").process(p -> {
            System.out.println("ESB (PP): PickupProtocol started");
        }).to("direct:pickupPoint.PickupProtocol.created");

        // ESB -> Application
        from("direct:pickupPoint.PickupProtocol").process(p -> {
            System.out.println("ESB (PP): Call PickupProtocol application for:" + p.getIn().getBody());
        }).to("activemq:queue:createPickupProtocolQueue");

        // protocol created, cancel or queue for car return, webservice call
        from("direct:pickupPoint.PickupProtocol.created").wireTap(mongoEndpointString).
                choice().when(simple("${body.isCanceledPickup}"))
                .choice().when(constant(Constants.ENABLE_CANCEL_PICKUP))
                .to("seda:pickupPoint.cancel").otherwise().log("Set Constants.ENABLE_CANCEL_PICKUP to true!")
                .endChoice()
                .otherwise().choice().when(constant(Constants.ENABLE_CAR_RETURN)).to("direct:StartPickupRoute.internal")
                .otherwise().log("Set Constants.ENABLE_CAR_RETURN to true!")
                .endChoice();

        // queue for car return
        from("direct:StartPickupRoute.internal").wireTap("direct:pickupPoint.callAvailability.inuse")
                .setHeader("carId", simple("${body.getReservation().getCarId()}")).to("activemq:queue:returnPoint.expectedReturns");

        // car in use
        from("direct:pickupPoint.callAvailability.inuse")
                .process((p) -> {
                    System.out.println("ESB (PP): Set car as in use");
                }).setHeader("parameters", simple("?licensePlate=${body.getReservation().getLicensePlateUrl()}&state=INUSE")
        ).to("restlet:" + config.getReservation().getAvailabilityUrl() + "{parameters}");
    }
}
