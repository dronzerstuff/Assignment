package com.assignment.carousel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eduard.flickrsearch.MainActivity;
import com.eduard.flickrsearch.R;
import com.squareup.picasso.Picasso;

public class MyFragment extends Fragment {
	ProgressDialog pdialog;
	static String pid;
	public static int pos;
	public static ArrayList<String> myfav = new ArrayList<String>();

	public static Fragment newInstance(CarouselActivity context, int pos,
			float scale) {
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);

		return Fragment.instantiate(context, MyFragment.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				400, 400);
		LinearLayout fragmentLL = (LinearLayout) inflater.inflate(R.layout.mf,
				container, false);
		final int pos = this.getArguments().getInt("pos");
		CarouselActivity.first_post=pos;
		ImageView iv = (ImageView) fragmentLL.findViewById(R.id.pagerImg);
		Button like = (Button) fragmentLL.findViewById(R.id.like);
		like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				/*
				 * https://api.flickr.com/services/rest/?method=flickr.favorites.
				 * add
				 * &api_key=ab7d8b1ae8f6e5991c44c31f529783f7&photo_id=26120969990
				 * &
				 * format=json&nojsoncallback=1&auth_token=72157664775988174-894
				 * b4da654c29a39&api_sig=159c2c6228e2a833433fd689119d0368
				 */

				String[] p;
				p = CarouselActivity.coverUrl.get(pos)
						.replace("http://farm", "").split("/");
				String[] cd = p[2].toString().split("_");
				pid = cd[0];
				System.out.println("image:" + pid);
				if (myfav.contains(CarouselActivity.coverUrl.get(pos))) {
					Toast.makeText(getActivity(),
							"Already added to Favourites", Toast.LENGTH_SHORT)
							.show();
				} else {
					myfav.add(CarouselActivity.coverUrl.get(pos));

					Toast.makeText(getActivity(), "Added to favourites",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		iv.setLayoutParams(layoutParams);
		Picasso.with(getActivity()).load(CarouselActivity.coverUrl.get(pos))
				.into(iv);

		iv.setPadding(15, 15, 15, 15);

		MyLinearLayout root = (MyLinearLayout) fragmentLL
				.findViewById(R.id.root);
		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);

		return fragmentLL;
	}

	/*
	 * private class Addedfav extends AsyncTask<String, Void, String> {
	 * 
	 * @Override protected void onPreExecute() {
	 * 
	 * }
	 * 
	 * @Override protected String doInBackground(String... params) { String
	 * qResult = null; String u =
	 * "https://api.flickr.com/services/rest/?method=flickr.favorites.add&api_key=ab7d8b1ae8f6e5991c44c31f529783f7&photo_id="
	 * + pid +
	 * "&format=json&nojsoncallback=1&auth_token=72157664775988174-894b4da654c29a39&api_sig=159c2c6228e2a833433fd689119d0368"
	 * ; HttpClient httpClient = new DefaultHttpClient(); HttpPost httpPost =
	 * new HttpPost(u); System.out.println(u);
	 * 
	 * try { HttpEntity httpEntity = httpClient.execute(httpPost) .getEntity();
	 * 
	 * if (httpEntity != null) { InputStream inputStream =
	 * httpEntity.getContent(); Reader in = new InputStreamReader(inputStream);
	 * BufferedReader bufferedreader = new BufferedReader(in); StringBuilder
	 * stringBuilder = new StringBuilder();
	 * 
	 * String stringReadLine = null;
	 * 
	 * while ((stringReadLine = bufferedreader.readLine()) != null) {
	 * stringBuilder.append(stringReadLine + "\n"); }
	 * 
	 * qResult = stringBuilder.toString();
	 * 
	 * inputStream.close(); }
	 * 
	 * } catch (ClientProtocolException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * System.out.println("result:" + qResult);
	 * 
	 * return qResult; }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * 
	 * }
	 * 
	 * }
	 */
}