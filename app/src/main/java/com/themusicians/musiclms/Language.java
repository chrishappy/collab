package com.themusicians.musiclms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;

import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

import java.util.Locale;

public class Language extends BaseAc {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("第一个Activity");
        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Language.this, SettingAc.class));
            }
        });
    }
}