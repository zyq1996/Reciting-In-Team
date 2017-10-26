package com.example.eden.dict.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import com.example.eden.dict.R;
import com.example.eden.dict.utils.BottomNavigationViewHelper;

import java.lang.reflect.Field;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setStatusBarColor(R.color.colorPrimaryLight);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        //关闭 BottomNav 三小一大的动画效果
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                HomeFragment homeFragment = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, homeFragment).commit();
                                break;
                            case R.id.action_team:
                                break;
                            case R.id.action_more:
                                break;
                            case R.id.action_info:
                                LoginFragment loginFragment = new LoginFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, loginFragment).commit();
                                break;
                            default:
                        }
                        return true;
                    }
                });
    }



}
