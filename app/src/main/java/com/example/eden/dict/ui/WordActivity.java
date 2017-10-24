package com.example.eden.dict.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eden.dict.R;
import com.example.eden.dict.WordBox;
import com.example.eden.dict.WordInfo;
import com.example.eden.dict.utils.ReadWordHelper;

public class WordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mNextWord;
    private Button mNotRemember;
    private TextView mShowWord;
    private TextView mShowInterpret;
    private WordBox mWordBox;
    private ReadWordHelper mReadwords;
    private WordInfo word = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        mNextWord = (Button) findViewById(R.id.bt_next_word);
        mNotRemember = (Button) findViewById(R.id.bt_not_remember);
        mShowWord = (TextView) findViewById(R.id.tv_display_word);
        mShowInterpret = (TextView) findViewById(R.id.tv_display_interpret);

        mNextWord.setOnClickListener(this);
        mNotRemember.setOnClickListener(this);

        mReadwords = new ReadWordHelper(this);
        mWordBox = new WordBox(this, "glossary");

        new Thread(new Runnable() {
            @Override
            public void run() {
                mReadwords.ReadWords();
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_next_word:
                word = mWordBox.popWord();
                Log.d("word", word.getWord() + "  " + word.getInterpret());
                if (word != null) {
                    mShowWord.setText(word.getWord());
                    if (mShowWord.getVisibility() == View.VISIBLE) {
                        mShowInterpret.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.bt_not_remember:
                if (word != null) {
                    mShowInterpret.setText(word.getInterpret());
                    mShowInterpret.setVisibility(View.VISIBLE);
                }
                break;
            default:
        }
    }
}
