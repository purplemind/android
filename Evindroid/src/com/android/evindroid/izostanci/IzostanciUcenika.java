package com.android.evindroid.izostanci;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.android.evindroid.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;
import android.widget.Toast;

public class IzostanciUcenika {

		public static class  Izostanak{
			protected String datum;
			protected String opravdani;
			protected String neopravdani;
			
			public Izostanak(String datum, String opravdani, String neopravdani) {
				this.datum = datum;
				if ( opravdani.equals("") || opravdani == null ) this.opravdani = "?";
				else this.opravdani = opravdani;
				if ( neopravdani.equals("") || neopravdani == null ) this.neopravdani = "?";
				else this.neopravdani = neopravdani;
			}
			
			public String getDatum() {
				return datum;
			}
			
			public String getOpravdani() {
				return opravdani;
			}
			
			public String getNeopravdani() {
				return neopravdani;
			}
			
			public int getYear() {
				String[] datumSplit = datum.split("\\.");
				return Integer.valueOf(datumSplit[2]);
			}

			public int getMonth() {
				String[] datumSplit = datum.split("\\.");
				return Integer.valueOf(datumSplit[1]);
			}
			
			public int getDay() {
				String[] datumSplit = datum.split("\\.");
				return Integer.valueOf(datumSplit[0]);
			}
			
		}
		
		public final static int ADD_AND_SAVE = 1;
		public final static int MODIFY_AND_SAVE = 2;
		private String ucenikJmbg;
		private String fileName;
		private List<Izostanak> izostanci;
		private Context context;
		
		public IzostanciUcenika() {
			
		}
		
		public IzostanciUcenika(Context context, String ucenikJmbg) {
			this.ucenikJmbg = ucenikJmbg;
			this.context = context;
			if ( ucenikJmbg != null ) {
				this.fileName = ucenikJmbg + "_izos" + ".xml";
			}
			this.izostanci = new IzostanciXmlParser().parse(context, this.fileName);
		}
		
		public String getUcenikJmbg() {
			return ucenikJmbg;
		}

		public List<Izostanak> getIzostanci() {
			return izostanci;
		}
		
		public void removeIzostanak(int pos) {
			if ( this.izostanci != null && this.izostanci.size() > 0 ) {
				this.izostanci.remove(pos);
			}
		}
		
		private boolean saveToFile() {
			FileOutputStream os = null;
			String eol = System.getProperty("line.separator");
			String textToWrite = "<izostanci>" + eol;
			try {
				os = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
				for (Izostanak p: izostanci) {
					textToWrite += "<single>" + eol;
					textToWrite += "<datum>" + p.getDatum() + "</datum>" + eol;
					textToWrite += "<opravdani>" + p.getOpravdani() + "</opravdani>" + eol;
					textToWrite += "<neopravdani>" + p.getNeopravdani() + "</neopravdani>" + eol;
					textToWrite += "</single>" + eol;
					os.write(textToWrite.getBytes());
					os.flush();
					textToWrite = "";
				}
				textToWrite = "</izostanci>";
				os.write(textToWrite.getBytes());
				os.flush();
				os.close();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
			}			
			
		}
		
		private boolean isInteger(String str) {
			if ( str.equals("?") ) return true;
			return str.matches("[1-9]{1}(\\d+)?");
		}
		
		private boolean correctInput(Izostanak izostanak) {
			if ( isInteger(izostanak.getOpravdani()) && isInteger(izostanak.getNeopravdani()) )
				return true;
			return false;
		}
		
		public void saveIzostanak(final Izostanak izostanak, int pos, int action) {
			
			if ( !correctInput(izostanak) ) {
				String eol = System.getProperty("line.separator");
				Toast.makeText(this.context, "Greska prilikom unosa podataka." + eol +
								"Morate uneti samo broj (bez razmaka).", Toast.LENGTH_LONG).show();
				return;
			}
			
			if ( izostanci == null ) izostanci = new ArrayList<Izostanak>();
			
			switch ( action ) {
			case ADD_AND_SAVE:
				izostanci.add(izostanak);
				break;
			case MODIFY_AND_SAVE:
				this.izostanci.set(pos, izostanak);
				break;
			}
			
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			        	if ( saveToFile() )
			        		Toast.makeText(IzostanciUcenika.this.context, "Izostanci su sačuvani", Toast.LENGTH_LONG).show();
			        	else
			        		Toast.makeText(IzostanciUcenika.this.context, "Izostanci NISU sačuvani. Greska prilikom upisa u memoriju.", Toast.LENGTH_LONG).show();
			            break;
			        case DialogInterface.BUTTON_NEGATIVE:
			        	//nothing or setSavedToFile(false);
			            break;
			        }
				}
        	};
			
			String eol = System.getProperty("line.separator");
			AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
			builder.setTitle("Sačuvati izostanak");
        	builder.setMessage("Datum: " + izostanak.getDatum() + eol + 
        						"Opravdani: " + izostanak.getOpravdani() + eol + 
        						"Neopravdani: " + izostanak.getNeopravdani());
        	builder.setPositiveButton(R.string.da, dialogClickListener);
        	builder.setNegativeButton(R.string.ne, dialogClickListener);
        	builder.show();
		}
		
		public void ukupnoIzostanaka() {
			if ( this.izostanci == null ) {
				this.izostanci = new IzostanciXmlParser().parse(this.context, this.fileName);
			}
			int opravdaniInt = 0;
			int neopravdaniInt = 0;
			for (Izostanak i: this.izostanci) {
				if ( i.getOpravdani() != "?" ) opravdaniInt += Integer.valueOf(i.getOpravdani());
				if ( i.getNeopravdani() != "?" ) neopravdaniInt += Integer.valueOf(i.getNeopravdani());
			}
			String eol = System.getProperty("line.separator");
			Toast.makeText(this.context, "Opravdani: " + String.valueOf(opravdaniInt) + eol +
							"Neopravdani: " + String.valueOf(neopravdaniInt), Toast.LENGTH_LONG).show();
		}
		
		public ListView makeListView() {
			ListView listIzostanci = new ListView(this.context);
			if ( this.izostanci != null ) {
				listIzostanci.setAdapter(new IzostanciListViewAdapter(this.context, this.izostanci));
			}
			return listIzostanci;
		}
		
		public boolean deleteIzostanak(int position) {
			if ( this.izostanci != null && this.izostanci.size() > 0 ) {
				this.izostanci.remove(position);
				if ( saveToFile() )
					return true;
			}
			return false;
		}

}
