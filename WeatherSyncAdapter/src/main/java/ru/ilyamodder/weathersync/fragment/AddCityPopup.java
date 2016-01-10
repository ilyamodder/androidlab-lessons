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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import ru.ilyamodder.weathersync.R;
import ru.ilyamodder.weathersync.sqlite.CitiesProvider;

/**
 * @author Daniel Serdyukov
 */
public class AddCityPopup extends DialogFragment {

    private EditText mCityId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fmt_add_feed_popup, null);
        mCityId = (EditText) view.findViewById(R.id.link);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_city)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onAddFeed();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void onAddFeed() {
        final String link = mCityId.getText().toString();
        if (!TextUtils.isEmpty(link)) {
            final ContentValues values = new ContentValues();
            values.put(CitiesProvider.Columns.WEATHERMAP_ID, Integer.parseInt(link));
            getActivity().getContentResolver().insert(CitiesProvider.URI, values);
        }
    }

}
