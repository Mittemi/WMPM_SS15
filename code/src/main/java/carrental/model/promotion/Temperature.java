package carrental.model.promotion;

/**
 * Created by Thomas on 07.06.2015.
 * all temperatures are measured in kelvin
 */
public class Temperature {

    private double day;
    private double min;
    private double max;
    private double night;
    private double eve;
    private double morn;

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public static double fromKelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
    public Temperature() {

    }
}
