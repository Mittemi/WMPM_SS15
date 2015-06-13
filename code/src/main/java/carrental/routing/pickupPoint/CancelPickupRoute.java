package carrental.routing.pickupPoint;

import carrental.CarRentalConfig;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.reservation.CarState;
import com.sun.javafx.fxml.builder.URLBuilder;
import com.sun.jndi.toolkit.url.UrlUtil;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.sys.process.*;
import scala.sys.process.ProcessBuilder;
import sun.net.util.URLUtil;

import java.net.URLEncoder;

/**
 * Created by Michael on 06.06.2015.
 */
@Component
public class CancelPickupRoute extends RouteBuilder {

    @Autowired
    private CarRentalConfig carRentalConfig;

    @Override
    public void configure() throws Exception {
        from("seda:pickupPoint.cancel").process((p) -> {
            System.out.println("ESB: CancelPickup for: " + p.getIn().getBody());
        }).choice().when(simple("${body.getClaims().size()} > 0")).to("direct:pickupPoint.callRepair").otherwise().to("direct:pickupPoint.callAvailability.available");

        from("direct:pickupPoint.callRepair").process((p) -> {
            System.out.println("ESB: Send to repair center");
        }).setHeader("carId", simple("${body.getReservation().getCarId()}")).setHeader("claimsCnt+Protocol",simple("${body.getClaims().size()+1}")).setHeader("protocolType", constant(PickupProtocol.class.getName().toString())).to("seda:queue:claimFixCenter");

        from("direct:pickupPoint.callAvailability.available").process((p) -> {
            System.out.println("ESB: Set car as available");
        }).setHeader("parameters", simple("?licensePlate=${body.getReservation().getLicensePlateUrl()}&state=AVAILABLE"))
                .to("restlet:" + carRentalConfig.getReservation().getAvailabilityUrl() + "{parameters}");
    }
}
