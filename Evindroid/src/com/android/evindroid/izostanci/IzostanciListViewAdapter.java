package com.android.evindroid.izostanci;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.evindroid.R;
import com.android.evindroid.izostanci.IzostanciUcenika.Izostanak;

public class IzostanciListViewAdapter extends ArrayAdapter<Izostanak> {

	private List<Izostanak> values;
	private Context context;
	
	
	public IzostanciListViewAdapter(Context context, List<Izostanak> values) {
		super(context, R.layout.row_izostanci_list_layout, values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//return super.getView(position, convertView, parent);
		View rowView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.row_izostanci_list_layout, parent, false);
		TextView txtDatum = (TextView) rowView.findViewById(R.id.txt_datum_list);
		TextView txtOpravdani = (TextView) rowView.findViewById(R.id.txt_opravdani_list);
		TextView txtNeopravdani = (TextView) rowView.findViewById(R.id.txt_neopravdani_list);
		if (values!=null) {
			Izostanak i = values.get(position);
			txtDatum.setText(i.getDatum());
			txtOpravdani.setText(i.getOpravdani());
			txtNeopravdani.setText(i.getNeopravdani());
			rowView.setTag(Integer.valueOf(position));
		}
		return rowView;

	}

}
