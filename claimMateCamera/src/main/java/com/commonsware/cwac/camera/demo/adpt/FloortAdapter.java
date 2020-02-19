package com.commonsware.cwac.camera.demo.adpt;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.commonsware.cwac.camera.demo.model.FloorItem;
import com.example.claimmate.R;

import java.util.ArrayList;

public class FloortAdapter extends BaseAdapter {

    ArrayList<FloorItem> Floor;
    Context ctx;
    LayoutInflater L_inflater;

    public FloortAdapter(Context context, ArrayList<FloorItem> reportarr) {
        this.ctx = context;
        this.Floor = reportarr;
        L_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Floor.size();
    }

    @Override
    public Object getItem(int position) {
        return Floor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View inView, ViewGroup parent) {
        View convertView = inView;
        final WalltAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new WalltAdapter.ViewHolder();
            convertView = L_inflater.inflate(R.layout.floornew, null);

            holder.lblMaterial = (TextView) convertView.findViewById(R.id.lblMaterial);
            holder.lblRoom = (TextView) convertView.findViewById(R.id.lblRoom);
            holder.lblInsulation = (TextView) convertView.findViewById(R.id.lblInsulation);
            holder.lblAreaType = (TextView) convertView.findViewById(R.id.lblAreaType);
            holder.lblQty = (TextView) convertView.findViewById(R.id.lblQty);
            holder.lblDamage = (TextView) convertView.findViewById(R.id.lblDamage);

            convertView.setTag(holder);
        } else {
            holder = (WalltAdapter.ViewHolder) convertView.getTag();
        }
        holder.lblMaterial.setText(Floor.get(position).getMaterial());
        holder.lblRoom.setText(Floor.get(position).getRoom());
        holder.lblInsulation.setText(Floor.get(position).getInsulation());
        holder.lblAreaType.setText(Floor.get(position).getAreaType());
        holder.lblQty.setText(Floor.get(position).getQty());
        holder.lblDamage.setText(Floor.get(position).getDamage());
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