package top.xujingguo.multiMediaPlayer.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatDelegate;

import top.xujingguo.multiMediaPlayer.service.MusicPlayerService;

/**
 * Created by xujingguo on 2017/7/24.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Intent startIntent = new Intent(MyApplication.this,MusicPlayerService.class);
        startService(startIntent);
        initNightMode();
    }

    protected void initNightMode() {
        boolean isNight = MyMusicUtil.getNightMode(context);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static Context getContext() {
        return context;
    }
}
