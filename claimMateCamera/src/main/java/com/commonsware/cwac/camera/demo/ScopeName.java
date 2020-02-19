package com.commonsware.cwac.camera.demo;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.commonsware.cwac.camera.demo.adpt.ScopeAapter_E;
import com.commonsware.cwac.camera.demo.adpt.ScopeAapter_I;
import com.commonsware.cwac.camera.demo.adpt.ScopeAapter_R;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.interiorsavedata;
import com.commonsware.cwac.camera.demo.model.roofsavedata;
import com.commonsware.cwac.camera.demo.other.Constants;
import com.commonsware.cwac.camera.demo.other.customitemclicklistener;
import com.example.claimmate.R;

import java.util.ArrayList;
import java.util.List;

public class ScopeName extends AppCompatActivity implements View.OnClickListener {

    String myPath;
    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    String SELECT_SQL;
    Cursor Cur;

    List<interiorsavedata> interiorsavedataList;
    List<interiorsavedata> CeallinginteriorsavedataList;
    List<interiorsavedata> FloorinteriorsavedataList;
    List<interiorsavedata> WallsinteriorsavedataList;


    List<roofsavedata> roofsavedataList;
    List<roofsavedata> frontsavedataList;
    List<roofsavedata> rightsavedataList;
    List<roofsavedata> leftsavedataList;
    List<roofsavedata> rearsavedataList;



    ScopeAapter_I scopeAapter;
    ScopeAapter_R scopeAapter_r;
    ScopeAapter_E scopeAapter_e;

    RecyclerView rv_scopename;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout rlback;

    int value = 0;
    ArrayList<String> itrarr;
    PopupMenu popupMenuroomtype;
    PopupMenu popupMenuBuildingtype;

    AppCompatButton btn_type;
    AppCompatTextView tv_nodata;
    AppCompatButton btn_r,btn_e,btn_i;
    CardView cv_roomtype;
    AppCompatButton btn_building;
    String selecttype = "R";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope_name);

        initview();

        I_Option();
        R_Option();
        E_Option();

        setBuildingtype();
        setInteriorvalue();


        btn_r.performClick();
