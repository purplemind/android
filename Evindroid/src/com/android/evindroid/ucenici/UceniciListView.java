package com.android.evindroid.ucenici;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class UceniciListView extends ListView {

	private List<Ucenik> arrUcenici = null;
	
	public UceniciListView(Context context) {
		super(context);
		this.arrUcenici = new UcenikXmlParser().parse(context, Ucenik.FILE);
		setAdapter(new ListViewUceniciAdapter(context, arrUcenici));		
	}

	public UceniciListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UceniciListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the arrUcenici
	 */
	public List<Ucenik> getArrUcenici() {
		return arrUcenici;
	}
	
	public void updateItem(Ucenik u, int position) {
		ListViewUceniciAdapter adapter = (ListViewUceniciAdapter) this.getAdapter();
		adapter.getValues().set(position, u);
		adapter.notifyDataSetChanged();
	}
	
	public void addItem(Ucenik u) {
		ListViewUceniciAdapter adapter = (ListViewUceniciAdapter) this.getAdapter();
		adapter.add(u);
		adapter.notifyDataSetChanged();
	}
	
	public void removeItem(int position) {
		ListViewUceniciAdapter adapter = (ListViewUceniciAdapter) this.getAdapter();
		adapter.remove(adapter.getItem(position));
	}
}
