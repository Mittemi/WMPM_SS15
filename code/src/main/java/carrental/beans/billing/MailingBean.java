package carrental.beans.billing;

import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.builder.Builder;
import org.apache.camel.component.mail.MailEndpoint;
import org.springframework.stereotype.Component;

@Component
public class MailingBean {
	public void sendEmail(Exchange exchange) throws Exception{
		File file=(File) exchange.getIn().getBody();
		CamelContext camelContext = exchange.getContext();
		String destinationEmailAddress = (String) exchange.getProperty("receiverEmailAddress");
		String receiverName = (String) exchange.getProperty("receiverName");
				
		final MailEndpoint ep = (MailEndpoint) camelContext.getEndpoint("smtp://mail.eclipso.de:587?password=RAW(12345678)&username=RAW(carrentalag3@eclipso.at)");
        ep.getConfiguration().setProtocol("smtp");
        ep.getConfiguration().setFrom("carrentalag3@eclipso.at");
        ep.getConfiguration().setTo(destinationEmailAddress);
        
        Exchange exchangeMail=ep.createExchange();
		Message in=exchangeMail.getIn();
		in.setBody(Builder.constant("Dear "+receiverName+", please check this mail's attachments for the invoice."));
		in.addAttachment("Invoice_"+receiverName+".pdf", new DataHandler(new FileDataSource(file)));
		exchangeMail.setIn(in);
       
        Producer producer = ep.createProducer();
		producer.start();
		producer.process(exchangeMail); 
		producer.stop();
		
		System.out.println("BillingPoint: E-Mail with Invoice.pdf sent to "+receiverName+" ("+destinationEmailAddress+")");
	}
}
