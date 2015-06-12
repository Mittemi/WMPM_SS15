package carrental;

import carrental.repository.pickupPoint.PickupProtocolRepository;
import carrental.repository.pickupPoint.ReservationRepository;
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

import java.io.File;

/* http://camel.apache.org/spring-boot.html */
@EntityScan(basePackages = { "carrental.model.reservation" })       //jpa only
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({CarRentalConfig.class})
public class CarRentalApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplication(CarRentalApplication.class).run(args);

        //CLEAN-UP
        cleanupDatabase(applicationContext);
        cleanupFileSystem(applicationContext);

        CamelSpringBootApplicationController applicationController =
                applicationContext.getBean(CamelSpringBootApplicationController.class);
        applicationController.blockMainThread();
    }

    private static void cleanupFileSystem(ApplicationContext applicationContext) {
        File file = new File("claims/");

        if(file.exists() && file.isDirectory()) {
            for (File claimFile : file.listFiles()) {
                claimFile.delete();
            }
        }
    }

    private static void cleanupDatabase(ApplicationContext applicationContext) {
        ReservationRepository reservationRepository = applicationContext.getBean(ReservationRepository.class);
        reservationRepository.deleteAll();
        PickupProtocolRepository pickupProtocolRepository = applicationContext.getBean(PickupProtocolRepository.class);
        pickupProtocolRepository.deleteAll();
    }
}
