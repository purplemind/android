package com.android.evindroid.ucenici;

import java.util.List;

import com.android.evindroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewUceniciAdapter extends ArrayAdapter<Ucenik> {

	private final Context context;
	private final List<Ucenik> values;
	private TextView textView;
	private ImageView imageView;
	
	public ListViewUceniciAdapter(Context context, List<Ucenik> values) {
		//myadapter_row_layout je jedna stavka sacinjena od instace ImageView i
		//jedne TextView.
		super(context, R.layout.myadapter_row_layout, values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//return super.getView(position, convertView, parent);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.myadapter_row_layout, parent, false);
		textView = (TextView) rowView.findViewById(R.id.txt_MyAdapterRow);
		imageView = (ImageView) rowView.findViewById(R.id.img_MyAdapterRow);
		Ucenik u = values.get(position);
		textView.setText(u.getIme() + " " + u.getPrezime() + ", " + u.getJmbg());
		imageView.setImageResource(R.drawable.no_photo);
		rowView.setTag(Integer.valueOf(position));
		
		return rowView;
	}

	public List<Ucenik> getValues() {
		return values;
	}

}
