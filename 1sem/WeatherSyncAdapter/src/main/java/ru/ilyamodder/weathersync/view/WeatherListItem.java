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

package ru.ilyamodder.weathersync.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.ilyamodder.weathersync.R;
import ru.ilyamodder.weathersync.sqlite.WeatherProvider;
import ru.ilyamodder.weathersync.widget.CursorBinder;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Daniel Serdyukov
 */
public class WeatherListItem extends LinearLayout implements CursorBinder {

    private TextView mTemp;

    private TextView mWindSpeed;

    private TextView mDate;

    public WeatherListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    @SuppressLint("StringFormatMatches")
    public void bindCursor(Cursor c) {
        mTemp.setText(WeatherProvider.getTemp(c) + " °C");
        mWindSpeed.setText("Ветер: " + WeatherProvider.getWindSpeed(c) + " м/c");
        final long pubDate = WeatherProvider.getDate(c);
        if (pubDate > 0) {
            mDate.setText(DateFormat.getDateTimeInstance().format(new Date(pubDate*1000)));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTemp = (TextView) findViewById(R.id.temp);
        mWindSpeed = (TextView) findViewById(R.id.wind_speed);
        mDate = (TextView) findViewById(R.id.date);
    }

}
