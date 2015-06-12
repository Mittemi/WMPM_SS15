package carrental.beans.returnPoint.esb;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by Michael on 08.06.2015.
 */
public class ClaimsAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {

        System.out.println("ESB (RP): Claims file received. Adding claims to pickup protocol");

        return original;
    }
}
