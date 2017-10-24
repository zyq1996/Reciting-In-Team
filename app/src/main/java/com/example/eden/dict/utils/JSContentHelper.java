package com.example.eden.dict.utils;

import com.example.eden.dict.WordValue;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Eden on 2017/3/9.
 */

public class JSContentHelper extends DefaultHandler {
    public WordValue wordValue;
    private String tagName;
    private String interpret="";
    private String orig="";
    private String trans="";
    private boolean isChinese=false;

    public JSContentHelper(){
        wordValue=new WordValue();
        isChinese=false;
    }

    public WordValue getWordValue(){
        return wordValue;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException{
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
        if(length<=0)
            return;
        for(int i=start; i<start+length; i++){
            if(ch[i]=='\n')
                return;
        }

        String str=new String(ch,start,length);
        switch (tagName)
        {
            case "key":wordValue.setWord(str); break;
            case "ps": if(wordValue.getPsE().length()<=0)
                wordValue.setPsE(str);
            else wordValue.setPsA(str);
                break;
            case "pron":if(wordValue.getPronE().length()<=0)
                wordValue.setPronE(str);
            else wordValue.setPronA(str);
                break;
            case "pos":isChinese=false;
                interpret+=str+" ";
                break;
            case "acceptation":interpret+=str+"\n";
                interpret=wordValue.getInterpret()+interpret;
                wordValue.setInterpret(interpret);
                interpret="";
                break;
            case "orig":orig=wordValue.getSentOrig();
                wordValue.setSentOrig(orig+str+"\n");
                break;
            case "trans":trans=wordValue.getSentTrans();
                wordValue.setSentTrans(trans+str+"\n");
                break;
            case "fy":isChinese=true;
                wordValue.setInterpret(str);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName
            , Attributes attributes) throws SAXException{
        super.startElement(uri, localName, qName, attributes);
        tagName=localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // TODO Auto-generated method stub
        super.endElement(uri, localName, qName);
        tagName=null;
    }

    @Override
    public void endDocument() throws SAXException{
        super.endDocument();
        if(isChinese)
            return;
        String interpret=wordValue.getInterpret();
        if(interpret!=null && interpret.length()>0){
            char[] strArray=interpret.toCharArray();
            wordValue.setInterpret(new String(strArray,0,interpret.length()-1));
        }
    }

}
