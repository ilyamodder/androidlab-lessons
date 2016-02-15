package ru.ilyamodder.weathersync.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Ilya Chirkov
 */
public class WeatherData {
    @SerializedName("list")
    private List<Measurement> mMeasurements;

    @SerializedName("city")
    private City mCity;

    private class City {
        @SerializedName("name")
        private String mName;
    }

    public class Measurement {
        @SerializedName("dt")
        private long mDate;
        @SerializedName("main")
        private Main mMain;
        @SerializedName("wind")
        private Wind mWind;

        public long getDate() {
            return mDate;
        }

        public double getTemp() {
            return mMain.mTemp;
        }

        public double getWindSpeed() {
            return mWind.mSpeed;
        }
    }

    private class Main {
        @SerializedName("temp")
        private double mTemp;
    }

    private class Wind {
        @SerializedName("speed")
        private double mSpeed;
    }

    public List<Measurement> getMeasurements() {
        return mMeasurements;
    }

    public String getCityName() {
        return mCity.mName;
    }
}
