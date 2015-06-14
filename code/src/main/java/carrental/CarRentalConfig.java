package carrental;

import carrental.model.pickupPoint.Reservation;

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

        private String availabilityUrl;

        public String getAvailabilityUrl() {
            return availabilityUrl;
        }

        public void setAvailabilityUrl(String availabilityUrl) {
            this.availabilityUrl = availabilityUrl;
        }

    }

    public static class PickupPoint {

        @NotNull
        private Mongo mongo;

        @NotNull
        private Integer customerEntryTime;

        public Mongo getMongo() {
            return mongo;
        }

        public void setMongo(Mongo mongo) {
            this.mongo = mongo;
        }
    }

    @NotNull
    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @NotNull
    private PickupPoint pickupPoint;

    @NotNull
    private Billing billing;
    
    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public PickupPoint getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(PickupPoint pickupPoint) {
        this.pickupPoint = pickupPoint;
    }
    
    //Created by Alex for EMail-purposes
    @NotNull
    private Email email;
    
    //Created by Alex for EMail-purposes
    public static class Email{
    	private String smtpAddress;
    	private String smtpUsername;
    	private String smtpPassword;
    	private String smtpPort;
    	
		public String getSmtpAddress() {
			return smtpAddress;
		}
		public String getSmtpUsername() {
			return smtpUsername;
		}
		public String getSmtpPassword() {
			return smtpPassword;
		}
		public String getSmtpPort() {
			return smtpPort;
		}
		public void setSmtpAddress(String smtpAddress) {
			this.smtpAddress = smtpAddress;
		}
		public void setSmtpUsername(String smtpUsername) {
			this.smtpUsername = smtpUsername;
		}
		public void setSmtpPassword(String smtpPassword) {
			this.smtpPassword = smtpPassword;
		}
		public void setSmtpPort(String smtpPort) {
			this.smtpPort = smtpPort;
		}
    }
    
    //Created by Alex for EMail-purposes
    public Email getEmail(){
    	return this.email;
    }
    
    //Created by Alex for EMail-purposes    
    public void setEmail(Email email){
    	this.email=email;
    }
}
