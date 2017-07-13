package com.harvey.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hanhui on 2017/7/13 0013 11:49
 */

public class Custom360CleanView extends View {
    Paint paint = new Paint();
    float defaultSupX = 0;
    float defaultSupY = 0;
    float mSupX = 0;
    float mSupY = 0;
    int mHeight, mWidth;
    boolean isDrawBack = false;
    int mFlyPercent = 100;
    Bitmap cleanBitmap = null;

    public Custom360CleanView(Context context) {
        this(context, null);
    }

    public Custom360CleanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Custom360CleanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cleanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        defaultSupX = mWidth / 2;
        defaultSupY = mHeight * 2 / 3;
        mSupX = defaultSupX;
        mSupY = defaultSupY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(mWidth / 6, defaultSupY);
        path.quadTo(mSupX, defaultSupY + (mSupY - defaultSupY) * mFlyPercent / 100, mWidth * 5 / 6, defaultSupY);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 6, defaultSupY, 30, paint);
        canvas.drawCircle(mWidth * 5 / 6, defaultSupY, 30, paint);
        canvas.drawBitmap(cleanBitmap,
                defaultSupX - cleanBitmap.getWidth() / 2,
                ((mSupY - defaultSupY) / 2 + defaultSupY - cleanBitmap.getHeight()) * mFlyPercent / 100,
                paint);

        paint.setColor(Color.BLUE);

        if (!isDrawBack) {
            mFlyPercent = 100;
        } else {
            if (mFlyPercent > 0) {
                mFlyPercent -= 5;
                postInvalidateDelayed(10);
            } else {
                mSupX = defaultSupX;
                mSupY = defaultSupY;
                isDrawBack = false;
                mFlyPercent = 100;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                System.out.println("----------ACTION_MOVE-------------");
                if (event.getY() > defaultSupY) {
                    mSupY = (int) event.getY();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("----------ACTION_UP-------------");
                isDrawBack = true;
                mFlyPercent = 100;
                invalidate();
                break;
        }

        return true;
    }
}
