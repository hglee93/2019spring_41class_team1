package com.team.gajimarket.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.team.gajimarket.R;
import com.team.gajimarket.fragment.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private MainFragment mainFragment;
    private FragmentHome homeFragment;
    private FragmentCate cateFragment;
    private FragmentMypage mypageFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        homeFragment = new FragmentHome();
        cateFragment = new FragmentCate();
        mypageFragment = new FragmentMypage();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Home"));
        tabs.addTab(tabs.newTab().setText("Category"));
        tabs.addTab(tabs.newTab().setText("Mypage"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                switch (position){
                    case 0:
                        selected = homeFragment;
                        break;
                    case 1:
                        selected = cateFragment;
                        break;
                    case 2:
                        selected = mypageFragment;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Log.d("CREATION", "MainActivity::onCreate()");
    }

    public void onArActivityStart() {
        Intent intent = new Intent(getApplicationContext(), ArActivity.class);
        startActivity(intent);
    }

    public void onFragmentChange(int index) {
        switch (index) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                break;
            case 1:
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, arFragment).commit();
                break;
        }
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
