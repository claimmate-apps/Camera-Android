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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.commonsware.cwac.camera.demo.model.InteriorRes;
import com.commonsware.cwac.camera.demo.model.InteriorRes.Ceiling;
import com.example.claimmate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InteriorAdapter
        extends RecyclerView.Adapter<InteriorAdapter.MyViewHolder> implements Filterable {
    public String TAG = getClass().getSimpleName();

//    private List<RequestRes.LogList> data = new ArrayList<>();
//    ArrayList<ReferralIncoming.Datum> dataSource = new ArrayList<>();

    int i = 1;
    Context context;

    private EventListener mEventListener;

    public interface EventListener {
        void onItemViewClicked(int position);
    }

    public void add() {
        i++;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.imgAddCeiling)
        public ImageView imgAddCeiling;
        @Nullable
        @BindView(R.id.imgAddFloor)
        public ImageView imgAddFloor;
        @Nullable
        @BindView(R.id.imgAddWalls)
        public ImageView imgAddWalls;
        @Nullable
        @BindView(R.id.imgAddPrep)
        public ImageView imgAddPrep;

//        @Nullable
//        @BindView(R.id.recyclerCeiling2)
        public RecyclerView recyclerCeiling;
//        @Nullable
//        @BindView(R.id.recyclerWalls)
        public RecyclerView recyclerWalls;
//        @Nullable
//        @BindView(R.id.recyclerFloor)
        public RecyclerView recyclerFloor;
//        @Nullable
//        @BindView(R.id.recyclerPrep)
        public RecyclerView recyclerPrep;

        CeilingAdapter ceilingAdapter;
        WallAdapter wallAdapter;
        FloorAdapter floorAdapter;
        PrepAdapter prepAdapter;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            recyclerCeiling = v.findViewById(R.id.recyclerCeiling);
            recyclerWalls = v.findViewById(R.id.recyclerWalls);
            recyclerFloor = v.findViewById(R.id.recyclerFloor);
            recyclerPrep = v.findViewById(R.id.recyclerPrep);

            LayoutManager mLayoutManager = new LinearLayoutManager(context);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerCeiling.setLayoutManager(mLayoutManager);

            ceilingAdapter = new CeilingAdapter(context);
            recyclerCeiling.setAdapter(ceilingAdapter);

            List<InteriorRes.Ceiling> ceilings = new ArrayList<>();
            ceilings.add(new Ceiling(1, 2, "From Camera"));
            ceilings.add(new Ceiling(2, 2, "plus above"));
            ceilings.add(new Ceiling(3, 0, "plus above"));
            ceilings.add(new Ceiling(4, 0, "W or Zero"));
            ceilings.add(new Ceiling(0, 0, ""));

            ceilingAdapter.addAll(ceilings);

            ceilingAdapter.setEventListener(new CeilingAdapter.EventListener() {
                @Override
                public void onItemViewClicked(int position) {

                }

                @Override
                public void onItemSpinnerClicked(int position) {
                    if (position == ceilingAdapter.getItemCount() - 1) {
                        ceilingAdapter.add(new Ceiling(0, 0, ""));
                    }
                }
            });

            LayoutManager mLayoutManager2 = new LinearLayoutManager(context);
            recyclerWalls.setLayoutManager(mLayoutManager2);

            wallAdapter = new WallAdapter(context);
            recyclerWalls.setAdapter(wallAdapter);

            List<InteriorRes.Ceiling> walls = new ArrayList<>();
            walls.add(new Ceiling(1, 2, "From Camera"));
            walls.add(new Ceiling(2, 2, "plus above"));
            walls.add(new Ceiling(3, 0, "plus above"));
            walls.add(new Ceiling(4, 0, "W or Zero"));
            walls.add(new Ceiling(0, 0, ""));

            wallAdapter.addAll(walls);

            wallAdapter.setEventListener(new WallAdapter.EventListener() {
                @Override
                public void onItemViewClicked(int position) {

                }

                @Override
                public void onItemSpinnerClicked(int position) {
                    if (position == wallAdapter.getItemCount() - 1) {
                        wallAdapter.add(new Ceiling(0, 0, ""));
                    }
                }
            });

            LayoutManager mLayoutManager3 = new LinearLayoutManager(context);
            recyclerFloor.setLayoutManager(mLayoutManager3);

            floorAdapter = new FloorAdapter(context);
            recyclerFloor.setAdapter(floorAdapter);

            List<InteriorRes.Ceiling> floor = new ArrayList<>();
            floor.add(new Ceiling(1, 0, "clean or replace"));
            floor.add(new Ceiling(0, 0, "^ F"));

            floorAdapter.addAll(floor);

            floorAdapter.setEventListener(new FloorAdapter.EventListener() {
                @Override
                public void onItemViewClicked(int position) {

                }

                @Override
                public void onItemSpinnerClicked(int position) {
                    if (position == floorAdapter.getItemCount() - 1) {
                        floorAdapter.add(new Ceiling(0, 0, ""));
                    }
                }
            });

            LayoutManager mLayoutManager4 = new LinearLayoutManager(context);
            recyclerPrep.setLayoutManager(mLayoutManager4);

            prepAdapter = new PrepAdapter(context);
            recyclerPrep.setAdapter(prepAdapter);

            List<InteriorRes.Ceiling> prep = new ArrayList<>();
            prep.add(new Ceiling(1, 0, "wrong option"));
            prep.add(new Ceiling(0, 0, ""));

            prepAdapter.addAll(prep);

            prepAdapter.setEventListener(new PrepAdapter.EventListener() {
                @Override
                public void onItemViewClicked(int position) {

                }

                @Override
                public void onItemSpinnerClicked(int position) {
                    if (position == prepAdapter.getItemCount() - 1) {
                        prepAdapter.add(new Ceiling(0, 0, ""));
                    }
                }
            });

            /*imgAddCeiling.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ceilingAdapter.add();
                }
            });*/
//            imgAddWalls.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    wallAdapter.add();
//                }
//            });
//            imgAddFloor.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    floorAdapter.add();
//                }
//            });
//            imgAddPrep.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    prepAdapter.add();
//                }
//            });

        }

    }

    public InteriorAdapter(Context context) {
        this.context = context;

        setHasStableIds(true);

    }

