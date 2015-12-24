package itis.lectures.reactiveweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String[] WEATHER_ITEMS = {
            "Kazan", "Moscow", "Paris", "London",
            "Washington", "Madrid", "Rome", "Berlin"
    };

    private WeatherAdapter mWeatherAdapter;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWeatherAdapter = new WeatherAdapter(this);
        recyclerView.setAdapter(mWeatherAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable<Weather> request = WeatherSingleton.getInstance().getWeatherRequest();
        if (request == null) {
            request = Observable.interval(0, 30, TimeUnit.SECONDS).flatMap(i -> Observable.from(WEATHER_ITEMS)
                        .flatMap(ApiFactory::weatherFromQuery))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache();
            WeatherSingleton.getInstance().saveWeatherRequest(request);
        }
        mSubscription = request.subscribe(mWeatherAdapter::add);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSubscription.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeatherSingleton.getInstance().removeWeatherRequest();
    }

    //TODO : add weather forecast for all weather; use any rx architecture you like

}
