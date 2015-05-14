package carrental;

import com.sun.istack.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael on 14.05.2015.
 */
@ConfigurationProperties(locations = "classpath:application.yml", ignoreUnknownFields = true, prefix = "spring")
public class CarRentalConfig {

    public static class Mongo {
        private String host;

        private String name;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Billing {

        @NotNull
        private Mongo mongo;

        public Mongo getMongo() {
            return mongo;
        }

        public void setMongo(Mongo mongo) {
            this.mongo = mongo;
        }
    }

    public static class Reservation {

        @NotNull
        private Mongo mongo;

        public Mongo getMongo() {
            return mongo;
        }

        public void setMongo(Mongo mongo) {
            this.mongo = mongo;
        }
    }

    @NotNull
    private Reservation reservation;

    @NotNull
    private Billing billing;

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
