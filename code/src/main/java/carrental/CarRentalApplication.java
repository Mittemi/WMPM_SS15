package carrental;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/* http://camel.apache.org/spring-boot.html */
@EnableAutoConfiguration
@ComponentScan(basePackages = { "carrental.beans" , "carrental.routing" })
@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplication(CarRentalApplication.class).run(args);
        CamelSpringBootApplicationController applicationController =
                applicationContext.getBean(CamelSpringBootApplicationController.class);
        applicationController.blockMainThread();
    }
}
