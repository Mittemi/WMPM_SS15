package carrental;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/**
 * Created by Thomas on 23.05.2015.
 */
@Configuration
public class JmsConfig {
    private static final String JMS_BROKER_URL = "vm://localhost";

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(JMS_BROKER_URL);
    }

}
