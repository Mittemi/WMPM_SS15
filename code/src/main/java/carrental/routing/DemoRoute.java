package carrental.routing;

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

     //   from("timer://foo?period=10s").bean(new DemoPrintTextBean(), "doSomething");

        from("file://test.txt").to("file://output.txt");

    }
}
