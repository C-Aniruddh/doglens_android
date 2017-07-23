package com.achandratre.doglens;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.achandratre.doglens.fragments.SlideFive;
import com.achandratre.doglens.fragments.SlideFour;
import com.achandratre.doglens.fragments.SlideOne;
import com.achandratre.doglens.fragments.SlideThree;
import com.achandratre.doglens.fragments.SlideTwo;
import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        addSlide(SlideOne.newInstance(R.layout.slide_one));
        addSlide(SlideTwo.newInstance(R.layout.slide_two));
        addSlide(SlideThree.newInstance(R.layout.slide_three));
        addSlide(SlideFour.newInstance(R.layout.slide_four));
        addSlide(SlideFive.newInstance(R.layout.slide_five));

        askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 2);
        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

        //setIndicatorColor(Color.parseColor("#fff"), Color.parseColor("#eee"));
        //setSeparatorColor(Color.parseColor("#000"));
        //setBarColor(Color.parseColor("#000"));
        showSkipButton(false);
        setProgressButtonEnabled(true);

    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
