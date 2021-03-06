package carrental.beans.billing.esb;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import carrental.model.billing.ExchangeWrapper;

/**
 * Created by Alexander on 13.06.2015
 * Translator EIP to assure that the BillingPoint's local applications are independent from external dataformats.
 * This bean is being processed on the server.
 */

@Component
public class TranslatorBean {
	public void transform(Exchange exchange){
		System.out.println("ESB (BP.Translator): Transforming all objects into formats which are processable by the local applications.");
		
		ExchangeWrapper exchangeWrapper=exchange.getIn().getBody(ExchangeWrapper.class);
		
		//loading the input objects
		carrental.model.reservation.Customer in_customer=exchangeWrapper.getIn_customer();
		carrental.model.reservation.Car in_car=exchangeWrapper.getIn_car();
		carrental.model.pickupPoint.ReturnProtocol in_returnProtocol=exchangeWrapper.getIn_returnProtocol();
		
		List<carrental.model.pickupPoint.Claim> in_claims=in_returnProtocol.getClaims();
				
		carrental.model.billing.Customer out_customer=new carrental.model.billing.Customer(in_customer.getName(),in_customer.getAddress(),in_customer.getBirthday(),in_customer.getSex(),in_customer.getDrivingLicense(), "max.duestermann@gmx.at"); //using only one email-address for simulation purposes
		carrental.model.billing.Car out_car=new carrental.model.billing.Car(in_car.getDescription(),in_car.getLicensePlate(),in_car.getColor(),in_car.getPower(),in_car.getCarState(),in_car.getPricePerDay());
		
		List<carrental.model.billing.Claim> out_claims=new ArrayList<carrental.model.billing.Claim>();
		
		//transformation of the claims
		for(carrental.model.pickupPoint.Claim in_claim: in_claims){
        	carrental.model.billing.Claim out_claim=new carrental.model.billing.Claim();
        	out_claim.setClaimType(in_claim.getClaimType());
        	out_claim.setDescription(in_claim.getDescription());
        	out_claims.add(out_claim);
        }
		
		carrental.model.billing.ReturnProtocol out_returnProtocol=new carrental.model.billing.ReturnProtocol(in_returnProtocol.getId(),out_claims,in_returnProtocol.getReservation().getReservationDate(),in_returnProtocol.getReturnDate());
		
		//setting the output objects
		exchangeWrapper.setOut_car(out_car);
		exchangeWrapper.setOut_customer(out_customer);
		exchangeWrapper.setOut_returnProtocol(out_returnProtocol);
		
		exchange.getIn().setBody(exchangeWrapper);
	}
}
