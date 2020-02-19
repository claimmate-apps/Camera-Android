package com.camer.app.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.camer.app.model.Material;

public class DatabaseMaterial extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "MaterailManager";

	// Contacts table name
	private static final String TABLE_MATERAIL = "Materail_Type";
	private static final String KET_MATERAIL_ID = "materail_id";
	private static final String KET_MATERAIL_NAME = "materail_name";
	private static final String KET_AREA_ID_ = "Area_id";
	// private static final String KET_AREA_SUB_ID = "Area_sub_id";
	// private static final String KET_DAMAGE_ID = "Damage_id";
	// private static final String KET_MATERIAL_ID = "Materials_id";
	//

	public DatabaseMaterial(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MATERAIL + "("
				+ KET_MATERAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KET_MATERAIL_NAME
				+ " TEXT," + KET_AREA_ID_ + " INTEGER "+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERAIL);

		// Create tables again
		onCreate(db);
	}

	public void addMaterial(Material material) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
//		values.put(KET_MATERAIL_ID, material.getMaterail_id());
		values.put(KET_MATERAIL_NAME, material.getMaterail_name());
		values.put(KET_AREA_ID_, material.getArea_id());
		
		// Inserting Row
		db.insert(TABLE_MATERAIL, null, values);
		db.close(); // Closing database connection
	}

	public ArrayList<Material> getAllMaterial(int area_id) {
		ArrayList<Material> contactList = new ArrayList<Material>();
		// Select All Query
//		String selectQuery = "SELECT  * FROM " + TABLE_MATERAIL;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_MATERAIL,new String[]{KET_MATERAIL_ID,KET_MATERAIL_NAME,KET_AREA_ID_}, KET_AREA_ID_+"=? ", new String[] { area_id+"" }, null, null, null);
		
		

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Material material = new Material(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1),Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				contactList.add(material);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

}
