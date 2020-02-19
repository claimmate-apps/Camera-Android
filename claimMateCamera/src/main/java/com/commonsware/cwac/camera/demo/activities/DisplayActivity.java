package com.commonsware.cwac.camera.demo.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.camer.app.database.PreferceManager;
import com.example.claimmate.R;

public class DisplayActivity extends Activity {
	public static byte[] imageToShow = null;
	String data;
	Button dialogbuttonEdit;
	Button dialogbuttonSave;
	Button dialogbuttoncancel;
	boolean editable;
	String file_directory = "";
	String file_name = "";
	RelativeLayout saveImage;
	EditText textViewCustomText;
	EditText textViewData;
	TextView textViewInvestor;
	EditText textViewTitle;
	RelativeLayout datalayout;
	String new_image_name = "";
	String update_new_name;
	String overview_closeup_name;
	int overview_closeup_no;
	String str3 = "";

	public static Uri addToTouchActiveAlbum(Context context, String title,
			String filePath) {
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, title);
		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(Images.Media.BUCKET_ID, filePath.hashCode());
		values.put(Images.Media.BUCKET_DISPLAY_NAME, "Claim mate");

		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(Media.DESCRIPTION,
				context.getResources().getString(R.string.app_name));
		values.put(MediaStore.MediaColumns.DATA, filePath);
		Uri uri = context.getContentResolver().insert(
				Media.EXTERNAL_CONTENT_URI, values);

