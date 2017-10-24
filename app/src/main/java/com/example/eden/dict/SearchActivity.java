package com.example.eden.dict;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends AppCompatActivity{

    public TextView textDictWord=null;
    public TextView textDictPhonSymbolEng=null;
    public TextView textDictPhonSymbolUSA=null;
    public TextView textDictInterpret=null;
    public ListView listViewDictSentence=null;
    public ImageButton imageBtnDictAddToWordList=null;
    public ImageButton imageBtnDictHornEng=null;
    public ImageButton imageBtnDictHornUSA=null;
    public ImageButton imageBtnDictSerach=null;
    public ImageButton imageBtnDictBackToGeneral=null;
    public ImageButton imageBtnDictDelteEditText=null;

    public Button buttonDictDialogConfirm=null;
    public Button buttonDictDialogCancel=null;

    public EditText editTextDictSearch=null;

    public Dict dict=null;

    public WordValue w=null;

    public DataBaseHelperDict dbGlossaryHelper=null;
    public SQLiteDatabase dbGlossaryR=null;
    public SQLiteDatabase dbGlossaryW=null;


    public Mp3Player mp3Box=null;

    public static String searchedWord=null;

    public Handler dictHandler=null;

    public void initial(){
        textDictWord=(TextView) findViewById(R.id.text_dict_word);
        textDictInterpret=(TextView)findViewById(R.id.text_dict_interpret);
        textDictPhonSymbolEng=(TextView)findViewById(R.id.text_dict_phosymbol_eng);
        textDictPhonSymbolUSA=(TextView)findViewById(R.id.text_dict_phosymbol_usa);
        listViewDictSentence=(ListView)findViewById(R.id.listview_dict_sentence);

        imageBtnDictAddToWordList=(ImageButton)findViewById(R.id.image_btn_dict_add_to_wordlist);
        imageBtnDictBackToGeneral=(ImageButton)findViewById(R.id.image_btn_dict_back_to_general);
        imageBtnDictHornEng=(ImageButton)findViewById(R.id.image_btn_dict_horn_accent_eng);
        imageBtnDictHornUSA=(ImageButton)findViewById(R.id.image_btn_dict_horn_accent_usa);
        imageBtnDictSerach=(ImageButton)findViewById(R.id.image_btn_dict_search);
        imageBtnDictDelteEditText=(ImageButton)findViewById(R.id.image_btn_dict_delete_all);


        editTextDictSearch=(EditText)findViewById(R.id.edit_text_dict);
        editTextDictSearch.setOnEditorActionListener(new EditTextDictEditActionLis());
        dict=new Dict(SearchActivity.this, "dict");
        mp3Box=new Mp3Player(SearchActivity.this, "dict");
        dbGlossaryHelper=new DataBaseHelperDict(SearchActivity.this, "glossary");
        dbGlossaryR=dbGlossaryHelper.getReadableDatabase();
        dbGlossaryW=dbGlossaryHelper.getWritableDatabase();


        dictHandler=new Handler(Looper.getMainLooper());

        //对searchedWord进行初始化
        Intent intent=this.getIntent();
        searchedWord=intent.getStringExtra("word");
        if(searchedWord==null)
            searchedWord="";
        //设置查找的文本
        textDictWord.setText(searchedWord);

    }


    /**
     * 该方法可能需要访问网络，因此放在线程里进行
     * @param word
     */
    public void searchWord(String word){
        //调用该方法后首先初始化界面
        dictHandler.post(new RunnableDictSetTextInterface(searchedWord, "", "", "", null, null));
        w=null;    //对w进行初始化
        if(!dict.isWordExist(word)){  //数据库中没有单词记录，从网络上进行同步
            if((w=dict.getWordFromInternet(word))==null ||w.getWord().equals("")){
                return;
            }
            //错词不添加进词典
            dict.insertWordToDict(w, true);   //默认添加到词典中
        }//能走到这一步说明从网上同步成功，数据库中一定存在单词记录
        w=dict.getWordFromDict(word);
        if(w==null){             //这里又进一步做了保护,若词典中还是没有，那么用空字符串代替
            w=new WordValue();
        }
        String searchStr=w.getWord();
        String phoSymEng=w.getPsE();
        String phoSymUSA=w.getPsA();
        String interpret=w.getInterpret();
        ArrayList<String> sentList=w.getOrigList();  //一定不会是null
        ArrayList<String> sentInChineseList=w.getTransList();
        dictHandler.post(new RunnableDictSetTextInterface(searchedWord, phoSymEng, phoSymUSA, interpret, sentList, sentInChineseList));
        if(!phoSymEng.equals("") && !phoSymUSA.equals("")){    //只有有音标时才去下载音乐

            mp3Box.playMusicByWord(searchedWord, Mp3Player.ENGLISH_ACCENT, true, false);
            mp3Box.playMusicByWord(searchedWord, Mp3Player.USA_ACCENT, true, false);

        }
    }



    public void setOnClickLis(){
        imageBtnDictBackToGeneral.setOnClickListener(new IBDictBackToGeneralClickLis() );
        imageBtnDictHornEng.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.ENGLISH_ACCENT));
        imageBtnDictHornUSA.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.USA_ACCENT));
       // imageBtnDictAddToWordList.setOnClickListener(new IBDictAddWordToGlossaryClickLis());
        imageBtnDictDelteEditText.setOnClickListener(new IBDictDeleteEditTextClickLis());
        imageBtnDictSerach.setOnClickListener(new IBDictSearchClickLis());
    }

