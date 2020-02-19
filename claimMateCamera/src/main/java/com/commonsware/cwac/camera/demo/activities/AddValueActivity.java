package com.commonsware.cwac.camera.demo.activities;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.adpt.ReportAdapter;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.NameItem;
import com.example.claimmate.R;

import java.util.ArrayList;

public class AddValueActivity extends Activity implements View.OnClickListener {

    public static String tblname = "tbl_r";
    String strtitle = "";
    String myPath ;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    String SELECT_SQL;
    Cursor Cur;


    ListView lvmenu;

    ReportAdapter adapter;
    ArrayList<NameItem> nameitems;
    ImageButton btnadd;

    RelativeLayout rlback;

    RelativeLayout rladdvalue;
    EditText txtname;
    Button btnok,btncancel;

    TextView lbltitle;

    TextView lblshortname;
    EditText txtshortname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvalue);

        lvmenu = (ListView)findViewById(R.id.lvmenuCeiling);
        btnadd = (ImageButton)findViewById(R.id.btnadd);


        tblname = getIntent().getStringExtra("tablename");
        strtitle = getIntent().getStringExtra("title");

        rladdvalue =(RelativeLayout)findViewById(R.id.rladdvalue);

        txtname =(EditText)findViewById(R.id.txtname);
        btnok = (Button)findViewById(R.id.btnok);
        btncancel = (Button)findViewById(R.id.btncancel);

        lbltitle = (TextView)findViewById(R.id.lbltitle);
        lbltitle.setText(strtitle);

        lblshortname = (TextView)findViewById(R.id.lblshortname);
        txtshortname = (EditText)findViewById(R.id.txtshortname);


        if(tblname.equals("tbl_interior"))
        {
//            lblshortname.setVisibility(View.VISIBLE);
//            txtshortname.setVisibility(View.VISIBLE);
        }

        rlback =(RelativeLayout)findViewById(R.id.rlback);

        if(tblname.equals("tbl_macros"))
        {
            getmacrovalue(tblname);

        }
        else
        {
            getvalue(tblname);

        }

        btnadd.setOnClickListener(this);
        rlback.setOnClickListener(this);

        btnok.setOnClickListener(this);
        btncancel.setOnClickListener(this);


    }


    private void getmacrovalue(String tblname)
    {
//        adapterCeiling = new ReportAdapter(this);

        opendatabase();
        nameitems = new ArrayList<>();

        SELECT_SQL = "SELECT * from "+tblname +" where iscoustom = 0";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {
                NameItem item = new NameItem();
                String id = ""+Cur.getInt(Cur.getColumnIndex("id"));
                Log.e("rowid===>",""+id);


                String value = Cur.getString(Cur.getColumnIndex("name"));
                item.setid(id);
                item.setname(value);


                nameitems.add(item);

            }
            while(Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        adapter = new ReportAdapter(this,nameitems);
        lvmenu.setAdapter(adapter);
        lvmenu.invalidate();

    }


    private void getvalue(String tblname)
    {
//        adapterCeiling = new ReportAdapter(this);

        opendatabase();
        nameitems = new ArrayList<>();

        SELECT_SQL = "SELECT * from "+tblname;
        Cur = DB.rawQuery(SELECT_SQL, null);
        if(Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do
            {
                NameItem item = new NameItem();
                String id = ""+Cur.getInt(Cur.getColumnIndex("id"));
                Log.e("rowid===>",""+id);


                String value = Cur.getString(Cur.getColumnIndex("value"));
                item.setid(id);
                item.setname(value);

                if(tblname.equals("tbl_interior"))
                {
                    String shortvalue = Cur.getString(Cur.getColumnIndex("shortname"));
                    item.setshortname(shortvalue);

                }
                nameitems.add(item);
                /*
                Tasbihitem saveTasbihitem = new Tasbihitem();
                AllTotal = AllTotal+ Cur.getInt(Cur.getColumnIndex("userTotal"));
                saveTasbihitem.setdate(Cur.getString(Cur.getColumnIndex("date")));
                saveTasbihitem.settId(Cur.getString(Cur.getColumnIndex("tId")));
                saveTasbihitem.setuserTotal(Cur.getString(Cur.getColumnIndex("userTotal")));
                Log.d("Tasbih", "Save Tasbih " + Cur.getString(Cur.getColumnIndex("date")) + " " + Cur.getString(Cur.getColumnIndex("userTotal")));
                Savetasbihitemsarr.add(saveTasbihitem);*/
            }
            while(Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        adapter = new ReportAdapter(this,nameitems);
        lvmenu.setAdapter(adapter);
        lvmenu.invalidate();

    }



    private void opendatabase()
    {
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == btnadd.getId())
        {
            rladdvalue.setVisibility(View.VISIBLE);
            txtname.setText("");

            if(tblname.equals("tbl_interior"))
            {
                txtshortname.setText("");
            }

        }
        else if(v.getId() == rlback.getId())
        {
            finish();
        }
        else if(v.getId() == btnok.getId())
        {
            rladdvalue.setVisibility(View.GONE);

            adddropdownvalue(tblname);
        }
        else if(v.getId() == btncancel.getId())
        {
            rladdvalue.setVisibility(View.GONE);
        }


    }

    private void adddropdownvalue(String tblname)
    {

            if(txtname.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG).show();
            }
            else
            {
                opendatabase();

                if(tblname.equals("tbl_interior"))
                {
                    if(txtname.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Please enter shortname", Toast.LENGTH_LONG).show();
                    }
                    else
                    {


                        String strqry = "Insert into "+tblname+" ('value','shortname') Values('"+txtname.getText().toString().trim()+"','"+txtname.getText().toString().trim()+"')";
                        addrow(strqry);
                    }
                }
                else if(tblname.equals("tbl_macros"))
                {
                    String strqry = "Insert into "+tblname+" ('name') Values('"+txtname.getText().toString().trim()+"')";
                    addrow(strqry);
                }
                else
                {
                    String strqry = "Insert into "+tblname+" ('value') Values('"+txtname.getText().toString().trim()+"')";
                    addrow(strqry);
                }





            }
    }

    private void addrow(String strqry)
    {
        DB.execSQL(strqry);
        DB.close();
        rladdvalue.setVisibility(View.GONE);

        if(tblname.equals("tbl_macros"))
        {
            getmacrovalue(tblname);

        }
        else
        {
            getvalue(tblname);

        }

    }
}
