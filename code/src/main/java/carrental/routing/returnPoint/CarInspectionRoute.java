package carrental.routing.returnPoint;

import carrental.beans.returnPoint.esb.*;
import carrental.model.pickupPoint.Claim;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Constantin and Michael on 12.05.2015.
 */
@Component
public class CarInspectionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("activemq:queue:esb.returnPoint.expectedReturns").bean(PickUpProtocolTranslator.class).to("activemq:queue:returnPoint.expectedReturns");

        from("activemq:queue:carToInspectQueue")
                .process(p -> {
                    System.out.println("ESB (RP): Return protocol partly finished, let's check for claims....");
                })
                .setHeader("carId", simple("${body.getReservation().getCarId()}"))
                .to("direct:returnPoint.mergeClaims");

        from("direct:returnPoint.mergeClaims").aggregate(header("carId"), new AggreationStrategyMergeClaims()).completionSize(2)
                .process(p -> {
                    System.out.println("ESB (RP): Return protocol finished, let's see how to proceed...");
                })
                .process(new ClaimsRouter()).recipientList(header("recipients"));

        // reads all the claims (csv) files from the file system parses them and forwards the extracted claims list to the inpectionqueue where it is merged using an aggregator into the return protocol
        from("file:claims/?noop=true").process(p -> {
            String fileName = (String)p.getIn().getHeader("CamelFileName");

            fileName = fileName.substring("claims_".length());

            fileName = fileName.substring(0, fileName.length() - 4);

            String[] ids = fileName.split("_");

            long reservationId = Long.parseLong(ids[1]);
            long carId = Long.parseLong(ids[0]);
            p.getIn().setHeader("carId", carId);
            p.getIn().setHeader("reservationId", reservationId);

            System.out.println("ESB (RP): Claims file received from inspection for reservation: " + reservationId);
        }).
        choice().when(header("CamelFileLength").isGreaterThan(0))
            .process(p -> {
            System.out.println("ESB (RP): Reading Claims from file and starting further processing.");
           }).unmarshal(new BindyCsvDataFormat(Claim.class)).
                // when there is only one line in the file we get a single claim
                // since the aggregator expacts a list we need to deal with this inside this route
            process(p -> {
                    if (p.getIn().getBody() instanceof Claim) {
                        LinkedList<Claim> body = new LinkedList<>();
                        body.add((Claim)p.getIn().getBody());
                        p.getIn().setBody(body);
                    }
                }).
        otherwise().setBody().constant(new LinkedList<Claim>())
            .process(p -> {
                System.out.println("ESB (RP): No claims for this car.");
            }).end().
        to("direct:returnPoint.mergeClaims");
    }
}
