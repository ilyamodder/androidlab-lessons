package ru.samples.itis.loaders;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ru.samples.itis.loaders.weather.RetrofitLoader;
import ru.samples.itis.loaders.weather.Weather;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Weather> {
    TextView mWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mWeather = (TextView) findViewById(R.id.weather);
        getLoaderManager().initLoader(R.id.weather_loader, Bundle.EMPTY, this);
    }

    @Override
    public Loader<Weather> onCreateLoader(int id, Bundle args) {
        if (id == R.id.weather_loader) {
            return new RetrofitLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Weather> loader, Weather data) {
        if (loader.getId() == R.id.weather_loader) {
            if (data == null) {
                mWeather.setText("Ошибка загрузки");
            } else {
                mWeather.setText(String.format("%s: %s°C", data.getName(), data.getTemp()));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Weather> loader) {
    }
}


