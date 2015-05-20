package carrental.beans.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import carrental.model.billing.Invoice;
import carrental.model.pickupPoint.ReturnProtocol;

public class BillingProcessor  implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		ReturnProtocol protocol = exchange.getIn().getBody(ReturnProtocol.class);
        System.out.println("Retrun Protocol arrived in Billing-Department, ID="+protocol.getId());
        
        //Create invoice and transform model.pickupPoint.Claims into model.billing.Claims
        Invoice invoice=new Invoice();
        invoice.setId(1L);
        invoice.setNumber(1);
        invoice.setAddress("Einestraße 12, 1020 Wien");
        invoice.setCustomer("David Hume");
        invoice.setDate(new Date());
        ArrayList<carrental.model.billing.Claim> billingClaims=new ArrayList<carrental.model.billing.Claim>();
        
        for(carrental.model.pickupPoint.Claim pickupPointClaim: protocol.getClaims()){
        	carrental.model.billing.Claim billingClaim=new carrental.model.billing.Claim();
        	billingClaim.setClaimType(pickupPointClaim.getClaimType());
        	billingClaim.setDescription(pickupPointClaim.getDescription());
        	billingClaim.setCosts(new BigDecimal(199.99));  //just a sample price-value
        	billingClaims.add(billingClaim);
        }
        
        invoice.setClaims(billingClaims);
        
        System.out.println("Invoice (Id:"+invoice.getId()+", Nr:"+invoice.getNumber()+") created. \nClaims:");
        BigDecimal sumcosts=new BigDecimal(0);
        
        for(carrental.model.billing.Claim billingClaim:invoice.getClaims()){
        	System.out.println("Claim Type: "+billingClaim.getClaimType()+", costs: "+billingClaim.getCosts());
        	System.out.println("Claim Description: "+billingClaim.getDescription());
        	sumcosts=sumcosts.add(billingClaim.getCosts());
        }
        sumcosts.setScale(2, RoundingMode.CEILING);
        System.out.println("overall costs="+sumcosts);
        

	}

}
