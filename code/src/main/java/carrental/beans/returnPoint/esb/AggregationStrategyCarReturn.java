package carrental.beans.returnPoint.esb;

import carrental.beans.pickupPoint.PickupBean;
import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ClaimType;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Constantin on 15.05.2015.
 */
public class AggregationStrategyCarReturn implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // the first time there are no existing message and therefore
        // the oldExchange is null. In these cases we just return
        // the newExchange
        if (oldExchange == null) {
            return newExchange;
        }


        PickupProtocol oldBody = oldExchange.getIn().getBody(PickupProtocol.class);
        ReturnProtocol newBody = newExchange.getIn().getBody(ReturnProtocol.class);

        newBody.setReservation(oldBody.getReservation());

        writeClaimsFile(newBody.getReservation().getLicensePlate());

        List<Claim> claimsList = oldBody.getClaims();

        newBody.setClaims(claimsList);
        newBody.setId(oldBody.getId());
        newBody.setReturnDate(new Date());

        // update the existing message with the added body
        oldExchange.getIn().setBody(newBody);

        // and return it
        return oldExchange;
    }

    private void writeClaimsFile(String licensePlate) {

    }
}
