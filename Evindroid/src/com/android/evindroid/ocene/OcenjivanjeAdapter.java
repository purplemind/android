package com.android.evindroid.ocene;

import java.util.List;

import com.android.evindroid.R;
import com.android.evindroid.ocene.OceneUcenika.PredmetOcena;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class OcenjivanjeAdapter extends ArrayAdapter<PredmetOcena> {

	private final Context context;
	private final List<PredmetOcena> values;	
	
	public OcenjivanjeAdapter(Context context, List<PredmetOcena> values) {
		super(context, R.layout.row_ocenjivanje_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_ocenjivanje_layout, parent, false);
		TextView txtPredmet = (TextView) rowView.findViewById(R.id.txt_naziv_predmeta);
		EditText edtOcene = (EditText) rowView.findViewById(R.id.edt_ocene_premdeta);
		if ( values!=null ) {
			txtPredmet.setText(values.get(position).getPredmet());
			edtOcene.setText(values.get(position).getOcena());
		}
		return rowView;
	}

}
