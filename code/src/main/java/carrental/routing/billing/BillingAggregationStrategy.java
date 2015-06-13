package carrental.routing.billing;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import carrental.model.pickupPoint.ReturnProtocol;
import carrental.model.reservation.Reservation;

public class BillingAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange.getIn().getBody()!=null)	
			//System.out.println("oldExchange: "+ oldExchange.getIn().getBody());
		System.out.println("newExchange: "+newExchange.getIn().getBody());
		return oldExchange;
	}

}
