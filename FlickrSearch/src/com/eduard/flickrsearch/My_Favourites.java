package com.eduard.flickrsearch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.assignment.carousel.MyFragment;

public class My_Favourites extends Activity {
	GridView gv;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my__favourites);

		gv = (GridView) findViewById(R.id.gridView1);
		gv.setAdapter(new CustomAdapter(this, MyFragment.myfav));
	}
}