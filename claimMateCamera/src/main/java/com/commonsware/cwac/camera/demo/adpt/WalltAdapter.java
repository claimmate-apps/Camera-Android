package com.commonsware.cwac.camera.demo.adpt;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;




import com.commonsware.cwac.camera.demo.model.Wallitems;
import com.example.claimmate.R;

import java.util.ArrayList;

public class WalltAdapter extends BaseAdapter {

    ArrayList<Wallitems> Wall;
    Context ctx;
    LayoutInflater L_inflater;

    public WalltAdapter(Context context, ArrayList<Wallitems> reportarr) {
        this.ctx = context;
        this.Wall = reportarr;
        L_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Wall.size();
    }

    @Override
    public Object getItem(int position) {
        return Wall.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View inView, ViewGroup parent) {
        View convertView = inView;
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = L_inflater.inflate(R.layout.wallnew, null);


            holder.lblMaterial = (TextView) convertView.findViewById(R.id.lblMaterial);
            holder.lblRoom = (TextView) convertView.findViewById(R.id.lblRoom);
            holder.lblInsulation = (TextView) convertView.findViewById(R.id.lblInsulation);
            holder.lblAreaType = (TextView) convertView.findViewById(R.id.lblAreaType);
            holder.lblQty = (TextView) convertView.findViewById(R.id.lblQty);
            holder.lblDamage = (TextView) convertView.findViewById(R.id.lblDamage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lblMaterial.setText(Wall.get(position).getMaterial());
        holder.lblRoom.setText(Wall.get(position).getRoom());
        holder.lblInsulation.setText(Wall.get(position).getInsulation());
        holder.lblAreaType.setText(Wall.get(position).getAreaType());
        holder.lblQty.setText(Wall.get(position).getQty());
        holder.lblDamage.setText(Wall.get(position).getDamage());
        return convertView;
    }

    public static class ViewHolder {
        TextView lblMaterial;
        TextView lblRoom;
        TextView lblInsulation;
        TextView lblAreaType;
        TextView lblQty;
        TextView lblDamage;


    }

}
