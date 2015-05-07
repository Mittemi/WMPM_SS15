package carrental;

import carrental.beans.DemoPrintTextBean;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael on 07.05.2015.
 */
@Configuration
public class CamelConfig {

    public CamelConfig() {
        System.out.println("Read Configuration");
    }

    @Autowired
    CamelContext camelContext;

    @Bean
    DemoPrintTextBean printTextBean() {
        System.out.println("Call bean getter!");
        return new DemoPrintTextBean();
    }

}
