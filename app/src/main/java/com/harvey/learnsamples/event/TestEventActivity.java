package com.harvey.learnsamples.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.harvey.learnsamples.R;

public class TestEventActivity extends AppCompatActivity {
    final String TAG = "Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "--onTouchEvent--super--" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "--dispatchTouchEvent--super--" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }
}
