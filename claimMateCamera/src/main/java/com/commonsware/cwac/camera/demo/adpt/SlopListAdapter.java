package com.commonsware.cwac.camera.demo.adpt;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.commonsware.cwac.camera.demo.model.SlopCount;
import com.commonsware.cwac.camera.demo.other.customitemclicklistener;
import com.example.claimmate.R;

import java.util.List;

public class SlopListAdapter extends RecyclerView.Adapter<SlopListAdapter.ViewHolder>
{
    private List<SlopCount> data;
    private Context mContext;
    private customitemclicklistener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_slopno, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {

        holder.tv_no.setText(""+data.get(position).getNo());


        if(data.get(position).getIscheck())
        {
            holder.tv_no.setBackgroundResource(R.drawable.roundred_select);
        }
        else
        {
            holder.tv_no.setBackgroundResource(R.drawable.roundgray_select);
        }

        holder.row_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UnCheckAll();
                data.get(position).setIscheck(true);
                notifyDataSetChanged();
                listener.onItemClick(view,position);
            }
        });
    }

    private void UnCheckAll() {


        for (int i=0;i<data.size();i++)
        {
            data.get(i).setIscheck(false);
        }


    }


    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public SlopListAdapter(Context mContext, List<SlopCount> data, customitemclicklistener listener)
    {
        this.data = data;
        this.mContext = mContext;
        this.listener = listener;

    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {


        AppCompatTextView tv_no;
        View row_main;


        ViewHolder(View v)
        {
            super(v);
            this.row_main = v;
            this.tv_no = (AppCompatTextView) v.findViewById(R.id.tv_no);
        }
    }



}
