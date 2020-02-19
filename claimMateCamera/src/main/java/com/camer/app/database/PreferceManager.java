package com.camer.app.database;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferceManager {
	public final String TAG_AUTO_SAVE = "auto_save";
	public final String TAG_CAMERA_APP = "camera_save";
	public final String TAG_ITEM_INCLUDE_HEADER = "itemIncludeheader";
	public final String TAG_ITEM_INCLUDE_TIME = "itemIncludeTime";
	SharedPreferences sharedPreferences;

	public PreferceManager(Context paramContext) {
		this.sharedPreferences = paramContext.getSharedPreferences(
				"camera_save", 32768);
	}

	public void autoIncludeHeader(boolean paramBoolean) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putBoolean("itemIncludeheader", paramBoolean);
		localEditor.commit();
	}

	public void autoIncludeTIME(boolean paramBoolean) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putBoolean("itemIncludeTime", paramBoolean);
		localEditor.commit();
	}

	public void autoSaveImage(boolean paramBoolean) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putBoolean("auto_save", paramBoolean);
		localEditor.commit();
	}

	public boolean isAutoSaveImage() {
		return this.sharedPreferences.getBoolean("auto_save", false);
	}

	public boolean isIncludeHeader() {
		return this.sharedPreferences.getBoolean("itemIncludeheader", false);
	}

	public void setNothing(boolean isNothing) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putBoolean("isNothing", isNothing);
		localEditor.commit();
	}

	public boolean isNothing() {
		return this.sharedPreferences.getBoolean("isNothing", false);
	}

	public boolean isIncludeTIME() {
		return this.sharedPreferences.getBoolean("itemIncludeTime", false);
	}
}

/*
 * Location: C:\Users\CTINFO\Desktop\apk\Claim
 * Mate\classes-dex2jar.jar!\com\camer\app\database\PreferceManager.class Java
 * compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */