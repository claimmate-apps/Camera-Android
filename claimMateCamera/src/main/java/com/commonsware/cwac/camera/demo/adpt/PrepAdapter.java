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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;

import com.commonsware.cwac.camera.demo.model.InteriorRes;
import com.example.claimmate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PrepAdapter
        extends RecyclerView.Adapter<PrepAdapter.MyViewHolder> implements Filterable {
    public String TAG = getClass().getSimpleName();

    private List<InteriorRes.Ceiling> data = new ArrayList<>();
//    ArrayList<ReferralIncoming.Datum> dataSource = new ArrayList<>();

    Context context;
    int i = 2;

    private EventListener mEventListener;

    public interface EventListener {
        void onItemViewClicked(int position);
        void onItemSpinnerClicked(int position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //@Nullable
        //@BindView(R.id.editCustom)
        public EditText editCustom;
        //@Nullable
        //@BindView(R.id.spinnerTitle)
        public Spinner spinnerTitle;
        //@Nullable
        //@BindView(R.id.spinnerSign)
        public Spinner spinnerSign;
        //@Nullable
        //@BindView(R.id.editInfo)
        public EditText editInfo;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            editCustom = v.findViewById(R.id.editCustom);
            spinnerTitle = v.findViewById(R.id.spinnerTitle);
            spinnerSign = v.findViewById(R.id.spinnerSign);
            editInfo = v.findViewById(R.id.editInfo);
        }

    }

    public PrepAdapter(Context context) {
        this.context = context;

        setHasStableIds(true);

    }

    public void add(InteriorRes.Ceiling ceiling) {
        data.add(ceiling);
        notifyItemInserted(getItemCount());
    }


    public void addAll(List<InteriorRes.Ceiling> mData) {
//        this.data.clear();
        this.data.addAll(mData);

//        if (isFilterable) {
//            this.dataSource.addAll(mData);
//        }
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
//        dataSource.clear();
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

//    public RequestRes.LogList getItem(int position) {
//        return data.get(position);
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_interior_prep, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final InteriorRes.Ceiling item = data.get(position);

//        holder.editTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mEventListener != null) {
//                    mEventListener.onItemViewClicked(position);
//                }
//            }
//        });

        holder.spinnerTitle.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                if (position1 != 0) {
                    if (mEventListener != null) {
                        mEventListener.onItemSpinnerClicked(position);
                    }
                }
                if (position1 == holder.spinnerTitle.getCount() - 1) {
                    holder.editCustom.setVisibility(View.VISIBLE);
                } else {
                    holder.editCustom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        holder.spinnerTitle.setSelection(item.title);
        holder.spinnerSign.setSelection(item.sign);
        holder.editInfo.setText(item.info);


//        if (item.status.equalsIgnoreCase("pending")) {
//            holder.tvAccess.setVisibility(View.VISIBLE);
//            holder.tvRevokeAccess.setVisibility(View.GONE);
//        } else {
//            holder.tvAccess.setVisibility(View.GONE);
//            holder.tvRevokeAccess.setVisibility(View.VISIBLE);
//        }
////
//        holder.tvClientName.setText(Utils.nullSafe(item.clientName));
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

//        if (position == getItemCount() - 1) {
//            holder.editTitle.setVisibility(View.VISIBLE);
//            holder.editTitle.setFocusable(false);
//            holder.editTitle.setFocusableInTouchMode(false);
//            holder.editTitle.setLongClickable(false);
//            holder.spinnerTitle.setVisibility(View.GONE);
//        } else {
//            holder.editTitle.setVisibility(View.GONE);
//            holder.spinnerTitle.setVisibility(View.VISIBLE);
//            holder.editTitle.setFocusable(true);
//            holder.editTitle.setFocusableInTouchMode(true);
//            holder.editTitle.setLongClickable(true);
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

    boolean isFilterable = false;

    public void setFilterable(boolean isFilterable) {
        this.isFilterable = isFilterable;
    }

    @Override
    public Filter getFilter() {

//        if (isFilterable) {
//            return new PTypeFilter();
//        }

        return null;
    }

//    private class PTypeFilter extends Filter {
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence prefix, FilterResults results) {
//            // NOTE: this function is *always* called from the UI thread.
//
//            data = (ArrayList<ReferralIncoming.Datum>) results.values;
//            if (data != null) {
//                notifyDataSetChanged();
//            } else {
//                data = dataSource;
//                notifyDataSetChanged();
//            }
//        }
//
//        protected FilterResults performFiltering(CharSequence prefix) {
//            // NOTE: this function is *always* called from a background thread,
//            // and
//            // not the UI thread.
//
//            FilterResults results = new FilterResults();
//            ArrayList<ReferralIncoming.Datum> new_res = new ArrayList<>();
//            if (prefix != null && prefix.toString().length() > 0) {
//                for (int index = 0; index < dataSource.size(); index++) {
//
//                    try {
//                        ReferralIncoming.Datum si = dataSource.get(index);
//
//                        if (si.propertyType.name.toLowerCase().contains(prefix.toString().toLowerCase()) ||
//                                Utils.nullSafe(si.avm).toLowerCase().contains(prefix.toString().toLowerCase())) {
//                            new_res.add(si);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                results.values = new_res;
//                results.count = new_res.size();
//
//            } else {
//                Debug.e("", "Called synchronized view");
//
//                results.values = dataSource;
//                results.count = dataSource.size();
//
//            }
//
//            return results;
//        }
//    }
}
