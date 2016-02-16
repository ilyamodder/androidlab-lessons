package ru.ilyamodder.intentservicetest.view;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.ilyamodder.intentservicetest.classes.Weather;

/**
 * Created by ilya on 15.02.16.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Weather weather;

    public WeatherAdapter(Weather weather) {
        this.weather = weather;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new WeatherViewHolder(inflater.inflate(android.R.layout.simple_list_item_2,
                parent, false));
    }

    @Override
    public int getItemCount() {
        return weather.getList().size();
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        Weather.Data data = weather.getList().get(position);
        holder.mText1.setText(data.getData().getTemperature() + "°C");
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
