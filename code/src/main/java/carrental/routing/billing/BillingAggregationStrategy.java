package carrental.routing.billing;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import carrental.model.pickupPoint.ReturnProtocol;
import carrental.model.reservation.Car;
import carrental.model.reservation.Reservation;

public class BillingAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		/*
		if(oldExchange.getIn().getBody()!=null)	
			//System.out.println("oldExchange: "+ oldExchange.getIn().getBody());
		System.out.println("newExchange: "+newExchange.getIn().getBody());
		return oldExchange;
		*/
		
		ReturnProtocol returnProtocol=oldExchange.getIn().getBody(ReturnProtocol.class);
		System.out.println("In BillingAggrStrategy: oldExchange.reservation.customerId="+returnProtocol.getReservation().getCustomerId());
		
		Collection collection=newExchange.getIn().getBody(Collection.class);
		Reservation reservation=null;
		
		System.out.println("In BillingAggrStrategy: newExchange.collection.size():"+collection.size());
		
		for(Object o:collection){
			//System.out.println("In BillingAggrStrategy: Object o="+o.toString());
			System.out.println("In BillingAggrStrategy: newExchange.Reservation.customerId:"+((Reservation)o).getCustomer().getId());
			if(((Reservation)o).getId().equals(returnProtocol.getReservation().getId())){
				reservation=(Reservation)o;
				System.out.println("We found the reservation !! :)");
			}
		}
		
		if(oldExchange!=null)
			return oldExchange;
		return newExchange;
	}

}
