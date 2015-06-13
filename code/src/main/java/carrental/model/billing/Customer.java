package carrental.model.billing;

import java.util.Date;

import carrental.model.reservation.Sex;

public class Customer {
	private String name;
    private String address;
    private Date birthday;
    private Sex sex;
    private String drivingLicense;
	
    public Customer(String name, String address, Date birthday, Sex sex,
			String drivingLicense) {
		super();
		this.name = name;
		this.address = address;
		this.birthday = birthday;
		this.sex = sex;
		this.drivingLicense = drivingLicense;
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
    
    
}
