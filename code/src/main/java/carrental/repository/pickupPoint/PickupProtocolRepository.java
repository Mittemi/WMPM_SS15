package carrental.repository.pickupPoint;

import carrental.model.pickupPoint.PickupProtocol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Michael on 13.05.2015.
 */
public interface PickupProtocolRepository extends MongoRepository<PickupProtocol, Long> {
}
