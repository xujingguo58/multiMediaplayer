package top.xujingguo.multiMediaPlayer.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.dl7.player.media.IjkPlayerView;

//import com.dl7.player.media.IjkPlayerView;

public class IjkFullscreenActivity extends AppCompatActivity {

    private static  String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/nNTov5571/SD/nNTov5571-mobile.mp4";
    private static String VIDEO_NAME;
    private static String IMAGE_URL;
    IjkPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get path from last activity
        Intent intent = getIntent();
        VIDEO_URL = intent.getStringExtra("videoPath");
        VIDEO_NAME = intent.getStringExtra("videoName");
        mPlayerView = new IjkPlayerView(this);
        setContentView(mPlayerView);
        Glide.with(this).load(IMAGE_URL).fitCenter().into(mPlayerView.mPlayerThumb);
        mPlayerView.init()
                .alwaysFullScreen()
                .enableOrientation()
                .setVideoPath(VIDEO_URL)
                //.enableDanmaku()
                //.setDanmakuSource(getResources().openRawResource(R.raw.bili))
                .setTitle(VIDEO_NAME)
                .start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
