package com.eduard.flickrsearch;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.StartElementListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.carousel.CarouselActivity;
import com.squareup.picasso.Picasso;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

	private TextView chatText;
	private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
	private LinearLayout singleMessageContainer;
	private ImageView img1;
	private FrameLayout fm;
	public static String keyword;
	public static int c;

	@Override
	public void add(ChatMessage object) {
		chatMessageList.add(object);
		super.add(object);
	}

	public ChatArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.chatMessageList.size();
	}

	public ChatMessage getItem(int index) {
		return this.chatMessageList.get(index);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.activity_chat_singlemessage,
					parent, false);
		}
		singleMessageContainer = (LinearLayout) row
				.findViewById(R.id.singleMessageContainer);
		ChatMessage chatMessageObj = getItem(position);
		chatText = (TextView) row.findViewById(R.id.singleMessage);
		img1 = (ImageView) row.findViewById(R.id.img1);
		fm = (FrameLayout) row.findViewById(R.id.fm);

		chatText.setText(chatMessageObj.message);
		if (chatMessageObj.left == true) {
			fm.setBackgroundResource(R.drawable.left);
		} else {
			fm.setBackgroundResource(R.drawable.right);

		}
		// chatText.setBackground(chatMessageObj.left);
		if (chatMessageObj.image.equals("")) {
			Picasso.with(getContext()).load(R.drawable.trans).into(img1);


		} else {

			Picasso.with(getContext()).load(chatMessageObj.image).into(img1);
		}
		img1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				c = position;
				 keyword=chatMessageList.get(position-1).message;
				Intent in = new Intent(getContext(), CarouselActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(in);

			}
		});

		singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT
				: Gravity.RIGHT);

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}
