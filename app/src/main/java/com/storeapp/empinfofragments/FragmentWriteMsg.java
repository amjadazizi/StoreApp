package com.storeapp.empinfofragments;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.storeapp.Prefs;
import com.storeapp.R;
import com.storeapp.db.CommunicationManager;
import com.storeapp.db.DbManager;
import com.storeapp.model.Message;
import com.storeapp.util.Utils;

public class FragmentWriteMsg extends Fragment {

	private EditText editMsg, editHeadline;
	private Button btnSendMsg;
	private View view;
	Calendar calender = Calendar.getInstance();

	CommunicationManager communicationManager = DbManager.getDbManager()
			.getCommunicationManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_writemsg, container, false);

		// EditTexts
		editMsg = (EditText) view.findViewById(R.id.editMsg);
		editHeadline = (EditText) view.findViewById(R.id.editHeadline);

		// Buttons
		btnSendMsg = (Button) view.findViewById(R.id.btnSendMsg);

		btnSendMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Message message = new Message();
				String headline = editHeadline.getText().toString();
				String messages = editMsg.getText().toString();
				if (!Utils.isNullOrEmpty(headline)
						&& !Utils.isNullOrEmpty(messages)) {

					message.setHeadline(headline);
					message.setMessage(messages);

					Calendar cal = Calendar.getInstance();

					message.setTime(cal.getTimeInMillis());

					String sender = Prefs.showInitialsPref() ? Prefs
							.getInitialsPref() : "Anonymous";

					message.setSender(sender);

					int msgId = 0;
					msgId = communicationManager.saveMsg(message);
					if (msgId > -1) {
						Utils.showToastShort(getResources().getString(
								R.string.text_message_sent));
						editHeadline.setText("");
						editMsg.setText("");

					} else {
						Utils.showToastShort(getResources().getString(
								R.string.text_message_not_sent));

					}

				} else {
					Utils.showToastShort(getResources().getString(
							R.string.text_alle_fields_required));
				}
			}
		});

		return view;

	}
}
