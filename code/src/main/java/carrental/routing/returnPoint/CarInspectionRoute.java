package carrental.routing.returnPoint;

import carrental.beans.returnPoint.esb.AddExpectedReturnProcessor;
import carrental.beans.returnPoint.esb.AggregationStrategyCarReturn;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Constantin on 12.05.2015.
 */
@Component
public class CarInspectionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

/*
        from("file://claimlists?noop=true").split(body().tokenize("\n"))
                .log("Line: ${body}")
                .unmarshal()
                .bindy(BindyType.Csv, "carrental.model")
                .to("mock:update");*/

        from("seda:queue:carToInspectQueue")
                .process(new AddExpectedReturnProcessor())
                .aggregate(header("carId"), new AggregationStrategyCarReturn()).completionSize(2).recipientList(header("recipients"));
    }
}
