package carrental.routing.pickupPoint;

import carrental.CarRentalConfig;
import carrental.Constants;
import carrental.beans.pickupPoint.PickupBean;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.Reservation;
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

        String mongoEndpointString = "mongodb:mongo?database=" + config.getPickupPoint().getMongo().getName() +"&collection=pickupProtocols&operation=save&writeResultAsHeader=true";

        RouteDefinition routeDefinition = from("direct:pickupPoint.PickupProtocol").log("Create pickup protocol for: ${body}")
                .bean(PickupBean.class, "showCar(carrental.model.pickupPoint.Reservation)")
                .to(mongoEndpointString);//.log("Pickup protocol created: ${body}");

        /*if(Constants.ENABLE_CAR_RETURN) {
            routeDefinition.to("seda:pickupPoint.CarReturn.internal");
        }
        else {
            routeDefinition.log("Process would return car: ${body}");
        }*/
    }
}
