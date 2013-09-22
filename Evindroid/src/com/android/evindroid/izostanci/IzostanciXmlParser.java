package com.android.evindroid.izostanci;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.android.evindroid.izostanci.IzostanciUcenika.Izostanak;


/*
 * <izostanci>
 *   <single>
 *     <datum></datum>
 *     <opravadani></opravdani>
 *     <neopravdani></neopravdani>
 *   </single>
 *   <single>
 *     ...
 *   </single>
 * </izostanci>
 */
public class IzostanciXmlParser {

	private static final String ns = null;
	
	private List<Izostanak> parse_process(FileInputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readIzostanci(parser);
        } finally {
            in.close();
        }
    }
    
    private List<Izostanak> readIzostanci(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Izostanak> entries = new ArrayList<Izostanak>();
        
        parser.require(XmlPullParser.START_TAG, ns, "izostanci");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("single")) {
            	entries.add(readSingleIzostanak(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
        
    private Izostanak readSingleIzostanak(XmlPullParser parser) throws XmlPullParserException, IOException {
        String datum = null;
        String opravdani = null;
        String neopravdani = null;
        
        parser.require(XmlPullParser.START_TAG, ns, "single");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("datum")) {
            	datum = readDatum(parser);
            } else if ( name.equals("opravdani") ) {
            	opravdani = readOpravdani(parser);
            } else if ( name.equals("neopravdani") ) {
            	neopravdani = readNeopravdani(parser);
            }
            else {
            	skip(parser);
            }
        }
        return new Izostanak(datum, opravdani, neopravdani);
    }
    
    private String readDatum(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "datum");
	    String datum = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "datum");
	    return datum;
    }

    private String readOpravdani(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "opravdani");
	    String opravdani = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "opravdani");
	    return opravdani;
    }
    
    private String readNeopravdani(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "neopravdani");
	    String neopravdani = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "neopravdani");
	    return neopravdani;
    }
    
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	public List<Izostanak> parse(Context c, String file) {
		try {
			FileInputStream in = null;
			in = c.openFileInput(file);
			return parse_process(in);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

}
