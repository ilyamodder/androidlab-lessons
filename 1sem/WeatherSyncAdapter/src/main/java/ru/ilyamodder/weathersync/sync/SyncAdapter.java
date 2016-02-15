/*
 * Copyright 2012-2014 Daniel Serdyukov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.ilyamodder.weathersync.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import ru.ilyamodder.weathersync.sqlite.CitiesProvider;
import ru.ilyamodder.weathersync.weather.WeatherParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author =Troy= <Daniel Serdyukov>
 * @version 1.0
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String KEY_CITY_ID = "ru.ilyamodder.weathersync.sync.KEY_CITY_ID";

    public SyncAdapter(Context context) {
        super(context, true);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
                              SyncResult syncResult) {
        final long feedId = extras.getLong(KEY_CITY_ID, -1);
        if (feedId > 0) {
            syncCities(provider, syncResult, CitiesProvider.Columns._ID + "=?", new String[]{String.valueOf(feedId)});
        } else {
            syncCities(provider, syncResult, null, null);
        }
    }

    private void syncCities(ContentProviderClient provider, SyncResult syncResult, String where, String[] whereArgs) {
        try {
            final Cursor cities = provider.query(
                    CitiesProvider.URI, new String[]{
                            CitiesProvider.Columns._ID,
                            CitiesProvider.Columns.WEATHERMAP_ID
                    }, where, whereArgs, null
            );
            try {
                if (cities.moveToFirst()) {
                    do {
                        syncCity(CitiesProvider.getId(cities), CitiesProvider.getWeatherMapId(cities), provider, syncResult);
                    } while (cities.moveToNext());
                }
            } finally {
                cities.close();
            }
        } catch (RemoteException e) {
            Log.e(SyncAdapter.class.getName(), e.getMessage(), e);
            ++syncResult.stats.numIoExceptions;
        }
    }

    private void syncCity(long cityId, int weatherMapCityId, ContentProviderClient provider, SyncResult syncResult) {
        try {
            final HttpURLConnection cn =
                    (HttpURLConnection) new URL("http://api.openweathermap.org/data/2.5/forecast?id="
                            + weatherMapCityId + "&mode=json&units=metric&appid=f5a276cac275c8fbfe0f527064ec9de1").openConnection();
            try {
                final WeatherParser parser = new WeatherParser(cn.getInputStream());
                try {
                    parser.parse(cityId, provider, syncResult);
                } finally {
                    parser.close();
                }
            } finally {
                cn.disconnect();
            }
        } catch (IOException e) {
            Log.e(SyncAdapter.class.getName(), e.getMessage(), e);
            ++syncResult.stats.numIoExceptions;
        }
    }

}
