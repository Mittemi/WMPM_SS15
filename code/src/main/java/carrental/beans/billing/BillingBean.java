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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import carrental.Constants;
import carrental.MongoDbConfiguration;
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
	
	@Autowired
	private MongoDbConfiguration mongoConfig;
	
	private static RestTemplate restClient=new RestTemplate();
	
	
	public void createInvoice(Exchange exchange) throws Exception{
		ReturnProtocol protocol = exchange.getIn().getBody(ReturnProtocol.class);
        System.out.println("Return Protocol arrived in Billing-Department, ID="+protocol.getId());
        
        //update the availabilityState of the returned car
        Car c=carRepo.findOne(protocol.getReservation().getCarId());

        if(c!=null)	
        	setCarAvailable(c.getLicensePlate());
        c=carRepo.findOne(protocol.getReservation().getCarId());
        
        //Create invoice and transform model.pickupPoint.Claims into model.billing.Claims
        Invoice invoice=new Invoice();

        ArrayList<carrental.model.billing.Claim> billingClaims=new ArrayList<carrental.model.billing.Claim>();
        
        for(carrental.model.pickupPoint.Claim protocolClaim: protocol.getClaims()){
        	carrental.model.billing.Claim billingClaim=new carrental.model.billing.Claim();
        	billingClaim.setClaimType(protocolClaim.getClaimType());
        	billingClaim.setDescription(protocolClaim.getDescription());
        	//billingClaim.setCosts(getRandomPrice()); 
        	billingClaim.setCosts(calculateCosts(Constants.servicePrices.get(protocolClaim.getClaimType())));
        	billingClaims.add(billingClaim);
        }
        
        invoice.setClaims(billingClaims);
        invoice.setDate(new Date());
        invoice.setNumber(generateInvoiceNumber());
        invoice.setEmailAddress("max.duestermann@gmx.at");  //using only one email-address for simulation purposes!
        
        /* waiting for the implementation of the preceding process
        
        Customer c = customerRepo.findOne(protocol.getReservation().getCustomerId());
        
        System.out.println("Customer c: " + c);
        List<Customer> list=(List<Customer>) customerRepo.findAll();
        for(Customer cu:list){
        	System.out.println("Customer name:"+cu.getName()+"\nCustomer id:"+cu.getId());
        }*/
        //invoice.setCustomer(c.getName()+" (ID:"+c.getId()+")");
        //invoice.setAddress(c.getAddress());
        
        invoice.setCustomer("Max Muster");  //Sample value
        invoice.setAddress("DavidHumeStreet 12");  //Sample value
        exchange.getIn().setBody(invoice);
	}
	
	/**
	 * @param servicePrice The standardized servicePrice is used as the mean for the randomized cost-calculation
	 */
	private static BigDecimal calculateCosts(double servicePrice){
		Random randomGenerator = new Random();		
		Double r=randomGenerator.nextGaussian()*90 + servicePrice;  //z*SD + mu
		return new BigDecimal(r).setScale(2, RoundingMode.FLOOR);
	}
	
	private static void setCarAvailable(String licensePlate){  
		String url = "http://127.0.0.1:9000/availability?licensePlate="+licensePlate+"&state=AVAILABLE";
		restClient.getForObject(url, CarDTO.class);
	}
	
	/*
	 * Generates a new invoice-number.
	 * If there already are invoices stored in the MongoDB: return highest invoicenumber + 1
	 * Else return 1.
	 */
	private int generateInvoiceNumber() throws Exception{
		DBCursor dbCursor=mongoConfig.mongo().getDB("carrental").getCollection("invoice").find().sort( new BasicDBObject( "number" , -1 ) ).limit(1);
		if(dbCursor.hasNext())
			return ((Integer) dbCursor.next().get("number")) + 1;
		return 1;
	}
	
}
