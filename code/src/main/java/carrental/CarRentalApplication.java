package carrental;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/* http://camel.apache.org/spring-boot.html */
@EntityScan(basePackages = { "carrental.model.reservation" })       //jpa only
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({CarRentalConfig.class})
public class CarRentalApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplication(CarRentalApplication.class).run(args);
        CamelSpringBootApplicationController applicationController =
                applicationContext.getBean(CamelSpringBootApplicationController.class);
        applicationController.blockMainThread();
    }
}
