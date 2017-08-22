package com.harvey.learnsamples.event;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hanhui on 2017/7/31 0031 14:50
 */

public class EventButtonView extends AppCompatButton implements View.OnClickListener, View.OnTouchListener {
    final String TAG = "View";

    public EventButtonView(Context context) {
        this(context, null);
    }

    public EventButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "--onTouchEvent--super--" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "--dispatchTouchEvent--super--" + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "--onClick--");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "--onTouch--true--" + event.getAction());
        return true;
    }
}
