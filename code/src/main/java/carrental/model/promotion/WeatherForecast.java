package carrental.model.promotion;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Thomas on 07.06.2015.
 */
public class WeatherForecast {

    @SerializedName("cod")
    private int code;

    @SerializedName("cnt")
    private int numberOfDays;

    @SerializedName("list")
    private List<DayWeather> dayWeatherList;

    public List<DayWeather> getDayWeatherList() {
        return dayWeatherList;
    }

    public void setDayWeatherList(List<DayWeather> dayWeatherList) {
        this.dayWeatherList = dayWeatherList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public WeatherForecast() {

    }
}