/*
    public void showAddDialog(){
        if(searchedWord==null)
            return;
        AlertDialog dialog=new AlertDialog.Builder(SearchActivity.this).create();
        dialog.show();
        Window window=dialog.getWindow();
        window.setContentView(R.layout.dialog_if_layout);
        buttonDictDialogConfirm=(Button)window.findViewById(R.id.dialog_confirm);
        buttonDictDialogCancel=(Button)window.findViewById(R.id.dialog_cancel);
        buttonDictDialogConfirm.setOnClickListener(new BDictDialogConfirmClickLis(dialog));
        buttonDictDialogCancel.setOnClickListener(new BDictDialogCancelClickLis(dialog));
        TextView dialogText=(TextView)window.findViewById(R.id.dialog_text);
        dialogText.setText("把"+searchedWord+"添加到单词本?");
    }
*/
    /*
    public void insertWordToGlossary(){
        if(w==null || w.getInterpret().equals("")){
            Toast.makeText(SearchActivity.this, "单词格式错误", Toast.LENGTH_SHORT).show();
            return;                   //若是不是有效单词，那么将不能添加到单词本
        }
        boolean isSuccess=dbGlossaryHelper.insertWordInfoToDataBase(searchedWord, w.getInterpret(), false);
        if(isSuccess){
            Toast.makeText(SearchActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SearchActivity.this, "单词已存在", Toast.LENGTH_SHORT).show();
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initial();
        setOnClickLis();
        new ThreadDictSearchWordAndSetInterface().start();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mp3Box.isMusicPermitted=true;
    }



    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        mp3Box.isMusicPermitted=false;
        super.onPause();
    }



    public class ThreadDictSearchWordAndSetInterface extends Thread{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            searchWord(searchedWord);
        }

    }

    public class RunnableDictSetTextInterface implements Runnable{
        String searchStr=null;
        String phoSymEng=null;
        String phoSymUSA=null;
        String interpret=null;
        ArrayList<String> sentList=null;
        ArrayList<String> sentInChineseList=null;


        public RunnableDictSetTextInterface(String searchStr, String phoSymEng,
                                            String phoSymUSA, String interpret, ArrayList<String> sentList,
                                            ArrayList<String> sentInChineseList) {
            super();
            this.searchStr = searchStr;
            this.phoSymEng = "英["+phoSymEng+"]";
            this.phoSymUSA = "美["+phoSymUSA+"]";
            this.interpret = interpret;
            this.sentList = sentList;
            this.sentInChineseList = sentInChineseList;
        }


        @Override
        public void run() {
            // TODO Auto-generated method stub
            textDictWord.setText(searchStr);
            textDictPhonSymbolEng.setText(phoSymEng);
            textDictPhonSymbolUSA.setText(phoSymUSA);
            textDictInterpret.setText(interpret);
            if(sentList==null ||sentInChineseList==null){     //对链表为空进行防护
                //    textDictSentence.setText("");
                return;
            }
            int count=0;
            if(sentList.size()<=sentInChineseList.size()){
                count=sentList.size();
            }else{
                count=sentInChineseList.size();             //保护机制，这里取两者长度最小值，但一般二者长度相等
            }
            ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
            for(int i=0; i<count;i++){
                //            sb.append(sentList.get(i)+"\n"+sentInChineseList.get(i)+"\n\n");
                HashMap<String,Object> map=new HashMap<String, Object>();
                map.put("sentence", sentList.get(i)+"\n"+sentInChineseList.get(i));
                list.add(map);
            }
            //        textDictSentence.setText(sb.toString());
            DictSentenceListAdapter adapter=new DictSentenceListAdapter(SearchActivity.this, R.layout.dict_sentence_list_item, list, new String[]{"sentence"}, new int[]{R.id.text_dict_sentence_list_item});
            listViewDictSentence.setAdapter(adapter);

        }

    }

    class IBDictBackToGeneralClickLis implements OnClickListener{

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            SearchActivity.this.finish();

        }

    }

    class IBDictPlayMusicByAccentClickLis implements OnClickListener{

        public int accentTemp=0;

        public IBDictPlayMusicByAccentClickLis(int accentTemp) {
            super();
            this.accentTemp = accentTemp;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            Log.d("MP3Player","StartMp3");
            mp3Box.playMusicByWord(searchedWord, accentTemp, true, true);

        }

    }
/*
    class IBDictAddWordToGlossaryClickLis implements OnClickListener{

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            showAddDialog();
        }

    }
*/
    class IBDictDeleteEditTextClickLis implements OnClickListener{

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            editTextDictSearch.setText("");
        }

    }
/*
    class BDictDialogConfirmClickLis implements OnClickListener{

        AlertDialog dialog=null;
        public BDictDialogConfirmClickLis(AlertDialog dialog){
            this.dialog=dialog;
        }
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            insertWordToGlossary();
            dialog.cancel();
        }

    }
*/
    class BDictDialogCancelClickLis implements OnClickListener{
        AlertDialog dialog=null;
        public BDictDialogCancelClickLis(AlertDialog dialog){
            this.dialog=dialog;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            dialog.cancel();
        }

    }

    class EditTextDictEditActionLis implements OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
            // TODO Auto-generated method stub
            if(arg1==EditorInfo.IME_ACTION_SEARCH || arg2!=null&&arg2.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                startSearch();
                return true;
            }

            return false;
        }

    }


    class IBDictSearchClickLis implements OnClickListener{

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            startSearch();

        }

    }

    public void startSearch(){

        String str=editTextDictSearch.getText().toString();
        if(str.equals(""))
            return;
        searchedWord=str;
        new ThreadDictSearchWordAndSetInterface().start();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextDictSearch.getWindowToken(), 0);

    }

}