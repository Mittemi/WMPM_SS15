package carrental.model.billing;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
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
@XmlRootElement
@XmlSeeAlso(carrental.model.billing.Claim.class)
@Document(collection = "invoice")
public class Invoice implements Serializable {

    @Id
    private String _id;

    private List<Claim> claims;

    private String customer;

    private String address;

    private Date date;
    
    private String emailAddress;

    private int number;
    
    private BigDecimal drivingCosts;
    
    public Invoice() {
        claims = new LinkedList<>();
    }
    
    @XmlAttribute
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }
    
    @XmlElement(name = "claim")
    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }
    
    @XmlElement
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    @XmlElement
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlElement
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlElement
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public String getEmailAddress(){
    	return this.emailAddress;
    }
    
    public void setEmailAddress(String emailAddress){
    	this.emailAddress=emailAddress;
    }
    
    public BigDecimal getDrivingCosts(){
    	return this.drivingCosts;
    }
    
    public void setDrivingCosts(BigDecimal drivingCosts){
    	this.drivingCosts=drivingCosts;
    }
}
