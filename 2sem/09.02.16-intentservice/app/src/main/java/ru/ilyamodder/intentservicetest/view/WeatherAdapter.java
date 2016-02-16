package ru.ilyamodder.intentservicetest.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.ilyamodder.intentservicetest.R;
import ru.ilyamodder.intentservicetest.classes.Weather;

/**
 * Created by ilya on 15.02.16.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Weather mWeather;
    private Context mContext;

    public WeatherAdapter(Context context, Weather weather) {
        this.mWeather = weather;
        this.mContext = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new WeatherViewHolder(inflater.inflate(android.R.layout.simple_list_item_2,
                parent, false));
    }

    @Override
    public int getItemCount() {
        return mWeather.getList().size();
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        Weather.Data data = mWeather.getList().get(position);
        holder.mText1.setText(String.format("%.2f °C", data.getData().getTemperature()));
        holder.mText2.setText(DateFormat.format("dd.MM hh:mm", data.getDatestamp()));
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1)
        TextView mText1;
        @Bind(android.R.id.text2)
        TextView mText2;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
