/*************** Copyright 2012 AsyncTech ******************************

This file is part of SilentZio.

AsyncTech is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

AsyncTech is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with AsyncTech.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************/



package com.iitg.call.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;

public class PhoneState extends PhoneStateListener {

	Context context;
	Boolean chkState;
	static String prevNum = "";

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		AudioManager audio_mngr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (state == 1) {
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		} else {
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			return;
		}
		if (incomingNumber == "") {
			return;
		}
		chkCalendar(incomingNumber);

	}

	public void contextCreator(Context con) {
		context = con;
	}

	private void chkCalendar(String number) {
		ContentResolver contentResolver = context.getContentResolver();
		// get current time
		// needed to compare with event time from calendar
		// all times are UTC so with one value we can check the date too
		long ntime = System.currentTimeMillis();
		int Flag = 0;
		long StartTime, EndTime = 0;
		String[] Selection = new String[] { "_id", "title",
				CalendarContract.Events.DESCRIPTION, "dtstart", "dtend",
				"eventLocation" };
		Cursor cursor = contentResolver.query(
				Uri.parse("content://com.android.calendar/events"), Selection,
				null, null, Selection[4]);
		// read from the first calendar
		cursor.moveToFirst();
		String[] CalNames = new String[cursor.getCount()];
		int[] CalIds = new int[cursor.getCount()];
		for (int i = 0; i < CalNames.length; i++) {
			CalIds[i] = cursor.getInt(0);
			CalNames[i] = "Event" + cursor.getInt(0) + "\n: Title: "
					+ cursor.getString(1) + "\nDescription: "
					+ cursor.getString(2) + "\nStart Date: "
					+ cursor.getLong(3) + "\nEnd Date : " + cursor.getLong(4)
					+ "\nLocation : " + cursor.getString(5);

			StartTime = cursor.getLong(3);
			EndTime = cursor.getLong(4);
			String Event = cursor.getString(1).toLowerCase();
			String Description;
			if (cursor.getString(2) == null)
				Description = "abc";
			else
				Description = cursor.getString(2).toLowerCase();

			if ((StartTime < ntime) && (ntime < EndTime)) {

				if (Description.contains("#silent") || filter(Event)) {
					Flag = 1;
					break;
				}
			}
			cursor.moveToNext();
		}
		cursor.close();
		AudioManager audio_mngr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (Flag == 1) {
			String message = "Sorry, I am busy right now! \nPlease call after "
					+ timeFormat(EndTime);
			KeywordsDB db = new KeywordsDB(context);
			if (db.getSmsState())
				sendSMS(number, message);
			db.close();
		} else {
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	}

	private boolean filter(String text) {
		KeywordsDB db = new KeywordsDB(context);
		List<String> Filter = db.getOnlyKeywords();
		for (int i = 0; i < Filter.size(); i++) {
			if (text.contains(Filter.get(i))) {
				db.close();
				return true;
			}
		}
		db.close();
		return false;

	}

	private void sendSMS(String phoneNumber, String message) {

		KeywordsDB db = new KeywordsDB(context);
		if (phoneNumber.equals(db.getNumber())) {
			db.close();
			return;
		}
		
		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> messagelist = sms.divideMessage(message);
		sms.sendMultipartTextMessage(phoneNumber, null, messagelist, null,null);
		db.setNumber(phoneNumber);
		db.close();

	}

	private String timeFormat(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date resultdate = new Date(time);
		return sdf.format(resultdate);
	}

}
