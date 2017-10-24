package com.example.eden.dict;

/**
 * Created by Eden on 2017/4/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperDict extends SQLiteOpenHelper{

    public Context mContext=null;
    public String tableName=null;
    public static int VERSION=1;
    public DataBaseHelperDict(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext=context;
        tableName=name;
    }

    public DataBaseHelperDict(Context context, String name, SQLiteDatabase.CursorFactory factory){
        this(context,name,factory,VERSION);
        mContext=context;
        tableName=name;
    }

    public DataBaseHelperDict(Context context, String name){
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
        db.execSQL("create table dict(word text,pse text,prone text,psa text,prona text," +
                "interpret text, sentorig text, senttrans text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}