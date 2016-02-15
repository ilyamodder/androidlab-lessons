package ru.ilyamodder.intentservicetest.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ilyamodder.intentservicetest.BuildConfig;
import ru.ilyamodder.intentservicetest.classes.Weather;

/**
 * Created by ilya on 15.02.16.
 */
public interface OpenWeatherMapApi {

    @GET("forecast?mode=json&appid=" + BuildConfig.API_KEY)
    Call<Weather> get5DaysForecast(@Query("q") String city);
}
