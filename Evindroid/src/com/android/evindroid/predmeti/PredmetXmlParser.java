package com.android.evindroid.predmeti;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class PredmetXmlParser {
	private static final String ns = null;
	
	public PredmetXmlParser() {

	}
	
    private List<Predmet> parse_process(FileInputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPredmeti(parser);
        } finally {
            in.close();
        }
    }
    
    private List<Predmet> readPredmeti(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Predmet> entries = new ArrayList<Predmet>();

        parser.require(XmlPullParser.START_TAG, ns, "predmeti");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the 'ucenik' tag
            if (name.equals("predmet")) {
                entries.add(readPredmet(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
    
    private Predmet readPredmet(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "predmet");
        String id = null;
        String naziv = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                id = readId(parser);
            } else if (name.equals("naziv")) {
                naziv = readNaziv(parser);
            } else {
                skip(parser);
            }
        }
        return new Predmet(id, naziv);
    }

	private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "id");
	    String id = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "id");
	    return id;
	}

	private String readNaziv(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "naziv");
	    String naziv = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "naziv");
	    return naziv;
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
	
	public List<Predmet> parse(Context c, String file) {
		try {
			FileInputStream in = null;
			in = c.openFileInput(file);
			return parse_process(in);
		} catch (XmlPullParserException e) {
			return new ArrayList<Predmet>();
		} catch (IOException e) {
			return new ArrayList<Predmet>();
		}
	}
	
}

