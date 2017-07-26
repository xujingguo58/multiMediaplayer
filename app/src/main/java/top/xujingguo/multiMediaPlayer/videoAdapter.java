package top.xujingguo.multiMediaPlayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xujingguo on 2017/7/24.
 */

public class videoAdapter extends ArrayAdapter<videoInfo> {
    private int resourceid;

    public videoAdapter(Context context, int textViewResoureID, List<videoInfo> objects){
        super(context,textViewResoureID,objects);
        resourceid = textViewResoureID;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        videoInfo videoinfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceid,null);
        ImageView videoThumb = (ImageView) view.findViewById(R.id.thumb_image);
        TextView videoName = (TextView) view.findViewById(R.id.video_name);
        videoThumb.setImageBitmap(videoinfo.getThumb());
        videoName.setText(videoinfo.getVideoName());
        return view;
    }
}
