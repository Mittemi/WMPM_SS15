package carrental.model.billing;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Created by Michael on 13.05.2015.
 */
@Document(collection = "invoice")
public class Invoice implements Serializable {

    @Id
    private String _id;

    private List<Claim> claims;

    private Date date;
    
    private int number;
        
    private Car car;
    
    private long daysUsed;

    //driving costs
    private BigDecimal drivingCostsBeforeTax;
    private BigDecimal salesTaxAmount_driving;
    private BigDecimal drivingCosts;
        
    //service costs
    private BigDecimal totalServiceCostsBeforeTax;
    private BigDecimal salesTaxAmount_service;
    private BigDecimal serviceCosts;
    
    //total costs
    private BigDecimal totalCostsBeforeTax;
    private BigDecimal totalCosts;
    
	private double salesTaxRate;
	
	private Customer customer;
    
    public Invoice(List<Claim> claims, Date date, int number, Car car, long daysUsed, BigDecimal drivingCostsBeforeTax, double salesTaxRate, Customer customer) {
		super();
		this.claims = claims;
		this.date = date;
		this.number = number;
		this.car = car;
		this.daysUsed = daysUsed;
		this.drivingCostsBeforeTax = drivingCostsBeforeTax;
		this.salesTaxRate = salesTaxRate;
		this.customer=customer;
		
		//calculate totalServiceCostsBeforeTax
		double d=0.0;
		this.totalServiceCostsBeforeTax=new BigDecimal(d);
		
		for(Claim c:claims){
			//tmpsum.add(c.getCosts());
			this.totalServiceCostsBeforeTax=this.totalServiceCostsBeforeTax.add(c.getCosts());
		}
		
		//calculate totalCostsBeforeTax
		this.totalCostsBeforeTax=this.totalServiceCostsBeforeTax.add(this.drivingCostsBeforeTax);
		
		//calculate salesTaxAmounts
		this.salesTaxAmount_driving=this.drivingCostsBeforeTax.multiply(new BigDecimal(this.salesTaxRate)).setScale(2, RoundingMode.FLOOR);
		this.salesTaxAmount_service=this.totalServiceCostsBeforeTax.multiply(new BigDecimal(this.salesTaxRate)).setScale(2, RoundingMode.FLOOR);
		
		//calculate totalCosts
		this.drivingCosts=this.drivingCostsBeforeTax.add(this.salesTaxAmount_driving);
		this.serviceCosts=this.totalServiceCostsBeforeTax.add(this.salesTaxAmount_service);
		this.totalCosts=this.drivingCosts.add(this.serviceCosts);
	}

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }
    
    public List<Claim> getClaims() {
        return claims;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public BigDecimal getDrivingCostsBeforeTax(){
    	return this.drivingCostsBeforeTax;
    }
    
    public void setDrivingCostsBeforeTax(BigDecimal drivingCostsBeforeTax){
    	this.drivingCostsBeforeTax=drivingCostsBeforeTax;
    }
    
    public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public long getDaysUsed() {
		return daysUsed;
	}

	public void setDaysUsed(long daysUsed) {
		this.daysUsed = daysUsed;
	}
	
	public BigDecimal getTotalCostsBeforeTax() {
		return totalCostsBeforeTax;
	}
	
	public BigDecimal getTotalServiceCostsBeforeTax(){
		return totalServiceCostsBeforeTax;
	}
	
	public double getSalesTaxRate() {
		return salesTaxRate;
	}

	public void setSalesTaxRate(double salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}
	
	public BigDecimal getSalesTaxAmount_driving(){
		return this.salesTaxAmount_driving;
	}
	
	public BigDecimal getSalesTaxAmount_service(){
		return this.salesTaxAmount_service;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getTotalCosts() {
		return totalCosts;
	}

	public BigDecimal getDrivingCosts() {
		return drivingCosts;
	}

	public BigDecimal getServiceCosts() {
		return serviceCosts;
	}
	
}
