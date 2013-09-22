package com.android.evindroid.ocene;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.android.evindroid.predmeti.Predmet;
import com.android.evindroid.predmeti.PredmetXmlParser;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OceneUcenika {

	public static class PredmetOcena {
		protected String predmet;
		protected String ocena;
		
		PredmetOcena(String predmet, String ocena) {
			if ( predmet == null ) predmet = "";
			if ( ocena == null ) ocena = "";
			this.predmet = predmet;
			this.ocena = ocena;
		}
		
		String getPredmet() {
			return predmet;
		}
		
		String getOcena() {
			return ocena;
		}
		
		void setPredmet(String predmet) {
			this.predmet = predmet;
		}
		
		void setOcena(String ocena) {
			this.ocena = ocena;
		}
	}
	
	private String ucenikJmbg;
	private String fileName;
	private List<PredmetOcena> predmetOcene;
	private Context context; 
	
	public OceneUcenika() {
		
	}
	
	public OceneUcenika(Context context, String ucenikJmbg) {
		this.ucenikJmbg = ucenikJmbg;
		this.context = context;
		if ( ucenikJmbg != null ) {
			this.fileName = ucenikJmbg + ".xml";
		}
		List<Predmet> predmeti = new PredmetXmlParser().parse(context, Predmet.FILE);
		if ( predmeti != null ) {
			this.predmetOcene =  new OceneXmlParser().parse(context, fileName);
			if ( predmetOcene == null ) {
				predmetOcene = new ArrayList<PredmetOcena>();
				for (Predmet p : predmeti) {
					predmetOcene.add(new PredmetOcena(p.getNaziv(), ""));
				}				
			}
			int i;
			for (i=predmetOcene.size(); i<predmeti.size(); i++) {
				predmetOcene.add( new PredmetOcena(predmeti.get(i).getNaziv(), "") );
			}
		}
	}
	
	public void addOcena(PredmetOcena ocena) {
		predmetOcene.add(ocena);
	}

	public String getUcenikjmbg() {
		return ucenikJmbg;
	}

	public List<PredmetOcena> getOcene() {
		return predmetOcene;
	}
	
	public void setListViewAdapter(ListView ocenjivanjeListView) {
		ocenjivanjeListView.setAdapter(new OcenjivanjeAdapter(this.context, this.predmetOcene));
	}
	
	private boolean correctInput(String ocene) {
		if ( ocene == null || ocene == "" ) return true;
		if ( ocene.matches("[1-5]{1}|([1-5]\\s+)*[1-5]") ) return true;
		return false;
	}
	
	private boolean populatePredmetOcene(ListView ocenjivanjeListView) {
		LinearLayout rowLayout;
		String predmet = "";
		String ocena = "";
		int i;
		for (i=0; i<ocenjivanjeListView.getCount(); i++) {
			rowLayout = (LinearLayout)ocenjivanjeListView.getChildAt(i);
			predmet = ((TextView)rowLayout.getChildAt(0)).getText().toString();
			ocena = ((EditText)rowLayout.getChildAt(1)).getText().toString();
			if ( !correctInput(ocena) )
				return false; 
			predmetOcene.set(i, new PredmetOcena(predmet, ocena) );
		}
		return true;
	}
	
	public boolean saveOcene(ListView ocenjivanjeListView) {
		if ( !this.populatePredmetOcene(ocenjivanjeListView) ) 
			return false;
		FileOutputStream os = null;
		String eol = System.getProperty("line.separator");
		String textToWrite = "<ocene>" + eol;
		try {
			os = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
			for (PredmetOcena p: this.predmetOcene) {
				textToWrite += "<single>" + eol;
				textToWrite += "<predmet>" + p.getPredmet() + "</predmet>" + eol;
				textToWrite += "<ocena>" + p.getOcena() + "</ocena>" + eol;
				textToWrite += "</single>" + eol;
				os.write(textToWrite.getBytes());
				os.flush();
				textToWrite = "";
			}
			textToWrite = "</ocene>";
			os.write(textToWrite.getBytes());
			os.flush();
			os.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	private int prosekPredmeta(String ocene) {
		if ( ocene == null || ocene == "" ) 
			return -1;
		ocene.trim();
		ocene += " "; //because of matcher 
		if ( !ocene.matches("([1-5]\\s+)*") ) {
			return 0;
		}
		String[] oceneSingle = ocene.split("\\s+");
		int suma = 0;
		for (String ocena: oceneSingle) {
			suma += Integer.valueOf(ocena);
		}
		return Math.round((float)suma/oceneSingle.length);
	}
	
	public String opstiUspeh() {
		this.predmetOcene =  new OceneXmlParser().parse(context, fileName);
		if ( this.predmetOcene == null )
			return null;
		int ocenaPredmeta;
		int suma = 0;
		int brojac = 0;
		for (PredmetOcena p: this.predmetOcene) {
			ocenaPredmeta = prosekPredmeta(p.ocena);
			switch (ocenaPredmeta) {
				case -1:
					break;
				case 0:
					return "Neke od ocene nisu unete ispravno.";
				case 1:
					return "Nedovoljan";
				default:
					brojac++;
					suma += ocenaPredmeta;
			}
		}
		String mathRound = String.valueOf(Math.round((float)suma/brojac*100.00)/100.00);
		float prosek = (float)suma/brojac;
		if ( prosek >= 4.50 ) return "Odličan " + mathRound;
		if ( prosek >= 3.50 && prosek < 4.50 ) return "Vrlo dobar " + mathRound;
		if ( prosek >= 2.50 && prosek < 3.50 ) return "Dobar " + mathRound;
		if ( prosek >= 1.50 && prosek < 2.50 ) return "Dovoljan " + mathRound;
		return null;
	}
}
