package com.android.evindroid.ucenici;

import com.android.evindroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UcenikDetailsActivity extends Activity {

	protected int tag;
	protected String op;
	protected Ucenik u;
	protected boolean saveOk;
	protected String saveText;
	private EditText ime_edit;
	private EditText prezime_edit;
	private EditText jmbg_edit;
	private EditText broj_mk_edit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ucenik_details);
	    Bundle extras = getIntent().getExtras();
	    saveText = "Succesfully saved!";
	    saveOk = true;
	    if (extras == null) {
	        return;
	    }
	    
		ime_edit = (EditText) findViewById(R.id.ime_editText);
		prezime_edit = (EditText) findViewById(R.id.prezime_editText);
		jmbg_edit = (EditText) findViewById(R.id.jmbg_editText);
		broj_mk_edit = (EditText) findViewById(R.id.broj_mk_editText);
		    
		u = extras.getParcelable("ucenik");
		//arrUcenici = extras.getParcelableArrayList("ucenici");
		if ( getIntent().hasExtra("tag") ) tag = extras.getInt("tag");
		else tag = -1;
		op = extras.getString("op");

		if ( u != null && op.compareTo("modify") == 0) {
	    	ime_edit.setText(u.getIme());
	    	prezime_edit.setText(u.getPrezime());
	    	jmbg_edit.setText(u.getJmbg());
	    	broj_mk_edit.setText(u.getBr_mk());
	    } //else: nothing - leave all EditText views empty
	    
	    //TODO: check empty field!!!
	    Button btn_sacuvaj = (Button) findViewById(R.id.btn_sacuvaj_ucenik);
	    btn_sacuvaj.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				UcenikDetailsActivity.this.doJobWithUcenik();
				UcenikDetailsActivity.this.finish();
			}
		});
	}
	
	protected void doJobWithUcenik() {
		if ( u == null ) u = new Ucenik();
		u.setIme(ime_edit.getText().toString());
		u.setPrezime(prezime_edit.getText().toString());
		u.setJmbg(jmbg_edit.getText().toString());
		u.setBr_mk(broj_mk_edit.getText().toString());
		
		if ( op.compareTo("add") == 0 ) {
			if ( !u.saveUcenik(UcenikDetailsActivity.this) ) {
				saveText = "Error saving...";
				saveOk = false;
			}
		}
		else if ( op.compareTo("modify") == 0 ) {
			if ( !u.modify(UcenikDetailsActivity.this, tag) ) {
				saveText = "Error saving...";
				saveOk = false;
			}
		}
		Toast.makeText(UcenikDetailsActivity.this, saveText, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("op", op);
		data.putExtra("saveOk", saveOk);
		data.putExtra("saveText", saveText);
		data.putExtra("tag", tag);
		data.putExtra("ucenik", (Parcelable)u);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
