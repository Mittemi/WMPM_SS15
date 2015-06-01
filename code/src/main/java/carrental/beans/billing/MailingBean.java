package carrental.beans.billing;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*; 

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.builder.Builder;
import org.apache.camel.component.mail.DefaultAuthenticator;
import org.apache.camel.component.mail.MailEndpoint;
import org.springframework.stereotype.Component;

@Component
public class MailingBean {
	public void sendEmail(Exchange exchange) throws Exception{
		File file=(File) exchange.getIn().getBody();
		CamelContext camelContext = exchange.getContext();
		
		/** ONLY TU-MAIL ACCOUT IS WORKING (WEB.de, GMX.at aren't)! **/
		
		Properties props = new Properties(); 	
		props.put( "mail.smtp.auth","true");
		props.put("mail.smtp.starttls.enable","true");
		
		/*  //account details for the wmpm mail adr.
		props.put("mail.smtp.host", "mail.gmx.net");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.user", "CarrentalAG");
		props.put("mail.password", "12345678");
		*/
		
		DefaultAuthenticator defaultAuthenticator=new DefaultAuthenticator("username","password");  //enter correct pw
			
		Session session = Session.getDefaultInstance( props , defaultAuthenticator); 
		
			final MailEndpoint ep = (MailEndpoint) camelContext.getEndpoint("smtp://mail.student.tuwien.ac.at:587?to=carrentalag@gmx.at"); 
			ep.getConfiguration().setSession(session);		
			Exchange exchangeMail=ep.createExchange();
			Message in=exchangeMail.getIn();
			
			in.setBody(Builder.constant("Dear customer, please check this mail's attachments for the invoice."));
			in.addAttachment("Invoice.pdf", new DataHandler(new FileDataSource(file)));
	        exchangeMail.setIn(in);
	        
	        Producer producer = ep.createProducer();
			producer.start();
			producer.process(exchangeMail); 
			producer.stop();
		
	}
}
