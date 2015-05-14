package carrental.routing.returnPoint;

import carrental.beans.DemoPrintTextBean;
import carrental.beans.returnPoint.CarReturnProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.stereotype.Component;

/**
 * Created by Constantin on 12.05.2015.
 */
@Component
public class CarInspectionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("file://claimlists?noop=true").split(body().tokenize("\n"))
                .log("Line: ${body}")
                .unmarshal()
                .bindy(BindyType.Csv, "carrental.model")
                .to("mock:update");

        from("activemq:queue:CarHandedOverQueue").process(new CarReturnProcessor());
    }
}
