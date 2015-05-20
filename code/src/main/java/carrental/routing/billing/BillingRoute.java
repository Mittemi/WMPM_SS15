package carrental.routing.billing;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import carrental.beans.billing.BillingProcessor;
import carrental.beans.returnPoint.CarInspectionProcessor;
/**
 * Created by Alexander on 20.05.2015
 */
@Component
public class BillingRoute  extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		 from("direct:endpoint").process(new BillingProcessor());
	}

}
