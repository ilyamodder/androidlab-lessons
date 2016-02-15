package ru.ilyamodder.intentservicetest.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.ilyamodder.intentservicetest.BuildConfig;
import ru.ilyamodder.intentservicetest.classes.Weather;
import ru.ilyamodder.intentservicetest.receiver.MyResultReceiver;


public class MyIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public MyIntentService() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        receiver.send(STATUS_RUNNING, new Bundle());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL + BuildConfig.API_VERSION + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapApi api = retrofit.create(OpenWeatherMapApi.class);

        String request = intent.getStringExtra("request");

        switch (request) {
            case "getWeather":
                processWeatherRequest(intent, receiver, api);
        }
    }

    private void processWeatherRequest(Intent intent, final ResultReceiver receiver, final OpenWeatherMapApi api) {
        String city = intent.getStringExtra("city");

        api.get5DaysForecast(city).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("response", response.body());
                receiver.send(STATUS_FINISHED, bundle);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("error", t);
                receiver.send(STATUS_ERROR, bundle);
            }
        });
    }
}
