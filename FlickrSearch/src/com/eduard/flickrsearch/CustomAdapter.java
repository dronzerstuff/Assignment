package com.eduard.flickrsearch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CustomAdapter extends BaseAdapter {

	Context context;
	ArrayList<String> imageId = new ArrayList<String>();;
	private static LayoutInflater inflater = null;

	public CustomAdapter(My_Favourites myfav, ArrayList<String> prgmImages) {
		// TODO Auto-generated constructor stub
		context = myfav;
		imageId = prgmImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageId.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		ImageView img;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.custom_item, null);
		holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
		Picasso.with(context).load(imageId.get(position)).into(holder.img);

	

		return rowView;
	}

}