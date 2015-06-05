package carrental.routing.billing;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carrental.CarRentalConfig;
import carrental.beans.billing.MailingBean;
import carrental.beans.billing.PrintBean;
import carrental.beans.billing.BillingBean;

/**
 * Created by Alexander on 20.05.2015
 */
@Component
public class BillingRoute  extends RouteBuilder{
	@Autowired
	private CarRentalConfig config;
	
	@Override
	public void configure() throws Exception {
		String mongoEndpointString = "mongodb:mongo?database=" + config.getBilling().getMongo().getName() +"&collection=invoice&operation=save&writeResultAsHeader=true";
		from("direct:endpoint").bean(BillingBean.class).wireTap(mongoEndpointString).bean(PrintBean.class).bean(MailingBean.class);
	}

}
