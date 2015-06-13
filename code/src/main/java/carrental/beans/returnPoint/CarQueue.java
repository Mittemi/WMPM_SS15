package carrental.beans.returnPoint;

import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.Reservation;
import carrental.model.pickupPoint.ReturnProtocol;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Constantin on 15.05.2015.
 */
public class CarQueue {
    public static List<Long> listOfPickups = Collections.synchronizedList(new LinkedList());
    public static List<Reservation> listForClaims = Collections.synchronizedList(new LinkedList());

}
