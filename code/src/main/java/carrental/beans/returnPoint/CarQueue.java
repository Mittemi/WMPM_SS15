package carrental.beans.returnPoint;

import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Constantin on 15.05.2015.
 */
public class CarQueue {
    public static List<PickupProtocol> listOfPickups = Collections.synchronizedList(new LinkedList());

}
