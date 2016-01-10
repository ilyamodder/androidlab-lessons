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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import ru.ilyamodder.weathersync.AppDelegate;
import ru.ilyamodder.weathersync.sync.SyncAdapter;

/**
 * @author Daniel Serdyukov
 */
public class CitiesProvider extends SQLiteTableProvider {

    public static final String TABLE_NAME = "cities";

    public static final Uri URI = Uri.parse("content://ru.ilyamodder.weathersync/" + TABLE_NAME);

    public CitiesProvider() {
        super(TABLE_NAME);
    }

    public static long getId(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns._ID));
    }

    public static int getWeatherMapId(Cursor c) {
        return c.getInt(c.getColumnIndex(Columns.WEATHERMAP_ID));
    }

    public static String getName(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.NAME));
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }

    @Override
    public void onContentChanged(Context context, int operation, Bundle extras) {
        if (operation == INSERT) {
            extras.keySet();
            final Bundle syncExtras = new Bundle();
            syncExtras.putLong(SyncAdapter.KEY_CITY_ID, extras.getLong(KEY_LAST_ID, -1));
            ContentResolver.requestSync(AppDelegate.sAccount, AppDelegate.AUTHORITY, syncExtras);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME +
                "(" + Columns._ID + " integer primary key on conflict replace, "
                + Columns.NAME + " text, "
                + Columns.WEATHERMAP_ID + " integer unique on conflict ignore)");
        db.execSQL("create trigger if not exists after delete on " + TABLE_NAME +
                " begin " +
                " delete from " + WeatherProvider.TABLE_NAME + " where " + WeatherProvider.Columns.CITY_ID + "=old." + Columns._ID + ";" +
                " end;");
    }

    public interface Columns extends BaseColumns {
        String NAME = "name";
        String WEATHERMAP_ID = "weathermap_id";
    }

}
