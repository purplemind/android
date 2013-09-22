package com.android.evindroid.ocene;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.android.evindroid.ocene.OceneUcenika.PredmetOcena;

/*
 * <ocene> 
 * 	 <ucenik>
 * 	    <predmet> </predmet>
 * 	    <ocena> </ocena>
 *	 </ucenik>
 *   <ucenik>
 *      ...
 *   </ucenik>
 * </ocene>
 */
public class OceneXmlParser {
	private static final String ns = null;
	
	public OceneXmlParser() {

	}
	
    private List<PredmetOcena> parse_process(FileInputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readOcene(parser);
        } finally {
            in.close();
        }
    }
    
    private List<PredmetOcena> readOcene(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<PredmetOcena> entries = new ArrayList<PredmetOcena>();
        
        parser.require(XmlPullParser.START_TAG, ns, "ocene");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("single")) {
            	entries.add(readSingleOcena(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
        
    private PredmetOcena readSingleOcena(XmlPullParser parser) throws XmlPullParserException, IOException {
        String predmet = null;
        String ocena = null;
        
        parser.require(XmlPullParser.START_TAG, ns, "single");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("predmet")) {
            	predmet = readPredmet(parser);
            } else if ( name.equals("ocena") ) {
            	ocena = readOcena(parser);
            } else {
            	skip(parser);
            }
        }
        return new PredmetOcena(predmet, ocena);
    }
    
    private String readPredmet(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "predmet");
	    String predmet = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "predmet");
	    return predmet;
    }

    private String readOcena(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "ocena");
	    String ocena = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "ocena");
	    return ocena;
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
	
	public List<PredmetOcena> parse(Context c, String file) {
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
