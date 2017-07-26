package com.lijunyan.blackmusic.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
//import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.media.IjkPlayerView;
import com.lijunyan.blackmusic.R;

public class IjkFullscreenActivity extends AppCompatActivity {

    //private static  String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/nNTov5571/SD/nNTov5571-mobile.mp4";
    private static  String VIDEO_URL;
    private static String VIDEO_NAME;
    private static final String IMAGE_URL = "http://vimg3.ws.126.net/image/snapshot/2016/11/C/T/VC628QHCT.jpg";
    IjkPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ijk_fullscreen);

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
}
