package carrental.beans.returnPoint.esb;

import carrental.model.pickupPoint.ReturnProtocol;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Constantin on 13.06.2015.
 */
public class PickUpProtocolTranslator {
    public ReturnProtocol process(carrental.model.pickupPoint.PickupProtocol pickupProtocol) {

        ReturnProtocol returnProtocol = new ReturnProtocol();
        returnProtocol.setReservation(pickupProtocol.getReservation());
        returnProtocol.setReturnDate(new Date());
        returnProtocol.setId(pickupProtocol.getReservation().getId());
        returnProtocol.setClaims(new LinkedList<>(pickupProtocol.getClaims()));

        System.out.println("ESB - ReturnPoint: MessageTranslator PickupProtocol --> ReturnProtocol [ID: " + pickupProtocol.getId() + "]" );

        return returnProtocol;
    }
}
