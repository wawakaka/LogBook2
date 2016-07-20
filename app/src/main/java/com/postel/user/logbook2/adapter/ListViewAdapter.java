package com.postel.user.logbook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.postel.user.logbook2.DetailActivity;
import com.postel.user.logbook2.R;
import com.postel.user.logbook2.config.ImageLoader;
import com.postel.user.logbook2.config.Koneksi;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView nama;
		TextView status;
		TextView jam;

		ImageView foto;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.listview_item, parent, false);
		// Get the position
		resultp = data.get(position);

		// Locate the TextViews in listview_item.xml
		nama = (TextView) itemView.findViewById(R.id.listNama);
		status = (TextView) itemView.findViewById(R.id.listStatus);
		jam = (TextView) itemView.findViewById(R.id.listJam);

		// Locate the ImageView in listview_item.xml
		foto = (ImageView) itemView.findViewById(R.id.listFoto);

		// Capture position and set results to the TextViews
		nama.setText(resultp.get(Koneksi.TAG_NAMA));
		status.setText(resultp.get(Koneksi.TAG_STATUS));
		jam.setText(resultp.get(Koneksi.TAG_WAKTU));
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		imageLoader.DisplayImage(resultp.get(Koneksi.TAG_FOTO), foto);
		// Capture ListView item click
		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//context.startActivity(new Intent(context, DetailActivity.class));
				resultp = data.get(position);
				Intent intent = new Intent(context, DetailActivity.class);
				// Pass all data rank
				intent.putExtra("id", resultp.get(Koneksi.TAG_ID));
				intent.putExtra("nama", resultp.get(Koneksi.TAG_NAMA));
				intent.putExtra("status", resultp.get(Koneksi.TAG_STATUS));
				intent.putExtra("email", resultp.get(Koneksi.TAG_EMAIL));
				intent.putExtra("tlp", resultp.get(Koneksi.TAG_TLP));
				intent.putExtra("perusahaan", resultp.get(Koneksi.TAG_PERUSAHAAN));
				intent.putExtra("keperluan", resultp.get(Koneksi.TAG_KEPERLUAN));
				intent.putExtra("keterangan", resultp.get(Koneksi.TAG_KETERANGAN));
				intent.putExtra("waktu", resultp.get(Koneksi.TAG_WAKTU));
				intent.putExtra("foto", resultp.get(Koneksi.TAG_FOTO));
				intent.putExtra("ktp", resultp.get(Koneksi.TAG_KTP));
				intent.putExtra("ttd", resultp.get(Koneksi.TAG_TTD));
				// Start SingleItemView Class
				context.startActivity(intent);
			}
		});
		return itemView;
	}
}
