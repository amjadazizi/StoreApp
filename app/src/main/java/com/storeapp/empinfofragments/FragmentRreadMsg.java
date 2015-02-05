package com.storeapp.empinfofragments;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.storeapp.AppContextProvider;
import com.storeapp.Message_Details;
import com.storeapp.Prefs;
import com.storeapp.R;
import com.storeapp.SoundEffects;
import com.storeapp.UpdateFragment;
import com.storeapp.db.CommunicationManager;
import com.storeapp.db.DbManager;

public class FragmentRreadMsg extends Fragment implements UpdateFragment {

	private ListView listViewMessages;
	private View view;
	Calendar calender = Calendar.getInstance();
	CommunicationManager communicationManager = DbManager.getDbManager()
			.getCommunicationManager();
	public static String EXTRA_MESSAGE_ID = "ekstra_message_id";

	public FragmentRreadMsg() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_readmsg, container, false);
		listViewMessages = (ListView) view.findViewById(R.id.listViewMessages);

		listViewMessages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(), Message_Details.class);
				intent.putExtra(EXTRA_MESSAGE_ID, (int) id);
				startActivity(intent);

			}
		});

		return view;
	}

	public void loadMessages() {

		String key = AppContextProvider.getContext().getString(
				R.string.pref_key_sort_messages);

		long timeMS = Long.parseLong(Prefs.getStringPref(key, "0")) * 1000;

		Cursor cursor = communicationManager.getMessagesByTime(timeMS);
		MessagesAdapter messageAdapter = new MessagesAdapter(getActivity()
				.getApplicationContext(), cursor);
		listViewMessages.setAdapter(messageAdapter);

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

		loadMessages();
	}

	@Override
	public void update() {
		loadMessages();
	}

	public class MessagesAdapter extends BaseAdapter {
		private final int INVALID = -1;
		protected int DELETE_POS = -1;

		private Cursor cursor;
		private Context ctx;

		public MessagesAdapter(Context ctx, Cursor cursor) {
			this.cursor = cursor;
			this.ctx = ctx;
		}

		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			cursor.moveToPosition(position);
			return cursor.getLong(cursor
					.getColumnIndex(CommunicationManager.COL_ID));
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view = convertView;
			TextView txtHeadline = null;
			TextView txtSender = null;
			TextView txtMsg = null;

			if (view == null) {

				LayoutInflater layoutinflater = LayoutInflater.from(ctx);
				view = layoutinflater.inflate(R.layout.messages_list_row,
						parent, false);
				txtHeadline = (TextView) view.findViewById(R.id.txtRowHeadline);
				txtSender = (TextView) view.findViewById(R.id.txtRoawSender);
				txtMsg = (TextView) view.findViewById(R.id.txtRowMsg);

				ViewHolder holder = new ViewHolder();
				holder.txtHeadline = txtHeadline;
				holder.txtSender = txtSender;
				holder.txtMsg = txtMsg;

				view.setTag(holder);

			} else {

				ViewHolder holder = (ViewHolder) view.getTag();
				txtHeadline = holder.txtHeadline;
				txtSender = holder.txtSender;
				txtMsg = holder.txtMsg;

			}

			cursor.moveToPosition(position);
			String textHeadline = cursor.getString(cursor
					.getColumnIndex(CommunicationManager.COL_HEADLINE));
			String textsender = cursor.getString(cursor
					.getColumnIndex(CommunicationManager.COL_SENDER));
			String textMsg = cursor.getString(cursor
					.getColumnIndex(CommunicationManager.COL_MESSAGE));

			txtHeadline.setText(textHeadline);
			txtSender.setText(textsender);
			txtMsg.setText(textMsg);

			return view;
		}
	}

	public static class ViewHolder {

		public TextView txtHeadline, txtSender, txtMsg;

	}

}
