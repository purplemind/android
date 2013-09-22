package com.android.evindroid.predmeti;

import com.android.evindroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PredmetDetailsActivity extends Activity {
	
	protected int tag;
	protected String op;
	protected Predmet p;
	protected boolean saveOk;
	protected String saveText;
	private EditText naziv_edit;
	private EditText id_edit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.predmet_details);
	    Bundle extras = getIntent().getExtras();
	    saveText = "Succesfully saved!";
	    saveOk = true;
	    if (extras == null) {
	        return;
	    }
	    
		naziv_edit = (EditText) findViewById(R.id.edt_naziv);
		id_edit = (EditText) findViewById(R.id.edt_id);
		    
		p = extras.getParcelable("predmet");
		if ( getIntent().hasExtra("tag") ) tag = extras.getInt("tag");
		else tag = -1;
		op = extras.getString("op");

		if ( p != null && op.compareTo("modify") == 0) {
	    	naziv_edit.setText(p.getNaziv());
	    	id_edit.setText(p.getId());
	    } //else: nothing - leave all EditText views empty
	    
	    //TODO: check empty field!!!
	    Button btn_sacuvaj = (Button) findViewById(R.id.btn_sacuvaj_predmet);
	    btn_sacuvaj.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				PredmetDetailsActivity.this.doJobWithPredmet();
				PredmetDetailsActivity.this.finish();
			}
		});
	}
	
	protected void doJobWithPredmet() {
		if ( p == null ) p = new Predmet();
		p.setNaziv(naziv_edit.getText().toString());
		p.setId(id_edit.getText().toString());
		
		if ( op.compareTo("add") == 0 ) {
			if ( !p.addToFile(PredmetDetailsActivity.this) ) {
				saveText = "Error saving...";
				saveOk = false;
			}
		}
		else if ( op.compareTo("modify") == 0 ) {
			if ( !p.modify(PredmetDetailsActivity.this, tag) ) {
				saveText = "Error saving...";
				saveOk = false;
			}
		}		
	}
	
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("op", op);
		data.putExtra("saveOk", saveOk);
		data.putExtra("saveText", saveText);
		data.putExtra("tag", tag);
		data.putExtra("predmet", (Parcelable)p);
		setResult(RESULT_OK, data);
		super.finish();
	}

}
