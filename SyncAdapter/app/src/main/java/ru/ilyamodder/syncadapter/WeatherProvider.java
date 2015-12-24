package ru.ilyamodder.syncadapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import ru.ilyamodder.syncadapter.sqlite.SQLiteTableProvider;

/**
 * @author Ilya Chirkov
 */
public class WeatherProvider extends SQLiteTableProvider {

    public static final String TABLE_NAME = "weather";

    public static final Uri URI = Uri.parse("content://ru.ilyamodder.syncadapter/" + TABLE_NAME);

    public WeatherProvider() {
        super(TABLE_NAME);
    }

    public static long getId(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns._ID));
    }

    public static String getIconUrl(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.IMAGE_URL));
    }

    public static String getTitle(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.TITLE));
    }

    public static String getLink(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.LINK));
    }

    public static long getPubDate(Cursor c) {
        return c.getLong(c.getColumnIndex(Columns.PUB_DATE));
    }

    public static String getRssLink(Cursor c) {
        return c.getString(c.getColumnIndex(Columns.RSS_LINK));
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
            syncExtras.putLong(SyncAdapter.KEY_FEED_ID, extras.getLong(KEY_LAST_ID, -1));
            ContentResolver.requestSync(AppDelegate.sAccount, AppDelegate.AUTHORITY, syncExtras);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME +
                "(" + Columns._ID + " integer primary key on conflict replace, "
                + Columns.TITLE + " text, "
                + Columns.LINK + " text, "
                + Columns.IMAGE_URL + " text, "
                + Columns.LANGUAGE + " text, "
                + Columns.PUB_DATE + " integer, "
                + Columns.RSS_LINK + " text unique on conflict ignore)");
        db.execSQL("create trigger if not exists after delete on " + TABLE_NAME +
                " begin " +
                " delete from " + NewsProvider.TABLE_NAME + " where " + NewsProvider.Columns.FEED_ID + "=old." + Columns._ID + ";" +
                " end;");
    }

    public interface Columns extends BaseColumns {
        String TITLE = "title";
        String LINK = "link";
        String IMAGE_URL = "imageUrl";
        String LANGUAGE = "language";
        String PUB_DATE = "pubDate";
        String RSS_LINK = "rssLink";
    }

}
