package com.android.evindroid;

import com.android.evindroid.predmeti.PredmetiListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ListaPredmeta extends Activity {

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
		predmetiListView = new PredmetiListView(this);
		layout.addView(predmetiListView);
	}

}
