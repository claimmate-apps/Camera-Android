package com.camer.app.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.camer.app.model.SubArea;

public class DatabaseSubArea extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Sub_Area_Manager";

	// Contacts table name
	private static final String TABLE_SUB_AREA = "Sub_Area_Type";
	private static final String KET_SUB_AREA_ID = "sub_area_id";
	private static final String KET_AREA_ID = "area_id";
	private static final String KET_SUB_AREA_NAME = "area_name";

	// private static final String KET_AREA_SUB_ID = "Area_sub_id";
	// private static final String KET_DAMAGE_ID = "Damage_id";
	// private static final String KET_MATERIAL_ID = "Materials_id";
	//

	public DatabaseSubArea(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SUB_AREA + "("
				+ KET_SUB_AREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KET_SUB_AREA_NAME + " TEXT," + KET_AREA_ID + " INTEGER "
				+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_AREA);

		// Create tables again
		onCreate(db);
	}

	public void addSubArea(SubArea area) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put(KET_SUB_AREA_ID, area.getId());

		values.put(KET_SUB_AREA_NAME, area.getSub_Area_name());
		values.put(KET_AREA_ID, area.getArea_id());

		db.insert(TABLE_SUB_AREA, null, values);
		db.close(); // Closing database connection
	}

	public ArrayList<SubArea> getAllSubArea(int area_id) {
		ArrayList<SubArea> contactList = new ArrayList<SubArea>();
		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_SUB_AREA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_SUB_AREA,new String[]{KET_SUB_AREA_ID,KET_SUB_AREA_NAME,KET_AREA_ID}, KET_AREA_ID+"=? ", new String[] { area_id+"" }, null, null, null);

		
//		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SubArea subArea = new SubArea(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1),Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				contactList.add(subArea);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}
}
