package com.android.evindroid.ucenici;

import java.io.FileOutputStream;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Ucenik implements Parcelable {
	public final static String FILE = "podaci.xml";
	private String ime;
	private String prezime;
	private String jmbg;
	private String br_mk;
	private String img_url;

	public Ucenik() {
		
	}
	
	public Ucenik(String ime, String prezime, String jmbg, String br_mk, String img_url) {
		this.ime = ime;
		this.prezime = prezime;
		this.jmbg = jmbg;
		this.br_mk = br_mk;
		this.img_url = img_url;
	}

	public Ucenik(Parcel source) {
		ime = source.readString();
		prezime = source.readString();
		jmbg = source.readString();
		br_mk = source.readString();
		img_url = source.readString();
	}
	
	public Ucenik(String ime, String prezime, String jmbg, String br_mk) {
		this.ime = ime;
		this.prezime = prezime;
		this.jmbg = jmbg;
		this.br_mk = br_mk;
		this.img_url = null;
	}

	public String getIme() {
		return ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public String getJmbg() {
		return jmbg;
	}

	public String getBr_mk() {
		return br_mk;
	}

	public String getImg_url() {
		return img_url;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(ime);
		dest.writeString(prezime);
		dest.writeString(jmbg);
		dest.writeString(br_mk);
		dest.writeString(img_url);
	}

	public static final Parcelable.Creator<Ucenik> CREATOR = new Parcelable.Creator<Ucenik>() {
		public Ucenik createFromParcel(Parcel in) {
			return new Ucenik(in);
		}

		public Ucenik[] newArray(int size) {
			return new Ucenik[size];
		}
	};

	/**
	 * @param ime the ime to set
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * @param prezime the prezime to set
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	/**
	 * @param jmbg the jmbg to set
	 */
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	/**
	 * @param br_mk the br_mk to set
	 */
	public void setBr_mk(String br_mk) {
		this.br_mk = br_mk;
	}

	/**
	 * @param img_url the img_url to set
	 */
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public boolean addToFile(Context c) {
		List<Ucenik> arrUcenici = new UcenikXmlParser().parse(c, FILE);
		arrUcenici.add(this);
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite;
		try {
			os = c.openFileOutput(FILE, Context.MODE_PRIVATE);
			textToWrite = "<ucenici>" + eol;
			for (Ucenik u: arrUcenici) {
				textToWrite += "<ucenik>" + eol;
				textToWrite += "<ime>" + u.getIme() + "</ime>" + eol;
				textToWrite += "<prezime>" + u.getPrezime() + "</prezime>" + eol;
				textToWrite += "<jmbg>" + u.getJmbg() + "</jmbg>" + eol;
				textToWrite += "<broj_mk>" + u.getBr_mk() + "</broj_mk>" + eol;
				textToWrite += "</ucenik>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = ""; //reset textToWrite
			}
			textToWrite = "</ucenici>" + eol;
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean modify(Context c, int tag) {
		List<Ucenik> arrUcenici = new UcenikXmlParser().parse(c, FILE);
		if ( tag == -1 ) return false;
		arrUcenici.set(tag, this); //ucenik with changed general data
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite;
		try {
			os = c.openFileOutput(FILE, Context.MODE_PRIVATE);
			textToWrite = "<ucenici>" + eol;
			for (Ucenik u: arrUcenici) {
				textToWrite += "<ucenik>" + eol;
				textToWrite += "<ime>" + u.getIme() + "</ime>" + eol;
				textToWrite += "<prezime>" + u.getPrezime() + "</prezime>" + eol;
				textToWrite += "<jmbg>" + u.getJmbg() + "</jmbg>" + eol;
				textToWrite += "<broj_mk>" + u.getBr_mk() + "</broj_mk>" + eol;
				textToWrite += "</ucenik>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = ""; //reset testToWrite
			}
			textToWrite = "</ucenici>" + eol;
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean delete(Context c, int tag) {
		List<Ucenik> arrUcenici = new UcenikXmlParser().parse(c, FILE);
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite = "";
		try {
			arrUcenici.remove(tag);
			os = c.openFileOutput(FILE, Context.MODE_PRIVATE);
			textToWrite = "<ucenici>" + eol;
			for (Ucenik u: arrUcenici) {
				textToWrite += "<ucenik>" + eol;
				textToWrite += "<ime>" + u.getIme() + "</ime>" + eol;
				textToWrite += "<prezime>" + u.getPrezime() + "</prezime>" + eol;
				textToWrite += "<jmbg>" + u.getJmbg() + "</jmbg>" + eol;
				textToWrite += "<broj_mk>" + u.getBr_mk() + "</broj_mk>" + eol;
				textToWrite += "</ucenik>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = "";
			}
			textToWrite = "</ucenici>" + eol;
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
}