//    public void addAll(List<RequestRes.LogList> mData) {
////        this.data.clear();
//        this.data.addAll(mData);
//
////        if (isFilterable) {
////            this.dataSource.addAll(mData);
////        }
//        notifyDataSetChanged();
//    }

//    public void clear() {
//        data.clear();
////        dataSource.clear();
//        notifyDataSetChanged();
//    }

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
        final View v = inflater.inflate(R.layout.item_interior, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        final RequestRes.LogList item = data.get(position);

//        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mEventListener != null) {
//                    mEventListener.onTitleClicked(position,holder.tvTitle);
//                }
//            }
//        });

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

//        LayoutManager mLayoutManager = new LinearLayoutManager(context);
//        holder.recycler1.setLayoutManager(mLayoutManager);
//
//        holder.firstAdapter = new InteriorFirstAdapter(context);
//        holder.recycler1.setAdapter(holder.firstAdapter);
//
//        LayoutManager mLayoutManager2 = new LinearLayoutManager(context);
//        holder.recycler2.setLayoutManager(mLayoutManager2);
//
//        holder.secondAdapter = new InteriorSecondAdapter(context);
//        holder.recycler2.setAdapter(holder.secondAdapter);
//
//        holder.btnAddFirst.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.firstAdapter.add();
//            }
//        });
//        holder.btnAddSecond.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.secondAdapter.add();
//            }
//        });


    }

    @Override
    public int getItemCount() {
//        return data.size();
        return i;
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
