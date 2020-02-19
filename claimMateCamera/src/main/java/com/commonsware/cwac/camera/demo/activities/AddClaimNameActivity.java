package com.commonsware.cwac.camera.demo.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.ClaimModel;
import com.commonsware.cwac.camera.demo.other.Utility;
import com.commonsware.cwac.camera.demo.retrofit.APIInterface;
import com.commonsware.cwac.camera.demo.retrofit.ApiClient;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.claimmate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddClaimNameActivity extends Activity implements DragSortListView.DragSortListener, View.OnClickListener {

    private String TAG = "AddClaimNameActivity";
    private Context mContext;

    String SELECT_SQL;
    Cursor Cur;

    ArrayAdapter<String> adapter;
    private String[] array;
    private ArrayList<String> list;
    List<Map<String, String>> claimdelail;

    DragSortListView dragSortListView;

    private DragSortController mController;

    public int dragStartMode = DragSortController.CLICK_REMOVE;
    public boolean removeEnabled = true;
    public int removeMode = DragSortController.CLICK_REMOVE;
    public boolean sortEnabled = true;
    public boolean dragEnabled = true;
    RelativeLayout rlback;
    ImageButton btnadd;

    RelativeLayout rladdvalue;
    EditText txtname;
    Button btnok, btncancel;

    String myPath;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdemo);

        mContext = AddClaimNameActivity.this;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        // comment after
        /*rladdvalue =findViewById(R.id.rladdvalue);

        txtname =findViewById(R.id.txtname);
        btnok = findViewById(R.id.btnok);
        btncancel = findViewById(R.id.btncancel);

        rlback = findViewById(R.id.rlback);
        btnadd = findViewById(R.id.btnadd);

        dragSortListView = (DragSortListView)findViewById(R.id.list);
        getvalue("tbl_claimname");

        dragSortListView.setDragEnabled(true);

        mController = buildController(dragSortListView);
        dragSortListView.setFloatViewManager(mController);
        dragSortListView.setOnTouchListener(mController);
        dragSortListView.setDragEnabled(dragEnabled);


        dragSortListView.setDragSortListener(this);
        dragSortListView.setRemoveListener(this);

        rlback.setOnClickListener(this);
        btnadd.setOnClickListener(this);
        btnok.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        rladdvalue.setOnClickListener(this);*/

        init();
    }

    private ListView listClaim;

    private void init() {
        listClaim = findViewById(R.id.listClaim);

        rlback = findViewById(R.id.rlback);
        btnadd = findViewById(R.id.btnadd);

        rlback.setOnClickListener(this);
        btnadd.setOnClickListener(this);

        if (Utility.haveInternet(mContext, false))
            setClaimData();
    }

    ArrayList<ClaimModel> arrayListClaim;

    private void setClaimData() {
        if (Utility.haveInternet(mContext, false)) {
            Utility.showProgress(mContext);
            ApiClient.getClient().create(APIInterface.class).getClaimList(pref.getString("user_id", "0")).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Utility.dismissProgress();
                    Log.i(TAG, "getClaimListRes = " + response.body());

                    if (response.body() == null) {
                        Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                        return;
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        arrayListClaim = new ArrayList<>();
                        if (jsonObject.getString("success").equalsIgnoreCase("success")) {
                            JSONArray datas = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < datas.length(); i++) {
                                JSONObject data = datas.getJSONObject(i);
                                ClaimModel claimModel = new ClaimModel();
                                claimModel.setId(data.getString("id"));
                                claimModel.setName(data.getString("claim_name"));
                                claimModel.setShortName(data.getString("short_name"));
                                arrayListClaim.add(claimModel);
                            }
                        }
                        listClaim.setAdapter(new ClaimAdpt());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        arrayListClaim = new ArrayList<>();
                        listClaim.setAdapter(new ClaimAdpt());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Utility.dismissProgress();
                    Log.i(TAG, "getClaimListError = " + t.toString());
                }
            });
        }
    }

    private void updateClaim(final ClaimModel claimModel) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.popup_add_claim);

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        final EditText edtClaimName = dialog.findViewById(R.id.edtClaimName);
        final EditText edtShortName = dialog.findViewById(R.id.edtShortName);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        txtTitle.setText("Update Claim");
        edtClaimName.setText(claimModel.getName());
        edtShortName.setText(claimModel.getShortName());
        btnSave.setText("Update");

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.haveInternet(mContext, true)) {
                    if (TextUtils.isEmpty(edtClaimName.getText())) {
                        edtClaimName.setError("Please Enter Claim Name");
                        edtClaimName.requestFocus();
                    }
//                    else if (TextUtils.isEmpty(edtShortName.getText())) {
//                        edtShortName.setError("Please Enter Short Name");
//                        edtShortName.requestFocus();
//                    }
                    else {
                        Utility.showProgress(mContext);
                        ApiClient.getClient().create(APIInterface.class).updateClaim(claimModel.getId(), edtClaimName.getText().toString()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                Utility.dismissProgress();
                                Log.i(TAG, "updateClaimRes = " + response.body());

                                if (response.body() == null) {
                                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                                    return;
                                }

                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getString("success").equals("success")) {
                                        dialog.dismiss();
                                        setClaimData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Utility.dismissProgress();
                                Log.i(TAG, "updateClaimError = " + t.toString());
                            }
                        });
                    }
                }
            }
        });
        dialog.show();
    }

    private void deleteClaim(final String cId) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure delete this?");

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Utility.showProgress(mContext);
                        ApiClient.getClient().create(APIInterface.class).deleteClaim(cId).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Utility.dismissProgress();
                                Log.i(TAG, "deleteClaimRes = " + response.body());

                                if (response.body() == null) {
                                    Utility.errorDialog(mContext, getString(R.string.error_data_not_found));
                                    return;
                                }

                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getString("success").equals("success")) {
                                        setClaimData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Utility.dismissProgress();
                                Log.i(TAG, "deleteClaimError = " + t.toString());
                            }
                        });
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    class ClaimAdpt extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayListClaim.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_click_remove, null);

            TextView text = view.findViewById(R.id.text);

            ClaimModel claimModel = arrayListClaim.get(i);
            text.setText(claimModel.getName());

            view.findViewById(R.id.btnedit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utility.haveInternet(mContext, true))
                        updateClaim(arrayListClaim.get(i));
                }
            });

            view.findViewById(R.id.click_remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utility.haveInternet(mContext, true))
                        deleteClaim(arrayListClaim.get(i).getId());
                }
            });
            return view;
        }
    }

    private void addClaim() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.popup_add_claim);

        final EditText edtClaimName = dialog.findViewById(R.id.edtClaimName);
        final EditText edtShortName = dialog.findViewById(R.id.edtShortName);

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.haveInternet(mContext, true)) {
                    if (TextUtils.isEmpty(edtClaimName.getText())) {
                        edtClaimName.setError("Please Enter Claim Name");
                        edtClaimName.requestFocus();
                    } else if (TextUtils.isEmpty(edtShortName.getText())) {
                        edtShortName.setError("Please Enter Short Name");
                        edtShortName.requestFocus();
                    } else {
                        Utility.showProgress(mContext);
                        ApiClient.getClient().create(APIInterface.class).addClaim(pref.getString("user_id", "0"), edtClaimName.getText().toString()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                Utility.dismissProgress();
                                Log.i(TAG, "addClaimRes = " + response.body());

                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getString("success").equals("success")) {
                                        dialog.dismiss();
                                        setClaimData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Utility.dismissProgress();
                                Log.i(TAG, "addClaimError = " + t.toString());
                            }
                        });
                    }
                }
            }
        });
        dialog.show();
    }

    public DragSortController buildController(DragSortListView dslv) {
        // defaults are
        //   dragStartMode = onDown
        //   removeMode = flingRight
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(removeEnabled);
        controller.setSortEnabled(sortEnabled);
        controller.setDragInitMode(dragStartMode);
        controller.setRemoveMode(removeMode);
        return controller;
    }

    @Override
    public void drag(int from, int to) {

    }

    @Override
    public void drop(int from, int to) {
        if (from != to) {

            Map<String, String> Data = claimdelail.get(from);
            claimdelail.remove(from);
            claimdelail.add(to, Data);

            String item = adapter.getItem(from);
            adapter.remove(item);
            adapter.insert(item, to);
            adapter.notifyDataSetChanged();

            DeleteDbRow();
        }
    }

    private void DeleteDbRow() {

        opendatabase();

        String sqlqry = "DELETE FROM tbl_claimname";
        DB.execSQL(sqlqry);
        DB.close();

        getallValue("tbl_claimname");

    }

    private void getallValue(String tblname) {
        opendatabase();

        for (int i = 0; i < claimdelail.size(); i++) {

            Map<String, String> Data = claimdelail.get(i);
            String item = Data.get("name").toString();
            Log.e("Row Item", "--> " + i + " " + item);

            String strqry = "Insert into " + tblname + " ('name') Values('" + item + "')";
            DB.execSQL(strqry);
        }

    }

    @Override
    public void remove(int which) {

        DeleteClaimName(which);
    }

    private void DeleteClaimName(final int which) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure delete this?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        adapterCeiling.remove(adapterCeiling.getItem(which));

                        deleterow("tbl_claimname", which);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    @Override
    public void onClick(View v) {

        int vid = v.getId();

        if (vid == rlback.getId()) {
            finish();
        } else if (vid == btnadd.getId()) {
            addClaim();
//            rladdvalue.setVisibility(View.VISIBLE);
        } else if (v.getId() == btnok.getId()) {
            rladdvalue.setVisibility(View.GONE);

            adddropdownvalue("tbl_claimname");
        } else if (v.getId() == btncancel.getId()) {
            rladdvalue.setVisibility(View.GONE);
        }

    }

    private void adddropdownvalue(String tblname) {

        if (txtname.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "please enter claim name", Toast.LENGTH_LONG).show();
        } else {
            opendatabase();
            String strqry = "Insert into " + tblname + " ('name') Values('" + txtname.getText().toString().trim() + "')";
            addrow(strqry);

        }
    }


    private void opendatabase() {
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void addrow(String strqry) {
        DB.execSQL(strqry);
        DB.close();
        rladdvalue.setVisibility(View.GONE);
        getvalue("tbl_claimname");
    }


    private void getvalue(String tblname) {

        list = new ArrayList<>();
        claimdelail = new ArrayList<>();


        opendatabase();

        SELECT_SQL = "SELECT * from " + tblname;
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                Map<String, String> Data = new HashMap<>();

                String id = "" + Cur.getInt(Cur.getColumnIndex("id"));
                Data.put("id", "" + id);

                String name = Cur.getString(Cur.getColumnIndex("name"));
                Data.put("name", name);

                list.add(name);
                claimdelail.add(Data);

            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_click_remove, R.id.text, list) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageButton btnedit = (ImageButton) view.findViewById(R.id.btnedit);
                btnedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateRowAlert(position);
                        Toast.makeText(getApplicationContext(), "Click On ---> " + position, Toast.LENGTH_LONG).show();
                    }
                });
                return view;
            }
        };

        dragSortListView.setAdapter(adapter);
        dragSortListView.invalidateViews();

        /*
        adapterCeiling = new ReportAdapter(this,Ceilingitems);
        lvmenuCeiling.setAdapter(adapterCeiling);
        lvmenuCeiling.invalidate();*/

    }

    private void updateRowAlert(final int position) {

        /* Alert Dialog Code Start*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter value"); //Set Alert dialog title here

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(adapter.getItem(position));
        input.setSelection(input.getText().toString().length());

        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.
//						String srt = input.getEditableText().toString();

                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter value", Toast.LENGTH_LONG).show();

                } else {
                    updaterow(input.getText().toString().trim(), "tbl_claimname", position);

                }
            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    private void deleterow(String tblname, int position) {

        opendatabase();

        Map<String, String> Data = claimdelail.get(position);

        String getid = Data.get("id").toString();
        SELECT_SQL = "delete from " + tblname + " where id = " + getid + "";
        DB.execSQL(SELECT_SQL);
        DB.close();

        list.remove(position);
        claimdelail.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void updaterow(String txt, String tblname, int position) {
        opendatabase();

        Map<String, String> Data = claimdelail.get(position);
        String getid = Data.get("id").toString();

        SELECT_SQL = "UPDATE " + tblname + " set name = '" + txt + "' where id = " + getid + " ";
        DB.execSQL(SELECT_SQL);
        DB.close();


        Data.put("name", txt);


        claimdelail.set(position, Data);
        list.remove(position);
        list.add(position, txt);

        adapter.notifyDataSetChanged();
//		reportarr.remove(position);
    }
}
