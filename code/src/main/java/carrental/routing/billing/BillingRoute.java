package carrental.routing.billing;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import carrental.beans.billing.BillingProcessor;
import carrental.beans.billing.TestProcessor;
import carrental.beans.returnPoint.CarInspectionProcessor;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;

/**
 * Created by Alexander on 20.05.2015
 */
@Component
public class BillingRoute  extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		//JaxbDataFormat jaxb = new JaxbDataFormat(); 
		from("direct:endpoint").process(new BillingProcessor()).to("direct:endpoint2");
		from("direct:endpoint2").process(new TestProcessor());
		//.marshal(jaxb).to("direct:endpoint2"); 
		//from("direct:endpoint2").marshal().jaxb();
	}

}
