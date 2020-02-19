package com.commonsware.cwac.camera.demo.adpt;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.commonsware.cwac.camera.demo.model.AreaTypeItem;
import com.example.claimmate.R;

import java.util.ArrayList;

public class AreaTypeAdapter extends BaseAdapter {

    ArrayList<AreaTypeItem> AreaTypeItem;
    Context ctx;
    LayoutInflater L_inflater;

    public AreaTypeAdapter(Context context, ArrayList<AreaTypeItem> reportarr) {
        this.ctx = context;
        this.AreaTypeItem = reportarr;
        L_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return AreaTypeItem.size();
    }

    @Override
    public Object getItem(int position) {
        return AreaTypeItem.get(position);
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


            holder.lblname = (TextView) convertView.findViewById(R.id.lblname);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lblname.setText(AreaTypeItem.get(position).getname());
        return convertView;
    }

    public static class ViewHolder {
        TextView lblname;


    }

}
