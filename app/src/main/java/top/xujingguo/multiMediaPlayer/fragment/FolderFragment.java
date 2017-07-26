package top.xujingguo.multiMediaPlayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.xujingguo.multiMediaPlayer.activity.ModelActivity;
import top.xujingguo.multiMediaPlayer.adapter.FolderAdapter;
import top.xujingguo.multiMediaPlayer.database.DBManager;
import top.xujingguo.multiMediaPlayer.entity.FolderInfo;
import top.xujingguo.multiMediaPlayer.util.MyMusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujingguo on 2017/7/24.
 */

public class FolderFragment extends Fragment {
    
    private static final String TAG = "FolderFragment";
    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private List<FolderInfo> folderInfoList = new ArrayList<>();
    private DBManager dbManager;
    private Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(top.xujingguo.multiMediaPlayer.R.layout.fragment_singer,container,false);
        dbManager = DBManager.getInstance(getContext());
        recyclerView = (RecyclerView)view.findViewById(top.xujingguo.multiMediaPlayer.R.id.singer_recycler_view);
        adapter = new FolderAdapter(getContext(),folderInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onDeleteMenuClick(View content, int position) {

            }

            @Override
            public void onContentClick(View content, int position) {
                Intent intent = new Intent(mContext,ModelActivity.class);
                intent.putExtra(ModelActivity.KEY_TITLE,folderInfoList.get(position).getName());
                intent.putExtra(ModelActivity.KEY_TYPE, ModelActivity.FOLDER_TYPE);
                intent.putExtra(ModelActivity.KEY_PATH,folderInfoList.get(position).getPath());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        folderInfoList.clear();
        folderInfoList.addAll(MyMusicUtil.groupByFolder((ArrayList)dbManager.getAllMusicFromMusicTable()));
        Log.d(TAG, "onResume: folderInfoList.size() = "+folderInfoList.size());
        adapter.notifyDataSetChanged();
    }
}
