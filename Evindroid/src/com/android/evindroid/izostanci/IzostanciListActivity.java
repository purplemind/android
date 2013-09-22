package com.android.evindroid.izostanci;

import com.android.evindroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class IzostanciListActivity extends Activity {
	
	ListView izostanciList;
	IzostanciUcenika izostanci;
	int izostanakPosition = -1;
	private String action = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_izostanci_layout);
		Bundle extras = getIntent().getExtras();
		if ( extras == null && !getIntent().hasExtra("ucenikJmbg") ) {
			return;
		}
		String ucenikJmbg = extras.getString("ucenikJmbg");
		izostanci = new IzostanciUcenika(this, ucenikJmbg);
		LinearLayout layout = (LinearLayout) findViewById(R.id.izostanci_list_linearlayout);
		izostanciList = izostanci.makeListView();
		layout.addView(izostanciList);
		registerForContextMenu(izostanciList);
		izostanciList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				izostanakPosition = arg2;
				openContextMenu(arg1);
			}
		});

	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("izostanakPosition", izostanakPosition);
		data.putExtra("action", action);
		setResult(RESULT_OK, data);
		super.finish();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.list_izostanci, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.izostanci_modify:
				action = "modify";
				IzostanciListActivity.this.finish();
	            return true;
	        case R.id.izostanci_delete:
	        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	if ( izostanci.deleteIzostanak(izostanakPosition) ) {
				        		IzostanciListViewAdapter adapter = (IzostanciListViewAdapter) izostanciList.getAdapter();
				        		adapter.remove(adapter.getItem(izostanakPosition));
				        		action = "delete";
				        		IzostanciListActivity.this.finish();
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
