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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AddKeyword extends Activity implements OnClickListener {
	EditText text;
	ScrollView sv;
	LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sv = new ScrollView(this);
		ll = new LinearLayout(this);

		setContentView(R.layout.add);

		Button Add = (Button) findViewById(R.id.bAdd);
		text = (EditText) findViewById(R.id.etAdd);
		Add.setOnClickListener(this);

		sv = (ScrollView) findViewById(R.id.svAdd);
		ll.setOrientation(LinearLayout.VERTICAL);
		fillList();

		sv.addView(ll);
		// print database over here

	}

	private void fillList() {
		KeywordsDB db = new KeywordsDB(getApplicationContext());
		List<String> arr = db.getOnlyKeywords();
		ll.removeAllViews();
		for (int j = arr.size() - 1; j >= 0; j--) {
			TextView txt = new TextView(this);
			txt.setTextSize(15);
			txt.setText(" \u00B7 " + Title(arr.get(j)));
			txt.setTextColor(Color.parseColor("#D4D4D4"));
			ll.addView(txt);
		}
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.add_menu, menu);
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
			i = new Intent(AddKeyword.this, Help.class);
			startActivity(i);
			return true;
		case R.id.home:
			i = new Intent(AddKeyword.this, Calendar.class);
			startActivity(i);
			return true;
		case R.id.aboutus:
			i = new Intent(AddKeyword.this, About.class);
			startActivity(i);
			return true;

		case R.id.remove:
			i = new Intent(AddKeyword.this, RemoveKeyword.class);
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

	public void onClick(View v) {
		// TODO Auto-generated method stub

		KeywordsDB db = new KeywordsDB(getApplicationContext());
		if (db.push(text.getText().toString().toLowerCase())) {
			fillList();
		} else {
			Toast.makeText(this, "Keyword already exists!", Toast.LENGTH_SHORT)
					.show();
		}
		text.setText("");
		db.close();

	}
	public static String Title(String string){
        String result = "";
        for (int i = 0; i < string.length(); i++){
            String next = string.substring(i, i + 1);
            if (i == 0){
                result += next.toUpperCase();
            } else {
                result += next.toLowerCase();
            }
        }
        return result;
    }

}