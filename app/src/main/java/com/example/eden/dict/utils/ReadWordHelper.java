package com.example.eden.dict.utils;

import android.content.Context;
import android.util.Log;

import com.example.eden.dict.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Eden on 2017/10/5.
 */

public class ReadWordHelper {
    public Context context;

    public ReadWordHelper(Context context) {
        this.context = context;
    }

    public void ReadWords() {
        try {
            String pathname = "";
            File filename = new File(pathname);
            InputStream inputStream = context.getResources().openRawResource(R.raw.wordsfile);
            InputStreamReader reader = new InputStreamReader(inputStream, "gbk");
            BufferedReader br = new BufferedReader(reader);

            String line = "";
            int i = 0;
            GrabWordHelper grabwords = new GrabWordHelper(context, "glossary");
            //line = br.readLine().replace("|", " ");
            line = br.readLine();
            while (line != null) {
                grabwords.parse(line);
                try {
                    line = br.readLine();
                } catch (Exception e) {
                    break;
                }
            }

            grabwords.insertWordToDatabase();

            Log.d("readwordfile", "insert completed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("readwordfile", "insert failed");
        }
    }
}
