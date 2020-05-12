/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.commonsware.cwac.camera.demo.adpt;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonsware.cwac.camera.demo.model.RoofRes;
import com.example.claimmate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RoofFirstAdapter
        extends RecyclerView.Adapter<RoofFirstAdapter.MyViewHolder> {
    public String TAG = getClass().getSimpleName();

    public static final int CHECKBOX = 1;
    public static final int EDIT_TEXT = 2;

    private List<RoofRes> data = new ArrayList<>();
//    ArrayList<ReferralIncoming.Datum> dataSource = new ArrayList<>();

    Context context;
    int i = 10;

    private EventListener mEventListener;

    public interface EventListener {
        void onItemViewClicked(int position);

        void onItemTitleClicked(int position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //        @Nullable
//        @BindView(R.id.container)
//        public FrameLayout container;
//        @Nullable
//        @BindView(R.id.tvRevokeAccess)
//        public TextView tvRevokeAccess;
//        @Nullable
//        @BindView(R.id.tvAccess)
//        public TextView tvAccess;
//        @Nullable
//        @BindView(R.id.tvTitle)
        public TextView tvTitle;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            tvTitle = v.findViewById(R.id.tvTitle);
        }
    }

    public RoofFirstAdapter(Context context) {
        this.context = context;

        setHasStableIds(true);
    }

    public void add() {
        i++;
        notifyDataSetChanged();
    }


    public void addAll(List<RoofRes> mData) {
        this.data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

//    public void remove(String cnreqid, int first, int last) {
//        try {
//            for (int i = 0; i < data.size(); i++) {
//
//                if (String.valueOf(data.get(i).id).equals(cnreqid)) {
//                    data.remove(i);
//                    dataSource.remove(i);
//                    if (i > first && i <= last) {
//                        notifyItemRemoved(i);
//                    } else {
//                        notifyDataSetChanged();
//                    }
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public RoofRes getItem(int position) {
        return data.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == CHECKBOX) {
            final View v = inflater.inflate(R.layout.item_roof_first_checkbox, parent, false);
            return new MyViewHolder(v);
        } else {
            final View v = inflater.inflate(R.layout.item_elevation_first, parent, false);
            return new MyViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RoofRes item = data.get(position);

//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mEventListener != null) {
//                    mEventListener.onItemViewClicked(position);
//                }
//            }
//        });

        holder.tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onItemTitleClicked(position);
                }
            }
        });

//        if (item.status.equalsIgnoreCase("pending")) {
//            holder.tvAccess.setVisibility(View.VISIBLE);
//            holder.tvRevokeAccess.setVisibility(View.GONE);
//        } else {
//            holder.tvAccess.setVisibility(View.GONE);
//            holder.tvRevokeAccess.setVisibility(View.VISIBLE);
//        }
////
        holder.tvTitle.setText(item.title);
//        holder.tvDate.setText(Utils.parseTime(item.referralDate, "yyyy-MM-dd HH:mm:ss", "dd MMM"));
//        holder.tvBal.setText(Utils.nullSafe(item.referralCoin));

//        holder.tvRefAVM.setText("" + item.avm + " %");
//        holder.tvRefPropType.setText("" + item.propertyType.name);
////        holder.tvRefWinBid.setText("" + item.avm);
//
//        if (!TextUtils.isEmpty(item.city) && !TextUtils.isEmpty(item.subdivision)) {
//            holder.tvRefCity.setText("" + item.city + ", " + item.subdivision);
//
//        } else if (!TextUtils.isEmpty(item.city)) {
//            holder.tvRefCity.setText("" + item.city);
//
//        } else if (!TextUtils.isEmpty(item.subdivision)) {
//            holder.tvRefCity.setText("" + item.subdivision);
//
//        } else {
//            holder.tvRefCity.setText("");
//        }


    }

    @Override
    public int getItemCount() {
        return data.size();
//        return i;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    private String getDisplayTime(long timestamp) {
//        return DateUtils
//                .getRelativeTimeSpanString(timestamp, new Date().getTime(), 0,
//                        DateUtils.FORMAT_ABBREV_RELATIVE).toString();
//    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isCheckbox) {
            return CHECKBOX;
        }
        return EDIT_TEXT;
    }
}
