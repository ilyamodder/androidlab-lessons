package ru.ilyamodder.intentservicetest.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ilya on 15.02.16.
 */
public class Weather implements Serializable {
    @SerializedName("list")
    private List<Data> mList;

    public static class Data implements Serializable {
        @SerializedName("main")
        private WeatherData mData;
        @SerializedName("dt")
        private long mDatestamp;

        public WeatherData getData() {
            return mData;
        }

        public long getDatestamp() {
            return mDatestamp;
        }

        public void setData(WeatherData mData) {
            this.mData = mData;
        }

        public void setDatestamp(long mDatestamp) {
            this.mDatestamp = mDatestamp;
        }
    }

    public static class WeatherData implements Serializable {
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

        public void setTemperature(double mTemperature) {
            this.mTemperature = mTemperature;
        }

        public void setHumidity(int mHumidity) {
            this.mHumidity = mHumidity;
        }
    }

    public List<Data> getList() {
        return mList;
    }

    public void setList(List<Data> mList) {
        this.mList = mList;
    }
}
