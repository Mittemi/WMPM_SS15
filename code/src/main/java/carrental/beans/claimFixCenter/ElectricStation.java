package carrental.beans.claimFixCenter;

import carrental.model.pickupPoint.Claim;
import carrental.routing.claimFixCenter.ClaimDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Constantin on 03.06.2015.
 */
@Component
public class ElectricStation{
    @Autowired
    private JmsTemplate jmsTemplate;
    @JmsListener(destination = "electricStation")
    public void fixClaim (ClaimDTO claimDto)
    {
        Claim claim= claimDto.getClaim();
        Long carId= claimDto.getCarId();
        System.out.println("Bean: ElectricStation fixed "+claim.getDescription() + " for car with ID="+ carId );
        jmsTemplate.convertAndSend("claimFixAdapter", claimDto);
    }
}
