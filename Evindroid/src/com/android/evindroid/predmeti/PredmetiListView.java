package com.android.evindroid.predmeti;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PredmetiListView extends ListView {

	private List<Predmet> arrPredmeti = null;
	
	public PredmetiListView(Context context) {
		super(context);
		this.arrPredmeti = new PredmetXmlParser().parse(context, Predmet.FILE);
		setAdapter(new PredmetiListViewAdapter(context, arrPredmeti));
	}

	public PredmetiListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PredmetiListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the arrUcenici
	 */
	public List<Predmet> getArrPredmeti() {
		PredmetiListViewAdapter adapter = (PredmetiListViewAdapter) this.getAdapter();
		return adapter.getValues();
	}
	
	public void updateItem(Predmet p, int position) {
		PredmetiListViewAdapter adapter = (PredmetiListViewAdapter) this.getAdapter();
		adapter.getValues().set(position, p);
		adapter.notifyDataSetChanged();
	}
	
	public void addItem(Predmet p) {
		PredmetiListViewAdapter adapter = (PredmetiListViewAdapter) this.getAdapter();
		adapter.add(p);
		adapter.notifyDataSetChanged();
	}
	
	public void removeItem(int position) {
		PredmetiListViewAdapter adapter = (PredmetiListViewAdapter) this.getAdapter();
		adapter.remove(adapter.getItem(position));
	}

}
