package com.vsmjsc.rdgt.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CacheManager {
	private static SQLiteDatabase sqldb;
	private Context mContext;

	public CacheManager(Context context) {
		mContext = context;
	}

	public void open() {
		sqldb = mContext.openOrCreateDatabase("vsmjsc_data", Context.MODE_PRIVATE, null);
		sqldb.execSQL("CREATE TABLE IF NOT EXISTS appdata (_KEY VARCHAR, _VALUE VARCHAR);");
	}

	public void close() {
		if (sqldb.isOpen())
			sqldb.close();
	}

	public void addData(String key, String value) {
		sqldb.execSQL("DELETE FROM appdata where _KEY = ?", new String[] { key });
		sqldb.execSQL("INSERT INTO appdata (_KEY, _VALUE)" + " VALUES ('" + key + "', '" + value + "');");
	}

	public void clearDb() {
		sqldb.execSQL("DELETE FROM appdata");
	}

	public void removeData(String key) {
		sqldb.execSQL("DELETE FROM appdata where _KEY = ?", new String[] { key });
	}

	public String getData(String key) {
		if (sqldb != null && sqldb.isOpen()) {
			Cursor cursor = sqldb.rawQuery("SELECT * FROM appdata WHERE _KEY=?", new String[] { key });
			if (cursor != null)
				cursor.moveToFirst();
			if (cursor.getCount() == 0) {
				return null;
			}
			String value = cursor.getString(1);
			cursor.close();
			return value;
		} else {
			return null;
		}
	}
}
