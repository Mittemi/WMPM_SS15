package carrental;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Michael on 13.05.2015.
 */
@Configuration
/*
One time only: docker run --name mongo -d -v /mongo -p 27017:27017 -p 28017:28017 -e AUTH=no tutum/mongodb
Start: docker start mongo
Stop: docker stop mongo
 */
public class MongoDbConfiguration extends AbstractMongoConfiguration {

    @Autowired
    private CarRentalConfig carRentalConfig;

    @Override
    public String getDatabaseName() {
        return carRentalConfig.getReservation().getMongo().getName();
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        //return new MongoClient(properties.getMongoDBConf().getIpOrHostname());
        return new MongoClient(carRentalConfig.getReservation().getMongo().getHost());
    }
}