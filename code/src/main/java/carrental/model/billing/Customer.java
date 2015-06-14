package carrental.model.billing;

import java.util.Date;

import carrental.model.reservation.Sex;

/**
 * Created by Alexander on 28.05.2015
 * Data-format which is processable by the BillingPoint's local applications.
 */

public class Customer {
	private String name;
    private String address;
    private Date birthday;
    private Sex sex;
    private String drivingLicense;
    private String emailAddress;
	
    public Customer(String name, String address, Date birthday, Sex sex,
			String drivingLicense, String emailAddress) {
		super();
		this.name = name;
		this.address = address;
		this.birthday = birthday;
		this.sex = sex;
		this.drivingLicense = drivingLicense;
		this.emailAddress=emailAddress;
	}
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public Sex getSex() {
		return sex;
	}
	
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
	public String getDrivingLicense() {
		return drivingLicense;
	}
	
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
    
}
