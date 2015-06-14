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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carrental.CarRentalConfig;

/**
 * Created by Alexander on 30.05.2015
 */

@Component
public class MailingBean {
	@Autowired
	private CarRentalConfig carRentalConfig;
	
	public void sendEmail(Exchange exchange) throws Exception{
		File file=(File) exchange.getIn().getBody();
		CamelContext camelContext = exchange.getContext();
		String destinationEmailAddress = (String) exchange.getProperty("receiverEmailAddress");
		String receiverName = (String) exchange.getProperty("receiverName");
	 
		String endpointString="smtp://"+carRentalConfig.getEmail().getSmtpAddress()+":"+carRentalConfig.getEmail().getSmtpPort()+"?password=RAW("+carRentalConfig.getEmail().getSmtpPassword()+")&username=RAW("+carRentalConfig.getEmail().getSmtpUsername()+")";
				
		final MailEndpoint ep = (MailEndpoint) camelContext.getEndpoint(endpointString);
        ep.getConfiguration().setProtocol("smtp");
        ep.getConfiguration().setFrom(carRentalConfig.getEmail().getSmtpUsername());
        //ep.getConfiguration().setSubject("Invoice");
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
		
		System.out.println("BillingPoint.Email_Sender: E-Mail with the file '"+file.getName()+"' sent to "+receiverName+" ("+destinationEmailAddress+").");
	}
}
