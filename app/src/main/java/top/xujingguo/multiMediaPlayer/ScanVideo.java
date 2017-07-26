package top.xujingguo.multiMediaPlayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xujingguo on 2017/7/24.
 */

public class ScanVideo {
    private List<videoInfo> list = null;
    Context context;
    public  ScanVideo(Context context){
        this.context = context;
    }
    //@NeedsPermission(Mainfest.permission.READ_EXTERNAL_STORAGE)
    public List<videoInfo> getList() {


        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor != null) {
            list = new ArrayList<videoInfo>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String title = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String album = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                String artist = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                String videoName = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String path = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                long size = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                Bitmap thumb = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(path);
                    thumb = retriever.getFrameAtTime();
                } catch(IllegalArgumentException e) {
                    e.printStackTrace();
                }
                //Log.w("后缀",path.substring(path.indexOf(".")+1));
                if(path.substring(path.indexOf(".")+1).equals("mp4")) {
                    videoInfo videoinfo = new videoInfo(id, path, videoName, thumb, size);
                    list.add(videoinfo);
                }
            }
            cursor.close();
        }
        return list;
    }
}
