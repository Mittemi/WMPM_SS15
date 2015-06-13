package carrental.model.billing;

/**
 * Created by Alexander on 06.06.2015
 */

public class ExchangeWrapper {
	private carrental.model.reservation.Customer in_customer;
	private carrental.model.reservation.Car in_car;
	private carrental.model.pickupPoint.ReturnProtocol in_returnProtocol;
	
	private carrental.model.billing.Customer out_customer;
	private carrental.model.billing.Car out_car;
	private carrental.model.billing.ReturnProtocol out_returnProtocol;
	
	public carrental.model.reservation.Customer getIn_customer() {
		return in_customer;
	}
	
	public void setIn_customer(carrental.model.reservation.Customer in_customer) {
		this.in_customer = in_customer;
	}
	
	public carrental.model.reservation.Car getIn_car() {
		return in_car;
	}
	
	public void setIn_car(carrental.model.reservation.Car in_car) {
		this.in_car = in_car;
	}
	
	public carrental.model.pickupPoint.ReturnProtocol getIn_returnProtocol() {
		return in_returnProtocol;
	}
	
	public void setIn_returnProtocol(
			carrental.model.pickupPoint.ReturnProtocol in_returnProtocol) {
		this.in_returnProtocol = in_returnProtocol;
	}
	
	public carrental.model.billing.Customer getOut_customer() {
		return out_customer;
	}
	
	public void setOut_customer(carrental.model.billing.Customer out_customer) {
		this.out_customer = out_customer;
	}
	
	public carrental.model.billing.Car getOut_car() {
		return out_car;
	}
	
	public void setOut_car(carrental.model.billing.Car out_car) {
		this.out_car = out_car;
	}
	
	public carrental.model.billing.ReturnProtocol getOut_returnProtocol() {
		return out_returnProtocol;
	}
	
	public void setOut_returnProtocol(
			carrental.model.billing.ReturnProtocol out_returnProtocol) {
		this.out_returnProtocol = out_returnProtocol;
	}
	
	
}
