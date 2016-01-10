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

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.ilyamodder.weathersync.R;
import ru.ilyamodder.weathersync.sqlite.CitiesProvider;
import ru.ilyamodder.weathersync.widget.CursorBinder;

/**
 * @author Daniel Serdyukov
 */
public class CitiesListItem extends LinearLayout implements CursorBinder {

    private TextView mName;

    public CitiesListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void bindCursor(Cursor c) {
        final String title = CitiesProvider.getName(c);
        if (!TextUtils.isEmpty(title)) {
            mName.setText(title);
        } else {
            mName.setText(getResources().getString(R.string.city_p, CitiesProvider.getId(c)));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mName = (TextView) findViewById(R.id.temp);
    }

}
