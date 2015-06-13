package carrental.routing.claimFixCenter;

import carrental.beans.claimFixCenter.esb.AggregationStrategyClaimFixes;
import carrental.beans.claimFixCenter.esb.ExtractClaimsListFromProtocol;
import carrental.beans.claimFixCenter.esb.IdentifyClaimType;
import carrental.model.pickupPoint.ClaimType;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Constantin on 03.06.2015.
 */
@Component
public class ClaimFixCenterRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("seda:queue:claimFixCenter").wireTap("seda:queue:claimFixAggregationPoint")
                .process(new ExtractClaimsListFromProtocol())
                .split(body())
                .process(new IdentifyClaimType())
                .choice() //Distribute to different claimFixCenters with ContentBasedRouter
                    .when(header("claimType").isEqualTo(ClaimType.Electrical)).process(new ClaimToDtoConverter()).to("activemq:queue:electricStation")
                    .when(header("claimType").isEqualTo(ClaimType.Cleaning)).process(new ClaimToDtoConverter()).to("activemq:queue:cleaningStation")
                    .when(header("claimType").isEqualTo(ClaimType.Mechanical)).process(new ClaimToDtoConverter()).to("activemq:queue:mechanicStation")
                    .when(header("claimType").isEqualTo(ClaimType.Refilling)).process(new ClaimToDtoConverter()).to("activemq:queue:refillStation")
                    .when(header("claimType").isEqualTo(ClaimType.Paintwork)).process(new ClaimToDtoConverter()).to("activemq:queue:paintStation");


        from("activemq:queue:claimFixAdapter").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                ClaimDTO claimdto = exchange.getIn().getBody(ClaimDTO.class);
                exchange.getIn().setBody(claimdto.getClaim());
                exchange.getIn().setHeader("carId",claimdto.getCarId());
                exchange.getIn().setHeader("protocolType",claimdto.getProtocolType());
            }
        }).to("seda:queue:claimFixAggregationPoint");

        //Receive Return Protocol with Meta info in the header about number of claims to fix
        //wait till all claims are fixed then forward return Protocol to Billing Point
        from("seda:queue:claimFixAggregationPoint")
                .aggregate(header("carId"), new AggregationStrategyClaimFixes())
                .completionSize(header("claimsCnt+Protocol")).process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("ClaimFixCenter: All claims for car with ID="+exchange.getIn().getHeader("carId")+" fixed.");
            }
        })
                .choice()
                .when(header("protocolType").isEqualTo(PickupProtocol.class.getName().toString()))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("ESB (ClaimFixCenter): Car with ID="+exchange.getIn().getHeader("carId")+" returned to PickupPoint - Available again.");
                    }
                }).to("direct:pickupPoint.callAvailability.available")
                .when(header("protocolType").isEqualTo(ReturnProtocol.class.getName().toString()))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("ESB (ClaimFixCenter)Forwarding Returnprotocol to BillingPoint.");
                    }
                })
                .to("direct:billingPoint");


        /*("direct:billingPoint").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("Hallo hier der BillingPoint");
            }
        });*/


    }


}
