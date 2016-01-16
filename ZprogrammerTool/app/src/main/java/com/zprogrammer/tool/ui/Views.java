package com.zprogrammer.tool.ui;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.zprogrammer.tool.BuildConfig;
import com.zprogrammer.tool.MyApplication;
import com.zprogrammer.tool.R;
import com.zprogrammer.tool.view.SampleSlide;

public class Views extends AppIntro2 {
    public static final String PREF_KEY_LAST_VERSION = "pref_key_last_version";

    @Override
    public void init(Bundle savedInstanceState) {
            addSlide(SampleSlide.newInstance(R.layout.one));
            addSlide(SampleSlide.newInstance(R.layout.two));
            addSlide(SampleSlide.newInstance(R.layout.three));
            setFadeAnimation();
    }

    @Override
    public void onDonePressed() {
        MyApplication.getInstance().editint(PREF_KEY_LAST_VERSION, BuildConfig.VERSION_CODE);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
