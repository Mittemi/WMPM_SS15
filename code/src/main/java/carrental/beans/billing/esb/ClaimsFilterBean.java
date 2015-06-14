package carrental.beans.billing.esb;

import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ReturnProtocol;

/**
 * Created by Alexander
 */

@Component
public class ClaimsFilterBean {
	
	public void filterClaims(Exchange exchange){		
		ReturnProtocol returnProtocol=exchange.getIn().getBody(ReturnProtocol.class);

		System.out.println("BillingPoint: Return Protocol for the Car with ID="+returnProtocol.getReservation().getCarId()+" arrived at Billingpoint");
		System.out.println("BillingPoint.ClaimsFilter: Removing all reported claims, which the customer does not have to pay for.");
		
		List<Claim> claims=returnProtocol.getClaims();
		int removeCounter=0;
		for(int i=0; i<claims.size(); i++){
			if(claims.get(i).isCustomerClaim()==false){
				claims.remove(i);
				removeCounter++;
			}
		}
		
		System.out.println("BillingPoint.ClaimsFilter: Removed "+removeCounter+" claims from the returnProtocol.");
		returnProtocol.setClaims(claims);
		exchange.getIn().setBody(returnProtocol);
	}
}
