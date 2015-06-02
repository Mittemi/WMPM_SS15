package carrental.beans.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carrental.beans.reservation.CarDTO;
import carrental.model.billing.Invoice;
import carrental.model.pickupPoint.ReturnProtocol;
import carrental.model.reservation.Car;
import carrental.model.reservation.Customer;
import carrental.repository.reservation.CarRepository;
import carrental.repository.reservation.CustomerRepository;

/**
 * Created by Alexander
 * 
 *
 */

@Component
public class BillingBean {
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	CarRepository carRepo;
	
	private static RestTemplate restClient=new RestTemplate();
	
	
	public void createInvoice(Exchange exchange){
		ReturnProtocol protocol = exchange.getIn().getBody(ReturnProtocol.class);
        System.out.println("Return Protocol arrived in Billing-Department, ID="+protocol.getId());
        
        //update the availabilityState of the returned car
        setCarAvailable(carRepo.findOne(protocol.getReservation().getCarId()).getLicensePlate());
        
        //Create invoice and transform model.pickupPoint.Claims into model.billing.Claims
        Invoice invoice=new Invoice();

        ArrayList<carrental.model.billing.Claim> billingClaims=new ArrayList<carrental.model.billing.Claim>();
        
        for(carrental.model.pickupPoint.Claim pickupPointClaim: protocol.getClaims()){
        	carrental.model.billing.Claim billingClaim=new carrental.model.billing.Claim();
        	billingClaim.setClaimType(pickupPointClaim.getClaimType());
        	billingClaim.setDescription(pickupPointClaim.getDescription());
        	billingClaim.setCosts(getRandomPrice()); 
        	billingClaims.add(billingClaim);
        }
        
        invoice.setClaims(billingClaims);
        invoice.setDate(new Date());

        /* wait for the implementation of the preceding process
        
        Customer c = customerRepo.findOne(protocol.getReservation().getCustomerId());
        
        System.out.println("Customer c: " + c);
        List<Customer> list=(List<Customer>) customerRepo.findAll();
        for(Customer cu:list){
        	System.out.println("Customer name:"+cu.getName()+"\nCustomer id:"+cu.getId());
        }*/
        //invoice.setCustomer(c.getName()+" (ID:"+c.getId()+")");
        //invoice.setAddress(c.getAddress());
        
        invoice.setCustomer("Max Muster");  //Sample value
        invoice.setAddress("DavidHumeStreet");  //Sample value
        exchange.getIn().setBody(invoice);
	}
	
	private static BigDecimal getRandomPrice(){	
		Random randomGenerator = new Random();
		Double r=randomGenerator.nextGaussian()*90 + 550;  //z*SD + mu
		return new BigDecimal(r).setScale(2, RoundingMode.FLOOR);
	}
	
	private static void setCarAvailable(String licensePlate){  
		String url = "http://127.0.0.1:9000/availability?licensePlate="+licensePlate+"&state=AVAILABLE";
		restClient.getForObject(url, CarDTO.class);
	}
	
}
