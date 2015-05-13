package carrental.model.pickupPoint;

import org.springframework.data.annotation.Id;

/**
 * Created by Michael on 13.05.2015.
 */
public class Claim {

    private ClaimType claimType;

    @Id
    private Long id;

    private String description;
}
