package com.commonsware.cwac.camera.demo.adpt;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import com.commonsware.cwac.camera.demo.model.CeilingItem;

import com.example.claimmate.R;

import java.util.ArrayList;

public class cellingAdapter extends BaseAdapter {

    ArrayList<CeilingItem> Ceiling;
    Context ctx;
    LayoutInflater L_inflater;

    public cellingAdapter(Context context, ArrayList<CeilingItem> reportarr) {
        this.ctx = context;
        this.Ceiling = reportarr;
        L_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Ceiling.size();
    }

    @Override
    public Object getItem(int position) {
        return Ceiling.get(position);
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
            convertView = L_inflater.inflate(R.layout.cellingnew, null);


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
        holder.lblMaterial.setText(Ceiling.get(position).getMaterial());
        holder.lblRoom.setText(Ceiling.get(position).getRoom());
        holder.lblInsulation.setText(Ceiling.get(position).getInsulation());
        holder.lblAreaType.setText(Ceiling.get(position).getAreaType());
        holder.lblQty.setText(Ceiling.get(position).getQty());
        holder.lblDamage.setText(Ceiling.get(position).getDamage());
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
