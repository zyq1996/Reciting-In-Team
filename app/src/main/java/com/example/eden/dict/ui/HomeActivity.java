package com.example.eden.dict.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.example.eden.dict.R;

public class HomeActivity extends AppCompatActivity {

    private EditText mSearchWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSearchWords = (EditText) findViewById(R.id.et_search);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                HomeFragment homeFragment = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, homeFragment).commit();
                                break;
                            case R.id.action_info:
                                LoginFragment loginFragment = new LoginFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, loginFragment).commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    public void startSearchActivity(View view) {
        Intent startSearch = new Intent(this, SearchActivity.class);
        String searchWord = mSearchWords.getText().toString();
        startSearch.putExtra("words", searchWord);
        startActivity(startSearch);
    }

    public void startWordActivity(View view) {
        Intent startWordAcitivity = new Intent(this, WordActivity.class);
        startActivity(startWordAcitivity);
    }
}
