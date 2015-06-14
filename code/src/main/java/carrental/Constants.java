package carrental;

import java.util.HashMap;
import java.util.Map;

import carrental.model.pickupPoint.ClaimType;

/**
 * Created by Michael on 13.05.2015.
 */
public class Constants {
    public static final String RESERVATION_PU = "reservation";

    public static final boolean ENABLE_CAR_RETURN = true;

    public static final boolean ENABLE_CANCEL_PICKUP = true;
    
    public static final Map<Enum<ClaimType>,Double> servicePrices=new HashMap<Enum<ClaimType>,Double>();
    
    static{
    	servicePrices.put(ClaimType.Paintwork, 350.50);
    	servicePrices.put(ClaimType.Mechanical, 632.39);
    	servicePrices.put(ClaimType.Electrical, 577.82);
    	servicePrices.put(ClaimType.Cleaning, 132.61);
    	servicePrices.put(ClaimType.Refilling, 60.54);
    }
}
