package com.commonsware.cwac.camera.demo.activities;

import android.app.Activity;
import android.content.Intent;
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

import com.commonsware.cwac.camera.demo.adpt.SubCatReportAdapter;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.NameItem;
import com.example.claimmate.R;

import java.util.ArrayList;

public class AddSubCatValueActivity extends Activity implements View.OnClickListener {

    public static String tblname = "tbl_macrossubcat";
    String strtitle = "";
    String strcid = "";

    String myPath ;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    String SELECT_SQL;
    Cursor Cur;


    ListView lvmenu;

    SubCatReportAdapter adapter;
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
    protected void onResume() {
        super.onResume();
        getmacrovalue(tblname);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvalue);

        lvmenu = (ListView)findViewById(R.id.lvmenuCeiling);
        btnadd = (ImageButton)findViewById(R.id.btnadd);


        tblname = getIntent().getStringExtra("tablename");
        strtitle = getIntent().getStringExtra("title");
        strcid = getIntent().getStringExtra("cid");


        rladdvalue =(RelativeLayout)findViewById(R.id.rladdvalue);

        txtname =(EditText)findViewById(R.id.txtname);
        btnok = (Button)findViewById(R.id.btnok);
        btncancel = (Button)findViewById(R.id.btncancel);

        lbltitle = (TextView) findViewById(R.id.lbltitle);
        lbltitle.setText(strtitle);

        lblshortname = (TextView)findViewById(R.id.lblshortname);
        txtshortname = (EditText)findViewById(R.id.txtshortname);


        rlback =(RelativeLayout) findViewById(R.id.rlback);





        btnadd.setOnClickListener(this);
        rlback.setOnClickListener(this);

        btnok.setOnClickListener(this);
        btncancel.setOnClickListener(this);


    }


    private void getmacrovalue(String tblname)
    {
//        adapterCeiling = new SubCatReportAdapter(this);

        opendatabase();
        nameitems = new ArrayList<>();

        SELECT_SQL = "SELECT * from "+tblname +" where cid ="+strcid;
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


        adapter = new SubCatReportAdapter(this,nameitems);
        lvmenu.setAdapter(adapter);
        lvmenu.invalidate();

    }

    private void opendatabase()
    {
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onClick(View v)
    {

        if(v.getId() == btnadd.getId())
        {
            Intent camviewact = new Intent(getApplicationContext(),CamViewActivity.class);
            camviewact.putExtra("cid",strcid);
            startActivity(camviewact);
            /*
            rladdvalue.setVisibility(View.VISIBLE);
            txtname.setText("");

            if(tblname.equals("tbl_interior"))
            {
                txtshortname.setText("");
            }*/

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

                String strqry = "Insert into "+tblname+" ('cid','name') Values('"+strcid+"','"+txtname.getText().toString().trim()+"')";
                addrow(strqry);

            }
    }

    private void addrow(String strqry)
    {
        DB.execSQL(strqry);
        DB.close();
        rladdvalue.setVisibility(View.GONE);

        getmacrovalue(tblname);



    }
}
