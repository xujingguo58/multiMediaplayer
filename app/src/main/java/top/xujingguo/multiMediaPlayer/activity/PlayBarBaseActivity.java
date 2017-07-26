package top.xujingguo.multiMediaPlayer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;

import top.xujingguo.multiMediaPlayer.R;
import top.xujingguo.multiMediaPlayer.fragment.PlayBarFragment;

public abstract class PlayBarBaseActivity extends BaseActivity {

    private PlayBarFragment playBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show();
    }

    private void show(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (playBarFragment == null) {
            playBarFragment = PlayBarFragment.newInstance();
            ft.add(R.id.fragment_playbar, playBarFragment).commit();
        }else {
            ft.show(playBarFragment).commit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
