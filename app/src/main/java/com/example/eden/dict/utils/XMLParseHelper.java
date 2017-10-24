package com.example.eden.dict.utils;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Eden on 2017/3/19.
 */

public class XMLParseHelper {
    public SAXParserFactory factory = null;
    public XMLReader reader = null;

    public XMLParseHelper() {
        try {
            factory = SAXParserFactory.newInstance();
            reader = factory.newSAXParser().getXMLReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseJinShanXml(DefaultHandler content, InputSource inSourse) {
        if (inSourse == null)
            return;
        try {
            reader.setContentHandler(content);
            reader.parse(inSourse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
