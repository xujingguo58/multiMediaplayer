package com.lijunyan.blackmusic;

import android.graphics.Bitmap;

/**
 * Created by deemo on 17-7-25.
 */

public class videoInfo {
    private int id; //id
    private String path; //路径
    private String videoName;  //名字
    private Bitmap  thumb;  //缩略图
    private long size;  //长度

    public videoInfo(int id, String path, String videoName, Bitmap  thumb,long size){
        this.id = id;
        this.path = path;
        this.videoName = videoName;
        this.thumb = thumb;
        this.size = size;
    }
    public int getId(){
        return  id;
    }
    public String getVideoName(){
        return videoName;
    }
    public String getPath(){
        return path;
    }
    public Bitmap getThumb(){
        return thumb;
    }
    public Long getSize(){
        return size;
    }
}
