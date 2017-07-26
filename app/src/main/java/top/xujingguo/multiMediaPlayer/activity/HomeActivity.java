package top.xujingguo.multiMediaPlayer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import top.xujingguo.multiMediaPlayer.R;
import top.xujingguo.multiMediaPlayer.ScanVideo;
import top.xujingguo.multiMediaPlayer.adapter.HomeListViewAdapter;
import top.xujingguo.multiMediaPlayer.database.DBManager;
import top.xujingguo.multiMediaPlayer.entity.PlayListInfo;
import top.xujingguo.multiMediaPlayer.fragment.VideoFragment;
import top.xujingguo.multiMediaPlayer.service.MusicPlayerService;
import top.xujingguo.multiMediaPlayer.util.Constant;
import top.xujingguo.multiMediaPlayer.util.HttpUtil;
import top.xujingguo.multiMediaPlayer.util.MyApplication;
import top.xujingguo.multiMediaPlayer.util.MyMusicUtil;

//zhihu
//import com.tbruyelle.rxpermissions2.RxPermissions;
import top.xujingguo.multiMediaPlayer.videoAdapter;
import top.xujingguo.multiMediaPlayer.videoInfo;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeActivity extends PlayBarBaseActivity {

    private static final String TAG = HomeActivity.class.getName();
    private DBManager dbManager;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ImageView navHeadIv;
    private View viewDivide;
    private FrameLayout fragementPlayer;
    private LinearLayout fixListLL;
    private RelativeLayout videoPlayList;
    private LinearLayout homeMyListAll;
    private LinearLayout localMusicLl;
    private LinearLayout lastPlayLl;
    private LinearLayout myLoveLl;
    private LinearLayout myListTitleLl;
    private Toolbar toolbar;
    private TextView localMusicCountTv;
    private TextView lastPlayCountTv;
    private TextView myLoveCountTv;
    private TextView myPLCountTv;
    private ImageView myPLArrowIv;
    private ImageView myPLAddIv;
    private ListView listView;
    private ListView videoListView;
    private GridView videoGridView;
    private HomeListViewAdapter adapter;
    private List<PlayListInfo> playListInfos;
    private List<videoInfo> videoinfoList = new ArrayList<videoInfo>();
    private int count;
    private boolean isOpenMyPL = false; //标识我的歌单列表打开状态
    private long exitTime = 0;
    private boolean isStartTheme = false;

    //fragment
    private VideoFragment videoFragment;
    private List<Fragment> fragments = new ArrayList<>(4);
    private int REQUEST_CODE_CHOOSE = 23;
    //float action button
    private FloatingActionButton showMusic;
    private FloatingActionButton showVideo;
    private FloatingActionButton showPicture;
    private FloatingActionsMenu multiActions;

    private RecyclerView localRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoinfoList = new ScanVideo(this).getList();

        setContentView(R.layout.activity_home);
        dbManager = DBManager.getInstance(HomeActivity.this);
        toolbar = (Toolbar)findViewById(R.id.home_activity_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        navHeadIv = (ImageView)headerView.findViewById(R.id.nav_head_bg_iv);
        loadBingPic();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.drawer_menu);
        }
        refreshNightModeTitle();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_theme:
                        isStartTheme = true;
                        Intent intentTheme = new Intent(HomeActivity.this,ThemeActivity.class);
                        startActivity(intentTheme);
                        break;
                    case R.id.nav_night_mode:
                        int preTheme = 0;
                        if(MyMusicUtil.getNightMode(HomeActivity.this)){
                            //当前为夜间模式，则恢复之前的主题
                            MyMusicUtil.setNightMode(HomeActivity.this,false);
                            preTheme = MyMusicUtil.getPreTheme(HomeActivity.this);
                            MyMusicUtil.setTheme(HomeActivity.this,preTheme);
                        }else {
                            //当前为白天模式，则切换到夜间模式
                            MyMusicUtil.setNightMode(HomeActivity.this,true);
                            MyMusicUtil.setTheme(HomeActivity.this,ThemeActivity.THEME_SIZE-1);
                        }
