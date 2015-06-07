package carrental.model.promotion;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Thomas on 07.06.2015.
 */
public class DayWeather {

    @SerializedName("dt")
    private long dateInMillies;

    private int humidity;

    private double pressure;

    @SerializedName("temp")
    private Temperature temperature;

    private double rain;

    private double clouds;

    public long getDateInMillies() {
        return dateInMillies;
    }

    public void setDateInMillies(long dateInMillies) {
        this.dateInMillies = dateInMillies;
    }

    public Date getDate() {
        return new Date(dateInMillies);
    }

    public void setDate(Date date) {
        this.dateInMillies = date.getTime();
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public DayWeather() {

    }
}
