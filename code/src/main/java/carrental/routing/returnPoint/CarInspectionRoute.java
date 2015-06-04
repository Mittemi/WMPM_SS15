package carrental.routing.returnPoint;

import carrental.beans.returnPoint.AddExpectedReturnProcessor;
import carrental.beans.returnPoint.AggregationStrategyCarReturn;
import carrental.beans.returnPoint.CarInspectionProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Constantin on 12.05.2015.
 */
@Component
public class CarInspectionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

//TODO:  cfr creates errors, check that!
/*
        from("file://claimlists?noop=true").split(body().tokenize("\n"))
                .log("Line: ${body}")
                .unmarshal()
                .bindy(BindyType.Csv, "carrental.model")
                .to("mock:update");*/

        from("seda:queue:carToInspectQueue")
                .process(new AddExpectedReturnProcessor())
                .aggregate(header("carId"), new AggregationStrategyCarReturn()).completionSize(2).recipientList(header("recipients"));
                /*.choice()
                .when(header("claimsToFix").isGreaterThan(0)).to("direct:claimFixCenter")
                .otherwise().to("direct:billingPoint");*/
        // guess what, the name should be unique: endpoint really does a good work concerning that ;)
        
        //commented-out by Alex for BillingRoute-purposes
        from("direct:billingPoint").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("Hallo hier der BillingPoint");
            }
        });
    }
}