//                        Intent intentNight = new Intent(HomeActivity.this,HomeActivity.class);
//                        startActivity(intentNight);
                        recreate();
                        refreshNightModeTitle();
//                        overridePendingTransition(R.anim.start_anim,R.anim.out_anim);
                        break;
                    case R.id.nav_about_me:
                        Intent aboutTheme = new Intent(HomeActivity.this,AboutActivity.class);
                        startActivity(aboutTheme);
                        break;
                    case R.id.nav_logout:
                        finish();
                        Intent intentBroadcast = new Intent(MusicPlayerService.PLAYER_MANAGER_ACTION);
                        intentBroadcast.putExtra(Constant.COMMAND, Constant.COMMAND_RELEASE);
                        sendBroadcast(intentBroadcast);
                        Intent stopIntent = new Intent(HomeActivity.this,MusicPlayerService.class);
                        stopService(stopIntent);
                        break;
                }
                return true;
            }
        });
        init();

        Intent startIntent = new Intent(HomeActivity.this,MusicPlayerService.class);
        startService(startIntent);


        //choose mode music/video/picture
        showMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //show
                viewDivide.setVisibility(View.VISIBLE);
                fragementPlayer.setVisibility(View.VISIBLE);
                fixListLL.setVisibility(View.VISIBLE);
                homeMyListAll.setVisibility(View.VISIBLE);
                //videoPlayList.setVisibility(View.INVISIBLE);
                videoListView.setVisibility(View.INVISIBLE);
                videoGridView.setVisibility(View.INVISIBLE);
                //localRecyclerView.setVisibility(View.INVISIBLE);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle(Constant.LABEL_MUSIC);
                }
                multiActions.collapse();

            }
        });
        showVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //hidden
                viewDivide.setVisibility(View.INVISIBLE);
                fragementPlayer.setVisibility(View.INVISIBLE);
                fixListLL.setVisibility(View.INVISIBLE);
                homeMyListAll.setVisibility(View.INVISIBLE);
                //videoPlayList.setVisibility(View.VISIBLE);
                videoListView.setVisibility(View.VISIBLE);
                videoGridView.setVisibility(View.VISIBLE);
                //localRecyclerView.setVisibility(View.VISIBLE);

                //toolbar = (Toolbar)findViewById(R.id.home_activity_toolbar);
                //setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle(Constant.LABEL_VIDEO);
                }


                videoAdapter videoadapter2 = new videoAdapter(HomeActivity.this,R.layout.video_item,videoinfoList);
                final GridView videoGridView = (GridView) findViewById(R.id.video_grid_view);
                videoGridView.setAdapter(videoadapter2);

                multiActions.collapse();

            }
        });
        showPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Matisse.from(HomeActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .capture(true)
                        .captureStrategy(
                                new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                        .maxSelectable(9)
                        //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
                multiActions.collapse();
            }
        });

        videoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                videoInfo videoinfo = videoinfoList.get(i);
                //Toast.makeText(HomeActivity.this,videoinfo.getVideoName(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                //Log.w("video path",videoinfo.getPath());
                intent.putExtra("videoPath",videoinfo.getPath());
                intent.putExtra("videoName",videoinfo.getVideoName());
                intent.setClass(HomeActivity.this,IjkFullscreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshNightModeTitle(){
        if (MyMusicUtil.getNightMode(HomeActivity.this)){
            navView.getMenu().findItem(R.id.nav_night_mode).setTitle("日间模式");
        }else {
            navView.getMenu().findItem(R.id.nav_night_mode).setTitle("夜间模式");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        count = dbManager.getMusicCount(Constant.LIST_ALLMUSIC);
        localMusicCountTv.setText(count + "");
        count = dbManager.getMusicCount(Constant.LIST_LASTPLAY);
        lastPlayCountTv.setText(count + "");
        count = dbManager.getMusicCount(Constant.LIST_MYLOVE);
        myLoveCountTv.setText(count + "");
        count = dbManager.getMusicCount(Constant.LIST_MYPLAY);
        myPLCountTv.setText("(" + count + ")");
        adapter.updateDataList();
    }

    private void init(){


        //video listView
        videoListView = (ListView) findViewById(R.id.video_list_view);
        videoGridView = (GridView) findViewById(R.id.video_grid_view);
        // float action button
        multiActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        showMusic = (FloatingActionButton) findViewById(R.id.showMusic);
        showVideo = (FloatingActionButton) findViewById(R.id.showVideo);
        showPicture = (FloatingActionButton) findViewById(R.id.showPicture);
        // show or hidden
        viewDivide = (View) findViewById(R.id.home_devider_view);
        fragementPlayer = (FrameLayout) findViewById(R.id.fragment_playbar);
        fixListLL = (LinearLayout) findViewById(R.id.fix_list_ll);
        homeMyListAll = (LinearLayout) findViewById(R.id.home_my_list_ll);
        //videoPlayList = (RelativeLayout) findViewById(R.id.video_play_list);
        localRecyclerView = (RecyclerView) findViewById(R.id.local_recycler_view);

        localMusicLl = (LinearLayout) findViewById(R.id.home_local_music_ll);
        lastPlayLl = (LinearLayout) findViewById(R.id.home_recently_music_ll);
        myLoveLl = (LinearLayout) findViewById(R.id.home_my_love_music_ll);
        myListTitleLl = (LinearLayout) findViewById(R.id.home_my_list_title_ll);
        listView = (ListView)findViewById(R.id.home_my_list_lv);
        localMusicCountTv = (TextView) findViewById(R.id.home_local_music_count_tv);
        lastPlayCountTv = (TextView) findViewById(R.id.home_recently_music_count_tv);
        myLoveCountTv = (TextView) findViewById(R.id.home_my_love_music_count_tv);
        myPLCountTv = (TextView) findViewById(R.id.home_my_list_count_tv);
        myPLArrowIv = (ImageView) findViewById(R.id.home_my_pl_arror_iv);
        myPLAddIv = (ImageView) findViewById(R.id.home_my_pl_add_iv);

        localMusicLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,LocalMusicActivity.class);
                startActivity(intent);
            }
        });

        lastPlayLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,LastMyloveActivity.class);
                intent.putExtra(Constant.LABEL,Constant.LABEL_LAST);
                startActivity(intent);
            }
        });

        myLoveLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,LastMyloveActivity.class);
                intent.putExtra(Constant.LABEL,Constant.LABEL_MYLOVE);
                startActivity(intent);
            }
        });

        playListInfos = dbManager.getMyPlayList();
        adapter = new HomeListViewAdapter(playListInfos,this,dbManager);
        listView.setAdapter(adapter);
        myPLAddIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加歌单
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_create_playlist,null);
                final EditText playlistEt = (EditText)view.findViewById(R.id.dialog_playlist_name_et);
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = playlistEt.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(HomeActivity.this,"请输入歌单名",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbManager.createPlaylist(name);
                        dialog.dismiss();
                        adapter.updateDataList();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();//配置好后再builder show
            }
        });
        myListTitleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展现我的歌单
                if (isOpenMyPL){
                    isOpenMyPL = false;
                    myPLArrowIv.setImageResource(R.drawable.arrow_right);
                    listView.setVisibility(View.GONE);
                }else {
                    isOpenMyPL = true;
                    myPLArrowIv.setImageResource(R.drawable.arrow_down);
                    listView.setVisibility(View.VISIBLE);
                    playListInfos = dbManager.getMyPlayList();
                    adapter = new HomeListViewAdapter(playListInfos,HomeActivity.this,dbManager);
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    public void updatePlaylistCount(){
        count = dbManager.getMusicCount(Constant.LIST_MYPLAY);
        myPLCountTv.setText("(" + count + ")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStartTheme){
            HomeActivity.this.finish();
        }
        isStartTheme = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次切换到桌面", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }
            return true;
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBingPic(){
        HttpUtil.sendOkHttpRequest(HttpUtil.requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String bingPic = response.body().string();
                    MyMusicUtil.setBingShared(bingPic);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MyApplication.getContext()).load(bingPic).into(navHeadIv);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    navHeadIv.setImageResource(R.drawable.bg_playlist);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                navHeadIv.setImageResource(R.drawable.bg_playlist);
            }
        });
        navHeadIv.setImageResource(R.drawable.bg_playlist);
    }
}
