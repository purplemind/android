package com.android.evindroid.ucenici;

import java.io.*;
import java.util.*;

import org.xmlpull.v1.*;

import android.content.Context;
import android.util.Xml;

public class UcenikXmlParser {
	
	private static final String ns = null;
	
	public UcenikXmlParser() {

	}
	
    private List<Ucenik> parse_process(FileInputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readUcenici(parser);
        } finally {
            in.close();
        }
    }
    
    private List<Ucenik> readUcenici(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Ucenik> entries = new ArrayList<Ucenik>();

        parser.require(XmlPullParser.START_TAG, ns, "ucenici");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the 'ucenik' tag
            if (name.equals("ucenik")) {
                entries.add(readUcenik(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
    
    private Ucenik readUcenik(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "ucenik");
        String ime = null;
        String prezime = null;
        String jmbg = null;
        String broj_mk = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("ime")) {
                ime = readIme(parser);
            } else if (name.equals("prezime")) {
                prezime = readPrezime(parser);
            } else if (name.equals("jmbg")) {
                jmbg = readJmbg(parser);
            } else if (name.equals("broj_mk")) {
            	broj_mk = readBroj_mk(parser);
            } else {
                skip(parser);
            }
        }
        return new Ucenik(ime, prezime, jmbg, broj_mk);
    }

	private String readBroj_mk(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "broj_mk");
	    String broj_mk = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "broj_mk");
	    return broj_mk;
	}

	private String readJmbg(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "jmbg");
	    String jmbg = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "jmbg");
	    return jmbg;
	}

	private String readPrezime(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "prezime");
	    String prezime = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "prezime");
	    return prezime;
	}

	private String readIme(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "ime");
	    String ime = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "ime");
	    return ime;
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

	public ArrayList<String> parseToString(FileInputStream in) {
		ArrayList<String> res = new ArrayList<String>();
		List<Ucenik> ucenici = new ArrayList<Ucenik>();
		try {
			ucenici = parse_process(in);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		for (Ucenik u: ucenici) {
			res.add(u.getIme() + " " + u.getPrezime() + ", " + u.getJmbg());
		}
		return res;
	}
	
	public List<Ucenik> parse(Context c, String file) {
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
