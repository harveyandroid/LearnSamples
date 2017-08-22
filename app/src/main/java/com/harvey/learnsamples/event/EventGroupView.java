package com.harvey.learnsamples.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by hanhui on 2017/7/31 0031 14:49
 */

public class EventGroupView extends LinearLayout {
    final String TAG = "ViewGroup";

    public EventGroupView(Context context) {
        super(context);
    }

    public EventGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "--onTouchEvent--super--"+event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "--dispatchTouchEvent--super--"+event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "--onInterceptTouchEvent--super--"+ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }
}
