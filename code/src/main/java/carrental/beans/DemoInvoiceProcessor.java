package carrental.beans;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Thomas on 12.05.2015.
 */
//@Component
public class DemoInvoiceProcessor {

    @Autowired
    private ProducerTemplate producerTemplate;

    //@Scheduled(fixedRate = 2500)
    public void processNextInvoice() {
            producerTemplate.sendBody("activemq:queue:invoices", "This is a test invoice! sent at: " + new Date());
    }

}
