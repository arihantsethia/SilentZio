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

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KeywordsDB extends SQLiteOpenHelper {

	// constructor
	public KeywordsDB(Context context) {
		super(context, "call_manager", null, 3);
		Log.d("db", "constructor called");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_table_query = "CREATE TABLE keywords (id INTEGER PRIMARY KEY, keyword TEXT)";
		db.execSQL(create_table_query);

		db.execSQL("CREATE TABLE sms (state INTEGER)"); // 0: disables
		db.execSQL("CREATE TABLE toggle (tgstate INTEGER)");// 0: disables
		db.execSQL("CREATE TABLE call (number TEXT)");
		db.execSQL("CREATE TABLE run (times INTEGER)");
		ContentValues values = new ContentValues();
		values.put("keyword", "meeting");
		db.insert("keywords", null, values);

		values = new ContentValues();
		values.put("keyword", "conference");
		db.insert("keywords", null, values);

		values = new ContentValues();
		values.put("keyword", "seminar");
		db.insert("keywords", null, values);

		values = new ContentValues();
		values.put("keyword", "discussion");
		db.insert("keywords", null, values);
		
		values = new ContentValues();
		values.put("keyword", "lecture");
		db.insert("keywords", null, values);
		
		values = new ContentValues();
		values.put("keyword", "class");
		db.insert("keywords", null, values);
		
		values = new ContentValues();
		values.put("keyword", "lab");
		db.insert("keywords", null, values);

		values = new ContentValues();
		values.put("state", 1);
		db.insert("sms", null, values);
		
		values = new ContentValues();
		values.put("tgstate", 1);
		db.insert("toggle", null, values);
		
		values = new ContentValues();
		values.put("number", "notanumber;;;''");
		db.insert("call", null, values);

		values = new ContentValues();
		values.put("times", 0);
		db.insert("run", null, values);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int intNewVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS keywords");
		onCreate(db);
	}

	public boolean push(String keyword) {

		if (keywordExists(keyword)) {
			return false;
		}

		ContentValues values = new ContentValues();
		values.put("keyword", keyword);
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert("keywords", null, values);
		return true;
	}

	public boolean keywordExists(String keyword) {
		String sql = "SELECT * FROM keywords WHERE keyword = '" + keyword + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		// cursor.close();

		if (cursor.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<Keyword> getKeywords() {

		List<Keyword> keywordsList = new ArrayList<Keyword>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM keywords", null);

		if (cursor.moveToFirst()) {
			do {
				Keyword keyword = new Keyword(Integer.parseInt(cursor
						.getString(0)), cursor.getString(1));
				keywordsList.add(keyword);
			} while (cursor.moveToNext());
		}

		return keywordsList;
	}

	public List<String> getOnlyKeywords() {

		List<String> keywordsList = new ArrayList<String>();

		Log.d("db", "getOnlyKeywords");

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM keywords", null);

		if (cursor.moveToFirst()) {
			do {

				// Keyword keyword = new
				// Keyword(Integer.parseInt(cursor.getString(0)),
				// cursor.getString(1));
				String keyword = cursor.getString(1);
				Log.d("db", keyword);
				keywordsList.add(keyword);
			} while (cursor.moveToNext());
		}

		return keywordsList;
	}

	public void remove(String keyword) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("keywords", "keyword = '" + keyword + "'", null);
		db.close();
	}

	public boolean getSmsState() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM sms", null);
		cursor.moveToFirst();
		if (Integer.parseInt(cursor.getString(0)) == 1)
			return true;
		else
			return false;
	}

	public void setSmsState(int i) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("state", i);
		db.update("sms", values, "", null);
	}

	public boolean getToggleState() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM toggle", null);
		cursor.moveToFirst();
		if (Integer.parseInt(cursor.getString(0)) == 1)
			return true;
		else
			return false;
	}

	public void setToggleState(int i) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("tgstate", i);
		db.update("toggle", values, "", null);
	}

	public String getNumber() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM call", null);
		cursor.moveToFirst();
		Log.d("number", cursor.getString(0));
		return (cursor.getString(0));
	}

	public void setNumber(String i) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", i);
		Log.d("number re", i);
		db.update("call", values, "", null);
	}

	public Boolean IsFirstRun() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM run", null);
		cursor.moveToFirst();
		Log.d("number", "asdas'f");
		if (cursor.getInt(0) == 0) {
			Log.d("number", "asfafssdas'f");
			return true;
		} else {
			Log.d("number", "asfasdfssdasf");
			return false;

		}
	}

	public void FirstRunDone() {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("times", 1);
		// Log.d("number re", i);
		db.update("run", values, "", null);
	}

}