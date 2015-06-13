package carrental.beans.billing.esb;

import java.util.HashMap;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carrental.model.billing.ExchangeWrapper;
import carrental.model.pickupPoint.ReturnProtocol;
import carrental.model.reservation.Car;
import carrental.model.reservation.Customer;
import carrental.repository.reservation.CarRepository;
import carrental.repository.reservation.CustomerRepository;

@Component
public class EnricherBean {
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private CarRepository carRepo;
	
	public void enrich(Exchange exchange) throws Exception {
		ReturnProtocol returnProtocol=exchange.getIn().getBody(ReturnProtocol.class);
		System.out.println("Billingpoint: Return Protocol with the ID="+returnProtocol.getId()+"arrived at Billingpoint");
		
        //enrich
		Iterable<carrental.model.reservation.Customer> customers= customerRepo.findAll();
		Customer customer = customerRepo.findOne(returnProtocol.getReservation().getCustomerId());
		Car car=carRepo.findOne(returnProtocol.getReservation().getCarId());
		
		ExchangeWrapper exchangeWrapper=new ExchangeWrapper();
		exchangeWrapper.setIn_returnProtocol(returnProtocol);
		exchangeWrapper.setIn_customer(customer);
		exchangeWrapper.setIn_car(car);
		
		exchange.getIn().setBody(exchangeWrapper);
    	
		System.out.println("BillingPoint: Exchanged object enriched with additional data.");
    }
}
