package com.camer.app.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.camer.app.model.TempletsType;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "templetManager";

	// Contacts table name
	private static final String TABLE_TEMPLET = "Templets_Type";

	private static final String KET_TEMPLET_ID = "templet_id";
	private static final String KET_TEMPLET_NAME = "templet_name";
	private static final String KET_CLAIM_NO = "claim_no";
	private static final String KET_CLAIM_DATE = "claim_date";
	private static final String KET_CLAIM_TIME = "claim_time";
	private static final String KET_CLAIM_USER = "claim_user";
	private static final String KET_CLAIM_OVERVIEW = "claim_overview";
	private static final String KET_CLAIM_DETAIL_1 = "claim_detail_1";
	private static final String KET_CLAIM_DETAIL_2 = "claim_detail_2";
	private static final String KET_CLAIM_DETAIL_3 = "claim_detail_3";
	private static final String KET_DAMAGE_TYPE = "damage_type";
	private static final String KET_IMAGE_URL = "image_url";
	private static final String KET_MATERIAILS_TYPE = "materials_type";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TEMPLET + "("
				+ KET_TEMPLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KET_TEMPLET_NAME
				+ " TEXT," + KET_CLAIM_NO + " TEXT,"
				+ KET_CLAIM_DATE + " TEXT," + KET_CLAIM_TIME
				+ " TEXT," + KET_CLAIM_USER + " TEXT,"
				+ KET_CLAIM_OVERVIEW + " TEXT,"
				+ KET_CLAIM_DETAIL_1 + " TEXT," + KET_CLAIM_DETAIL_2
				+ " TEXT," + KET_CLAIM_DETAIL_3 + " TEXT,"
				+ KET_DAMAGE_TYPE + " TEXT," + KET_IMAGE_URL
				+ " TEXT," + KET_MATERIAILS_TYPE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLET);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addTempletsType(TempletsType templetsType) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
//		values.put(KET_TEMPLET_ID, templetsType.getTemplet_id());
		values.put(KET_TEMPLET_NAME, templetsType.getTemplet_name());
		values.put(KET_CLAIM_NO, templetsType.getClaim_no());
		values.put(KET_CLAIM_DATE, templetsType.getClaim_date());
		values.put(KET_CLAIM_TIME, templetsType.getClaim_time());
		values.put(KET_CLAIM_USER, templetsType.getClaim_user());
		values.put(KET_CLAIM_OVERVIEW, templetsType.getClaim_overview());
		values.put(KET_CLAIM_DETAIL_1, templetsType.getClaim_detail_1());
		values.put(KET_CLAIM_DETAIL_2, templetsType.getClaim_detail_2());
		values.put(KET_CLAIM_DETAIL_3, templetsType.getClaim_detail_3());
		values.put(KET_DAMAGE_TYPE, templetsType.getDamage_type());
		values.put(KET_IMAGE_URL, templetsType.getImage_url());
		values.put(KET_MATERIAILS_TYPE, templetsType.getMaterials_type());

		// Inserting Row
		db.insert(TABLE_TEMPLET, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	TempletsType  getTemplet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_TEMPLET, new String[] { KET_TEMPLET_ID,KET_TEMPLET_NAME,KET_CLAIM_NO, KET_CLAIM_DATE,KET_CLAIM_TIME,KET_CLAIM_USER,KET_CLAIM_OVERVIEW,KET_CLAIM_DETAIL_1,KET_CLAIM_DETAIL_2,KET_CLAIM_DETAIL_3,KET_DAMAGE_TYPE,KET_IMAGE_URL,KET_MATERIAILS_TYPE }, KET_TEMPLET_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        TempletsType templetsType = new TempletsType(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12));
        // return contact
        return templetsType;
    }

	// Getting All Contacts
	public List<TempletsType> getAllTempletsType() {
		List<TempletsType> contactList = new ArrayList<TempletsType>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TEMPLET;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				 TempletsType templetsType = new TempletsType(Integer.parseInt(cursor.getString(0)),
			                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12));
				// Adding contact to list
				contactList.add(templetsType);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Updating single contact
	public int updateTempletsType(TempletsType templetsType) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KET_TEMPLET_ID, templetsType.getTemplet_id());
		values.put(KET_TEMPLET_NAME, templetsType.getTemplet_name());
		values.put(KET_CLAIM_NO, templetsType.getClaim_no());
		values.put(KET_CLAIM_DATE, templetsType.getClaim_date());
		values.put(KET_CLAIM_TIME, templetsType.getClaim_time());
		values.put(KET_CLAIM_USER, templetsType.getClaim_user());
		values.put(KET_CLAIM_OVERVIEW, templetsType.getClaim_overview());
		values.put(KET_CLAIM_DETAIL_1, templetsType.getClaim_detail_1());
		values.put(KET_CLAIM_DETAIL_2, templetsType.getClaim_detail_2());
		values.put(KET_CLAIM_DETAIL_3, templetsType.getClaim_detail_3());
		values.put(KET_DAMAGE_TYPE, templetsType.getDamage_type());
		values.put(KET_IMAGE_URL, templetsType.getImage_url());
		values.put(KET_MATERIAILS_TYPE, templetsType.getMaterials_type());


		// updating row
		return db.update(TABLE_TEMPLET, values, KET_TEMPLET_ID + " = ?",
				new String[] { String.valueOf(templetsType.getTemplet_id()) });
	}

	// Deleting single contact
	public void deleteTempletsType(TempletsType templetsType) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TEMPLET, KET_TEMPLET_ID + " = ?",
				new String[] { String.valueOf(templetsType.getTemplet_id()) });
		db.close();
	}

	// Getting contacts Count
	public int getTempletsTypeCount() {
		String countQuery = "SELECT  * FROM " + TABLE_TEMPLET;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
