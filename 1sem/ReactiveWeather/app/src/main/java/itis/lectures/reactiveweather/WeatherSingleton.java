package itis.lectures.reactiveweather;

import rx.Observable;
import rx.Subscription;

/**
 * @author Ilya Chirkov
 */
public class WeatherSingleton {
    private static WeatherSingleton mInstance;
    private Observable<Weather> mRequest;

    private WeatherSingleton() {
    }

    public static WeatherSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new WeatherSingleton();
        }
        return mInstance;
    }

    public void saveWeatherRequest(Observable<Weather> request) {
        mRequest = request;
    }

    public Observable<Weather> getWeatherRequest() {
        return mRequest;
    }

    public void removeWeatherRequest() {
        mRequest = null;
    }
}
