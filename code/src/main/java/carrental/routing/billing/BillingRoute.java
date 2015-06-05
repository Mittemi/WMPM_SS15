package carrental.routing.billing;

import java.util.Properties;

import javax.mail.Session;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mail.DefaultAuthenticator;
import org.apache.camel.component.mail.MailComponent;
import org.apache.camel.component.mail.MailConfiguration;
import org.apache.camel.component.mail.MailEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
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
		
       // CamelContext camelContext = new DefaultCamelContext(); 
		
		//Properties props = System.getProperties();
	    /*
		props.put("mail.smtp.host", "mail.eclipso.de");
	    props.put("mail.smtp.socketFactory.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.user", "carrentalag@eclipso.at ");
	    props.put("mail.password", "12345678");
		*/
		/*
		props.put("mail.smtp.starttls.enable","true");
		
		DefaultAuthenticator defaultAuthenticator=new DefaultAuthenticator("carrentalag@eclipso.at","12345678");
		Session session = Session.getDefaultInstance( props , defaultAuthenticator); 
		MailComponent mailComponent=new MailComponent();
		MailConfiguration mconfig=new MailConfiguration();
		mconfig.setSession(session);
		mailComponent.setConfiguration(mconfig);
		
		camelContext.addComponent("mail", mailComponent);
		
		final MailEndpoint ep = (MailEndpoint) camelContext.getEndpoint("mail://mail.eclipso.de:587?password=RAW(12345678)&username=RAW(carrentalag@eclipso.at)");
        ep.getConfiguration().setProtocol("smtp");
        ep.getConfiguration().setFrom("carrentalag@eclipso.at");
        ep.getConfiguration().setTo("max.duestermann@gmx.at"); 
		
		
				from("direct:endpoint").bean(BillingBean.class).wireTap(mongoEndpointString).bean(PrintBean.class).log("Working on file ${header.CamelFileName}")
			    .setBody(Builder.constant("Salut !")).to(ep);         
			    //.to("smtp://mail.gmx.net:587?password=RAW(12345678)&username=RAW(carrentalag@gmx.at)&to=RAW(max.duestermann@gmx.at)"); 	
         */   
		/*
		from("file://target/subfolder")
	    .log("Working on file ${header.CamelFileName}")
	    .setHeader("subject", simple("New incident: ${header.CamelFileName}"))
	    .to("MY_ID@smtp://mail.gmx.net:587?password=12345678&to=max.duestermann@gmx.at");
	    */ 
	}

}
