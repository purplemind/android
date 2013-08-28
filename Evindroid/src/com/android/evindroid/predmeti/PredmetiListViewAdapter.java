package com.android.evindroid.predmeti;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.evindroid.R;

public class PredmetiListViewAdapter extends ArrayAdapter<Predmet>{

	private final Context context;
	private final List<Predmet> values;
	private TextView id_nazivTextView;
	
	public PredmetiListViewAdapter(Context context, List<Predmet> values) {
		//myadapter_row_layout je jedna stavka sacinjena od instace ImageView i
		//jedne TextView.
		super(context, R.layout.predmet_row_layout, values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//return super.getView(position, convertView, parent);
		View rowView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.predmet_row_layout, parent, false);
		id_nazivTextView = (TextView) rowView.findViewById(R.id.id_naziv_PredmetTextView);
		if (values!=null) {
			Predmet p = values.get(position); 
			id_nazivTextView.setText(p.getNaziv() + ", " + p.getId());
			rowView.setTag(Integer.valueOf(position));
		}
		return rowView;
	}

	public List<Predmet> getValues() {
		return values;
	}

}
