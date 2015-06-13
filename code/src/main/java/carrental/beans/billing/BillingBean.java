package carrental.beans.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import carrental.Constants;
import carrental.MongoDbConfiguration;
import carrental.beans.reservation.CarDTO;
import carrental.model.billing.Car;
import carrental.model.billing.Claim;
import carrental.model.billing.Customer;
import carrental.model.billing.ExchangeWrapper;
import carrental.model.billing.Invoice;
import carrental.model.billing.ReturnProtocol;

/**
 * Created by Alexander
 * 
 *
 */

@Component
public class BillingBean {
	
	@Autowired
	private MongoDbConfiguration mongoConfig;
	
	private static RestTemplate restClient=new RestTemplate();
	
	public void createInvoice(Exchange exchange) throws Exception{
		ExchangeWrapper exchangeWrapper=exchange.getIn().getBody(ExchangeWrapper.class);
		ReturnProtocol returnProtocol = exchangeWrapper.getOut_returnProtocol();
		Customer customer = exchangeWrapper.getOut_customer();
		Car car=exchangeWrapper.getOut_car();
        
        //update the availabilityState of the returned car
        setCarAvailable(car.getLicensePlate());
        
        //Create invoice and transform model.pickupPoint.Claims into model.billing.Claims
        exchange.getIn().setBody(createInvoice(returnProtocol, customer, car));
        
        System.out.println("Billingpoint: Invoice-calculation completed.");
	}
	
	
	private Invoice createInvoice(ReturnProtocol returnProtocol, Customer customer, Car car) throws Exception{
		Invoice invoice=new Invoice();
		List<Claim> claims=new ArrayList<Claim>();
		
		for(Claim c:returnProtocol.getClaims()){
			c.setCosts(simulateRepairCosts(Constants.servicePrices.get(c.getClaimType())));
			claims.add(c);
		}
		
		invoice.setClaims(claims);
		invoice.setDrivingCosts(simulateDrivingCosts(returnProtocol,car));
		invoice.setDate(new Date());
        invoice.setNumber(generateInvoiceNumber());
        invoice.setEmailAddress("max.duestermann@gmx.at");  //using only one email-address for simulation purposes!
        invoice.setCustomer(customer.getName());
        invoice.setAddress(customer.getAddress());
		
		return invoice;
	}
	
	/**
	 * @param servicePrice The standardized servicePrice is used as the mean for the randomized cost-calculation
	 */
	private static BigDecimal simulateRepairCosts(double servicePrice){
		Random randomGenerator = new Random();		
		Double r=randomGenerator.nextGaussian()*90 + servicePrice;  //z*SD + mu
		return new BigDecimal(r).setScale(2, RoundingMode.FLOOR);
	}
	
	private static BigDecimal simulateDrivingCosts(ReturnProtocol returnProtocol, Car car){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(returnProtocol.getReturnDate()); // Now use today date.
		
		Random r = new Random();
		int randomNumberOfDays = r.nextInt(365-1) + 1;
		c.add(Calendar.DATE, randomNumberOfDays); // Adding a randomNumberOfDays for simulationPurposes days
		
		Date newReturnDate=c.getTime();
		
		long daysUsed = newReturnDate.getTime() - returnProtocol.getReservationDate().getTime();
		double drivingCosts=daysUsed*car.getPricePerDay();
		
		return new BigDecimal(drivingCosts).setScale(2, RoundingMode.FLOOR);
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
		System.out.println("In BillingBean: mongoConfig="+mongoConfig);
		DBCursor dbCursor=mongoConfig.mongo().getDB("carrental").getCollection("invoice").find().sort( new BasicDBObject( "number" , -1 ) ).limit(1);
		if(dbCursor.hasNext())
			return ((Integer) dbCursor.next().get("number")) + 1;
		return 1;
	}
	
}
