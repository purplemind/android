package com.android.evindroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View root = findViewById(R.id.id_LayoutRoot);
		root.setBackgroundResource(R.drawable.bg_gradient);
		((Button) findViewById(R.id.btn_ListaUcenika)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent spisakUcenikaAcivity = new Intent(MainActivity.this, ListaUcenika.class);
					startActivity(spisakUcenikaAcivity);
				}
			
			}
		);
		
		((Button) findViewById(R.id.btn_ListPredmeta)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent spisakPredmetaAcivity = new Intent(MainActivity.this, ListaPredmeta.class);
				startActivity(spisakPredmetaAcivity);
			}
		
		}
	);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
