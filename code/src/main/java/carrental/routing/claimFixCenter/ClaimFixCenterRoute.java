package carrental.routing.claimFixCenter;

import carrental.beans.claimFixCenter.*;
import carrental.beans.claimFixCenter.esb.AggregationStrategyClaimFixes;
import carrental.beans.claimFixCenter.esb.ExtractClaimsList;
import carrental.beans.claimFixCenter.esb.IdentifyClaimType;
import carrental.model.pickupPoint.ClaimType;
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
        from("seda:queue:claimFixCenter")
                .process(new ExtractClaimsList())
                .split(body())
                .process(new IdentifyClaimType())
                .choice() //Distribute to different claimFixCenters with ContentBasedRouter
                    .when(header("claimType").isEqualTo(ClaimType.Electrical)).to("seda:queue:electricStation")
                    .when(header("claimType").isEqualTo(ClaimType.Cleaning)).to("seda:queue:cleaningStation")
                    .when(header("claimType").isEqualTo(ClaimType.Mechanical)).to("seda:queue:mechanicalStation")
                    .when(header("claimType").isEqualTo(ClaimType.Refilling)).to("seda:queue:refillingStation")
                    .when(header("claimType").isEqualTo(ClaimType.Paintwork)).to("seda:queue:paintworkStation");

        //Do Claim Fixing
        from("seda:queue:electricStation").process(new ElectricStation()).to("seda:queue:claimFixAggregationPoint");
        from("seda:queue:cleaningStation").process(new CleaningStation()).to("seda:queue:claimFixAggregationPoint");
        from("seda:queue:mechanicalStation").process(new MechanicStation()).to("seda:queue:claimFixAggregationPoint");;
        from("seda:queue:refillingStation").process(new RefillStation()).to("seda:queue:claimFixAggregationPoint");;
        from("seda:queue:paintworkStation").process(new PaintStation()).to("seda:queue:claimFixAggregationPoint");;

        //Receive Return Protocol with Meta info in the header about number of claims to fix
        //wait till all claims are fixed then forward return Protocol to Billing Point
        from("seda:queue:claimFixAggregationPoint")
                .aggregate(header("carId"), new AggregationStrategyClaimFixes())
                .completionSize(header("claimsCnt+Protocol")).process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("ClaimFixCenter: All claims for car with ID="+exchange.getIn().getHeader("carId")+" fixed. Forwarding Returnprotocol to BillingPoint.");
            }
        })
                .to("direct:billingPoint");
        /*
        from("direct:billingPoint").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("Hallo hier der BillingPoint");
            }
        });
         */
    }


}
