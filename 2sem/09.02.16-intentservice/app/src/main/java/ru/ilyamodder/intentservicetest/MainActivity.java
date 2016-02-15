package ru.ilyamodder.intentservicetest;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ru.ilyamodder.intentservicetest.receiver.MyResultReceiver;
import ru.ilyamodder.intentservicetest.service.MyIntentService;

import static ru.ilyamodder.intentservicetest.service.MyIntentService.*;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        MyResultReceiver resultReceiver = new MyResultReceiver(new Handler(Looper.getMainLooper()));
        resultReceiver.setReceiver(this);

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("receiver", resultReceiver);
        intent.putExtra("request", "getWeather");
        startService(intent);

        mTextView.setText("Loading...");
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        if (resultCode == STATUS_FINISHED) {
            mTextView.setText("Success");
        }
    }
}
