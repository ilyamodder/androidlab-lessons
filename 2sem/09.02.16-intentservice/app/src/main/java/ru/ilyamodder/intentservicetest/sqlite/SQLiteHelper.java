package ru.ilyamodder.intentservicetest.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ilya on 16.02.16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;

    public static final String TABLE_WEATHER = "weather";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_TEMPERATURE = "temperature";
    public static final String FIELD_HUMIDITY = "humidity";

    public SQLiteHelper(Context context) {
        super(context, "cache.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_WEATHER +
                "(" + FIELD_ID + " integer primary key autoincrement, " +
                FIELD_DATE + " integer, " + FIELD_HUMIDITY + " integer, " +
                FIELD_TEMPERATURE + " double);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static long getDate(Cursor c) {
        return c.getLong(c.getColumnIndex(FIELD_DATE));
    }

    public static double getTemp(Cursor c) {
        return c.getDouble(c.getColumnIndex(FIELD_TEMPERATURE));
    }

    public static int getHumidity(Cursor c) {
        return c.getInt(c.getColumnIndex(FIELD_HUMIDITY));
    }
}