		return uri;
	}

	private boolean storeImage(Bitmap paramBitmap) {

		File pictureFile = getOutputMediaFile();

		if (pictureFile == null) {
			Log.d("Display Activity",
					"Error creating media file, check storage permissions: ");
			return false;
		}

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d("Display Activity", "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d("Display Activity", "Error accessing file: " + e.getMessage());
		}

		// try {
		// String filePath = iconsStoragePath + filename;
		// FileOutputStream fileOutputStream = new FileOutputStream(filePath);
		//
		// BufferedOutputStream bos = new BufferedOutputStream(
		// fileOutputStream);
		//
		// // choose another format if PNG doesn't suit you
		// paramBitmap.compress(CompressFormat.PNG, 100, bos);
		// bos.close();
		//
		// } catch (FileNotFoundException e) {
		// Log.w("TAG", "Error saving image file: " + e.getMessage());
		// return false;
		// } catch (IOException e) {
		// Log.w("TAG", "Error saving image file: " + e.getMessage());
		// return false;
		// }

		// addToTouchActiveAlbum(getApplicationContext(), filename,
		// iconsStoragePath);
		return true;

	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.full_imageview);
		textViewData = ((EditText) findViewById(R.id.textViewData));
		textViewInvestor = ((TextView) findViewById(R.id.textViewInvestor));
		textViewTitle = ((EditText) findViewById(R.id.textViewTitle));
		TextView textViewAdjuster = (TextView) findViewById(R.id.textViewAdjuster);
		TextView textViewTime = (TextView) findViewById(R.id.textViewTime);
		TextView textViewDate = (TextView) findViewById(R.id.textViewDate);
		// textViewCustomText = ((EditText)findViewById(2131427451));
		ImageView imageViewFullView = (ImageView) findViewById(R.id.imageViewFullView);
		dialogbuttoncancel = ((Button) findViewById(R.id.dialogbuttoncancel));
		dialogbuttonSave = ((Button) findViewById(R.id.dialogbuttonSave));
		saveImage = ((RelativeLayout) findViewById(R.id.saveImage));
		datalayout = ((RelativeLayout) findViewById(R.id.datalayout));
		data = getIntent().getExtras().getString("data");

		try {

			JSONObject jsonObject = new JSONObject(data);
			String areaString = jsonObject.getString("area");
			String subareaString = jsonObject.getString("subarea");
			String materailString = jsonObject.getString("materail");
			String damageString = jsonObject.getString("damage");
			String overview = jsonObject.getString("overview");
			String cov = jsonObject.getString("cov");

			// Toast.makeText(
			// getApplicationContext(),
			// "areaString:" + areaString + ", subareaString:"
			// + subareaString + ", materailString:"
			// + materailString + ", damageString:" + damageString
			// + ", cov:" + cov, Toast.LENGTH_LONG).show();

			String title = "";
			String details = "";

			title = cov + " ";

			if ((areaString).equalsIgnoreCase("Blank")
					|| (areaString).equalsIgnoreCase("SELECT AREA")) {
				areaString = "";
			}

			if ((subareaString).equalsIgnoreCase("Blank")
					|| (subareaString).equalsIgnoreCase("SELECT SUB AREA")) {
				subareaString = "";
			}

			if ((materailString).equalsIgnoreCase("Blank")
					|| (materailString).equalsIgnoreCase("SELECT MATERIAL")) {
				materailString = "";
			}

			if (damageString.equalsIgnoreCase("Blank")
					|| (damageString).equalsIgnoreCase("SELECT DAMAGE")) {
				damageString = "";
			}

			DateFormat.getDateTimeInstance().format(
					Calendar.getInstance().getTime());

			SharedPreferences localSharedPreferences = getSharedPreferences(
					"CW-claims", 0);

			String str1 = "";
			str3 = "";

			if (localSharedPreferences.contains("Insured Name")) {
				str3 = localSharedPreferences.getString("Insured Name", "");
				String str5 = localSharedPreferences.getString("Adjuster Name",
						"");
				str1 = localSharedPreferences.getString("Custom text", "");

				textViewInvestor.setText(str3);
				(textViewAdjuster).setText(str5);
				file_directory = (str3 + "/");
			}

			Time date = new Time(Time.getCurrentTimezone());
			(date).setToNow();
			textViewDate.setText(date.monthDay + "/" + (date.month + 1) + "/"
					+ date.year);
			textViewTime.setText(date.format("%k:%M:%S"));

			if (!"".trim().equals("")) {

			}

			if (!areaString.trim().equals("")) {
				title = areaString + " :" + title;
				file_directory = (file_directory + areaString + "_");

				if (areaString.trim().equals("Roof")) {
					new_image_name = "a";
				} else if (areaString.trim().equals("Elevation")) {
					new_image_name = "f";
				} else if (areaString.trim().equals("Interior")) {
					new_image_name = "k";
				} else {
					new_image_name = areaString;
				}
			}

			if (!overview.trim().equals("Blank")) {
				details = details + overview + " of ";
				overview_closeup_name = overview;
				if (overview.trim().equals("Overview")) {
					new_image_name = new_image_name + "1";
					overview_closeup_no = 1;
				} else if (overview.trim().equals("Close up")) {
					new_image_name = new_image_name + "2";
					overview_closeup_no = 2;
				} else {
					// new_image_name = new_image_name + " " + overview;
				}
			}

			if (!(subareaString.trim().equals(""))) {
				details = subareaString + "-";
				file_directory = file_directory + subareaString + "_";
				update_new_name = subareaString;
				if (subareaString.trim().equals("Front Slope")) {
					new_image_name = "b";
				} else if (subareaString.trim().equals("Right Slope")) {
					new_image_name = "c";
				} else if (subareaString.trim().equals("Rear Slope")) {
					new_image_name = "d";
				} else if (subareaString.trim().equals("Left Slope")) {
					new_image_name = "e";
				} else if (subareaString.trim().equals("Front Elevation")) {
					new_image_name = "g";
				} else if (subareaString.trim().equals("Right Elevation")) {
					new_image_name = "h";
				} else if (subareaString.trim().equals("Rear Elevation")) {
					new_image_name = "i";
				} else if (subareaString.trim().equals("Left Elevation")) {
					new_image_name = "j";
				} else {
					new_image_name = new_image_name + " " + subareaString;
				}
				new_image_name = new_image_name + overview_closeup_no + " "
						+ update_new_name + " - " + overview_closeup_name
						+ " of";

			}

			if (!(damageString.trim().equals(""))) {
				details = damageString + "-";
				file_directory = file_directory + damageString + "_";
				new_image_name = new_image_name + " " + damageString + " to";
			}

			if (!(materailString.trim().equals(""))) {
				details = materailString + "-";
				file_directory = file_directory + materailString + "_";
				new_image_name = new_image_name + " " + materailString;
			}

			if (!(cov.trim().equals(""))) {
				details = cov + "-";
				file_directory = file_directory + cov + "_";

				if (cov.trim().equals("Cov B")) {
					new_image_name = "s" + new_image_name;
				} else if (cov.trim().equals("Cov C")) {
					new_image_name = "t" + new_image_name;
				} else {
					// new_image_name = new_image_name + " " + cov;
				}
			}

			if (!((String) damageString).trim().equals("")) {
				details = details + (String) damageString + " to ";
				file_name = (file_name + "_");
				// new_image_name = new_image_name + " " + details;
			}

			if (!((String) materailString).trim().equals("")) {
				details = details + materailString;
				file_name = (file_name + materailString + "_");
			}

			if (localSharedPreferences.contains("new_custom_text")) {
				details = details
						+ "\n"
						+ localSharedPreferences.getString("new_custom_text",
								"");
			}

			textViewData.setText(details);
			textViewTitle.setText(title);
		} catch (JSONException e) {

		}
		if (imageToShow == null) {
			Toast.makeText(this, "file not save", Toast.LENGTH_LONG).show();
			finish();

		} else {

			BitmapFactory.Options opts = new BitmapFactory.Options();

			opts.inPurgeable = true;
			opts.inInputShareable = true;
			opts.inMutable = false;
			opts.inSampleSize = 2;

			imageViewFullView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageViewFullView.setImageBitmap(BitmapFactory.decodeByteArray(
					imageToShow, 0, imageToShow.length, opts));

			int imageWidth = opts.outWidth;
			int imageHeight = opts.outHeight;

			if (imageWidth > imageHeight) {

				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			}

			dialogbuttoncancel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					DisplayActivity.this.finish();
				}
			});

			PreferceManager preferceManager = new PreferceManager(
					getApplicationContext());
			if (preferceManager.isIncludeTIME()) {
				textViewDate.setVisibility(0);
				textViewTime.setVisibility(0);
			}
			if (preferceManager.isIncludeHeader()) {
				textViewTitle.setVisibility(8);
			}
			// Toast.makeText(getApplicationContext(),
			// "" + preferceManager.isNothing(), Toast.LENGTH_LONG).show();
			if (preferceManager.isNothing()) {
				// Ash
				textViewInvestor.setVisibility(View.GONE);
				textViewAdjuster.setVisibility(View.GONE);
				datalayout.setVisibility(View.GONE);
			}
			if (preferceManager.isAutoSaveImage()) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						saveImage.setDrawingCacheEnabled(true);
						saveImage.buildDrawingCache();
						Bitmap localBitmap = saveImage.getDrawingCache();
						// int i = new Random().nextInt(15);
						// storeImage(localBitmap, i + 65 + ".png");
						storeImage(localBitmap);
						finish();
					}
				}, 100L);
			}
			dialogbuttonSave.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					saveImage.setDrawingCacheEnabled(true);
					saveImage.buildDrawingCache();
					Bitmap bitmap = saveImage.getDrawingCache();
					// int i = new Random().nextInt(15);
					// storeImage(bitmap, i + 65 + ".png");
					storeImage(bitmap);
					finish();
				}
			});

		}

	}

	@SuppressLint("SimpleDateFormat")
	private File getOutputMediaFile() {

		int i = new Random().nextInt(10000);
		String filename = new_image_name + "_" + i + ".jpg";
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				.toString() + "/Claim mate/" + str3;
		Toast.makeText(getApplicationContext(),
				iconsStoragePath + " file save in directory.",
				Toast.LENGTH_LONG).show();
		File sdIconStorageDir = new File(iconsStoragePath);
		// create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();
		File mediaFile;
		mediaFile = new File(sdIconStorageDir.getPath() + File.separator
				+ filename);
		return mediaFile;
	}

}
