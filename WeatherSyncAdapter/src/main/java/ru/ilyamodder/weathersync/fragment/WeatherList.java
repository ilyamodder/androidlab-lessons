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

package ru.ilyamodder.weathersync.fragment;

import android.accounts.Account;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import ru.ilyamodder.weathersync.AppDelegate;
import ru.ilyamodder.weathersync.R;
import ru.ilyamodder.weathersync.sqlite.WeatherProvider;
import ru.ilyamodder.weathersync.sync.SyncAdapter;
import ru.ilyamodder.weathersync.widget.CursorBinderAdapter;

/**
 * @author Daniel Serdyukov
 */
public class WeatherList extends SwipeToRefreshList implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String KEY_CITY_ID = "ru.ilyamodder.weathersync.KEY_CITY_ID";

    private long mFeedId;

    private CursorAdapter mListAdapter;

    public static WeatherList newInstance(long feedId) {
        final WeatherList fragment = new WeatherList();
        final Bundle args = new Bundle();
        args.putLong(KEY_CITY_ID, feedId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFeedId = getArguments().getLong(KEY_CITY_ID, -1);
        mListAdapter = new CursorBinderAdapter(getActivity(), R.layout.li_weather);
        setListAdapter(mListAdapter);
        getLoaderManager().initLoader(R.id.weather_loader, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.weather_loader) {
            return new CursorLoader(
                    getActivity().getApplicationContext(),
                    WeatherProvider.URI, null,
                    WeatherProvider.Columns.CITY_ID + "=?",
                    new String[]{String.valueOf(mFeedId)},
                    WeatherProvider.Columns.DATE
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == R.id.weather_loader) {
            mListAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == R.id.weather_loader) {
            mListAdapter.swapCursor(null);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor news = mListAdapter.getCursor();
        if (news.moveToPosition(position)) {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(WeatherProvider.getLink(news))));
        }
    }

    @Override
    protected void onRefresh(Account account) {
        final Bundle extras = new Bundle();
        extras.putLong(SyncAdapter.KEY_CITY_ID, mFeedId);
        ContentResolver.requestSync(account, AppDelegate.AUTHORITY, extras);
    }

    @Override
    protected void onSyncStatusChanged(Account account, boolean isSyncActive) {
        setRefreshing(isSyncActive);
    }

}
