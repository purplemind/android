package com.android.evindroid.ucenici;


import com.android.evindroid.R;
import com.android.evindroid.izostanci.IzostanciActivity;
import com.android.evindroid.ocene.OcenjivanjeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ListaUcenika extends Activity {

	private static final int REQUEST_CODE = 10; 
	private UceniciListView uceniciListView;
	private Button btn_dodaj;
	private int ucenik_pos;
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_ucenici, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.modify_ucenik:
				Intent ucenikDetailsIntent = new Intent(ListaUcenika.this, UcenikDetailsActivity.class);
				ucenikDetailsIntent.putExtra("ucenik", (Parcelable)uceniciListView.getArrUcenici().get(ucenik_pos));
				ucenikDetailsIntent.putExtra("tag", ucenik_pos);
				ucenikDetailsIntent.putExtra("op", "modify");
				startActivityForResult(ucenikDetailsIntent, REQUEST_CODE);
	            return true;
	        case R.id.delete_ucenik:
	        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	if ( Ucenik.delete(ListaUcenika.this, ucenik_pos) ) {
				        		uceniciListView.removeItem(ucenik_pos);
				        	}
				            break;
				        case DialogInterface.BUTTON_NEGATIVE:
				            //do nothing
				            break;
				        }
					}
	        	};
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.da_li_ste_sigurni);
	        	builder.setPositiveButton(R.string.da, dialogClickListener);
	        	builder.setNegativeButton(R.string.ne, dialogClickListener);
	        	builder.show();
	            return true;
	        case R.id.show_ocene:
				Intent ocenjivanjeIntent = new Intent(ListaUcenika.this, OcenjivanjeActivity.class);
				ocenjivanjeIntent.putExtra("ucenikJmbg", uceniciListView.getArrUcenici().get(ucenik_pos).getJmbg());
				ocenjivanjeIntent.putExtra("ime",  uceniciListView.getArrUcenici().get(ucenik_pos).getIme());
				ocenjivanjeIntent.putExtra("prezime",  uceniciListView.getArrUcenici().get(ucenik_pos).getPrezime());
				startActivityForResult(ocenjivanjeIntent, REQUEST_CODE);	        	
	        	return true;
	        case R.id.show_izostanci:
				Intent izostanciIntent = new Intent(ListaUcenika.this, IzostanciActivity.class);
				izostanciIntent.putExtra("ucenikJmbg", uceniciListView.getArrUcenici().get(ucenik_pos).getJmbg());
				izostanciIntent.putExtra("ime",  uceniciListView.getArrUcenici().get(ucenik_pos).getIme());
				izostanciIntent.putExtra("prezime",  uceniciListView.getArrUcenici().get(ucenik_pos).getPrezime());
				startActivityForResult(izostanciIntent, REQUEST_CODE);	        	
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spisak_ucenika);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_list_ucenici);
		uceniciListView = new UceniciListView(this);
		registerForContextMenu(uceniciListView);

		uceniciListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ucenik_pos = arg2;
				openContextMenu(arg1);
			}
		});
		layout.addView(uceniciListView, 0);
		btn_dodaj = (Button) findViewById(R.id.btn_dodaj_ucenika);
		btn_dodaj.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( v.getId() == R.id.btn_dodaj_ucenika) { //TODO: da li je uopste potrebno?!
					Intent ucenikDetailsIntent = new Intent(ListaUcenika.this, UcenikDetailsActivity.class);
					ucenikDetailsIntent.putParcelableArrayListExtra("ucenik", null);
					ucenikDetailsIntent.putExtra("op", "add");
					startActivityForResult(ucenikDetailsIntent, REQUEST_CODE);
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if ( data.hasExtra("saveOk") ) {
				boolean saveOk = data.getBooleanExtra("saveOk", false);;
				if ( saveOk ) {
					Ucenik u = data.getParcelableExtra("ucenik");
					int tag = data.getIntExtra("tag", -1);
					//update uceniciListView
					if ( tag != -1 && data.getStringExtra("op").compareTo("modify") == 0) {
						uceniciListView.updateItem(u, tag);
					}
					//add item to uceniciListView
					else if ( data.getStringExtra("op").compareTo("add") == 0 ) {
						uceniciListView.addItem(u);
					}
				}
			}
			else {
				Toast.makeText(this, "Something is wrong!", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//populateListView();
	}

}
