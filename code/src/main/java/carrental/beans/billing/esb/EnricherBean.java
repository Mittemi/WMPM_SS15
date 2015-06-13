package carrental.beans.billing.esb;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import carrental.model.billing.ExchangeWrapper;
import carrental.model.pickupPoint.ReturnProtocol;
import carrental.model.reservation.Car;
import carrental.model.reservation.Customer;
import carrental.repository.reservation.CarRepository;
import carrental.repository.reservation.CustomerRepository;

/**
 * Created by Alexander on 13.06.2015
 */

@Component
public class EnricherBean {
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private CarRepository carRepo;
	
	public void enrich(Exchange exchange) throws Exception {
		ReturnProtocol returnProtocol=exchange.getIn().getBody(ReturnProtocol.class);
		System.out.println("BillingPoint: Return Protocol with the ID="+returnProtocol.getId()+"arrived at Billingpoint");
		
        System.out.println("BillingPoint.Enricher: Enriching the exchange object with necessary customer-data and car-data.");
		Customer customer = customerRepo.findOne(returnProtocol.getReservation().getCustomerId());
		Car car=carRepo.findOne(returnProtocol.getReservation().getCarId());
		
		ExchangeWrapper exchangeWrapper=new ExchangeWrapper();
		exchangeWrapper.setIn_returnProtocol(returnProtocol);
		exchangeWrapper.setIn_customer(customer);
		exchangeWrapper.setIn_car(car);
		
		exchange.getIn().setBody(exchangeWrapper);
    	
		System.out.println("BillingPoint.Enricher: Exchanged object enriched with additional data.");
    }
}
