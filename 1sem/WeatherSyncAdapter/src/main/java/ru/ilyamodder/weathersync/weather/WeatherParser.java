package ru.ilyamodder.weathersync.weather;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.SyncResult;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ilyamodder.weathersync.sqlite.CitiesProvider;
import ru.ilyamodder.weathersync.sqlite.WeatherProvider;

/**
 * @author Ilya Chirkov
 */
public class WeatherParser implements Closeable {

    private final InputStream mInputStream;

    public WeatherParser(InputStream mInputStream) {
        this.mInputStream = mInputStream;
    }

    public void parse(long cityId, ContentProviderClient provider, SyncResult result) {
        try {
            WeatherData data = new Gson().fromJson(new InputStreamReader(mInputStream), WeatherData.class);

            result.stats.numDeletes += provider.delete(WeatherProvider.URI, WeatherProvider.Columns.CITY_ID + "=?", new String[]{String.valueOf(cityId)});

            ContentValues cityValues = new ContentValues();
            cityValues.put(CitiesProvider.Columns.NAME, data.getCityName());
            result.stats.numUpdates += provider
                    .update(CitiesProvider.URI, cityValues, CitiesProvider.Columns._ID + "=?", new String[]{String.valueOf(cityId)});

            List<ContentValues> insertData = new ArrayList<>();
            for (WeatherData.Measurement measurement : data.getMeasurements()) {
                ContentValues cv = new ContentValues();
                cv.put(WeatherProvider.Columns.CITY_ID, cityId);
                cv.put(WeatherProvider.Columns.TEMP, measurement.getTemp());
                cv.put(WeatherProvider.Columns.DATE, measurement.getDate());
                cv.put(WeatherProvider.Columns.WIND_SPEED, measurement.getWindSpeed());
                insertData.add(cv);
            }
            result.stats.numInserts += provider.bulkInsert(WeatherProvider.URI, insertData.toArray(new ContentValues[insertData.size()]));
        } catch (Exception e) {
            ++result.stats.numParseExceptions;
        }
    }

    @Override
    public void close() {
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), e.getMessage(), e);
            }
        }
    }
}
