package com.harvey.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hanhui on 2017/7/13 0013 11:04
 */

public class BezierCurveView extends View {
    Paint paint;
    float supX = 300;
    float supY = 400;
    private int mHeight, mWidth;

    public BezierCurveView(Context context) {
        this(context, null);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        System.out.println("----------BezierCurveView-------------");
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        System.out.println("----------onMeasure-------------");
        System.out.println("mHeight:" + mHeight + ",mWidth:" + mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("----------onDraw-------------");
        Path path = new Path();
        path.moveTo(0, mHeight / 2);
        path.quadTo(supX, supY, mWidth, mHeight / 2);
        paint.setColor(Color.RED);
        canvas.drawPath(path, paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(0, mHeight / 2, supX, supY, paint);
        canvas.drawLine(supX, supY, mWidth, mHeight / 2, paint);
        super.onDraw(canvas);
    }


    @Override
    protected void onFinishInflate() {
        System.out.println("----------onFinishInflate-------------");
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        System.out.println("mHeight:" + mHeight + ",mWidth:" + mWidth);
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        System.out.println("----------onSizeChanged-------------");
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        System.out.println("mHeight:" + mHeight + ",mWidth:" + mWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.out.println("----------onLayout-------------");
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        System.out.println("mHeight:" + mHeight + ",mWidth:" + mWidth);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        System.out.println("----------onWindowFocusChanged-------------");
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        System.out.println("mHeight:" + mHeight + ",mWidth:" + mWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                supX = event.getX();
                supY = event.getY();
                invalidate();
                break;
        }
        return true;
    }
}
