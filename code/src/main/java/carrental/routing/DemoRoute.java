package carrental.routing;

import java.util.Random;

import carrental.beans.DemoPrintTextBean;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Michael on 07.05.2015.
 */
@Component
public class DemoRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        System.out.println("Register route");
        from("timer://foo?period=5000").setBody().constant("Hello there").log("Message: ${body}").to("direct:starting");
        from("file://inbox?noop=true").process(new Processor() {
			
			@Override
			public void process(Exchange ex) throws Exception {
				System.out.println("File contents: " + ex.getIn().getBody(String.class));
			}
		});
        
        from("direct:starting").bean(DemoPrintTextBean.class);


    }
}
