package carrental.model.billing;

import java.util.Date;
import java.util.List;

import carrental.model.billing.Claim;

/**
 * Created by Alexander on 28.05.2015
 */

public class ReturnProtocol {
	private Long id;
	private List<Claim> claims;
    private Date reservationDate;
	private Date returnDate;
	
    public ReturnProtocol(Long id, List<Claim> claims, Date reservationDate, Date returnDate) {
		super();
		this.id = id;
		this.claims = claims;
		this.reservationDate=reservationDate;
		this.returnDate = returnDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Claim> getClaims() {
		return claims;
	}

	public void setClaims(List<Claim> claims) {
		this.claims = claims;
	}
	
	public Date getReservationDate(){
		return reservationDate;
	}
	
	public void setReservationDate(Date reservationDate){
		this.reservationDate=reservationDate;
	}
	
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
    
    
}
