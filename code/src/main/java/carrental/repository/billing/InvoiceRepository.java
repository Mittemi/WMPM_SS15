package carrental.repository.billing;

import carrental.model.billing.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Michael on 14.05.2015.
 */
public interface InvoiceRepository extends MongoRepository<Invoice, Long> {
}
