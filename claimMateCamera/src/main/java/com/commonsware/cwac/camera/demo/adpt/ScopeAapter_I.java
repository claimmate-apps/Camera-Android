package com.commonsware.cwac.camera.demo.adpt;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.interiorsavedata;
import com.commonsware.cwac.camera.demo.other.Constants;
import com.commonsware.cwac.camera.demo.other.customitemclicklistener;
import com.example.claimmate.R;

import java.util.List;

public class ScopeAapter_I extends RecyclerView.Adapter<ScopeAapter_I.ViewHolder>
{
    List<interiorsavedata> data;
    Context mContext;
    customitemclicklistener listener;

    String myPath ;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    String SELECT_SQL;
    Cursor Cur;



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scope, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);



        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.tv_title.setText(data.get(position).getAreaname());
        holder.tv_damage_item.setText(data.get(position).getSubareaname());
//        holder.tv_damage_type.setText(data.get(position).getDamagename());
        holder.tv_damage_type.setText("+");

        holder.tv_quantity.setText(data.get(position).getNo());


        if(position == 0)
        {
            holder.tv_title.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tv_title.setVisibility(View.GONE);

            if(data.get(position).getAreaname().equalsIgnoreCase(data.get(position-1).getAreaname()))
            {
                holder.tv_title.setVisibility(View.GONE);
            }
            else
            {
                holder.tv_title.setVisibility(View.VISIBLE);

            }

        }
        holder.rlmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(v,position);
            }
        });

        holder.tv_damage_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowOptionType(data.get(position).getAreaname(),holder.tv_damage_item,position);
            }
        });

        holder.tv_damage_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Showoperation(holder.tv_damage_type);
            }
        });



        holder.tv_quantity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                ShowOptionNumber(position);
            }
        });


        holder.tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(data.get(position).getSubareaname().equalsIgnoreCase("Blank"))
                {
                    updaterow("",data.get(position).getNo(),"tbl_interior_savedata",data.get(position).getId());

                }
                else
                {
                    updaterow(data.get(position).getSubareaname(),data.get(position).getNo(),"tbl_interior_savedata",data.get(position).getId());

                }

            }
        });

    }

    private void ShowOptionType(String strAreaname, AppCompatTextView tv_damage_item, final int position)
    {

        String[] items = new String[0];

        if(strAreaname.equalsIgnoreCase("Ceiling"))
        {
            items = new String[Constants.Ceilingzrr.size()];
            for (int i=0;i<Constants.Ceilingzrr.size();i++)
            {
                items[i]=Constants.Ceilingzrr.get(i);
            }
        }
        else if(strAreaname.equalsIgnoreCase("Wall"))
        {
            items = new String[Constants.Wallzrr.size()];
            for (int i=0;i<Constants.Wallzrr.size();i++)
            {
                items[i]=Constants.Wallzrr.get(i);
            }
        }
        else if(strAreaname.equalsIgnoreCase("Floor"))
        {
            items = new String[Constants.Floorzrr.size()];
            for (int i=0;i<Constants.Floorzrr.size();i++)
            {
                items[i]=Constants.Floorzrr.get(i);
            }
        }
       


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select");
        final String[] finalItems = items;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos)
            {
                data.get(position).setSubareaname(finalItems[pos]);
                notifyDataSetChanged();

                // Do something with the selection
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
        builder.show();



    }

    private void Showoperation(final AppCompatTextView tv_type)
    {

        String[] items = {"+","-","&","R","I","M"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select");
        final String[] finalItems = items;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos)
            {
                tv_type.setText(finalItems[pos].toString());
//                data.get(position).setQuantity(finalItems[pos]);
//                notifyDataSetChanged();

                // Do something with the selection
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
        builder.show();



    }

    private void ShowOptionNumber(final int position)
    {

        String[] items = {"0","1","2","3","4","5","6","7","8","9","10"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select");
        final String[] finalItems = items;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos)
            {
                data.get(position).setNo(finalItems[pos]);
                notifyDataSetChanged();

                // Do something with the selection
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
        builder.show();



    }



    @Override
    public int getItemCount() {
        return data.size();
    }
    public ScopeAapter_I(Context mContext, List<interiorsavedata> data, customitemclicklistener listener)
    {
        this.data = data;
        this.mContext = mContext;
        this.listener = listener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {

        AppCompatTextView tv_title,tv_damage_item,tv_damage_type,tv_quantity,tv_save;
        LinearLayout rlmain;


        ViewHolder(View v)
        {
            super(v);
            this.tv_title = v.findViewById(R.id.tv_title);
            this.tv_damage_item = v.findViewById(R.id.tv_damage_item);
            this.tv_damage_type = v.findViewById(R.id.tv_damage_type);
            this.tv_quantity = v.findViewById(R.id.tv_quantity);
            this.rlmain = v.findViewById(R.id.rlmain);
            this.tv_save = v.findViewById(R.id.tv_save);


        }
    }

    private void updaterow(String damageitem, String no,String tblname, String getid)
    {

        opendatabase();
        SELECT_SQL = "UPDATE "+tblname+" set subareaname = '"+damageitem+"', no = '"+no+"' where id = "+getid+" ";
        Log.e("Updatsub",SELECT_SQL);
        DB.execSQL(SELECT_SQL);
        DB.close();

        Toast.makeText(mContext,"Saved Data",Toast.LENGTH_LONG).show();
    }


    private void opendatabase()
    {
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }



}
