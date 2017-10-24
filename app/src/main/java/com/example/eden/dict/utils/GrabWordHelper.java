package com.example.eden.dict.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eden on 2017/10/5.
 */

public class GrabWordHelper {

    public DatabaseHelper dbHelper = null;
    public Context context = null;
    public String tableName = null;
    int countWord = 0;
    int countInterpret = 0;//单词的解释
    int count = 0;
    public ArrayList<String> wordList = new ArrayList<String>();
    public ArrayList<String> interpretList = new ArrayList<String>();

    public GrabWordHelper(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
        dbHelper = new DatabaseHelper(context, tableName);
    }

    public void parse(String lineStr) {
//        String strInterpret = "";
//        String str = "";
//        char[] charArray = null;
//        //接下来两个为正则表达式筛选单词模式
//        Pattern patternWord = Pattern.compile("[a-zA-Z]+[ ]+");//抓取单词，单词和注解之间应有空格
//        //"%>[^<%%>]*<%",此为正则表达式，含义是过滤掉在%>和<%中的<%%>,此为抓取单词核心
//        Pattern patternInterpret = Pattern.compile("%E>[^<S%%E>]+<S%");
//        Matcher matcherWord = patternWord.matcher(lineStr);
//        Matcher matcherInterpret = null;
//
//        while (matcherWord.find()) {
//            str = matcherWord.group();//group中记录了所有符合指定表达式的字符串。
//            charArray = str.toCharArray();
//            if (charArray.length > 0 && (charArray[0] >= 'A' && charArray[0] <= 'Z')) {
//                charArray[0] += ('a' - 'A');//首字母转化小写
//            }
//            str = new String(charArray, 0, charArray.length);//将原char数组转化为一个字符串
//            wordList.add(str.trim());//去掉字符串首尾的空格
//        }
//
//        if (wordList.size() <= 0)
//            return;
//        //抓取单词后，重置字符串然后开始抓取注释
//        matcherWord.reset(lineStr);
//        if (matcherWord.find()) {
//            strInterpret = matcherWord.replaceAll("<S%%E>");
//        }
//        strInterpret += "<S%%E>";//在字符串末尾加入<S%%E> 以便之后提取注解
//
//        matcherInterpret = patternInterpret.matcher(strInterpret);
//        while (matcherInterpret.find()) {
//            str = matcherInterpret.group();
//            //str.length-6:因为之前加入了<S%%E>那么进入注释数组的长度应该减去加入的<S%%E>
//            interpretList.add(new String(str.toCharArray(), 3, str.length() - 6));
//        }
        Log.d("indexofBar", lineStr);
        int indexOfBar = lineStr.indexOf('|');
        Log.d("indexofBar", String.valueOf(indexOfBar));
        String word = lineStr.substring(0, indexOfBar);
        String interpret = lineStr.substring(indexOfBar + 1, lineStr.length());
        wordList.add(word);
        interpretList.add(interpret);
    }

    public void insertWordToDatabase() {
        for(int k = 0;k<3000;k++) {
            Log.d("wordInsert", wordList.get(k) + " " + interpretList.get(k));
        }
        countWord = wordList.size();
        countInterpret = interpretList.size();
        count = countWord > countInterpret ? countInterpret : countWord;
        for (int i = 0; i < count; i++) {
            try {
                dbHelper.insertWordInfoToDataBase(wordList.get(i), interpretList.get(i), false);
            } catch (Exception e) {
                break;
            }
        }
    }
}