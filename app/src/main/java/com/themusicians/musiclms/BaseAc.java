package com.themusicians.musiclms;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;


public class BaseAc extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        changeAppLanguage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        switch (str) {
            case "EVENT_REFRESH_LANGUAGE":
                changeAppLanguage();
                recreate();
                break;
        }
    }


    public void changeAppLanguage() {
        String sta = Store.getLanguageLocal(this);
        if(sta != null && !"".equals(sta)){
            Locale myLocale = new Locale(sta);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}