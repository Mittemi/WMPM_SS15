package carrental.repository.pickupPoint;

import carrental.model.pickupPoint.ReturnProtocol;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Michael on 14.05.2015.
 */
public interface ReturnProtocolRepository extends MongoRepository<ReturnProtocol, Long> {
}
