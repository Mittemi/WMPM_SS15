package carrental.beans.billing;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import carrental.model.billing.Invoice;
import carrental.model.pickupPoint.ReturnProtocol;

public class TestProcessor  implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		Invoice invoice= exchange.getIn().getBody(Invoice.class);
		System.out.println(invoice.getId()+" - "+invoice.getCustomer());
	}

}
