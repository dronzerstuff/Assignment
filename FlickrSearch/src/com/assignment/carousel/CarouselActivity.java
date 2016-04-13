package com.assignment.carousel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.eduard.flickrsearch.ChatArrayAdapter;
import com.eduard.flickrsearch.FloatingActionButton;
import com.eduard.flickrsearch.MainActivity;
import com.eduard.flickrsearch.My_Favourites;
import com.eduard.flickrsearch.R;

public class CarouselActivity extends FragmentActivity {
	public final static int LOOPS = 1;
	// public static int FIRST_PAGE; // = count * LOOPS / 2;
	public final static float BIG_SCALE = 1.0f;
	public final static float SMALL_SCALE = 0.7f;
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
	public MyPagerAdapter adapter;
	public ViewPager pager;
	public static int first_post = 0;

	int page = 10;
	/*** variables for the View */
	public static ArrayList<String> coverUrl = new ArrayList<String>();
	public static int count;

	public static CarouselActivity mainActivityCtx;

	public static int currentPage = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_carousel);
		Button load = (Button) findViewById(R.id.load);
		if(!coverUrl.isEmpty())
		{
			coverUrl.clear();
		}
		String p = MainActivity.Allimage.get(ChatArrayAdapter.c).toString()
				.replace("[", "").replace("]", "").replace(" ", "");
		coverUrl = new ArrayList<String>(Arrays.asList(p.split(",")));
		FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
				.withDrawable(getResources().getDrawable(R.drawable.favorites))
				.withButtonColor(Color.WHITE)
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 16, 100).create();

		fabButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (MyFragment.myfav.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"You hav'nt liked any image ", Toast.LENGTH_SHORT)
							.show();

				} else {
					Intent in = new Intent(getApplicationContext(),
							My_Favourites.class);
					startActivity(in);
				}

			}
		});
		load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				page = page + 10;
				new Loadmore().execute();

			}
		});
		mainActivityCtx = this;
		pager = (ViewPager) findViewById(R.id.myviewpager);
		count = coverUrl.size();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int pageMargin = 0;
		pageMargin = (int) ((metrics.widthPixels / 4) * 2);
		pager.setPageMargin(-pageMargin);

		try {
			adapter = new MyPagerAdapter(this, this.getSupportFragmentManager());
			pager.setAdapter(adapter);
			adapter.notifyDataSetChanged();


			pager.setOnPageChangeListener(adapter);
			pager.setCurrentItem(first_post); // FIRST_PAGE
			pager.setOffscreenPageLimit(3);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private class Loadmore extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String qResult = null;
			String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
			String FlickrQuery_per_page = "&per_page=10";
			String FlickrQuery_page = "&page=" + Integer.toString(page);
			String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
			String FlickrQuery_format = "&format=json";
			String FlickrQuery_tag = "&tags=";
			String FlickrQuery_key = "&api_key=";
			String searchQ;
			String FlickrApiKey = "8c71c2f6b1340444643239f17fa2b8dc";
			String qString = FlickrQuery_url + FlickrQuery_per_page
					+ FlickrQuery_page + FlickrQuery_nojsoncallback
					+ FlickrQuery_format + FlickrQuery_tag + ""+ChatArrayAdapter.keyword+""
					+ FlickrQuery_key + FlickrApiKey;
			System.out.println(qString);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(qString);

			try {
				HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

				if (httpEntity != null) {
					InputStream inputStream = httpEntity.getContent();
					Reader in = new InputStreamReader(inputStream);
					BufferedReader bufferedreader = new BufferedReader(in);
					StringBuilder stringBuilder = new StringBuilder();

					String stringReadLine = null;

					while ((stringReadLine = bufferedreader.readLine()) != null) {
						stringBuilder.append(stringReadLine + "\n");
					}

					qResult = stringBuilder.toString();

					inputStream.close();
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				JSONObject JsonObject = new JSONObject(qResult);
				JSONObject Json_photos = JsonObject.getJSONObject("photos");
				JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");

				for (int i = 0; i < JsonArray_photo.length(); i++) {
					JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(i);
					String flickrId = FlickrPhoto.getString("id");
					String flickrOwner = FlickrPhoto.getString("owner");
					String flickrSecret = FlickrPhoto.getString("secret");
					String flickrServer = FlickrPhoto.getString("server");
					String flickrFarm = FlickrPhoto.getString("farm");
					String flickrTitle = FlickrPhoto.getString("title");

					coverUrl.add("http://farm" + flickrFarm
							+ ".static.flickr.com/" + flickrServer + "/"
							+ flickrId + "_" + flickrSecret + "_m.jpg");

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println(qResult);

			return qResult;
		}

		@Override
		protected void onPostExecute(String result) {
			MainActivity.Allimage.set(ChatArrayAdapter.c, coverUrl.toString().replace("[","").replace("]","").replace(" ",""));
			pager = (ViewPager) findViewById(R.id.myviewpager);
			count = coverUrl.size();
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int pageMargin = 0;
			pageMargin = (int) ((metrics.widthPixels / 4) * 2);
			pager.setPageMargin(-pageMargin);

			try {
				pager.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				//pager.setOnPageChangeListener(adapter);
				pager.setCurrentItem(first_post);


			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

}