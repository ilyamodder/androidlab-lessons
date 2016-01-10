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

package ru.ilyamodder.weathersync.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Daniel Serdyukov
 */
public class WeatherProvider extends SQLiteTableProvider {

    public static final String TABLE_NAME = "weather_data";

    public static final Uri URI = Uri.parse("content://ru.ilyamodder.weathersync/" + TABLE_NAME);

    public WeatherProvider() {
        super(TABLE_NAME);
    }

    public static long getId(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns._ID));
    }

    public static double getTemp(Cursor c) {
        return c.getDouble(c.getColumnIndex(Columns.TEMP));
    }

    public static long getDate(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.DATE));
    }

    public static double getWindSpeed(Cursor c) {
        return c.getDouble(c.getColumnIndex(Columns.WIND_SPEED));
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME +
                "(" + Columns._ID + " integer primary key on conflict replace, "
                + Columns.TEMP + " double, "
                + Columns.DATE + " integer, "
                + Columns.WIND_SPEED + " double, "
                + Columns.CITY_ID + " integer);");
        db.execSQL("create index if not exists " +
                TABLE_NAME + "_" + Columns.CITY_ID + "_index" +
                " on " + TABLE_NAME + "(" + Columns.CITY_ID + ");");
    }

    public interface Columns extends BaseColumns {
        String TEMP = "temp";
        String DATE = "date";
        String WIND_SPEED = "windSpeed";
        String CITY_ID = "cityId";
    }

}
