package carrental.beans.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.camel.Exchange;
import org.joda.time.DateTime;
import org.joda.time.Days;
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
 * Bean for the simulation of the invoice-calculating (local) application.
 * Uses the exchanged wrapper object for the calculation of all relevant matters of expense.
 * This bean is being processed on the clientside.
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
        System.out.println("BillingPoint.Invoice_Calculator: Invoice-calculation completed.");
	}
	
	private Invoice createInvoice(ReturnProtocol returnProtocol, Customer customer, Car car) throws Exception{
		
		List<Claim> claims=new ArrayList<Claim>();
		
		for(Claim c:returnProtocol.getClaims()){
			c.setCosts(simulateRepairCosts(Constants.servicePrices.get(c.getClaimType())));
			claims.add(c);
		}
		
		Invoice invoice=new Invoice(claims, new Date(), generateInvoiceNumber(),car,simulateDaysUsed(returnProtocol),calculateDrivingCosts(simulateDaysUsed(returnProtocol),car),0.2, customer, returnProtocol.getReservationDate(), simulateReturnDate(returnProtocol));
		
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
	
	private static BigDecimal calculateDrivingCosts(long daysUsed, Car car){		
		return new BigDecimal(daysUsed*car.getPricePerDay()).setScale(2, RoundingMode.FLOOR);
	}
	
	private int simulateDaysUsed(ReturnProtocol returnProtocol){
		DateTime newReturnDate=new DateTime(simulateReturnDate(returnProtocol));
		DateTime reservationDate=new DateTime(returnProtocol.getReservationDate().getTime());
		return Days.daysBetween(reservationDate.withTimeAtStartOfDay(), newReturnDate.withTimeAtStartOfDay()).getDays();
	}
	
	private Date simulateReturnDate(ReturnProtocol returnProtocol){
		Calendar c = Calendar.getInstance();
		c.setTime(returnProtocol.getReturnDate()); 
		int randomNumberOfDays = (int)(new Random().nextGaussian()*4 + 23);
		c.add(Calendar.DATE, randomNumberOfDays); // Adding a randomNumberOfDays for simulationPurposes 
		Date newReturnDate=c.getTime();
		return newReturnDate;
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
