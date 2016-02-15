package ru.ilyamodder.intentservicetest;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vlonjatg.progressactivity.ProgressActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.ilyamodder.intentservicetest.classes.Weather;
import ru.ilyamodder.intentservicetest.receiver.MyResultReceiver;
import ru.ilyamodder.intentservicetest.service.MyIntentService;
import ru.ilyamodder.intentservicetest.view.WeatherAdapter;

import static ru.ilyamodder.intentservicetest.service.MyIntentService.*;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.progressActivity)
    ProgressActivity mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendRequest();
    }

    private void sendRequest() {
        MyResultReceiver resultReceiver = new MyResultReceiver(new Handler(Looper.getMainLooper()));
        resultReceiver.setReceiver(this);

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("receiver", resultReceiver);
        intent.putExtra("request", "getWeather");
        intent.putExtra("city", "Kazan,ru");
        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case STATUS_RUNNING:
                mProgress.showLoading();
                break;
            case STATUS_FINISHED:
                Weather weather = (Weather) data.getSerializable("response");
                mRecyclerView.setAdapter(new WeatherAdapter(weather));
                mProgress.showContent();
                break;
            case STATUS_ERROR:
                mProgress.showError(null, "Ошибка загрузки!", "Попробуйте попытку позднее",
                        "Повторить", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendRequest();
                            }
                        });
                break;
        }
    }
}
