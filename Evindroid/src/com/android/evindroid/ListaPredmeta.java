package com.android.evindroid;

import com.android.evindroid.predmeti.Predmet;
import com.android.evindroid.predmeti.PredmetiListView;
import com.android.evindroid.ucenici.Ucenik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ListaPredmeta extends Activity {

	private static final int REQUEST_CODE = 10;
	private int predmet_pos;
	
	PredmetiListView predmetiListView;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spisak_predmeta);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_list_predmeti);
		
		try {
			predmetiListView = new PredmetiListView(this);
			layout.addView(predmetiListView, 0);
			registerForContextMenu(predmetiListView);
			predmetiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					predmet_pos = arg2;
					openContextMenu(arg1);
				}
			});
		} catch (Exception e) {
			//predmetiListView may be null!
			//TODO: search for better solution!
		}
		
		Button btn_dodaj = (Button) findViewById(R.id.btn_dodaj_predmet);
		btn_dodaj.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent predmetDetailsIntent = new Intent(ListaPredmeta.this, PredmetDetailsActivity.class);
				predmetDetailsIntent.putParcelableArrayListExtra("predmet", null);
				predmetDetailsIntent.putExtra("op", "add");
				startActivityForResult(predmetDetailsIntent, REQUEST_CODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if ( data.hasExtra("saveOk") && data.hasExtra("saveText") ) {
				boolean saveOk = data.getBooleanExtra("saveOk", false);
				String saveText = data.getStringExtra("saveText");
				Toast.makeText(this, saveText, Toast.LENGTH_LONG).show();
				if ( saveOk ) {
					Predmet p = data.getParcelableExtra("predmet");
					int tag = data.getIntExtra("tag", -1);
					//update predmetiListView
					if ( tag != -1 && data.getStringExtra("op").compareTo("modify") == 0) {
						predmetiListView.updateItem(p, tag);
					}
					//add item to predmetiListView
					else if ( data.getStringExtra("op").compareTo("add") == 0 ) {
						predmetiListView.addItem(p);
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
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_predmeti, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.modify_predmet:
				Intent predmetDetailsActivity = new Intent(ListaPredmeta.this, PredmetDetailsActivity.class);
				predmetDetailsActivity.putExtra("predmet", (Parcelable)predmetiListView.getArrPredmeti().get(predmet_pos));
				predmetDetailsActivity.putExtra("tag", predmet_pos);
				predmetDetailsActivity.putExtra("op", "modify");
				startActivityForResult(predmetDetailsActivity, REQUEST_CODE);
	            return true;
	        case R.id.delete_predmet:
	        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	if ( Ucenik.delete(ListaPredmeta.this, predmet_pos) ) {
				        		predmetiListView.removeItem(predmet_pos);
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
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

}
