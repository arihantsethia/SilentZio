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
import java.util.Date;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Calendar extends Activity {
	TextView[] tx = new TextView[5];
	TextView[] txt = new TextView[5];
	CheckBox cb;
	Switch toggle;
	Context context;
	ImageButton help;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Entered oncreate", "yes");
		//this.setTheme(R.style.AppThemeDark);
		context = getApplicationContext();
		//setContentView(R.layout.main);
		KeywordsDB db = new KeywordsDB(context);
		if (db.IsFirstRun()) {
			Log.d("Entered First run", "yes");
			db.close();
			Intent i = new Intent(Calendar.this, Welcome.class);
			startActivity(i);
		} 
		Log.d("Entered First run", "no");
		start();
		
	}

	public void start() {
		this.setTheme(R.style.AppThemeDark);
		setContentView(R.layout.main);
		help = (ImageButton) findViewById(R.id.ibHelp);
		help.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Calendar.this, Help.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		cb = (CheckBox) findViewById(R.id.cbSms);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				KeywordsDB db = new KeywordsDB(context);
				db.setSmsState((cb.isChecked() ? 1 : 0));
				db.close();

			}
		});
		KeywordsDB db = new KeywordsDB(context);
		if (db.getSmsState()) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}

		tx[0] = (TextView) findViewById(R.id.tx1);
		tx[1] = (TextView) findViewById(R.id.tx2);
		tx[2] = (TextView) findViewById(R.id.tx3);
		tx[3] = (TextView) findViewById(R.id.tx4);
		tx[4] = (TextView) findViewById(R.id.tx5);
		txt[0] = (TextView) findViewById(R.id.txt1);
		txt[1] = (TextView) findViewById(R.id.txt2);
		txt[2] = (TextView) findViewById(R.id.txt3);
		txt[3] = (TextView) findViewById(R.id.txt4);
		txt[4] = (TextView) findViewById(R.id.txt5);
		startService(new Intent(context, MyService.class));
		toggle = (Switch) findViewById(R.id.tgState);

		if (db.getToggleState()) {
			toggle.setChecked(true);
			startService(new Intent(context, MyService.class));
		} else {
			toggle.setChecked(false);
			stopService(new Intent(context, MyService.class));
		}
		db.close();
		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { // TODO Auto-generated method stub
				KeywordsDB db = new KeywordsDB(context);
				if (toggle.isChecked()) {
					startService(new Intent(context, MyService.class));

				} else {
					stopService(new Intent(context, MyService.class));
				}
				db.setToggleState((toggle.isChecked() ? 1 : 0));
				db.close();

			}
		});
		set();

	}

	public void set() {

		long ntime = System.currentTimeMillis();
		String[] Selection = new String[] { "_id", "title", "description",
				"dtstart", "dtend", "eventLocation", "duration" };
		Context context = getApplicationContext();
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(
				Uri.parse("content://com.android.calendar/events"), Selection,
				null, null, Selection[3]);
		// read from the first calendar

		cursor.moveToFirst();
		int count = 0;
		String[] CalNames = new String[cursor.getCount()];
		int[] CalIds = new int[cursor.getCount()];
		for (int i = 0; i < CalNames.length; i++) {

			CalIds[i] = cursor.getInt(0);
			CalNames[i] = "Event" + cursor.getInt(0) + "\n: Title: "
					+ cursor.getString(1) + "\nDescription: "
					+ cursor.getString(2) + "\nStart Date: "
					+ cursor.getLong(3) + "\nEnd Date : " + cursor.getLong(4)
					+ "\nLocation : " + cursor.getString(5) + "\n Duration :"
					+ cursor.getString(6);
			String Event = (cursor.getString(1));
			long StartTime = cursor.getLong(3);
			long EndTime = cursor.getLong(4);

			if (ntime <= EndTime) {

				tx[count].setText((count + 1) + ". Event : " + Event);
				txt[count].setText("   Time  : " + timeFormat(StartTime)
						+ " - " + timeFormat(EndTime));
				count++;
				if (count == 5) {
					cursor.close();
					break;
				}
			}
			cursor.moveToNext();
		}
		cursor.close();

	}

	private String timeFormat(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  dd MMM");
		Date resultdate = new Date(time);
		return sdf.format(resultdate);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.home_main, menu);
		return true;
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.help:
			i = new Intent(Calendar.this, Help.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		case R.id.aboutus:
			i = new Intent(Calendar.this, About.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		case R.id.add:
			i = new Intent(Calendar.this, AddKeyword.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		case R.id.remove:
			i = new Intent(Calendar.this, RemoveKeyword.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;

		case R.id.exit:
			stopService(new Intent(this, MyService.class));
			System.exit(0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		set();
	}
}
