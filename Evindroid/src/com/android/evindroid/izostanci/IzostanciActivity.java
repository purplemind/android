package com.android.evindroid.izostanci;

import com.android.evindroid.R;
import com.android.evindroid.izostanci.IzostanciUcenika.Izostanak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class IzostanciActivity extends Activity {

	private final static int GET_IZOSTANAK_POS = 1;
	protected IzostanciUcenika izostanci;
	private Bundle extras;
	private String action = "";
	int izostanakPosition = -1;
	
	/*****	OnCreate   *****/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.izostanci_layout);
		
		extras = getIntent().getExtras();
		if ( extras == null || !getIntent().hasExtra("ucenikJmbg")) {
			return;
		}

		TextView txt_ucenik_info = (TextView) findViewById(R.id.txt_izostanci_jmbg);
		String ucenikInfo = extras.getString("ime") + " " + extras.getString("prezime")
							+ ", " + extras.getString("ucenikJmbg");  
		txt_ucenik_info.setText(ucenikInfo);
		
		izostanci = new IzostanciUcenika(this, extras.getString("ucenikJmbg"));
		
		Button btn_sacuvaj = (Button) findViewById(R.id.btn_sacuvaj_izostanci);
		btn_sacuvaj.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePicker datum = (DatePicker) findViewById(R.id.datePicker1);
				EditText edtOpravdani = (EditText) findViewById(R.id.edtOpravdani);
				EditText edtNeopravdani = (EditText) findViewById(R.id.edtNeopravdani);
				String datumString = datum.getDayOfMonth() + "." + (datum.getMonth() + 1) + "." + datum.getYear();
				String oString = edtOpravdani.getText().toString();
				String neoString = edtNeopravdani.getText().toString();
				if ( action.equals("") ) {
					izostanci.saveIzostanak( new Izostanak(datumString, oString, neoString), izostanakPosition, IzostanciUcenika.ADD_AND_SAVE );
				}
				if ( action.equals("modify") ) {
					izostanci.saveIzostanak( new Izostanak(datumString, oString, neoString), izostanakPosition, IzostanciUcenika.MODIFY_AND_SAVE );
					action = ""; //reset action
				}
			}
		});
		
		Button btn_ukupno = (Button) findViewById(R.id.btn_izostanci_ukupno);
		btn_ukupno.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				izostanci.ukupnoIzostanaka();
			}
			
		});
		
		Button btn_prikazi_izostanke = (Button) findViewById(R.id.btn_prikaz_izostanaka);
		btn_prikazi_izostanke.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( extras == null || !getIntent().hasExtra("ucenikJmbg")) {
					return;
				}
				Intent listIzostanciIntent = new Intent(IzostanciActivity.this, IzostanciListActivity.class);
				listIzostanciIntent.putExtra("ucenikJmbg", extras.getString("ucenikJmbg"));
				startActivityForResult(listIzostanciIntent, GET_IZOSTANAK_POS);
			}
			
		});
	}
	
	/**** onActivityResult ****/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( requestCode == GET_IZOSTANAK_POS && resultCode == RESULT_OK && data.hasExtra("izostanakPosition") ) {
			izostanakPosition = data.getIntExtra("izostanakPosition", -1);
			action = data.getStringExtra("action");
			if ( izostanakPosition > -1 && action.equals("delete") ) {
				izostanci.removeIzostanak(izostanakPosition);
				action = ""; //reset action
			}
			if ( izostanakPosition > -1 && action.equals("modify") ) {
				Izostanak izostanak = izostanci.getIzostanci().get(izostanakPosition);
				DatePicker datum = (DatePicker) findViewById(R.id.datePicker1);
				EditText edtOpravdani = (EditText) findViewById(R.id.edtOpravdani);
				EditText edtNeopravdani = (EditText) findViewById(R.id.edtNeopravdani);
				datum.updateDate(izostanak.getYear(), izostanak.getMonth()-1, izostanak.getDay());
				edtOpravdani.setText(izostanak.getOpravdani());
				edtNeopravdani.setText(izostanak.getNeopravdani());
			}
		}
	}
}
