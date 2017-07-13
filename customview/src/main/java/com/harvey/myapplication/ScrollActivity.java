package com.harvey.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScrollActivity extends AppCompatActivity implements MyScrollView.OnScrollListener {
    @Bind(R.id.sv_class)
    MyScrollView myScrollView;
    @Bind(R.id.lv_class)
    ListView lvClass;
    @Bind(R.id.tv_setting_time)
    TextView settingTimeView;
    @Bind(R.id.tv_bottom_setting_time)
    TextView bottomSettingTimeView;
    @Bind(R.id.btn_bottom)
    Button btnBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        ButterKnife.bind(this);
        myScrollView.setOnScrollListener(this);
        List data = new ArrayList();
        for (int i = 0; i < 20; i++)
            data.add("幼儿园（" + i + "班）");
        lvClass.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, data));
        findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(myScrollView.getScrollY());
            }
        });

    }

    @Override
    public void onScroll(int scrollY) {
        Log.d("ScrollActivity", "onScroll==" + scrollY);
        Log.d("ScrollActivity", "btnBottom Top()" + btnBottom.getTop() + "bottom()" + btnBottom.getBottom());
        Log.d("ScrollActivity", "settingTimeView Top()" + settingTimeView.getTop() + "bottom()" + settingTimeView.getBottom());
        Log.d("ScrollActivity", "bottomSettingTimeView Top()" + bottomSettingTimeView.getTop() + "bottom()" + bottomSettingTimeView.getBottom());

        int bottomSettingTop = 0;
        int bottomSettingBottom = 0;

        if (settingTimeView.getTop() > btnBottom.getTop()) {
            bottomSettingBottom = btnBottom.getTop();
            bottomSettingTop = bottomSettingBottom - bottomSettingTimeView.getHeight();
        } else {
            bottomSettingTop = settingTimeView.getTop();
            bottomSettingBottom = settingTimeView.getTop() + bottomSettingTimeView.getHeight();
        }
        bottomSettingTimeView.layout(0, bottomSettingTop, bottomSettingTimeView.getWidth(), bottomSettingBottom);


/*顶部悬浮*/
//        int mBuyLayout2ParentTop = Math.max(scrollY, settingTimeView.getTop());
//        bottomSettingTimeView.layout(0, mBuyLayout2ParentTop, bottomSettingTimeView.getWidth(), mBuyLayout2ParentTop + bottomSettingTimeView.getHeight());
    }
}