//        getInteriorSaveData("I","");
    }

    private void setBuildingtype()
    {
        value = 0;
        itrarr = new ArrayList<>();

        popupMenuBuildingtype = new PopupMenu(this, findViewById(R.id.btn_building));

        opendatabase();

         SELECT_SQL = "SELECT * FROM tblsubcat";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strname = Cur.getString(Cur.getColumnIndex("name"));

                if(!strname.equalsIgnoreCase("Custom Text"))
                {
                    popupMenuBuildingtype.getMenu().add(Menu.NONE, value, Menu.NONE, strname);
                    value++;

                }

                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();



        popupMenuBuildingtype.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {

            @Override
            public boolean onMenuItemClick(MenuItem arg0)
            {
                btn_building.setText(arg0.getTitle());
                slectBuildingType(arg0.getTitle().toString());
                popupMenuBuildingtype.dismiss();
                return false;
            }
        });



    }

    private void slectBuildingType(String title)
    {

        if(selecttype.equalsIgnoreCase("R"))
        {
            if(title.equalsIgnoreCase("None"))
            {
                title = "Main Building";

            }

            GetRoofSaveData(title);



        }
        else if(selecttype.equalsIgnoreCase("E"))
        {
            if(title.equalsIgnoreCase("None"))
            {
                title = "Main Building";

            }

            GetelEvationaveData(title);

        }
        else if(selecttype.equalsIgnoreCase("I"))
        {
            if(title.equalsIgnoreCase("None"))
            {
                title = "Main Building";

            }

            String type = btn_type.getText().toString();
            if(type.equalsIgnoreCase("Blank"))
            {
                getInteriorSaveData(title.toString(),"");
            }
            else
            {
                getInteriorSaveData(title,type);
            }

        }


    }

    private void E_Option() {

        Constants.EvationaMaterial = new ArrayList<>();
        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_e ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (!strvalue.equalsIgnoreCase("Custom Text"))
                {
                    Constants.EvationaMaterial.add(strvalue);
                    Log.e("strvalueCeiling","==>"+strvalue);
                }

            }
            while (Cur.moveToNext());

        }
        Cur.close();
        DB.close();



        /*opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_r ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (strvalue.equalsIgnoreCase("Custom Text"))
                    continue;
                Log.e("Value==>", "" + strvalue);
                if(strvalue.equalsIgnoreCase("Blank"))
                {

                }else {

                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }
            }
            while (Cur.moveToNext());
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Flat Roof");
            value++;
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
            value++;
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");*/

    }



    private void R_Option() {

        Constants.RoofMaterial = new ArrayList<>();
        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_r ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (!strvalue.equalsIgnoreCase("Custom Text"))
                {
                    Constants.RoofMaterial.add(strvalue);
                    Log.e("strvalueCeiling","==>"+strvalue);
                }

            }
            while (Cur.moveToNext());

        }
        Cur.close();
        DB.close();



        /*opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_r ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (strvalue.equalsIgnoreCase("Custom Text"))
                    continue;
                Log.e("Value==>", "" + strvalue);
                if(strvalue.equalsIgnoreCase("Blank"))
                {

                }else {

                    popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }
            }
            while (Cur.moveToNext());
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Flat Roof");
            value++;
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Material");
            value++;
            popupMenu2.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom Text");*/

    }

    private void GetRoofSaveData(String selectbuildingtype)
    {

        roofsavedataList = new ArrayList<>();
        frontsavedataList = new ArrayList<>();
        rightsavedataList = new ArrayList<>();
        leftsavedataList = new ArrayList<>();
        rearsavedataList = new ArrayList<>();



        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_roof_savedata";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                roofsavedata roofsavedata = new roofsavedata();
                String id = Cur.getString(Cur.getColumnIndex("id"));
                String slopetype = Cur.getString(Cur.getColumnIndex("slopetype"));
                String material = Cur.getString(Cur.getColumnIndex("material"));
                String damagetype = Cur.getString(Cur.getColumnIndex("damagetype"));
                String quantity = Cur.getString(Cur.getColumnIndex("quantity"));
                String buildingtype = Cur.getString(Cur.getColumnIndex("buildingtype"));
                String claim_id = Cur.getString(Cur.getColumnIndex("claim_id"));

                if(selectbuildingtype.equalsIgnoreCase(""))
                {
                    if(slopetype.equalsIgnoreCase("front"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        frontsavedataList.add(roofsavedata);
                    }
                    else if(slopetype.equalsIgnoreCase("Right"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        rightsavedataList.add(roofsavedata);
                    }
                    else if(slopetype.equalsIgnoreCase("Left"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        leftsavedataList.add(roofsavedata);
                    }
                    else if(slopetype.equalsIgnoreCase("Rear"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        rearsavedataList.add(roofsavedata);
                    }
                }
                else
                {
                    if(selectbuildingtype.equalsIgnoreCase(buildingtype))
                    {
                        if(slopetype.equalsIgnoreCase("front"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            frontsavedataList.add(roofsavedata);
                        }
                        else if(slopetype.equalsIgnoreCase("Right"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            rightsavedataList.add(roofsavedata);
                        }
                        else if(slopetype.equalsIgnoreCase("Left"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            leftsavedataList.add(roofsavedata);
                        }
                        else if(slopetype.equalsIgnoreCase("Rear"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            rearsavedataList.add(roofsavedata);
                        }
                    }

                }



                /*if(selectroomtype.equalsIgnoreCase(""))
                {
                    interiorsavedata interiorsavedata = new interiorsavedata();
                    interiorsavedata.setId(id);
                    interiorsavedata.setClaim_id(claim_id);
                    interiorsavedata.setAreaname(areaname);
                    interiorsavedata.setSubareaname(subareaname);
                    interiorsavedata.setDamagename(damagename);
                    interiorsavedata.setRoomtype(roomtype);
                    interiorsavedata.setNo(no);

                    if(areaname.equalsIgnoreCase("ceiling"))
                    {
                        CeallinginteriorsavedataList.add(interiorsavedata);

                    }
                    else if(areaname.equalsIgnoreCase("wall"))
                    {
                        FloorinteriorsavedataList.add(interiorsavedata);

                    }
                    else if(areaname.equalsIgnoreCase("floor"))
                    {
                        WallsinteriorsavedataList.add(interiorsavedata);

                    }
                }
                else if(selectroomtype.equalsIgnoreCase("Blank"))
                {
                    if(roomtype.equalsIgnoreCase(""))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }

                }
                else
                {
                    if(roomtype.equalsIgnoreCase(selectroomtype))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }
                }*/


            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        roofsavedataList.addAll(frontsavedataList);
        roofsavedataList.addAll(rightsavedataList);
        roofsavedataList.addAll(rearsavedataList);
        roofsavedataList.addAll(leftsavedataList);



        if(roofsavedataList.size()>0)
        {
            tv_nodata.setVisibility(View.GONE);
        }
        else
        {
            tv_nodata.setVisibility(View.VISIBLE);
        }


        scopeAapter_r = new ScopeAapter_R(ScopeName.this, roofsavedataList, new customitemclicklistener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        rv_scopename.setAdapter(scopeAapter_r);


    }


    private void GetelEvationaveData(String selectbuildingtype) {

        roofsavedataList = new ArrayList<>();
        frontsavedataList = new ArrayList<>();
        rightsavedataList = new ArrayList<>();
        leftsavedataList = new ArrayList<>();
        rearsavedataList = new ArrayList<>();



        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_elevation_savedata";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                roofsavedata roofsavedata = new roofsavedata();
                String id = Cur.getString(Cur.getColumnIndex("id"));
                String slopetype = Cur.getString(Cur.getColumnIndex("slopetype"));
                String material = Cur.getString(Cur.getColumnIndex("material"));
                String damagetype = Cur.getString(Cur.getColumnIndex("damagetype"));
                String quantity = Cur.getString(Cur.getColumnIndex("quantity"));
                String buildingtype = Cur.getString(Cur.getColumnIndex("buildingtype"));
                String claim_id = Cur.getString(Cur.getColumnIndex("claim_id"));


                if(selectbuildingtype.equalsIgnoreCase(""))
                {
                    if(slopetype.equalsIgnoreCase("front"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        frontsavedataList.add(roofsavedata);
                    }
                    else if(slopetype.equalsIgnoreCase("Right"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        rightsavedataList.add(roofsavedata);
                    }
                    else if(slopetype.equalsIgnoreCase("Left"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        leftsavedataList.add(roofsavedata);
                    }
                    else if(slopetype.equalsIgnoreCase("Rear"))
                    {
                        roofsavedata.setId(id);
                        roofsavedata.setSlopetype(slopetype);
                        roofsavedata.setMaterial(material);
                        roofsavedata.setDamagetype(damagetype);
                        roofsavedata.setQuantity(quantity);
                        roofsavedata.setBuildingtype(buildingtype);
                        roofsavedata.setClaim_id(claim_id);
                        rearsavedataList.add(roofsavedata);
                    }
                }
                else
                {
                    if(selectbuildingtype.equalsIgnoreCase(buildingtype))
                    {
                        if(slopetype.equalsIgnoreCase("front"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            frontsavedataList.add(roofsavedata);
                        }
                        else if(slopetype.equalsIgnoreCase("Right"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            rightsavedataList.add(roofsavedata);
                        }
                        else if(slopetype.equalsIgnoreCase("Left"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            leftsavedataList.add(roofsavedata);
                        }
                        else if(slopetype.equalsIgnoreCase("Rear"))
                        {
                            roofsavedata.setId(id);
                            roofsavedata.setSlopetype(slopetype);
                            roofsavedata.setMaterial(material);
                            roofsavedata.setDamagetype(damagetype);
                            roofsavedata.setQuantity(quantity);
                            roofsavedata.setBuildingtype(buildingtype);
                            roofsavedata.setClaim_id(claim_id);
                            rearsavedataList.add(roofsavedata);
                        }
                    }

                }

                /*if(selectroomtype.equalsIgnoreCase(""))
                {
                    interiorsavedata interiorsavedata = new interiorsavedata();
                    interiorsavedata.setId(id);
                    interiorsavedata.setClaim_id(claim_id);
                    interiorsavedata.setAreaname(areaname);
                    interiorsavedata.setSubareaname(subareaname);
                    interiorsavedata.setDamagename(damagename);
                    interiorsavedata.setRoomtype(roomtype);
                    interiorsavedata.setNo(no);

                    if(areaname.equalsIgnoreCase("ceiling"))
                    {
                        CeallinginteriorsavedataList.add(interiorsavedata);

                    }
                    else if(areaname.equalsIgnoreCase("wall"))
                    {
                        FloorinteriorsavedataList.add(interiorsavedata);

                    }
                    else if(areaname.equalsIgnoreCase("floor"))
                    {
                        WallsinteriorsavedataList.add(interiorsavedata);

                    }
                }
                else if(selectroomtype.equalsIgnoreCase("Blank"))
                {
                    if(roomtype.equalsIgnoreCase(""))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }

                }
                else
                {
                    if(roomtype.equalsIgnoreCase(selectroomtype))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }
                }*/


            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();


        roofsavedataList.addAll(frontsavedataList);
        roofsavedataList.addAll(rightsavedataList);
        roofsavedataList.addAll(rearsavedataList);
        roofsavedataList.addAll(leftsavedataList);



        if(roofsavedataList.size()>0)
        {
            tv_nodata.setVisibility(View.GONE);
        }
        else
        {
            tv_nodata.setVisibility(View.VISIBLE);
        }


        scopeAapter_e = new ScopeAapter_E(ScopeName.this, roofsavedataList, new customitemclicklistener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        rv_scopename.setAdapter(scopeAapter_e);


    }


    private void initview() {

        rv_scopename = findViewById(R.id.rv_scopename);
        tv_nodata =findViewById(R.id.tv_nodata);

        cv_roomtype = findViewById(R.id.cv_roomtype);
        linearLayoutManager = new LinearLayoutManager(ScopeName.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_scopename.setLayoutManager(linearLayoutManager);


        btn_type =findViewById(R.id.btn_type);
        btn_r=findViewById(R.id.btn_r);
        btn_e=findViewById(R.id.btn_e);
        btn_i=findViewById(R.id.btn_i);



        rlback =findViewById(R.id.rlback);
        btn_building = findViewById(R.id.btn_building);

        rlback.setOnClickListener(this);
        btn_type.setOnClickListener(this);

        btn_e.setOnClickListener(this);
        btn_i.setOnClickListener(this);
        btn_r.setOnClickListener(this);
        btn_building.setOnClickListener(this);


    }


    private void getInteriorSaveData(String strbuildingtype,String selectroomtype)
    {

        interiorsavedataList = new ArrayList<>();
        CeallinginteriorsavedataList = new ArrayList<>();
        FloorinteriorsavedataList = new ArrayList<>();
        WallsinteriorsavedataList = new ArrayList<>();

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_interior_savedata";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                String id = Cur.getString(Cur.getColumnIndex("id"));
                String areaname = Cur.getString(Cur.getColumnIndex("areaname"));
                String subareaname = Cur.getString(Cur.getColumnIndex("subareaname"));
                String damagename = Cur.getString(Cur.getColumnIndex("damagename"));
                String buildingtype = Cur.getString(Cur.getColumnIndex("buildingtype"));
                String roomtype = Cur.getString(Cur.getColumnIndex("roomtype"));
                String no = Cur.getString(Cur.getColumnIndex("no"));
                String claim_id = Cur.getString(Cur.getColumnIndex("claim_id"));

                if(strbuildingtype.equalsIgnoreCase("") && roomtype.equalsIgnoreCase(""))
                {
                    interiorsavedata interiorsavedata = new interiorsavedata();
                    interiorsavedata.setId(id);
                    interiorsavedata.setClaim_id(claim_id);
                    interiorsavedata.setAreaname(areaname);
                    interiorsavedata.setSubareaname(subareaname);
                    interiorsavedata.setDamagename(damagename);
                    interiorsavedata.setRoomtype(roomtype);
                    interiorsavedata.setNo(no);

                    if(areaname.equalsIgnoreCase("ceiling"))
                    {
                        CeallinginteriorsavedataList.add(interiorsavedata);

                    }
                    else if(areaname.equalsIgnoreCase("wall"))
                    {
                        FloorinteriorsavedataList.add(interiorsavedata);

                    }
                    else if(areaname.equalsIgnoreCase("floor"))
                    {
                        WallsinteriorsavedataList.add(interiorsavedata);

                    }
                }
                else if(!strbuildingtype.equalsIgnoreCase("") && selectroomtype.equalsIgnoreCase(""))
                {
                    if(strbuildingtype.equalsIgnoreCase(buildingtype))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }

                }
                else if(strbuildingtype.equalsIgnoreCase("") && !selectroomtype.equalsIgnoreCase(""))
                {
                    if(selectroomtype.equalsIgnoreCase(roomtype))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }

                }
                else if(!strbuildingtype.equalsIgnoreCase("") && !selectroomtype.equalsIgnoreCase(""))
                {
                    if(selectroomtype.equalsIgnoreCase(roomtype) && strbuildingtype.equalsIgnoreCase(buildingtype))
                    {
                        interiorsavedata interiorsavedata = new interiorsavedata();
                        interiorsavedata.setId(id);
                        interiorsavedata.setClaim_id(claim_id);
                        interiorsavedata.setAreaname(areaname);
                        interiorsavedata.setSubareaname(subareaname);
                        interiorsavedata.setDamagename(damagename);
                        interiorsavedata.setRoomtype(roomtype);
                        interiorsavedata.setNo(no);

                        if(areaname.equalsIgnoreCase("ceiling"))
                        {
                            CeallinginteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("wall"))
                        {
                            FloorinteriorsavedataList.add(interiorsavedata);

                        }
                        else if(areaname.equalsIgnoreCase("floor"))
                        {
                            WallsinteriorsavedataList.add(interiorsavedata);

                        }
                    }

                }







                Log.e("savedataareaname-->",""+areaname);
                Log.e("savedatasubareaname-->",""+subareaname);


            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();



        interiorsavedataList.addAll(CeallinginteriorsavedataList);
        interiorsavedataList.addAll(WallsinteriorsavedataList);
        interiorsavedataList.addAll(FloorinteriorsavedataList);



        if(interiorsavedataList.size()>0)
        {
            tv_nodata.setVisibility(View.GONE);
        }
        else
        {
            tv_nodata.setVisibility(View.VISIBLE);
        }


        scopeAapter = new ScopeAapter_I(ScopeName.this, interiorsavedataList, new customitemclicklistener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        rv_scopename.setAdapter(scopeAapter);

    }


    private void opendatabase()  throws SQLException {

    /*    claimDbHelper = new ClaimSqlLiteDbHelper(HomeActivity.this, claimDbHelper.claimdb_NAME);
        claimDbHelper.openDataBase();
*/
        myPath = claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    private void setInteriorvalue()
    {
        value = 0;
        itrarr = new ArrayList<>();

        popupMenuroomtype = new PopupMenu(this, findViewById(R.id.btn_type));

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_interior ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (!strvalue.equalsIgnoreCase("Custom Text"))
                {
                    Log.e("interiorvalue","==>"+strvalue);
                    String strshortname = Cur.getString(Cur.getColumnIndex("shortname"));
                    itrarr.add(strshortname);
                    popupMenuroomtype.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);

                    value++;
                }

            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();

        popupMenuroomtype.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {

            @Override
            public boolean onMenuItemClick(MenuItem arg0)
            {

                String buildingtype = btn_building.getText().toString();
                if(buildingtype.equalsIgnoreCase("None"))
                {
                    buildingtype = "Main Building";

                }


                if(arg0.getTitle().toString().equalsIgnoreCase("Blank"))
                {
                    getInteriorSaveData(buildingtype,"");

                }
                else
                {
                    getInteriorSaveData(buildingtype,arg0.getTitle().toString());

                }
                btn_type.setText(arg0.getTitle());
                popupMenuroomtype.dismiss();
                return false;
            }
        });
    }







    @Override
    public void onClick(View v) {


        if(v == rlback)
        {
            finish();
        }
        else if(v == btn_type)
        {

            if(popupMenuroomtype!=null)
            {
                popupMenuroomtype.show();

            }


        }
        else if(v == btn_building)
        {

            if(popupMenuBuildingtype!=null)
            {
                popupMenuBuildingtype.show();

            }


        }
        else if(v == btn_e)
        {
            selecttype = "E";
            cv_roomtype.setVisibility(View.VISIBLE);
            btn_type.setText("");
            btn_building.setText("");
            GetelEvationaveData("");
            selecttab(btn_e);
        }
        else if(v == btn_i)
        {
            selecttype = "I";
            cv_roomtype.setVisibility(View.GONE);
            btn_type.setText("");
            btn_building.setText("");
            getInteriorSaveData("","");
            selecttab(btn_i);
        }
        else if(v == btn_r)
        {
            selecttype = "R";
            cv_roomtype.setVisibility(View.GONE);
            btn_type.setText("");
            btn_building.setText("");
            selecttab(btn_r);
            GetRoofSaveData("");

        }
    }

    private void selecttab(AppCompatButton btn_selecttab)
    {

        btn_i.setBackgroundColor(Color.WHITE);
        btn_i.setTextColor(Color.parseColor("#00A4D4"));

        btn_e.setBackgroundColor(Color.WHITE);
        btn_e.setTextColor(Color.parseColor("#00A4D4"));

        btn_r.setBackgroundColor(Color.WHITE);
        btn_r.setTextColor(Color.parseColor("#00A4D4"));

        btn_selecttab.setTextColor(Color.WHITE);
        btn_selecttab.setBackgroundColor(Color.parseColor("#00A4D4"));

    }



    private void I_Option()
    {
        Constants.Ceilingzrr = new ArrayList<>();
        Constants.Wallzrr = new ArrayList<>();
        Constants.Floorzrr = new ArrayList<>();


        getCeiling();
        getWall();
        getFloor();

      }

    private void getFloor() {

        Constants.RoofMaterial = new ArrayList<>();




        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_f ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (!strvalue.equalsIgnoreCase("Custom Text"))
                {
                    Log.e("ValueFloor==>", "" + strvalue);
                    Constants.Floorzrr.add(strvalue);
                }

            }
            while (Cur.moveToNext());

        }
        Cur.close();
        DB.close();


    }

    private void getWall()
    {

        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_c ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (!strvalue.equalsIgnoreCase("Custom Text"))
                {
                    Log.e("ValueWall==>", "" + strvalue);
                    Constants.Wallzrr.add(strvalue);
                }
                    continue;

            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();




    }

    private void getCeiling()
    {




        opendatabase();

        SELECT_SQL = "SELECT * FROM tbl_w ORDER BY value ASC";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0)
        {
            Cur.moveToFirst();
            do {

                String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                if (!strvalue.equalsIgnoreCase("Custom Text"))
                {
                    Constants.Ceilingzrr.add(strvalue);
                    Log.e("strvalueCeiling","==>"+strvalue);
                }

            }
            while (Cur.moveToNext());

        }
        Cur.close();
        DB.close();


    }

}
