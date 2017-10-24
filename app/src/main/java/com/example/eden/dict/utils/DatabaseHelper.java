package com.example.eden.dict.utils;

/**
 * Created by Eden on 2017/4/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

    public Context mContext=null;
    public String tableName=null;
    public static int VERSION = 1;

    public SQLiteDatabase dbR;
    public SQLiteDatabase dbW;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        mContext = context;
        tableName = name;
        //使得该数据库可以进行读取修改操作
        dbR = this.getReadableDatabase();
        dbW = this.getWritableDatabase();
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory){
        this(context,name,factory,VERSION);
        mContext=context;
        tableName=name;
    }

    public DatabaseHelper(Context context, String name){
        this(context,name,null);
        mContext=context;
        tableName=name;
    };

    /*
    public boolean insertWordInfoToDataBase(String searchedWord,String interpret, boolean isOverWrite)
    {
    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.d("databasecreate", "Create databases");
        db.execSQL("create table dict(" +
                "word text primary key, " +
                "pse text, " +
                "prone text, " +
                "psa text, " +
                "prona text, " +
                "interpret text, " +
                "sentorig text, " +
                "senttrans text)");
        db.execSQL("create table glossary(" +
                "word text primary key, " +
                "interpret text, " +
                "right int, " +
                "wrong int, " +
                "grasp int, " +
                "learned int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    //word:单词 interpret:翻译 right:答对次数 wrong:打错次数 grasp:掌握程度 learned:用于记录单词是否背过
    public boolean insertWordInfoToDataBase(String word, String interpret, boolean overWrite)//向数据库加入单词，interpret:解释理解
    {
        Cursor cursor = null;//Cursor:每一行的集合,游标作用
        //Log.d("insertword","1");
        cursor = dbR.query(tableName, new String[]{"word"}, "word=?", new String[]{word}, null, null, "word");

        //ContenValues：向数据库中插入数据，首先需要建立对象
        //Log.d("insertword","2");

        if (cursor.moveToNext()) {
            if (overWrite)//是否覆盖数据库中原有单词信息
            {
                ContentValues values = new ContentValues();
                values.put("interpret", interpret);
                values.put("right", 0);
                values.put("wrong", 0);
                values.put("grasp", 0);
                values.put("learned", 0);

                dbW.update(tableName, values, "word=?", new String[]{word});
                cursor.close();
                return true;
            }
        }
        else {
            ContentValues values = new ContentValues();
            values.put("word", word);
            values.put("interpret", interpret);
            values.put("right", 0);
            values.put("wrong", 0);
            values.put("grasp", 0);
            values.put("learned", 0);

            dbW.insert(tableName, null, values);
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean isTableExist(String tabName){
        boolean result = false;
        if(tabName.equals("")){
            return false;
        }

        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master " +
                    "where type ='table' and name ='" +
                    tabName.trim() + "'" ;
            cursor = dbR.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
}