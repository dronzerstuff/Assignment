package com.eduard.flickrsearch;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static ArrayList<String> img = new ArrayList<String>();
	public static ArrayList<String> Allimage = new ArrayList<String>();
	ProgressDialog pdialog;
	private ChatArrayAdapter chatArrayAdapter;
	private boolean side = false;

	int pageNr = 1;

	public int increasePageNr(int nr) {
		pageNr = pageNr + nr;
		return pageNr;
	}

	public int decreasePageNr(int nr) {
		pageNr = pageNr - nr;
		return pageNr;
	}

	String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
	String FlickrQuery_per_page = "&per_page=10";
	String FlickrQuery_page = "&page=" + Integer.toString(pageNr);
	String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
	String FlickrQuery_format = "&format=json";
	String FlickrQuery_tag = "&tags=";
	String FlickrQuery_key = "&api_key=";
	String searchQ;
	String FlickrApiKey = "8c71c2f6b1340444643239f17fa2b8dc";
	private ArrayAdapter<String> adapter;

	final String DEFAULT_SEARCH = "infilect";

	Spinner searchText;

	Button searchButton;

	ListView listView;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		searchText = (Spinner) findViewById(R.id.searchtext);
		if (!Allimage.isEmpty()) {
			Allimage.clear();
		}
		if(!img.isEmpty())
		{
			img.clear();
		}
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.suggestions, R.layout.spinner_item);

		adapter.setDropDownViewResource(R.layout.spinner_item);
		searchText.setAdapter(adapter);

		searchText.setPrompt("Select Keyword to serach");

		searchButton = (Button) findViewById(R.id.searchbutton);
		listView = (ListView) findViewById(R.id.photobar);
		chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(),
				R.layout.activity_chat_singlemessage);
		listView.setAdapter(chatArrayAdapter);
		searchText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					return sendChatMessage();
				}
				return false;
			}
		});
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FlickrQuery_page = "&page=1"; // Set it to page 1;
				searchQ = searchText.getSelectedItem().toString()
						.replaceAll("\\s", "_");

				Allimage.add("");
				new LongOperation().execute();

			}
		});
		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setAdapter(chatArrayAdapter);

		// to scroll the list view to bottom on data change
		chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				listView.setSelection(chatArrayAdapter.getCount() - 1);
			}
		});

	}

	private class LongOperation extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			pdialog = ProgressDialog.show(MainActivity.this, "",
					"Loading Properties...");

		}

		@Override
		protected String doInBackground(String... params) {
			String qResult = null;
			System.out.println(FlickrQuery_page);
			String qString = FlickrQuery_url + FlickrQuery_per_page
					+ FlickrQuery_page + FlickrQuery_nojsoncallback
					+ FlickrQuery_format + FlickrQuery_tag + searchQ
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

					img.add("http://farm" + flickrFarm + ".static.flickr.com/"
							+ flickrServer + "/" + flickrId + "_"
							+ flickrSecret + "_m.jpg");

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println(qResult);

			return qResult;
		}

		@Override
		protected void onPostExecute(String result) {
			sendChatMessage();
			side = true;

			Allimage.add(img.toString());
			img.clear();
			sendChatMessage1();
			pdialog.dismiss();
		}

	}

	private boolean sendChatMessage() {
		chatArrayAdapter.add(new ChatMessage(side, searchText.getSelectedItem()
				.toString(), ""));
		side = !side;
		return true;
	}

	private boolean sendChatMessage1() {
		int size = MainActivity.Allimage.size();
		String k = MainActivity.Allimage.get(size - 1).replace("[", "")
				.replace("]", "").replace(" ", "");
		String[] firstimage = k.split(",");
		chatArrayAdapter.add(new ChatMessage(side, "", firstimage[0]));
		side = !side;
		return true;
	}
}