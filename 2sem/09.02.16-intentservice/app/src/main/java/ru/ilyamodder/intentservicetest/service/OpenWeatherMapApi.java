package ru.ilyamodder.intentservicetest.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ilyamodder.intentservicetest.classes.Weather;

/**
 * Created by ilya on 15.02.16.
 */
public interface OpenWeatherMapApi {

    @GET("/forecast?mode=json")
    Weather get5DaysForecast(@Query("city") String city);
}
