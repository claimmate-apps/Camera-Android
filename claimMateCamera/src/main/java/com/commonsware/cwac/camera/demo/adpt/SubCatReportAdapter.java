package com.commonsware.cwac.camera.demo.adpt;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.activities.AddSubCatValueActivity;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.NameItem;
import com.example.claimmate.R;

import java.util.ArrayList;


public class SubCatReportAdapter extends BaseAdapter
{

	ArrayList<NameItem> reportarr;
	Context ctx ;
	LayoutInflater L_inflater;


	String myPath ;
	ClaimSqlLiteDbHelper claimDbHelper;
	SQLiteDatabase DB;

	String SELECT_SQL;
	Cursor Cur;

	public SubCatReportAdapter(Context context, ArrayList<NameItem> reportarr)
	{
		this.ctx = context;
		this.reportarr = reportarr;
		L_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		 
	}

	
	@Override
	public int getCount() 
	{
		return reportarr.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return reportarr.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(final int position, View inView, ViewGroup parent)
	{
		View convertView = inView;
		final ViewHolder holder ; 
		
		    if(convertView == null)
	   	 	{	 
	   		 	holder = new ViewHolder();
	   		 	convertView = L_inflater.inflate(R.layout.reportrow, null);
//
//	   		 	holder.imgadItem = (ImageView)convertView.findViewById(R.id.imgAd);
//
	   		 	holder.lblname = (TextView)convertView.findViewById(R.id.lblname);
	   		 	holder.lblstatus = (TextView)convertView.findViewById(R.id.lblstatus);
				holder.btndelete =(ImageButton)convertView.findViewById(R.id.btndelete);
				holder.btnedit = (ImageButton)convertView.findViewById(R.id.btnedit);
				holder.btnedit.setVisibility(View.GONE);
//	   		 	holder.lblphone = (TextView)convertView.findViewById(R.id.lblphone);
//
//	   		 	holder.btnCall = (Button)convertView.findViewById(R.id.btnCall);
//	   		 	holder.imgLoderProgressbar = (ProgressBar)convertView.findViewById(R.id.imgLoderProgressbar);
	   		 	convertView.setTag(holder);

	   	 	}
	        else
	   	 	{
	        	holder = (ViewHolder)convertView.getTag();  
	   	 	}


		    holder.lblname.setText(reportarr.get(position).getname());
//		    holder.lblstatus.setText(reportarr.get(position).getstatus());

		holder.btndelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
				builder1.setMessage("Are you sure delete this?");
				builder1.setCancelable(true);

				builder1.setPositiveButton(
						"Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								deleterow(AddSubCatValueActivity.tblname, reportarr.get(position).getid(), position);

								dialog.cancel();
							}
						});

				builder1.setNegativeButton(
						"No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				AlertDialog alert11 = builder1.create();
				alert11.show();

			}
		});


		holder.btnedit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


						/* Alert Dialog Code Start*/
					AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
					alert.setTitle("Enter value"); //Set Alert dialog title here

					// Set an EditText view to get user input
					final EditText input = new EditText(ctx);
					input.setText(reportarr.get(position).getname());
					input.setSelection(input.getText().toString().length());

					alert.setView(input);

					alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							//You will get as string input data in this variable.
							// here we convert the input to a string and show in a toast.
//						String srt = input.getEditableText().toString();

							if (input.getText().toString().trim().equals("")) {
								Toast.makeText(ctx, "Enter value", Toast.LENGTH_LONG).show();

							} else {
								updaterow(input.getText().toString().trim(), AddSubCatValueActivity.tblname, reportarr.get(position).getid(), position);

							}
						} // End of onClick(DialogInterface dialog, int whichButton)
					}); //End of alert.setPositiveButton
					alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Canceled.
							dialog.cancel();
						}
					}); //End of alert.setNegativeButton
					AlertDialog alertDialog = alert.create();
					alertDialog.show();

				}


		});

		convertView.setClickable(true);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {


			}
		});
		  return convertView;
	}

	private void addmenuvalue(String tname, String strtitle,String strcid)
	{

		Intent addnewvalue = new Intent(ctx,AddSubCatValueActivity.class);
		addnewvalue.putExtra("tablename", tname);
		addnewvalue.putExtra("cid", strcid);
		addnewvalue.putExtra("title", strtitle);
		ctx.startActivity(addnewvalue);
	}


	private void deleterow(String tblname, String getid,int position)
	{

		opendatabase();
		SELECT_SQL = "delete from "+tblname+" where id = "+getid+"";
		DB.execSQL(SELECT_SQL);
		DB.close();

		reportarr.remove(position);
		notifyDataSetChanged();
	}

	private void updaterow(String txt,String tblname, String getid,int position)
	{

		opendatabase();
		SELECT_SQL = "UPDATE "+tblname+" set name = '"+txt+"' where id = "+getid+" ";
		Log.e("Updatsub",SELECT_SQL);
		DB.execSQL(SELECT_SQL);
		DB.close();

		NameItem item = reportarr.get(position);
		item.setname(txt);
		reportarr.set(position, item);
		notifyDataSetChanged();
//		reportarr.remove(position);
	}

	private void updatesubcatgryrow(String txt,String shortname,String tblname, String getid,int position)
	{

		opendatabase();
		SELECT_SQL = "UPDATE "+tblname+" set value = '"+txt+"',shortname='"+shortname+"' where id = "+getid+" ";
		DB.execSQL(SELECT_SQL);
		DB.close();

		NameItem item = reportarr.get(position);
		item.setname(txt);
		reportarr.set(position,item);
		notifyDataSetChanged();
//		reportarr.remove(position);
	}

	private void opendatabase()
	{
		myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
		DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}


	public static class ViewHolder
	{
		TextView  lblname , lblstatus ;
		ImageButton btndelete,btnedit;

	}
  
	

}
