package com.harvey.glide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GlideActivity extends AppCompatActivity {
    @Bind(R.id.iv_glide)
    ImageView ivGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        ButterKnife.bind(this);
//        Glide.with(this).load(R.mipmap.ic_guide_1).thumbnail(0.1f).skipMemoryCache(true).override(720, 1280).into(ivGlide);
    }
}
