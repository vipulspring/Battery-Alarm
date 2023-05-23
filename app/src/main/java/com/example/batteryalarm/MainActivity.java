package com.example.batteryalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.batteryalarm.arcprogressbar.ColorArcProgressBar;
import com.github.furkankaplan.fkblurview.FKBlurView;

public class MainActivity extends AppCompatActivity {
ColorArcProgressBar colorArcProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

colorArcProgressBar = findViewById(R.id.batteryProgressBar);
        for (int i = 0; i <100 ; i++) {
            colorArcProgressBar.setCurrentValues(i);
        }

        //https://github.com/kyleduo/SwitchButton
        //https://github.com/warkiz/IndicatorSeekBar


    }
}