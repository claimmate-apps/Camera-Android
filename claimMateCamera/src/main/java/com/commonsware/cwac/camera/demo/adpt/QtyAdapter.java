package com.commonsware.cwac.camera.demo.adpt;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.commonsware.cwac.camera.demo.model.QtyItem;
import com.example.claimmate.R;

import java.util.ArrayList;

public class QtyAdapter extends BaseAdapter {

    ArrayList<QtyItem> QtyItem;
    Context ctx;
    LayoutInflater L_inflater;

    public QtyAdapter(Context context, ArrayList<QtyItem> reportarr) {
        this.ctx = context;
        this.QtyItem = reportarr;
        L_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return QtyItem.size();
    }

    @Override
    public Object getItem(int position) {
        return QtyItem.get(position);
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
            convertView = L_inflater.inflate(R.layout.qtynew, null);

            holder.lblname = (TextView) convertView.findViewById(R.id.lblname);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lblname.setText(QtyItem.get(position).getname());
        return convertView;
    }

    public static class ViewHolder {
        TextView lblname;
    }
}
