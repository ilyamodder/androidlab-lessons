package ru.ilyamodder.intentservicetest.classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya on 15.02.16.
 */
public class Weather {
    @SerializedName("list")
    private List<Data> mList;

    private class Data {
        @SerializedName("main")
        private WeatherData mData;
        @SerializedName("dt")
        private int mDatestamp;

        public WeatherData getData() {
            return mData;
        }

        public int getDatestamp() {
            return mDatestamp;
        }
    }

    private class WeatherData {
        @SerializedName("temp")
        private double mTemperature;
        @SerializedName("humidity")
        private int mHumidity;

        public double getTemperature() {
            return mTemperature;
        }

        public int getHumidity() {
            return mHumidity;
        }
    }

    public List<Data> getList() {
        return mList;
    }
}
