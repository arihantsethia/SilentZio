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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class RemoveKeyword extends Activity {
	ScrollView sv;
	LinearLayout ll;
	int i = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundResource(R.drawable.wallpaper);
		sv.addView(ll);
		fillList();
		this.setContentView(sv);

	}

	private void fillList() {
		KeywordsDB db = new KeywordsDB(getApplicationContext());
		List<String> arr = db.getOnlyKeywords();
		ll.removeAllViews();
		TextView delete = new TextView(this);
		delete.setTextSize(17);
		delete.setText("Select Keywords to Remove");
		ll.addView(delete);
		for (int j = arr.size() - 1; j >= 0; j--) {
			CheckBox cb = new CheckBox(this);
			cb.setTextSize(15);
			cb.setText(" " + Title(arr.get(j)));
			cb.setTextColor(Color.parseColor("#D4D4D4"));
			cb.setId(i++);
			ll.addView(cb);
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.remove_menu, menu);
		return true;
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent ih;
		switch (item.getItemId()) {
		case R.id.help:
			ih = new Intent(RemoveKeyword.this, Help.class);
			ih.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ih);
			return true;
		case R.id.home:
			ih = new Intent(RemoveKeyword.this, Calendar.class);
			ih.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ih);
			return true;
		case R.id.aboutus:
			ih = new Intent(RemoveKeyword.this, About.class);
			ih.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ih);
			return true;
		case R.id.delete:
			i--;
			KeywordsDB db = new KeywordsDB(getApplicationContext());
			CheckBox cb;
			for (; i >= 1000; i--) {
				cb = (CheckBox) findViewById(i);
				if (cb.isChecked()) {
					db.remove(cb.getText().toString().trim().toLowerCase());
				}
			}
			db.close();
			fillList();
			return true;

		case R.id.exit:
			stopService(new Intent(this, MyService.class));
			System.exit(0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static String Title(String string) {
		String result = "";
		for (int i = 0; i < string.length(); i++) {
			String next = string.substring(i, i + 1);
			if (i == 0) {
				result += next.toUpperCase();
			} else {
				result += next.toLowerCase();
			}
		}
		return result;
	}

}
