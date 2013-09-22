package com.android.evindroid.ocene;

import com.android.evindroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OcenjivanjeActivity extends Activity {

	//TODO: moze se desiti da se obrise predmet ili ima vise predmeta nego ocena!!!
	
	private OceneUcenika oceneUcenika;
	protected ListView oceneListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.ocenjivanje_layout);
		if ( extras == null || !getIntent().hasExtra("ucenikJmbg")) {
			return;
		}
		String ucenikInfo = extras.getString("ime") + " " + extras.getString("prezime") +
							", " + extras.getString("ucenikJmbg");
		TextView txt_ucenik_info = (TextView) findViewById(R.id.txt_ucenik_info_ocene);
		txt_ucenik_info.setText(ucenikInfo);
		
		oceneUcenika = new OceneUcenika(this, extras.getString("ucenikJmbg"));
		oceneListView = (ListView) findViewById(R.id.listView_ocenjivanje);
		oceneUcenika.setListViewAdapter(oceneListView);
		Button btn_sacuvaj = (Button) findViewById(R.id.btn_sacuvaj_ocene);
		btn_sacuvaj.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( oceneUcenika.saveOcene(oceneListView) ) {
					Toast.makeText(OcenjivanjeActivity.this, "Ocene su sačuvane", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(OcenjivanjeActivity.this, "Ocene NISU sačuvane. Proverite unesene ocene.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		Button btn_prikazi_prosek = (Button) findViewById(R.id.btn_prikazi_prosek);
		btn_prikazi_prosek.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				String uspeh = oceneUcenika.opstiUspeh();
				if ( uspeh != null ) {
					Toast.makeText(OcenjivanjeActivity.this, uspeh, Toast.LENGTH_LONG).show();
				}
			}
				
		});
		
		if ( oceneUcenika.getOcene() == null ) {
			Toast.makeText(this, "Nije pronađen ni jedan predmet. Proverite da li ste uneli predmete",
						Toast.LENGTH_LONG).show();
		}
	}

}
