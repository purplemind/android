package com.android.evindroid.predmeti;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Predmet implements Parcelable {

	public final static String FILE = "predmeti.xml";
	private String id;
	private String naziv;
	
	public Predmet() {
		
	}
	
	public Predmet(String id, String naziv) {
		this.id = id;
		this.naziv = naziv;
	}
	
	public Predmet(Parcel source) {
		id = source.readString();
		naziv = source.readString();
	}
	
	public String getId() {
		return id;
	}
	
	public String getNaziv() {
		return naziv;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public boolean addToFile(Context c) {
		List<Predmet> arrPredmeti = new PredmetXmlParser().parse(c, FILE);
		if ( arrPredmeti == null ) {
			arrPredmeti = new ArrayList<Predmet>();
		}
		arrPredmeti.add(this);
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite;
		try {
			os = c.openFileOutput(FILE, Context.MODE_PRIVATE);
			textToWrite = "<predmeti>" + eol;
			for (Predmet p: arrPredmeti) {
				textToWrite += "<predmet>" + eol;
				textToWrite += "<id>" + p.getId() + "</id>" + eol;
				textToWrite += "<naziv>" + p.getNaziv() + "</naziv>" + eol;
				textToWrite += "</predmet>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = ""; //reset textToWrite
			}
			textToWrite = "</predmeti>" + eol;
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean modify(Context c, int tag) {
		List<Predmet> arrPredmeti = new PredmetXmlParser().parse(c, FILE);
		if ( tag == -1 ) return false;
		arrPredmeti.set(tag, this); //ucenik with changed general data
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite;
		try {
			os = c.openFileOutput(FILE, Context.MODE_PRIVATE);
			textToWrite = "<predmeti>" + eol;
			for (Predmet p: arrPredmeti) {
				textToWrite += "<predmet>" + eol;
				textToWrite += "<id>" + p.getId() + "</id>" + eol;
				textToWrite += "<naziv>" + p.getNaziv() + "</naziv>" + eol;
				textToWrite += "</predmet>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = ""; //reset testToWrite
			}
			textToWrite = "</predmeti>" + eol;
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean delete(Context c, int tag) {
		List<Predmet> arrPredmeti = new PredmetXmlParser().parse(c, FILE);
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite = "";
		try {
			arrPredmeti.remove(tag);
			os = c.openFileOutput(FILE, Context.MODE_PRIVATE);
			textToWrite = "<predmeti>" + eol;
			for (Predmet p: arrPredmeti) {
				textToWrite += "<predmet>" + eol;
				textToWrite += "<id>" + p.getId() + "</id>" + eol;
				textToWrite += "<naziv>" + p.getNaziv() + "</naziv>" + eol;
				textToWrite += "</predmet>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = "";
			}
			textToWrite = "</predmeti>" + eol;
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(naziv);
	}
	
	public static final Parcelable.Creator<Predmet> CREATOR = new Parcelable.Creator<Predmet>() {
		public Predmet createFromParcel(Parcel in) {
			return new Predmet(in);
		}

		public Predmet[] newArray(int size) {
			return new Predmet[size];
		}
	};

}
