package com.camer.app.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.camer.app.model.Damage;

public class DatabaseDamage extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "DamageManager";

	// Contacts table name
	private static final String TABLE_DAMAGE = "damage_Type";
	private static final String KET_DAMAGE_ID = "damage_id";
	
	private static final String KET_DAMAGE_NAME = "damage_name";

	// private static final String KET_AREA_SUB_ID = "Area_sub_id";
	// private static final String KET_DAMAGE_ID = "Damage_id";
	// private static final String KET_MATERIAL_ID = "Materials_id";
	//

	public DatabaseDamage(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DAMAGE + "("
				+ KET_DAMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KET_DAMAGE_NAME
				+ " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAMAGE);

		// Create tables again
		onCreate(db);
	}

	public void addDamage(Damage damage) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
//		values.put(KET_DAMAGE_ID, damage.getDamage_id());
		values.put(KET_DAMAGE_NAME, damage.getDamage_name());

		// Inserting Row
		db.insert(TABLE_DAMAGE, null, values);
		db.close(); // Closing database connection
	}

	public ArrayList<Damage> getAllDamage() {
		ArrayList<Damage> contactList = new ArrayList<Damage>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DAMAGE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Damage subArea = new Damage(Integer.parseInt(cursor.getString(0)),
						cursor.getString(1));
				// Adding contact to list
				contactList.add(subArea);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

}
